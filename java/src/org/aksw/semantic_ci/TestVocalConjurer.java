package org.aksw.semantic_ci;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVocalConjurer {

	static String[] urls = {"http://www.w3.org/1999/02/22-rdf-syntax-ns#"	//working
			, "http://purl.org/vocab/changeset/schema#"						//not working
			, "http://www.w3.org/ns/prov#"};								//working
	
	@Test
	public final void testGetVocab() {
		for (int i = 0; i < urls.length; i++) {
			VocalConjurer vc = new VocalConjurer(urls[i]);
			
			String vocab = vc.getVocab();
			
			if (i == 1)
				assertEquals(null, vocab);
			else
				assertNotEquals(null, vocab);
		}
	}

}
