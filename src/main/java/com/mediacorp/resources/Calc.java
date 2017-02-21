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
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCalc(@QueryParam("expression") String expression) {
    	if(expression == null) {
    		throw new InvalidExpressionException("No input provided");
    	} 
    	
    	//Validate the expression
    	//Process the expression based on BODMAS
    	
    	ArrayList<String> expArrayList = new ArrayList<String>();
    	//understand lookahead & lookbehind regex.
    	String[] expSplit = URLEncoder.encode(expression).split("(?<=[-+*/^])|(?=[-+*/^])");
    	for(String element : expSplit) {
    		expArrayList.add(element);
    	}
    	
    	
    /*	expArrayList.add("3");
    	expArrayList.add("+");
    	expArrayList.add("4.9");
    	expArrayList.add("-");    	
    	expArrayList.add("1.9");
    	expArrayList.add("+");
    	expArrayList.add("3");
    	expArrayList.add("^");    
    	expArrayList.add("2");   */
    	System.out.println(Arrays.toString(expArrayList.toArray()));
    	
    	expArrayList = calculate(expArrayList,"^");
    	System.out.println(Arrays.toString(expArrayList.toArray()));
    	expArrayList = calculate(expArrayList,"/");   
    	System.out.println(Arrays.toString(expArrayList.toArray()));
    	expArrayList = calculate(expArrayList,"*"); 
    	System.out.println(Arrays.toString(expArrayList.toArray()));
    	expArrayList = calculate(expArrayList,"+");   	
    	System.out.println(Arrays.toString(expArrayList.toArray()));
    	expArrayList = calculate(expArrayList,"-");   	
    	System.out.println(Arrays.toString(expArrayList.toArray()));
    	
        return expArrayList.get(0).toString();
    }
    
    
    public ArrayList<String> calculate(ArrayList<String> inputExp, String operator) {
        int precision = 10;                         
        BigDecimal tempOutput = new BigDecimal(0);   
        for (int i = 0; i < inputExp.size(); i++) {
            if (inputExp.get(i).equals(operator)) {
                switch (inputExp.get(i)) {
                    case "^":
                    	tempOutput = McUtils.power(inputExp.get(i - 1),inputExp.get(i + 1));
                        break;
                    case "*":
                    	tempOutput = McUtils.multiply(inputExp.get(i - 1),inputExp.get(i + 1));
                        break;
                    case "/":
                        tempOutput = McUtils.divide(inputExp.get(i - 1),inputExp.get(i + 1));
                        break;
                    case "+":
                    	tempOutput = McUtils.add(inputExp.get(i - 1),inputExp.get(i + 1));
                        break;
                    case "-":
                    	tempOutput = McUtils.subtract(inputExp.get(i - 1),inputExp.get(i + 1));
                        break;
                }
                inputExp.set(i, tempOutput.setScale(precision, RoundingMode.UP).stripTrailingZeros().toPlainString());
                inputExp.remove(i + 1);                                                              
                inputExp.remove(i - 1);        
            } else {
                continue;
            }
            i = 0;
        }
       // System.out.println("Result");
        return inputExp;
    }
}

