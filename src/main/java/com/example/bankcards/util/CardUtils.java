package com.example.bankcards.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class CardUtils {
	
	private static final Random random = new Random();

	private static final String DEFAULT_BIN = "510510";
	
	private static final String MASK = "**** **** **** %s";

	public String getMaskedPan(String pan) {
		String lastDigits = pan.substring(12);
		return MASK.formatted(lastDigits);
	}
	
    public String generatePan() {
        StringBuilder pan = new StringBuilder(DEFAULT_BIN);
        for (int i = 0; i < 9; i++) {
        	pan.append(random.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(pan.toString());
        pan.append(checkDigit);

        return pan.toString();
    }

    // Lunh Algorithm
    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}
