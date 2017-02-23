package com.mediacorp.resources;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.mediacorp.exception.*;
import com.mediacorp.utils.McUtils;

/**
 * Root resource (exposed at "/rest/calc" path)
 */
@Path("calc")
public class Calc {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getCalc(@QueryParam("expression") String expression)
			throws McExceptions {
		try {
			if (expression.equals("")) {
				throw new McExceptions("Expression is empty");
			}

			// Validate the expression. Need to be implemented

			ArrayList<String> expArrayList = new ArrayList<String>();
			// understand lookahead & lookbehind regex.
			String[] expSplit = expression.split("(?<=[-+*/^()])|(?=[-+*/^)])");
			for (String element : expSplit) {
				expArrayList.add(element);
			}
			System.out.println(Arrays.toString(expArrayList.toArray()));

			// process functions & remove
			expArrayList = caclulateFunctions(expArrayList);
			// process paranthesis & remove
			expArrayList = caclulateExpInBraces(expArrayList);

			return caclulateExp(expArrayList);
		} catch (McExceptions e) {
			return e.getErrorMessage();
		}
	}

	public ArrayList<String> caclulateFunctions(ArrayList<String> expFunctions)
			throws McExceptions {
		ArrayList<String> expTempArrList = new ArrayList<String>();
		int started = 0;
		for (int i = 0; i < expFunctions.size(); i++) {
			System.out.println("Focussed element : " + expFunctions.get(i));
			if (started == 0) {
				if (expFunctions.get(i).matches("[a-zA-Z]+[(]")) {
					System.out.println("Function name matched");
					expTempArrList.add(expFunctions.get(i));
					started++;
				}
			} else {
				if (expFunctions.get(i).equals(")") && started == 1) {
					System.out.println("closing brace");
					started--;
					expTempArrList.add(expFunctions.get(i));
					expFunctions.remove(i);
					i--;
					System.out.println("functions temp array : "
							+ Arrays.toString(expTempArrList.toArray()));
					String functionName = expTempArrList.get(0).substring(0,
							expTempArrList.get(0).length() - 1);
					expFunctions.set(i, new McUtils().funcMethod(functionName,
							expTempArrList.get(1)));
					expTempArrList.clear();
				} else {
					System.out.println("adding items");
					expTempArrList.add(expFunctions.get(i));
					expFunctions.remove(i);
					i--;
				}
			}

		}
		return expFunctions;
	}

	public ArrayList<String> caclulateExpInBraces(
			ArrayList<String> expWithBraces) throws McExceptions {
		ArrayList<String> expTempArrayList = new ArrayList<String>();
		int started = 0;
		for (int i = 0; i < expWithBraces.size(); i++) {
			if (started == 0) {
				if (expWithBraces.get(i).equals("(")) {
					started++;
					expWithBraces.remove(i);
					i--;
				}
			} else {
				if (expWithBraces.get(i).equals(")") && started == 1) {
					started--;
					if (expTempArrayList.contains("(")) {
						expTempArrayList = caclulateExpInBraces(expTempArrayList);
					}
					expWithBraces.set(i, caclulateExp(expTempArrayList));
					expTempArrayList.clear();
				} else {
					if (expWithBraces.get(i).equals("(")) {
						started++;
					}
					if (expWithBraces.get(i).equals(")")) {
						started--;
					}
					expTempArrayList.add(expWithBraces.get(i));
					expWithBraces.remove(i);
					i--;
				}
			}
		}
		return expWithBraces;
	}

	public String caclulateExp(ArrayList<String> inputExp1) throws McExceptions {
		inputExp1 = calculate(inputExp1, "^");
		inputExp1 = calculate(inputExp1, "/");
		inputExp1 = calculate(inputExp1, "*");
		inputExp1 = calculate(inputExp1, "+");
		inputExp1 = calculate(inputExp1, "-");
		return inputExp1.get(0).toString();
	}

	public ArrayList<String> calculate(ArrayList<String> inputExp,
			String operator) throws McExceptions {
		try {
			int precision = 10;
			BigDecimal tempOutput = new BigDecimal(0);
			for (int i = 0; i < inputExp.size(); i++) {
				if (inputExp.get(i).equals(operator)) {
					switch (inputExp.get(i)) {
					case "^":
						tempOutput = McUtils.power(inputExp.get(i - 1),
								inputExp.get(i + 1));
						break;
					case "*":
						tempOutput = McUtils.multiply(inputExp.get(i - 1),
								inputExp.get(i + 1));
						break;
					case "/":
						tempOutput = McUtils.divide(inputExp.get(i - 1),
								inputExp.get(i + 1));
						break;
					case "+":
						tempOutput = McUtils.add(inputExp.get(i - 1),
								inputExp.get(i + 1));
						break;
					case "-":
						tempOutput = McUtils.subtract(inputExp.get(i - 1),
								inputExp.get(i + 1));
						break;
					}
					inputExp.set(i,
							tempOutput.setScale(precision, RoundingMode.UP)
									.stripTrailingZeros().toPlainString());
					inputExp.remove(i + 1);
					inputExp.remove(i - 1);
				} else {
					continue;
				}
				i = 0;
			}
			// System.out.println("Result");
			return inputExp;
		} catch (Exception e) {
			throw new McExceptions("Invalid Expression");
		}
	}
}
