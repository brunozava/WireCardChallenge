package br.com.brunozava.tests;

import java.util.Base64;

import io.restassured.http.ContentType;

public interface Constantes {
	
	String CREDENTIALS = "LYIK8JBNJW1JWJY3XVH329OO5IIZS5IJ" + ":" + "UYTJONSUQPOHSF9F28RAOZMLJGRALTGSXDOEOWHR";
	
	String APP_BASE_URL = "https://sandbox.moip.com.br/";
	//String ACCESS_TOKEN = "TFlJSzhKQk5KVzFKV0pZM1hWSDMyOU9PNUlJWlM1SUo6VVlUSk9OU1VRUE9IU0Y5RjI4UkFPWk1MSkdSQUxUR1NYRE9FT1dIUg==";
	String ACCESS_TOKEN = Base64.getEncoder().encodeToString(CREDENTIALS.getBytes());
	//Integer APP_PORT = 443; //http -> 80
	//String APP_BASE_PATH = "";
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	long MAX_TIMEOUT = 50000L;

}
