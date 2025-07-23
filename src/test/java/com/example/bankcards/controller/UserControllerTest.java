package com.example.bankcards.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.bankcards.controller.payload.UserCreateRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapperImpl;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.JwtUtils;
import com.example.bankcards.utils.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)

public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	 
	
	@SpyBean
	private UserMapperImpl userMapper;

	@MockBean
	private UserService userService;
	
	@MockBean
	private JwtUtils jwtUtils;

	@Test
	public void getAllUsers_shouldReturnListWithAllUsers() throws Exception {
		// given
		List<User> users = DataUtils.getUserList();
		when(userService.getAll()).thenReturn(users);
		
		// when
		ResultActions result = mockMvc.perform(get("/api/v1/admin/users"));
		
		//then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(users.size())))
			.andExpect(jsonPath("$.content[0].email").value(users.get(0).getEmail()))
			.andExpect(jsonPath("$.content[1].email").value(users.get(1).getEmail()));
	}
	
	@Test
	public void createNewUser_shouldSaveNewUserAndReturnUserDto() throws Exception  {
		// given
		User user = DataUtils.getUser();
		when(userService.save(any(User.class))).thenReturn(user);
		UserCreateRequest request = new UserCreateRequest(user.getEmail(), "password");
		
		// when
		ResultActions result = mockMvc.perform(post("/api/v1/admin/users")
		    	.contentType(MediaType.APPLICATION_JSON)
		    	.content(objectMapper.writeValueAsString(request)));
		
		//then
		result
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(user.getId().toString()));
	}
	
}
