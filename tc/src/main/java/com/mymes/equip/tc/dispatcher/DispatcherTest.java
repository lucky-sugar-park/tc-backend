package com.mymes.equip.tc.dispatcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DispatcherTest {

	public static void main(String[] args) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
//			HttpPost postRequest=new HttpPost("http://localhost:8088/api/v1/ifsvc/request");
			HttpPost postRequest=new HttpPost("http://localhost:8088/api/v1/ifsvc/request-and-reply");
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Connection", "keep-alive");
			postRequest.setHeader("Content-Type", "application/json");
		
//			TcMessageRequest req=generateWriteTcMessageRequest();
			TcMessageRequest req=generateReadTcMessageRequest();
			Gson gson=new GsonBuilder().setPrettyPrinting().create();
			postRequest.setEntity(new StringEntity(gson.toJson(req)));
			
			HttpResponse response=client.execute(postRequest);
			if(response.getStatusLine().getStatusCode()==200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body=handler.handleResponse(response);
				System.out.println("RESPONSE :"+body);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static TcMessageRequest generateWriteTcMessageRequest() {
		TcMessageRequest request=new TcMessageRequest();
		request.setOpcode("opcode.test.write");
		request.setTransactionId("TRX-"+UUID.randomUUID().toString());
		request.setMessageId("MSG-"+UUID.randomUUID().toString());
		request.setTimestamp(new Date().getTime());
		request.setPushResult(false);
		request.setShouldReply(false);
		request.setSync(true);
		request.setSender("TC_DISPATCHER_TEST");
		request.setReceiver("TC_SYSTEM");
		
		Map<String, Object> data=new HashMap<>();
		data.put("name", "cpoisp");
		data.put("age", 47);
		request.setData(data);
		return request;
	}
	
	private static TcMessageRequest generateReadTcMessageRequest() {
		TcMessageRequest request=new TcMessageRequest();
		request.setOpcode("opcode.test.read");
		request.setTransactionId("TRX-"+UUID.randomUUID().toString());
		request.setMessageId("MSG-"+UUID.randomUUID().toString());
		request.setPushResult(false);
		request.setShouldReply(true);
		request.setSync(true);
		request.setSender("TC_DISPATCHER_TEST");
		request.setReceiver("TC_SYSTEM");
		
		return request;
	}
}
