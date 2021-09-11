package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		Node ptr1 = poly1, ptr2 = poly2;
		Node front = null;
		
		if (poly1 == null && poly2 == null) {
			return null;
		}
		while (ptr1 != null || ptr2 != null) {
			
			if( ptr1 == null ) {
				Node n = new Node(ptr2.term.coeff, ptr2.term.degree, front);
				front = n;
				ptr2 = ptr2.next;
			}else if (ptr2 == null) {
				Node n = new Node(ptr1.term.coeff, ptr1.term.degree, front);
				front = n;
				ptr1 = ptr1.next;
			}else {
				if(ptr1.term.degree == ptr2.term.degree) {
					float c = ptr1.term.coeff + ptr2.term.coeff;
					if ( c!= 0) {
					Node n = new Node(c, ptr1.term.degree, front);
					front = n;
					}
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
				}else if (ptr1.term.degree < ptr2.term.degree) {
					Node n = new Node(ptr1.term.coeff, ptr1.term.degree, front);
					front = n;
					ptr1 = ptr1.next;
				}else {
					Node n = new Node(ptr2.term.coeff, ptr2.term.degree, front);
					front = n;
					ptr2 = ptr2.next;
				}
			}
			
		}
		
		Node finalFront = null;
		while (front != null) {
			Node m = new Node(front.term.coeff, front.term.degree, finalFront);
			finalFront = m;
			front = front.next;
		}
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		return finalFront;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		
		if(poly1 == null || poly2 == null) {
			return null;
		}
		Node ptr1 = poly1; 
		Node front = null;
	while( ptr1 != null ) {
		Node ptr2 = poly2;
		while(ptr2!=null){
			Node n = new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, front);
			front = n;
			ptr2 = ptr2.next;
		}
		ptr1 = ptr1.next;
	}
	
	Node finalFront = null;
	int count = front.term.degree;
	while (count >= 0) {
		Node f = front;
		float c = 0;
		while (f!=null) {
			if(f.term.degree == count) {
				c += f.term.coeff;
			}
			f = f.next;
		}
		if(c != 0 ) {
			Node m = new Node (c, count, finalFront);
			finalFront = m;
		}
		count -- ;
		
	}
	

		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		return finalFront;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		float y = 0;
		Node ptr = poly;
		while( ptr != null ) {
			float value = ptr.term.coeff * (float)Math.pow(x, ptr.term.degree);
			y += value;
			ptr = ptr.next;
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		return y;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
