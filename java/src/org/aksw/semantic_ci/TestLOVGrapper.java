package org.aksw.semantic_ci;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLOVGrapper {
	
	static String namespace = "http://purl.org/vocab/changeset/schema#";

	@Test
	public final void testFetchRDFURL() {
		LOVGrapper grapper = new LOVGrapper(namespace);
		
		String rdfurl = grapper.fetchRDFURL();
		
		assertNotNull(rdfurl);
	}

}
