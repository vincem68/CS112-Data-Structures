package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {

    	
    	
    	StringTokenizer separate = new StringTokenizer(expr, delims);
    	
    	
    	
    	while (separate.hasMoreTokens()) {
    		
    		String name = separate.nextToken();
    		if (!Character.isDigit(name.charAt(0))) {
    			
    			int index = expr.indexOf(name);
    			
    			if (!(index + name.length() >= expr.length()) && expr.charAt(index+name.length()) == '[') {
    				
    				Array newArray = new Array(name);
    				
    				if (!arrays.contains(newArray)) {
    				arrays.add(newArray);
    				}
    				
    			} else {
    				
    			
    			Variable newVariable = new Variable(name);
    			if (!vars.contains(newVariable)) {
    				vars.add(newVariable);
    			}
    		}
    	}
    }
    	//System.out.println(vars); 
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	Stack<Float> variableStack = new Stack<Float>(); 
    	Stack<Character> operatorStack = new Stack<Character>();
    	Stack<Float> tempVariableStack = new Stack<Float>();
    	Stack<Character> tempOperatorStack = new Stack<Character>();
    //	Stack<Character> substringStack = new Stack<Character>();
    	
    	//System.out.println(vars);
    	
    // this deals with parentheses 
    	int cycle = 0; int open = 0; int close  = 0; int openBracket = 0; int closeBracket = 0;
    	while (cycle < expr.length()) {
    		if (expr.charAt(cycle) == '(') {
    			open = cycle;
    		}
    		if (expr.charAt(cycle) == ')') {
    			close = cycle;
    	//		System.out.println("open was at "+open+" and close is at "+close);
    			float res = evaluate(expr.substring(open+1, close), vars, arrays);
    			String done = Float.toString(res);
    			expr = expr.substring(0, open) + done + expr.substring(close+1, expr.length());
    			return evaluate(expr, vars, arrays);
    			
    		}
    		
    		if (expr.charAt(cycle) == '[') {
    			openBracket = cycle;
    	//		System.out.println("openBracket is " + openBracket);
    		}
    		if (expr.charAt(cycle) == ']') {
    			closeBracket = cycle;
    	//		System.out.println("close bracket is "+closeBracket);
    			float bRes = evaluate(expr.substring(openBracket+1, closeBracket), vars, arrays);
    			int hold = (int)bRes;
    		//	System.out.println("bRes is "+ bRes);
    			String bDone = Integer.toString(hold);
    			expr = expr.substring(0, openBracket)+ ' ' + bDone + expr.substring(closeBracket+1, expr.length());
    //			System.out.println("the new string is "+expr);
    			return evaluate(expr, vars, arrays);
    		}
    		cycle++;
    	} 
    	
    	
    	
    	//this adds the integers, variables, and arrays to the stack
    	StringTokenizer numbers = new StringTokenizer(expr, delims);
    	
    	
    	while (numbers.hasMoreTokens()) {
    		String single = numbers.nextToken(); float digit;
    		System.out.println(single);
    	//	boolean negative = false;
			if (Character.isDigit(single.charAt(0))) { 
				if (expr.charAt(0) == '-') {
					digit = Float.valueOf(single); digit *= -1;
					expr = expr.substring(1, expr.length());
				}
				else if ( expr.indexOf(single) != 0 && expr.charAt(expr.indexOf(single)-1) == '-' && (expr.charAt(expr.indexOf(single)-2) == '+' ||
						expr.charAt(expr.indexOf(single)-2) == '-' || expr.charAt(expr.indexOf(single)-2) == '*'||
						expr.charAt(expr.indexOf(single)-2) == '/')) {
					digit = Float.valueOf(single);
					digit *= -1;
					expr = expr.substring(0, expr.indexOf(single)-1) + expr.substring(expr.indexOf(single), expr.length());
				} else {
					digit = Float.valueOf(single);
				}
				tempVariableStack.push(digit);
			}
			if (Character.isLetter(expr.charAt(0))) {
				boolean inArray = false;
				for (int y = 0; y < arrays.size(); y++) {
					Array see = arrays.get(y);
					if (see.name.equals(single)){
						inArray = true;
					}
				}
				if (inArray == true) {
					for (int y = 0; y < arrays.size(); y++) {
    					Array compare = arrays.get(y);
    					if (compare.name.equals(single)) {
    					    String position = numbers.nextToken();
    			//		    System.out.println("the position is " + position);
    					    int arrayIndex = Integer.valueOf(position);
    					    float digit2 = compare.values[arrayIndex];
    				//	    System.out.println("the value is "+digit2);
    					    tempVariableStack.push(digit2);
    					}
    				}
				} else {
					for (int x = 0; x < vars.size(); x++) {
						Variable compare = vars.get(x);
						if (compare.name.equals(single)){
							digit = compare.value;
							System.out.println("digit of variable is "+digit);
							tempVariableStack.push(digit);
					    }
				    }
				}
				
		    }
		}
    	System.out.println("top of temp stack is "+tempVariableStack.peek());
    	
    	//puts the numbers in order from left to right
    	while (!tempVariableStack.isEmpty()) {
    		variableStack.push(tempVariableStack.pop());
    	}
    	System.out.println("top of variable stack is "+variableStack.peek());
    	//pushes the operators from right to left so they align with the numbers
    	
    	for (int i = expr.length()-1; i >= 0; i--) {
    		if (expr.charAt(i) == '+' || expr.charAt(i) == '-' || expr.charAt(i) == '*' ||
    				expr.charAt(i) == '/') {
    			operatorStack.push(expr.charAt(i));
    		}
    	}
    	
    	//goes through both stacks to perform the * and / operations first, discards these operators
    	
    	while (!variableStack.isEmpty() && !operatorStack.isEmpty()) {
    		char check = operatorStack.pop();
    	//	System.out.println("the operator is " + check);
    		
    		if (check == '*' || check == '/') {
    			float num1 = variableStack.pop();
    		//	System.out.println("the number is "+num1);
    		   	float num2 = variableStack.pop();
    		  // 	System.out.println("the second number is "+num2);
    			 if (check == '*') {
    				 float result = num1*num2;
    				// System.out.println("the product is "+result);
    				 variableStack.push(result);
    			 }
    			 if (check == '/') {
    				 float result = num1/num2;
    				// System.out.println("the quotient is "+result);
    				 variableStack.push(result);
    			 }
    		} else {
    			tempVariableStack.push(variableStack.pop());
    			tempOperatorStack.push(check);
    		}
    	}
    	// this checks if we have a number left when we are out of operators
    	if (operatorStack.isEmpty() && !variableStack.isEmpty()) {
    		tempVariableStack.push(variableStack.pop());
    	}
    	
    	//we reorganize the numbers and operations again to get it left to right
    	while (!tempVariableStack.isEmpty()) {
    		variableStack.push(tempVariableStack.pop());
    		//System.out.println(variableStack.peek());
    	}
    	while (!tempOperatorStack.isEmpty()) {
    		operatorStack.push(tempOperatorStack.pop());
    	}
    	
    	 // goes through both stacks once more to perform the + and - operations 
    	while(!variableStack.isEmpty() && !operatorStack.isEmpty()) {
    	//	System.out.println("stack is empty: " + variableStack.isEmpty());
			char operator = operatorStack.pop();
			if (operator == '+') {
				float number1 = variableStack.pop();
				float number2 = variableStack.pop();
				float result = number1 + number2;
				variableStack.push(result);
			//	System.out.println("we add "+number1+" with "+number2+" and get "+result);
			}
			if (operator == '-') {
				float number1 = variableStack.pop();
				float number2 = variableStack.pop();
				float result = number1 - number2;
				variableStack.push(result);
			//	System.out.println("we have "+number1+" minus "+number2+" and get "+result);
			}
			
			
		}
    	
    	if (variableStack.isEmpty()) {
    		return 0;
    	}
    	return variableStack.pop();
    	
    	
    	
    }
}
