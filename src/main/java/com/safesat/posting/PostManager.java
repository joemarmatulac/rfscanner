package com.safesat.posting;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;


public class PostManager {
private String endPoint= "http://5c3e0e1e.ngrok.io/safesat/api/rfid/read?code=";
ExecutorService executorService = Executors.newFixedThreadPool(50);
List<Future<HttpResponse>> futures= new ArrayList<>();
public PostManager() {
}
public String getEndPoint() {
	return endPoint;
}

public void setEndPoint(String endPoint) {
	this.endPoint = endPoint;
}

public void send(String endPoint) throws IOException, URISyntaxException {
}


}
