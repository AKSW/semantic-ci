/**
 * 
 */
package org.aksw.semantic_ci;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * @author kjunghanns
 * Provides functions for the command line interface.
 * Parameters are:
 * 	-i file						: input file which should be processed
 * 	-vocab URL					: URL of a vocabulary
 * 	-c MODES					: set what should be done - MODES could be all/cv/cvdeep/cl/ct/cdr
 */
public class CLI {

	OptionSet options;
	OptionParser parser;
	
	//TODO: throw error when arguments are wrong
	public CLI (String[] args) {
		parser = new OptionParser("c:i:v:");
		options = parser.parse( args );
		checkParams();
	}
	
	private void checkParams() {
		if (!options.has( "c" ))
			throw new Error("Wrong parameter!");
		
		if (	   !options.valuesOf( "c" ).contains("all") 
				&& !options.valuesOf( "c" ).contains("cv") 
				&& !options.valuesOf( "c" ).contains("cvdeep") 
				&& !options.valuesOf( "c" ).contains("cl") 
				&& !options.valuesOf( "c" ).contains("ct") 
				&& !options.valuesOf( "c" ).contains("cdr")
			)
			throw new Error("Wrong value for parameter c!");
			
		if (!(options.has( "i" ) || options.has( "vocab" )))
			throw new Error("Missing parameters!");
	}
	
	public String getMode() {
		return (String)options.valueOf("c");
	}
	
	public String getFileURI() {
		return (String)options.valueOf("i");
	}
	
	public String getVocabularyURI() {
		return (String)options.valueOf("vocab");
	}
}
