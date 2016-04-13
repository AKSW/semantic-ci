/**
 * 
 */
package org.aksw.semantic_ci;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
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
	
	Model model;
	Map<String, Set<String>> namespacesToNames;

	public LocalGraph(String filename) {

		// create an empty model
		model = ModelFactory.createDefaultModel();

		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(filename);
		if (in == null) {
			throw new IllegalArgumentException("File: " + filename + " not found");
		}

		// read the RDF N-Triple file
		model.read(in, null, "N-TRIPLE");
		
		namespacesToNames = new HashMap<String, Set<String>>();
	}
	
	private void addToMap(String namespace, String name) {
		if (!namespacesToNames.containsKey(namespace))
		{
			HashSet<String> set = new HashSet<String>();
			set.add(name);
			namespacesToNames.put(namespace, set);
		}
		else
			namespacesToNames.get(namespace).add(name);
	}

	public String[] getAllVocabs() {
		
		StmtIterator iter = model.listStatements();
		
		while (iter.hasNext()) {
		    Statement r = iter.nextStatement();
		    Triple triple = r.asTriple();
		    
		    Node[] nodes = new Node[2];
		    
		    nodes[0] = triple.getPredicate();
		    nodes[1] = triple.getObject();
		    //nodes[2] = triple.getSubject();
		    
		    for (int i = 0; i < 2; i++) {
		    	Node node = nodes[i];
		    	
		    	if (!node.isBlank()
		    			&& !node.isLiteral()
		    			&& node.isURI()) 
		    	{
		    		String uri = "";
				    try {
				    	uri = node.getNameSpace();
				    	addToMap(uri, node.getLocalName());
					} catch (Exception e) {
						
					}
		    	}
		    }
		    
		}
		
		return namespacesToNames.keySet().toArray(new String[0]);
	}
}
