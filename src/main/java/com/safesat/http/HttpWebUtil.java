package com.safesat.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.net.URI;

public class HttpWebUtil {
	
	public static HttpResponse POST(String jsonJsonUrl, String request, String mimeType) throws ClientProtocolException, IOException {
		HttpPost postRequest = new HttpPost(jsonJsonUrl);
		HttpClient client = HttpWebClientUtil.httpClient();
		StringEntity input = new StringEntity(request);
		input.setContentType(mimeType);
		postRequest.setEntity(input);
		HttpResponse response = client.execute(postRequest);
		return response;
	}

	public static HttpResponse GET(URI uri) throws IOException {
		HttpGet req = new HttpGet(uri);
		HttpClient client = HttpWebClientUtil.httpClient();
		return client.execute(req);
	}

	public static String content(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public static String dtoToStr(Object dto){
		try {
			Writer strWriter = new StringWriter();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(strWriter, dto);
			return strWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
