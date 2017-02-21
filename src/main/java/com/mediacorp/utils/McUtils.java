package com.mediacorp.utils;

import java.math.BigDecimal;

public class McUtils{
	
	//handle arithmetic exceptions to all the numerical operations defined here.
	
	static int precision = 10;
	
	public static BigDecimal add(String operand1,String operand2) {
		return new BigDecimal(operand1).add(new BigDecimal(operand2));
	}
	
	public static BigDecimal subtract(String operand1,String operand2) {
		return new BigDecimal(operand1).subtract(new BigDecimal(operand2));
	}
	
	public static BigDecimal divide(String operand1,String operand2) {
		//handle zero check & zero divison exception
		return new BigDecimal(operand1).divide(new BigDecimal(operand2), precision, BigDecimal.ROUND_DOWN);
	}
	
	public static BigDecimal multiply(String operand1,String operand2) {
		return new BigDecimal(operand1).multiply(new BigDecimal(operand2));
	}
	
	public static BigDecimal power(String operand1,String operand2) {
		return new BigDecimal(operand1).pow(Integer.parseInt(operand2));
	}

}
