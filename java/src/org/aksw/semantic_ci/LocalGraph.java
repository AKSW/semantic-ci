/**
 * 
 */
package org.aksw.semantic_ci;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

/**
 * @author kjunghanns 
 * Class which will be constructed with rdf, creates a graph
 * in memory and provides functionalities for validate the graph and run
 * SPARQL on it.
 * 
 * Needed functionality: 	check if vocabs are available in the www (direct with http or indirect in LOV) [1.c]
 * 							check if all used classes and properties of the vocabulary are existent [1.c]
 * 							check literals for correct datatypes (using regex) [1.d]
 * 							check for existing types of all resources (perhaps SPARQL) [2.a]
 * 							check range and domain of vocabs (perhaps SPARQL) [2.b]
 */
public class LocalGraph {

	public LocalGraph(String filename) {

		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(filename);
		if (in == null) {
			throw new IllegalArgumentException("File: " + filename + " not found");
		}

		// read the RDF N-Triple file
		model.read(in, null, "N-TRIPLE");
	}

}
