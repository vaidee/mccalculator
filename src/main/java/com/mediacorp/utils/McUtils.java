package com.mediacorp.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.mediacorp.exception.McExceptions;

import org.apache.commons.math3.analysis.function.Gaussian;

public class McUtils {

	// handle arithmetic exceptions to all the numerical operations defined
	// here.

	public enum funcs {
		gauss, sqrt
	};

	static int precision = 10;

	public static BigDecimal add(String operand1, String operand2) {
		return new BigDecimal(operand1).add(new BigDecimal(operand2));
	}

	public static BigDecimal subtract(String operand1, String operand2) {
		return new BigDecimal(operand1).subtract(new BigDecimal(operand2));
	}

	public static BigDecimal divide(String operand1, String operand2) {
		// handle zero check & zero divison exception
		return new BigDecimal(operand1).divide(new BigDecimal(operand2),
				precision, BigDecimal.ROUND_DOWN);
	}

	public static BigDecimal multiply(String operand1, String operand2) {
		return new BigDecimal(operand1).multiply(new BigDecimal(operand2));
	}

	public static BigDecimal power(String operand1, String operand2) {
		return new BigDecimal(operand1).pow(Integer.parseInt(operand2));
	}

	public String gauss(String operand1, String operand2, String operand3,
			String operand4) throws McExceptions {
		try {
			Gaussian gs = new Gaussian(Double.parseDouble(operand1),
					Double.parseDouble(operand2), Double.parseDouble(operand3));
			Double output = gs.value(Double.parseDouble(operand4));
			System.out.println("op--" + output.toString());
			return output.toString();
		} catch (Exception e) {
			throw new McExceptions(e.getMessage());
		}
	}

	public String funcMethod(String funcName, String args) throws McExceptions {
		String val = "";
		try {
			for (funcs f : funcs.values()) {
				if (f.name().equalsIgnoreCase(funcName)) {
					Class c = Class.forName(McUtils.class.getName());
					Object obj = c.newInstance();
					String[] arguments = args.split(",");
					Class paramTypes[] = new Class[4];
					paramTypes[0] = String.class;
					paramTypes[1] = String.class;
					paramTypes[2] = String.class;
					paramTypes[3] = String.class;
					Method mtd = c.getDeclaredMethod(funcName, paramTypes);
					Object out = mtd.invoke(obj, arguments[0], arguments[1],
							arguments[2], arguments[3]);
					val = (String) out;
				}
			}
			return val;
		} catch (NoSuchMethodException e) {
			// e.printStackTrace(System.out);
			throw new McExceptions(
					"Function used in the expression is not supported");
		} catch (InvocationTargetException e) {
			throw new McExceptions(
					"Function used in the expression has invalid arguments");
		} catch (Exception e) {
			// e.printStackTrace(System.out);
			throw new McExceptions(e.getMessage());
		}
	}

}
