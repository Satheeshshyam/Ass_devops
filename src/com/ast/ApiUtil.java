package com.ast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class ApiUtil {

	public static String currentLocation1(String deviceId) {
		String response="";
		try{
			String dateValues=MainClass.getTime();
			System.out.println(dateValues);
			byte[] authEncBytes = Base64.encodeBase64("admin@yatragenie.com:yg123$$".getBytes());
			String authStringEnc = new String(authEncBytes);
			HttpClient httpclientreq=HttpClientBuilder.create().build();
			HttpPost req=new HttpPost("http://www.assettrackr.com/assets/share_asset_location/auth_type/basic/");
			req.addHeader("Authorization", "Basic "+authStringEnc);
			req.addHeader("device_id",deviceId);
			req.addHeader("end_datetime_utc",dateValues.split(",")[1]);
			req.addHeader("start_datetime_utc",dateValues.split(",")[0]);
			
			HttpResponse res=httpclientreq.execute(req);
			BufferedReader br = new BufferedReader(new InputStreamReader((res.getEntity().getContent())));
			System.out.println("Output from Server .... \n");
			String output ;
			while ((output = br.readLine()) != null) {
				response=output;
			}
			System.out.println(response);
			System.out.println(res.getStatusLine().getStatusCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	

	public static String currentLocation(String deviceId) {
		String response="";
		try{
			
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			Calendar calendar = Calendar.getInstance(timeZone);
			SimpleDateFormat simpleDateFormat = 
			       new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
			simpleDateFormat.setTimeZone(timeZone);
			
			
			
			byte[] authEncBytes = Base64.encodeBase64("admin@yatragenie.com:yg123$$".getBytes());
			String authStringEnc = new String(authEncBytes);
			HttpClient httpclientreq=HttpClientBuilder.create().build();
			String time=calendar.getTimeInMillis()/1000+"";
			System.out.println("bala::"+time);
			HttpGet req=new HttpGet("http://www.assettrackr.com/assets/last_known_location/sn/"+
					deviceId+"/utime_utc/" 
					+time+
							"/auth_type/basic/");
			System.out.println("url:::"+req.getRequestLine());
			
			req.addHeader("Authorization", "Basic "+authStringEnc);
			HttpResponse res=httpclientreq.execute(req);
			BufferedReader br = new BufferedReader(new InputStreamReader((res.getEntity().getContent())));
			System.out.println("Output from Server .... \n");
			String output ;
			while ((output = br.readLine()) != null) {
				response=response+output;
			}
			System.out.println(response);
			System.out.println(res.getStatusLine().getStatusCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	public static void addAsset() {
		String deviceId="1211212121212";
		String deviceName="Komitla";
		try{
			
			
			String authString = "track@at.comasdasdas:mytrack123";
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: "+new Date()+":" + authStringEnc);

			URL obj = new URL("http://dev.assettrackr.com/assets/list_all_assets/auth_type/basic");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Basic " + authStringEnc);
			con.setRequestProperty("CURLOPT_USERPWD", "track@at.com:mytrack123");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

//			String urlParameters = "sn=" +deviceId+"&name=" +deviceName;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println(new Date()+"::Sending 'POST' request to URL :: "+":"  + MainClass.baseURL);
//			System.out.println(new Date()+"::Post parameters :"  + urlParameters);
			System.out.println(new Date()+"::Response Code : "  + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(new Date()+"::"+ response.toString());
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
