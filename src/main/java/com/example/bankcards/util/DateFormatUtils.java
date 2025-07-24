package com.example.bankcards.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateFormatUtils {

	public String format(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/YY");
        return date.format(formatter);
	}
}