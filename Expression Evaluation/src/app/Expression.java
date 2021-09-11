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
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	for(int i = 0; i < expr.length(); i++) {
    		char c = expr.charAt(i);
    		
    		if ((c >= 'a' && c <= 'z' )||(c >= 'A' && c <= 'Z' )) {
    			String v = "";
    			int j;
    			for( j = 0; j < expr.length() - i; j++) {
    				char d = expr.charAt(i+j);
    				if((d >= 'a' && d <= 'z' )||(d >= 'A' && d <= 'Z' )) {
    					v += d+"";
    					
    				}else {
    					break;
    				}
    			}
    			i = i+j-1;
    			if ((i+1) != expr.length()) {
    				if(expr.charAt(i+1) == '[') {
    					boolean check1 = true;
    					for(int k = 0; k < arrays.size(); k++) {
    						if (v.equals(arrays.get(k).name)) {
    							check1 = false; break;
    						}
    					}
    					if (check1) {
    					Array a = new Array(v);
    					arrays.add(a);
    				}
    					continue;
    			}
    			
    		}
    			boolean check2 = true;
    			for(int k = 0; k < vars.size(); k++) {
					if (v.equals(vars.get(k).name)) {
						check2 = false; break;
					}
				}
    			if(check2) {
    			Variable b = new Variable(v);
    			vars.add(b);
    	        }
    		}
    	}
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
    	/** COMPLETE THIS METHOD **/
    	int n = expr.length(); 
    	
    	Stack<Float> numbers = new Stack<Float>(); 
    	Stack<Character> operations = new Stack<Character>();
    	
    	for (int i = 0; i < n; i++) {
    		
    		char c = expr.charAt(i);
    		
    		if (c == '+' || c == '-'|| c == '*' || c == '/') {
    			operations.push(c);
    		}
    		
    		if (Character.isDigit(c)) {
    			String v = ""; int j;
    			for (j = 0; j < n - i; j++) {
    				char d = expr.charAt(i+j);
    				if(Character.isDigit(d)) {
    					v += d+"";
    				}else {
    					break;
    				}
    			}
    			i = i+j-1;	
    			float f = Float.parseFloat(v);
    			numbers.push(f);
    		}
    		
    		if ((c >= 'a' && c <= 'z' )||(c >= 'A' && c <= 'Z' )) {
    			
    			String v = "";
    			int j;
    			for( j = 0; j < n - i; j++) {
    				char d = expr.charAt(i+j);
    				if((d >= 'a' && d <= 'z' )||(d >= 'A' && d <= 'Z' )) {
    					v += d+"";
    					
    				}else {
    					break;
    				}
    			}
    			i = i+j-1;
    			
    			if ((i+1) < n) {
    				if (expr.charAt(i+1) == '[') {
    					String index = ""; int count = 0; i+=2;
    					while (!((expr.charAt(i) == ']') && (count == 0))) {
    						char cInIndex = expr.charAt(i);	
    						if (cInIndex == '[') count++;
    						if (cInIndex == ']') count--;
    						index += cInIndex+"";
    						i++;
    					}
    					int valueOfIndex = (int)evaluate(index, vars, arrays);   					
    					int k;
    					for (k = 0; k < arrays.size(); k++) {
    	    				if (v.equals(arrays.get(k).name)){
    	    					break;
    	    				}
    	    			}
    					float f = arrays.get(k).values[valueOfIndex];
    					numbers.push(f);
    					continue;
    				}
    			}
    				
    			int k;
    			for (k = 0; k < vars.size(); k++) {
    				if (v.equals(vars.get(k).name)){
    					break;
    				}
    			}
    			float f = vars.get(k).value;
    			numbers.push(f);
    		}
    		
    		if( c == '(') {
    			int count = 0;
    			i += 1;
    			String parenthesis = "";
    			while (!((expr.charAt(i) == ')') && (count == 0))) {
    				char cInParenthesis = expr.charAt(i);
    				if (cInParenthesis == '(') count++;
					if (cInParenthesis == ')') count--;
					parenthesis += cInParenthesis + "";
					i++;
    			}
    			float f = evaluate(parenthesis, vars, arrays);
				numbers.push(f);
    		}    		
    	}
    	
    	Stack<Float> finalNumbers = new Stack<Float>(); 
    	Stack<Character> finalOperations = new Stack<Character>();
    	while(!numbers.isEmpty()) {
    		finalNumbers.push(numbers.pop());
    	}
    	while(!operations.isEmpty()) {
    		finalOperations.push(operations.pop());
    	}
    	
    	while(!finalOperations.isEmpty()) {
    		float first = finalNumbers.pop();
    		float second = finalNumbers.pop();
    		char c = finalOperations.pop();
    		switch(c) {
    		case '*': finalNumbers.push(first * second); break;
    		case '/': finalNumbers.push(first / second); break;
    		case '+': if(finalOperations.isEmpty()) {
    					  finalNumbers.push(first + second);
    				  }else {
    					  char postOperation = finalOperations.pop();
    					  if(postOperation == '*'||postOperation == '/') {
    						  finalOperations.push(c);
    						  float third = finalNumbers.pop();
    						  if(postOperation == '*') {
    							  finalNumbers.push(second*third);
    						  }else {
    							  finalNumbers.push(second/third); 
    						  }
    						  finalNumbers.push(first);
    					  }else {
    						  finalOperations.push(postOperation);
    						  finalNumbers.push(first + second);
    					  }
    				  } break;
    		case '-': if(finalOperations.isEmpty()) {
				  		finalNumbers.push(first - second);
			  		  }else {
			  			char postOperation = finalOperations.pop();
			  			if(postOperation == '*'||postOperation == '/') {
			  				finalOperations.push(c);
			  				float third = finalNumbers.pop();
			  				if(postOperation == '*') {
			  					finalNumbers.push(second*third);
			  				}else {
			  					finalNumbers.push(second/third); 
			  				}
			  				finalNumbers.push(first);
			  			}else {
			  				finalOperations.push(postOperation);
			  				finalNumbers.push(first - second);
			  			}
			  		  }    		
    				}
    	}
    	
    	return finalNumbers.pop();			  
      
    	
    	
    	
    	// following line just a placeholder for compilation
    	
    }
}
