package org.openflow.nchc.restful;


import java.net.*;
import java.io.*;

public class HttpConnection {

	private String urlString = null;
	private String method = null;
	private String respond = null;
	
	public HttpConnection(String method, String host, String port,
			String resourceLocation){
		this.urlString = ("http://" + host + ':' + port + resourceLocation );
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
		MessageHandler.isRespond = false;
		int returnCode = connection.getResponseCode();
		
		InputStream connectionIn = null;
		if (returnCode==200)
			connectionIn = connection.getInputStream();
		else
			connectionIn = connection.getErrorStream();
		// print resulting stream
		MessageHandler.isRespond = true;
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(connectionIn));
		String inputLine;
		while ((inputLine = buffer.readLine()) != null){
			System.out.println("Get Http respond data");
			this.respond = inputLine;
		}	

	}

	public String getRespond() {
		return respond;
	}

	public void setRespond(String respond) {
		this.respond = respond;
	}
	
	
}
