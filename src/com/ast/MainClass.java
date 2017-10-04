package com.ast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

public class MainClass {

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
		
//		String messagePassenger="Hello Balajirao,Your Journey will starts shortly, Your journey details- Ticket ID: asdasdasdasd,Vehicle Number: "+busNumber+", Driver Contact: "+driverContact+" Now Track your bus at: "+tinyUrl;
//		
//		SMSThread smsThreadWebRequest = new SMSThread(messagePassenger,customerContact);
//		smsThreadWebRequest.start();
//		System.out.println(CurrentLocation.currentLocation("353990030316603"));
//		JSONObject response =(JSONObject) JSONValue.parse(CurrentLocation.currentLocation("353990030316603"));
//		System.out.println("as:"+response);
//		JSONObject data=(JSONObject) response.get("result");
//		String val=getLocationInfo(Double.valueOf(data.get("latitude").toString()), Double.valueOf(data.get("longitude").toString()));
//		System.out.println("print::"+val);
//		currentLocation();
//		ApiUtil.currentLocation1("354868054219979");
		MainClass obj=new MainClass();
		//System.out.println(ApiUtil.checkrb("11"));
		
	}
	public MainClass() {
		// TODO Auto-generated constructor stub
//		try{
//			ApiUtil.currentLocation("354868054219979");	
//		}catch(Exception e){
//			e.printStackTrace();
//		}

		getTime();
		getDetailsForStart();
		getDetailsForBeforeBoard();
//		try{
//			pushAssetDetailsToOTA();
//		}catch(Exception e){
//			e.printStackTrace();
//		}

	}
	private void pushAssetDetailsToOTA() throws Exception{
		
		String url = "http://track1.yourbus.in/processGPSV2.php?acc_key=AsSeTtrkPoLH5MV3&gps_id=354868054219979&llt1=18.6643966666667,73.7663966666667,1408366983,0,0,0,258.89";

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
	public static String getLocationInfo( double l1, double l2) {

		 
		 String locationString="";
		    BigDecimal lat =  new BigDecimal(l1, MathContext.DECIMAL64);
		    BigDecimal lng =  new BigDecimal(l2, MathContext.DECIMAL64);

		    LatLng location = new LatLng(lat, lng);

		    try {
		     Geocoder geocoder;
		     geocoder = new Geocoder();//"gme-paxterrasoftware","BGUrf21sEFFgavmADGhfElvKs5c=");
		     GeocoderRequest geocoderRequest
		         = new GeocoderRequestBuilder()
		           .setLocation(location) // location
		           .setLanguage("en") // language
		           .getGeocoderRequest();
		       GeocodeResponse geocoderResponse;
		     geocoderResponse = geocoder.geocode(geocoderRequest);
		        List<GeocoderResult> results = geocoderResponse.getResults();
		        for( GeocoderResult geores: results ){
		            if( geores.getTypes().contains( "sublocality" ) ){
		             locationString=geores.getFormattedAddress();
		           }
		        }
		       // System.out.println("geocoderResponse:"+geocoderResponse);
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		        return locationString;
		    }
		    return locationString;
	}
	@SuppressWarnings("unchecked")
	private void getDetailsForBeforeBoard() {
		// TODO Auto-generated method stub
		Connection con=null;
		CallableStatement callableStatement = null;
		
		JSONArray dataArray=new JSONArray();
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://"+DBurl+":3306/yg_inventory",dbUname,dbPwd);  
			callableStatement = con.prepareCall("{call TICKET_DETAILS_FOR_BUS_TRACKING_SMS()}");
			ResultSet rs=callableStatement.executeQuery();  
			while(rs.next()){
				//replace
				JSONObject dataObject=new JSONObject();
				dataObject.put("device_id", rs.getString("device_id"));
				dataObject.put("passenger_details", rs.getString("passenger_details"));
				
				dataArray.add(dataObject);
			}
			System.out.println(new Date()+"::data before:"+dataArray);
			SMSGateWayConnectionOther(dataArray);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{con.close();}catch(Exception e){e.printStackTrace();}
		}
	}
	private void SMSGateWayConnectionOther(JSONArray dataArray) {
		
		for(int i=0;i<dataArray.size();i++){
			JSONObject dataObject=(JSONObject) dataArray.get(i);
			String passengerDetails=""+dataObject.get("passenger_details");
			String[] passengerDetailsArray = passengerDetails.split(",");
		
			
			JSONObject locationData =(JSONObject) JSONValue.parse(ApiUtil.currentLocation((String)dataObject.get("device_id")));//+dataObject.get("device_id")));
			System.out.println("as:"+locationData);
			JSONObject data=(JSONObject) locationData.get("result");
			String addressVal=getLocationInfo(Double.valueOf(data.get("latitude").toString()), Double.valueOf(data.get("longitude").toString()));

			for(int j=0;j<passengerDetailsArray.length;j++){
//				String customerName=passengerDetailsArray[j].split(":")[0];
				String customerContact=passengerDetailsArray[j].split(":")[1];
				String customerTicket=passengerDetailsArray[j].split(":")[2];
				String tinyUrl=getTicketTiny(customerTicket);
				String messagePassenger="Komitla Travels-, Your Bus is arriving soon at your boarding point,\nCurrent location:" +
						addressVal+"\nNow Track your bus live at: "+tinyUrl;
				if(!tinyUrl.equalsIgnoreCase("")){
					SMSThread smsThreadWebRequest = new SMSThread(messagePassenger,customerContact);
					smsThreadWebRequest.start();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getDetailsForStart() {
		Connection con=null;
		CallableStatement callableStatement = null;
		
		JSONArray dataArray=new JSONArray();
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://"+DBurl+":3306/yg_inventory",dbUname,dbPwd);  
			callableStatement = con.prepareCall("{call GET_DRIVER_DETAILS_FOR_BUS_TRACKING_SMS()}");
			ResultSet rs=callableStatement.executeQuery();  
			System.out.println(rs.getRow());
			while(rs.next()){
				JSONObject response =(JSONObject) JSONValue.parse(genarateTinyURL(rs.getString("device_id")));//rs.getString("device_id")));
				JSONObject dataObject=new JSONObject();
				dataObject.put("device_id", rs.getString("device_id"));
				dataObject.put("passenger_details", rs.getString("passenger_details"));
				dataObject.put("status", response.get("status"));
				dataObject.put("DRIVER_NO", rs.getString("DRIVER_NO"));
				dataObject.put("VEHICLE_NO", rs.getString("VEHICLE_NO"));
				dataObject.put("atly", response.get("atly"));
				dataArray.add(dataObject);
			}  
			System.out.println(new Date()+"::data:"+dataArray);
			SMSGateWayConnection(dataArray);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{con.close();}catch(Exception e){e.printStackTrace();}
		}  
	}  
	
	private void SMSGateWayConnection(JSONArray dataArray) {
		
		for(int i=0;i<dataArray.size();i++){
			JSONObject dataObject=(JSONObject) dataArray.get(i);
			String passengerDetails=""+dataObject.get("passenger_details");
			String[] passengerDetailsArray = passengerDetails.split(",");
			String busNumber=""+dataObject.get("VEHICLE_NO");
			String driverContact=""+dataObject.get("DRIVER_NO");
			String tinyUrl=""+dataObject.get("atly");
			String status=""+dataObject.get("status");
			for(int j=0;j<passengerDetailsArray.length;j++){

				String customerName=passengerDetailsArray[j].split(":")[0];
				String customerContact=passengerDetailsArray[j].split(":")[1];
				String customerTicket=passengerDetailsArray[j].split(":")[2];
				String messagePassenger="Hello "+customerName+",Your journey will start shortly,\nYour journey details- Ticket ID: "+customerTicket
						+",Vehicle Number: "+busNumber+", Driver Contact: "+driverContact+"\nNow Track your bus live at: "+tinyUrl;
				if(status.equalsIgnoreCase("success")){
					System.out.println(customerTicket+" "+tinyUrl);
					insertCatcheData(customerTicket,tinyUrl);

					SMSThread smsThreadWebRequest = new SMSThread(messagePassenger,customerContact);
					smsThreadWebRequest.start();
				}
			}
		}
	}
	private String genarateTinyURL(String deviceID) throws Exception{
		
		
		String authString = astAuthDetails;//"track@at.comasdasdas:mytrack123";
		System.out.println("auth string: " + authString);
		byte[] authEncBytes = Base64.encodeBase64("admin@yatragenie.com:yg123$$".getBytes());
		String authStringEnc = new String(authEncBytes);

		System.out.println("Base64 encoded auth string: "+new Date()+":" + authStringEnc);

		URL obj = new URL(baseURL+"/auth_type/basic");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + authStringEnc);
		String dateValues=getTime();
		con.setRequestProperty("device_id", deviceID);
		con.setRequestProperty("start_datetime_utc", dateValues.split(",")[0]);
		con.setRequestProperty("end_datetime_utc", dateValues.split(",")[1]);
		con.setRequestProperty("CURLOPT_USERPWD", "admin@yatragenie.com:yg123$$");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		System.out.println("deviceID::"+deviceID);
		String urlParameters = "device_id=" +deviceID+"&start_datetime_utc=" +dateValues.split(",")[0]+"&end_datetime_utc="+dateValues.split(",")[1];
//		String urlParameters = "device_id=" +deviceID+"&start_datetime_utc=1496221305135&end_datetime_utc=1496221305135";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println(new Date()+"::Sending 'POST' request to URL :: "+":"  + baseURL);
		System.out.println(new Date()+"::Post parameters :"  + urlParameters);
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
		
		return response.toString();
	}
	public static void sendSMS(String message,String contactNum) {
		try{
			if(SendSmsService(contactNum, message)){
				contactNum=contactNum.trim();
				if(contactNum.length()==10){
					String DestNo="91"+contactNum;
					message=message.replace("%","%25").replace(" ","%20").replace("\n","%0A").replace("\r","%0A").replace("!","%21").replace("*","%2A").replace("@","%40");
				}
			}
		}catch(Exception ex){ex.printStackTrace();}
	}
	@SuppressWarnings("unchecked")
	public static boolean SendSmsService(String phoneNum,String message) {
		  boolean messageStatus=false;
		  System.out.println(phoneNum +"   "+message);
		  String serverHostname ="115.119.204.222";
		 //String serverHostname ="192.168.102.40";

		        Socket echoSocket = null;
		        PrintWriter out = null;
		        BufferedReader in = null;

		        try {
		         
		         System.out.println ("Attemping to connect to server " +serverHostname + "");

		         int port = 10101;

		            echoSocket = new Socket(serverHostname, port);
		            out = new PrintWriter(echoSocket.getOutputStream(), true);
		            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		        } catch (Exception e) {
		            System.err.println("Don't know about host: " + serverHostname);
		        } 
		        try{
		        	 JSONObject obj=new JSONObject();
				        obj.put("phoneNumber", phoneNum);
				        obj.put("message", message);
		         BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		         out.println(obj.toJSONString());
		         
		            messageStatus=Boolean.valueOf(in.readLine());

		         out.close();
		         in.close();
		         stdIn.close();
		         echoSocket.close();
		        }catch(Exception e){
		         e.printStackTrace();
		        }
		        return messageStatus;
		 }
	public static String getTime() {
		
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -5);
		cal.add(Calendar.MINUTE, -30);
		
		String value =dateFormat.format(cal.getTime());

		cal.add(Calendar.HOUR_OF_DAY, 12);
		value=value+","+dateFormat.format(cal.getTime());
		System.out.println("cal::"+value);
		return value;
	}
	
	public String getTicketTiny(String TicketId) {
		Connection con=null;
		CallableStatement callableStatement = null;
		String url="";
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://"+DBurl+":3306/yg_inventory",dbUname,dbPwd);  
			callableStatement = con.prepareCall("{call SMS_CATCHE_DATA_ACTIONS(?,?,?)}");
			callableStatement.setString(1, TicketId);
			callableStatement.setString(2, "");
			callableStatement.setString(3, "FETCH");
			ResultSet rs=callableStatement.executeQuery();  
			while(rs.next()){
				url=rs.getString("TINY_URL");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{con.close();}catch(Exception e){e.printStackTrace();}
		}  
		return url;
	}
	public void	insertCatcheData(String TicketId,String TinyUrl) {
		Connection con=null;
		CallableStatement callableStatement = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://"+DBurl+":3306/yg_inventory",dbUname,dbPwd);  
			callableStatement = con.prepareCall("{call SMS_CATCHE_DATA_ACTIONS(?,?,?)}");
			callableStatement.setString(1, TicketId);
			callableStatement.setString(2, TinyUrl);
			callableStatement.setString(3, "INSERT");
			int val=callableStatement.executeUpdate();  
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{con.close();}catch(Exception e){e.printStackTrace();}
		}
	}
}
