package org.aksw.semantic_ci;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLocalGraph {

	@Test
	public final void testLocalGraph() {
		LocalGraph lg;
		
		try {
			lg = new LocalGraph("/home/kjunghanns/x.ntriples");
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
			
	}

	@Test
	public final void testGetAllVocabs() {
		LocalGraph lg = null;
		
		try {
			lg = new LocalGraph("/home/kjunghanns/x.ntriples");
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		String[] namespaces = lg.getAllVocabs();
		
		
		for (int i = 0; i < namespaces.length; i++) {
			System.out.println(namespaces[i]);
		}
		
		assertNotNull(namespaces);
		assertNotEquals(namespaces.length, 0);
	}

}
