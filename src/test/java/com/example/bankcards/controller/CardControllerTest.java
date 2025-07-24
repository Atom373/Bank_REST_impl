package com.example.bankcards.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.bankcards.controller.payload.CardCreateRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.mapper.CardMapperImpl;
import com.example.bankcards.mapper.DetailedCardMapperImpl;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.JwtUtils;
import com.example.bankcards.utils.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {CardController.class})
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private CardService bankCardService; 
	
	@SpyBean
    private CardMapperImpl cardMapper; 
	
	@SpyBean
	private DetailedCardMapperImpl detailedCardMapper;

	@MockBean
	private UserService userService;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	@Test
	public void getUserCards_shouldReturnAllUsersCards() throws Exception {
		// given
		User user = DataUtils.getUser();
		List<Card> cards = DataUtils.getCardList();
		when(bankCardService.getAllByOwnerId(anyLong())).thenReturn(cards);
		
		Authentication auth = setUpSecurityContext(user);
	    
	    // when
	    ResultActions result = mockMvc.perform(get("/api/v1/cards")
	        .with(authentication(auth)));
	    
		// then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(cards.size())))
			.andExpect(jsonPath("$.content[0].owner_email").value(user.getEmail()))
			.andExpect(jsonPath("$.content[1].owner_email").value(user.getEmail()));
	}
	
	@Test
	public void getCardById_shouldReturnDetailedCardDto() throws Exception {
		// given
		User user = DataUtils.getUser();
		Card card = DataUtils.getNewCard();
		when(bankCardService.getById(card.getId(), user.getId())).thenReturn(card);
		
		Authentication auth = setUpSecurityContext(user);
	    
	    // when
	    ResultActions result = mockMvc.perform(get("/api/v1/cards/{id}", card.getId())
	        .with(authentication(auth)));
	    
		// then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.balance").value(card.getBalance().toString()))
			.andExpect(jsonPath("$.status").value(CardStatus.ACTIVE.name()));
	}
	
	@Test
	public void getAllCards_shouldReturnListWithAllCards() throws Exception {
		// given
		User admin = DataUtils.getAdmin();
		List<Card> cards = DataUtils.getCardList();
		when(bankCardService.getAll()).thenReturn(cards);
		
		Authentication auth = setUpSecurityContext(admin);
	    
	    // when
	    ResultActions result = mockMvc.perform(get("/api/v1/admin/cards")
	        .with(authentication(auth)));
	    
		// then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(cards.size())))
			.andExpect(jsonPath("$[0].status").value(CardStatus.ACTIVE.name()));
	}
	
	@Test
	public void createNewCard_shouldReturnListWithAllCards() throws Exception {
		// given
		User admin = DataUtils.getAdmin();
		User user = DataUtils.getUser();
		
		Card card = DataUtils.getNewCard();
		CardCreateRequest request = new CardCreateRequest(user.getEmail(), "100.00");
		
		when(bankCardService.save(any(Card.class))).thenReturn(card);
		
		Authentication auth = setUpSecurityContext(admin);
	    
	    // when
	    ResultActions result = mockMvc.perform(post("/api/v1/admin/cards")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(objectMapper.writeValueAsString(request))
	        .with(authentication(auth)));
	    
		// then
		result
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.owner_email").value(user.getEmail()))
			.andExpect(jsonPath("$.status").value(CardStatus.ACTIVE.name()));
	}
	
	private Authentication setUpSecurityContext(User user) {
	    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
	    Authentication authentication = new UsernamePasswordAuthenticationToken(
	        user.getId(),
	        null,
	        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
	    );
	    securityContext.setAuthentication(authentication);
	    SecurityContextHolder.setContext(securityContext);
	    
	    return authentication;
	}
}
