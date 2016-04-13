/**
 * 
 */
package org.aksw.semantic_ci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author kjunghanns
 * Class which uses the LOV SPARQL API to get the URL for the RDF representation of the vocab.
 */
public class LOVGrapper {

	static String query1 = "PREFIX vann:<http://purl.org/vocab/vann/>"
							+ "PREFIX voaf:<http://purl.org/vocommons/voaf#>"
							+ "PREFIX http: <http://www.w3.org/2011/http#>"
							+ "PREFIX rdf: <http://www.w3.org/ns/dcat#>"
							+ "SELECT DISTINCT ?vocabURI ?vocabNS ?distURL {"
							+ "   GRAPH <http://lov.okfn.org/dataset/lov>{"
							+ "        ?vocabURI a voaf:Vocabulary ."
							+ "        ?vocabURI vann:preferredNamespaceUri ?vocabNS ."
							+ "        ?vocabURI rdf:distribution ?distURL ."
							+ "}"
							+ " FILTER ( CONTAINS(str(?vocabURI) ,  \"";
	static String query2 = "\" ) && STRSTARTS(STR(?distURL), \"http://\") )"
							+ "} ORDER BY ?distURL";
	static String url = "http://lov.okfn.org/dataset/lov/sparql";
	
	String namespace;
	
	String JSONResponse;
	
	public LOVGrapper(String namespace) {
		this.namespace = namespace;
	}
	
	private boolean doRequest() throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		
		String data = "query=" 
					+ URLEncoder.encode(query1, "UTF-8") 
					+ URLEncoder.encode(namespace.substring(7, namespace.length() - 2), "UTF-8") 
					+ URLEncoder.encode(query2, "UTF-8");
		
		byte[] postData       = data.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		
		con.setDoOutput( true );
		con.setInstanceFollowRedirects( false );
		con.setRequestMethod( "POST" );
		// add request header
		con.setRequestProperty("Accept", "application/sparql-results+json,*/*;q=0.9");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		con.setRequestProperty("Content-Length", Integer.toString( postDataLength ));
		con.setUseCaches( false );
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write( postData );
		

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		if (responseCode != 200)
			return false;

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONResponse = response.toString();
		
		return true;
	}
	
	private void parseJSON() {
		
	}
}
