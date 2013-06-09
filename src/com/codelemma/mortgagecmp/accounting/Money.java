package com.codelemma.mortgagecmp.accounting;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

	private Money() {}
	
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal ZERO = BigDecimal.ZERO;    

    // Number of decimals to retain, ie. "scale".
    public static final int DECIMALS = 8;
        
    public static BigDecimal getPercentage(BigDecimal base, BigDecimal perc) {
    	return scale(base.multiply(perc)); 
    }
   
    public static BigDecimal scale(BigDecimal num){
        return num.setScale(DECIMALS, ROUNDING_MODE);
    }
}

