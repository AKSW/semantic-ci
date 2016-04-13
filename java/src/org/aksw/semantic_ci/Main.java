/**
 * 
 */
package org.aksw.semantic_ci;

/**
 * @author kjunghanns
 *
 */
public class Main {
	
	static CLI cli;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			cli = new CLI(args);
		} catch (Exception e) {
			return;
		}
        
		
	}

}
