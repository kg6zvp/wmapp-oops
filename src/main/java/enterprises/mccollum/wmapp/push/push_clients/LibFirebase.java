package enterprises.mccollum.wmapp.push.push_clients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.ExponentialBackOff;

public class LibFirebase {
	public static final String SERVER_KEY = "AIzaSyAnGWCVE06NcZn8vJfg0MxDJNTuxxZymO4";
	public static final String SENDER_ID = "653658321849";
	public static final int FCM_MULTICAST_LIMIT = 1000;
	
	public static class FCM{
		public static final String AUTHORIZATION_HEADER = "key="+LibFirebase.SERVER_KEY;
		public static final String KEY_TO = "to";
		public static final String KEY_DATA = "data";
		
		public static final String SEND_URL = "https://fcm.googleapis.com/fcm/send";
		
		public static void sendMessage(MultiNotificationMessage objMsg){
			if(objMsg.registration_ids.size() > FCM_MULTICAST_LIMIT){
				for(MultiNotificationMessage msg : splitMessage(objMsg)){
					sendSingleMulticast(msg);
				}
			}else{
				sendSingleMulticast(objMsg);
			}
		}
		private static List<MultiNotificationMessage> splitMessage(MultiNotificationMessage input) {
			List<MultiNotificationMessage> messages = new ArrayList<>();
			JsonObject message = input.data;
			List<String> listA = input.registration_ids.subList(0, FCM_MULTICAST_LIMIT-1); //written out for readability by new programmers even though it's bad practice
			messages.add(new MultiNotificationMessage(listA, message));
			List<String> listB = input.registration_ids.subList(FCM_MULTICAST_LIMIT-1, input.registration_ids.size());
			messages.add(new MultiNotificationMessage(listB, message));
			return messages;
		}
		private static void sendSingleMulticast(MultiNotificationMessage objMsg){
			String msg = objMsg.toJson();
			System.out.println("Sending message: "+msg);
			HttpRequestFactory requestFactory = (new NetHttpTransport()).createRequestFactory();
			try {
				HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(SEND_URL), ByteArrayContent.fromString("application/json" , msg));
				request.setUnsuccessfulResponseHandler(
						new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff()));
				request.getHeaders().setAuthorization(AUTHORIZATION_HEADER);
				HttpResponse response = request.execute();
				System.out.println(
						String.format("Posting message...response:\n%s", response));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static void sendMessage(NotificationMessage objMsg){
			String msg = objMsg.toJson();
			System.out.printf("Sending message: %s\n",msg);
			System.out.println("");
			HttpRequestFactory requestFactory = (new NetHttpTransport()).createRequestFactory();
			try {
				HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(SEND_URL), ByteArrayContent.fromString("application/json" , msg));
				request.setUnsuccessfulResponseHandler(
						new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff()));
				request.getHeaders().setAuthorization(AUTHORIZATION_HEADER);
				HttpResponse response = request.execute();
				System.out.println(
						String.format("Posting message...response:\n%s", response));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static class MultiNotificationMessage implements JsonAbleObject{
		List<String> registration_ids;
		JsonObject data;
		
		public MultiNotificationMessage(List<String> to, JsonObject payload){
			this.registration_ids = to;
			this.data = payload;
		}

		@Override
		public String toJson() {
			JsonObjectBuilder job = Json.createObjectBuilder();
			JsonArrayBuilder jab = Json.createArrayBuilder();
			for(String client : registration_ids){
				jab.add(client);
			}
			job.add("registration_ids", jab);
			job.add("data", data);
			return job.build().toString();
		}
	}
	public static class NotificationMessage implements JsonAbleObject{
		String to;
		JsonObject data;
		
		public NotificationMessage(String to, JsonObject msg){
			this.to = to;
			this.data = msg;
		}

		@Override
		public String toJson() {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("to", to);
			for(Entry<String, JsonValue> val : data.entrySet()){
				if(val.getValue().getValueType() == ValueType.OBJECT){
					val.getValue().toString();
					//TODO: Convert to string or something
				}
			}
			job.add("data", data);
			return job.build().toString();
		}
	}
}
