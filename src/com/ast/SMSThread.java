package com.ast;

import java.io.IOException;


public class SMSThread extends Thread {

	private final int TIMEOUT = 100;	
	String contactNo="",message="";
	
	public SMSThread(String message,String contactNo) {
		this.contactNo=contactNo;
		this.message=message;
	}
	public void run() {
		
		try {
			Thread.sleep(TIMEOUT);
			MainClass.sendSMS(message, contactNo);
			
		} catch (InterruptedException ex) {
			
		} 
		}
	
}
