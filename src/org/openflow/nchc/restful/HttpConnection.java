package org.openflow.nchc.restful;


import java.net.*;
import java.io.*;

public class HttpConnection {

	private String urlString = null;
	private String method = null;
	
	public HttpConnection(String url_base, String method, String host, String port,
			String resourceLocation){
		this.urlString = (url_base + host + ':' + port + resourceLocation );
		this.method = method;
	}
	
	public void DoConnection() throws Exception {

		// construct URL
		URL url = null;
		url = new URL(urlString);
		
		HttpURLConnection connection = null;
		
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Content-Type", "text/plain; charset=\"utf8\"");
		connection.setRequestMethod(method);

		// execute HTTPS request
		int returnCode = connection.getResponseCode();
		InputStream connectionIn = null;
		if (returnCode==200)
			connectionIn = connection.getInputStream();
		else
			connectionIn = connection.getErrorStream();
		// print resulting stream
		BufferedReader buffer = new BufferedReader(new InputStreamReader(connectionIn));
		String inputLine;
		while ((inputLine = buffer.readLine()) != null){
			System.out.println(inputLine);
		}
			
		buffer.close();
	}
	
	
}
