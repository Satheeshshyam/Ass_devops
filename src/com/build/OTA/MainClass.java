package com.build.OTA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ast.ApiUtil;

public class MainClass {

	public MainClass(String apiURL) {
		try{
			pushAssetDetailsToOTA(apiURL);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void pushAssetDetailsToOTA(String apiURL) throws Exception{
		
		JSONObject locationData =(JSONObject) JSONValue.parse(ApiUtil.currentLocation("354868054219979"));
		System.out.println(locationData);
		
		String url = apiURL+"acc_key=AsSeTtrkPoLH5MV3&gps_id=354868054219979&llt1=18.6643966666667,73.7663966666667,1408366983,0,0,0,258.89";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
//		con.setRequestProperty("User-Agent", USER_AGENT);
//		con.setRequestProperty("acc_key", "AsSeTtrkPoLH5MV3");
//		con.setRequestProperty("gps_id", "354868054219979");
//		con.setRequestProperty("llt1","18.6643966666667,73.7663966666667,1408366983,0,0,0,258.89");						

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainClass object=new MainClass(args[0]);
		
	}

}
