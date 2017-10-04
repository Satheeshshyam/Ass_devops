package com.ast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class PushLocations {

	/**
	 * @param args
	 */
	static String baseURL="";
	static String DBurl="";
	static String dbUname="";
	static String dbPwd="";
	static String astAuthDetails="";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//gathering required parameters
		baseURL= "http://www.assettrackr.com/assets/share_asset_location";//arg[0]
		DBurl="ygcloud.cnfhxzgp4e9u.ap-southeast-1.rds.amazonaws.com";//args[1];
		dbUname="ygroot";//args[2];
		dbPwd="PaxYG$2015";//args[3];
		astAuthDetails="admin@yatragenie.com:yg123$$";//args[4];
		getDeviceDetails();
	}
	public static String getDeviceDetails(){
		String status="";
		Connection con=null;
		CallableStatement callableStatement = null;
		JSONObject response=new JSONObject();
		JSONObject finalResponse=new JSONObject();
		try{
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			   Calendar calendar = Calendar.getInstance(timeZone);
			   SimpleDateFormat simpleDateFormat = 
			          new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
			   simpleDateFormat.setTimeZone(timeZone);
			   String time=calendar.getTimeInMillis()/1000+"";
				Class.forName("com.mysql.jdbc.Driver");  
				con=DriverManager.getConnection("jdbc:mysql://"+DBurl+":3306/yg_inventory",dbUname,dbPwd);  
				callableStatement = con.prepareCall("{call VEHICLE_DETAILS_FOR_OTA()}");
				ResultSet rs=callableStatement.executeQuery();  
			while(rs.next()){
				JSONObject data=new JSONObject();
				data.put("inventoryId",rs.getString("INVENTORY_ID"));
				data.put("vehicleNumber",rs.getString("VEHICLE_NO"));
				data.put("deviceId",rs.getString("DEVICE_ID"));
				
				try{
					String completeLocation=currentLocation(rs.getString("DEVICE_ID").toString());
					org.json.JSONObject locationJson=new org.json.JSONObject(completeLocation);
					if(locationJson.get("status").toString().equalsIgnoreCase("success")){
						   org.json.JSONObject resultArray=locationJson.getJSONObject("result");
							response.put("longitude",resultArray.get("longitude"));
							response.put("latitude",resultArray.get("latitude"));
							response.put("speed",resultArray.get("speed"));
							response.put("status","200");
							response.put("message","success");
							response.put("deviceId",rs.getString("DEVICE_ID"));
							response.put("time",time);
							status=pushAssetDetailsToOTA(response);
							finalResponse.put("status",status);
						
					}else{
						finalResponse.put("status","409");
						finalResponse.put("message","There is some technical issue.Please try again later");
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return JSONValue.toJSONString(finalResponse);
	}
	private static String pushAssetDetailsToOTA(JSONObject object) throws Exception{
		  
		  
		  String url = "http://track1.yourbus.in/processGPSV2.php?acc_key=AsSeTtrkPoLH5MV3&gps_id="+(String)object.get("deviceId")+
				  			"&llt1="+(String)object.get("longitude")+","+(String)object.get("latitude")+","+(String)object.get("time")+","+(String)object.get("speed")+","+0+","+0+",NA";

		  URL obj = new URL(url);
		  HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		  // optional default is GET
		  con.setRequestMethod("GET");

		  //add request header
		//  con.setRequestProperty("User-Agent", USER_AGENT);
		//  con.setRequestProperty("acc_key", "AsSeTtrkPoLH5MV3");
		//  con.setRequestProperty("gps_id", "354868054219979");
		//  con.setRequestProperty("llt1","18.6643966666667,73.7663966666667,1408366983,0,0,0,258.89");      

		  int responseCode = con.getResponseCode();
		  System.out.println("\nSending 'GET' request to URL : " + url);
		  System.out.println("Response Code : " + responseCode);

		  BufferedReader in = new BufferedReader(
		          new InputStreamReader(con.getInputStream()));
		  String inputLine;
		  String response = "";

		  while ((inputLine = in.readLine()) != null) {
		   response=response+inputLine;
		  }
		  in.close();

		  //print result
		  System.out.println("responseCode::"+responseCode);
		 return responseCode+"";
		 }
	public static String currentLocation(String deviceId) throws Exception{
		  String response="";
		   
		   TimeZone timeZone = TimeZone.getTimeZone("UTC");
		   Calendar calendar = Calendar.getInstance(timeZone);
		   SimpleDateFormat simpleDateFormat = 
		          new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		   simpleDateFormat.setTimeZone(timeZone);
		   
		   
		   sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		  // byte[] authEncBytes = Base64.encodeBase64("admin@yatragenie.com:yg123$$".getBytes());
		   String authStringEnc = encoder.encode("admin@yatragenie.com:yg123$$".getBytes());//new String(authEncBytes);
		   HttpClient httpclientreq=HttpClientBuilder.create().build();
		   String time=calendar.getTimeInMillis()/1000+"";
		   System.out.println("bala::"+time);
		   HttpGet req=new HttpGet("http://www.assettrackr.com/assets/last_known_location/sn/"+
		     deviceId+"/utime_utc/" +time+
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
		 
		  return response;
		 }
}
