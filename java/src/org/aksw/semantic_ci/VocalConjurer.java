/**
 * 
 */
package org.aksw.semantic_ci;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author kjunghanns This class trys o get a vocabulary from the internet. It
 *         needs an URL and will return the whole vocabulary as a String.
 */
public class VocalConjurer {

	private String url;
	private String vocab = null;

	public VocalConjurer(String url) {
		this.url = url;
	}

	public String getVocab() {
		if (vocab == null)
			try {
				boolean fetched = fetch();
				if (fetched) {
					boolean isValid = validateVocab();
					if (!isValid)
						return null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		return vocab;
	}

	boolean fetch() throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Accept", "application/rdf+xml");
		con.setRequestProperty("Accept-Charset", "utf-8");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		if (responseCode != 200)
			return false;

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		int linecounter = 0;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			
			if (linecounter++ < 7 && inputLine.startsWith("<html"))
				return false;
		}
		in.close();

		vocab = response.toString();

		return true;
	}

	private boolean validateVocab() {
		// create an empty model
		Model model = ModelFactory.createDefaultModel();
        try {
        	model.read(new ByteArrayInputStream(vocab.getBytes()), null);
		} catch (Exception e) {
			return false;
		}
        
        if (model.isEmpty())
        	return false;
        
        return true;
	}

	private void searchOnLOV() {

	}
}
