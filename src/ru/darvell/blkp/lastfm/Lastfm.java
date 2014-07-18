package ru.darvell.blkp.lastfm;

import ru.darvell.blkp.lastfm.Parsers.Parsers;
import ru.darvell.blkp.lastfm.model.AuthResp;
import ru.darvell.blkp.lastfm.model.NowPlngResp;
import ru.darvell.blkp.utils.MD5;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import sun.security.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 15.07.14
 * Time: 12:53
 * Work with lastfm api
 */
public class Lastfm {
	private static final String API_KEY = "310fef49182709ec7f0a2e44fc82b9fa";
	private static final String API_SECRET = "37e759a0b0ba2215937e779d21ebe9e1";

	private static final String MAIN_URL = "http://ws.audioscrobbler.com/2.0/";
	private static final String AUTH_METOD = "auth.getMobileSession";
	private static final String UPD_NOW_PLNG = "track.updateNowPlaying";

	private final String USER_AGENT = "Mozilla/5.0";

	String sessionKey = "";
	String usrname = "";
	String usrpasswd = "";


	public Lastfm (String usrname, String usrpasswd) throws LastFMException{
		this.usrname = usrname;
		this.usrpasswd = usrpasswd;
		String s;
		if ((s = getAuth()) != null){
			this.sessionKey = s;
		}else{
			throw new LastFMException("don't connect to LastFM");
		}
	}

	public String getSessionKey(){
		return this.sessionKey;
	}


	String getSignature(String method){
		String sign = "";

		String tmp = "api_key"+API_KEY
				+"method"+method
				+"password"+usrpasswd
				+"username"+usrname+API_SECRET;
		System.out.println(tmp);
		sign = MD5.getMd5(tmp);
		return sign;
	}

	HttpsURLConnection getConnectionSSL (URL url,String parameters){
		try {
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			if(responseCode == 200 ){
				return connection;
			}else return null;

		}catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
			return null;
		}
	}

	HttpURLConnection getConnection (URL url,String parameters){
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			//connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			//connection.setRequestProperty("User-Agent", USER_AGENT);
			//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//connection.setRequestProperty("charset", "utf-8");

			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			if(responseCode == 200 ){
				return connection;
			}else return null;

		}catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
			return null;
		}
	}


	String getAuth(){
		String key = null;
		try{


			URL url = new URL(null, MAIN_URL+"?method="+AUTH_METOD ,new sun.net.www.protocol.https.Handler());

			String parameters = "username="+ usrname
								+"&password=" + usrpasswd
								+"&api_key=" + API_KEY
								+"&api_sig=" + getSignature(AUTH_METOD);
			//System.out.println(url.toString());
			//System.out.println(parameters);
			HttpsURLConnection connection = getConnectionSSL(url,parameters);
			if(connection != null ){
				InputStream is = connection.getInputStream();
				AuthResp authResp = Parsers.parseAuthResp(is);
				is.close();
				key = authResp.getKey();
			}

		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}

		return key;
	}



	public void sendNowPlay(String artist, String track){
		try{
			//URL url = new URL(null, MAIN_URL+UPD_NOW_PLNG,new sun.net.www.protocol.https.Handler());
			URL url = new URL(MAIN_URL);//+UPD_NOW_PLNG+"&api_key="+API_KEY);
			//System.out.println(url.toString());
			//System.out.println(sessionKey);
			String parameters = "method="+UPD_NOW_PLNG
								+"&artist="+artist
								+"&track="+track
								+"&sk="+sessionKey
								+"&api_key="+API_KEY
								+"&api_sig="+getSignature(UPD_NOW_PLNG)
								;
			System.out.println(MAIN_URL);
			System.out.println(parameters);
			HttpURLConnection connection = getConnection(url,parameters);
			/*
			if(connection != null ){
				InputStream is = connection.getInputStream();
				NowPlngResp nowPlngResp = Parsers.parseNowPlngResp(is);
				System.out.println(nowPlngResp.getArtist());
				is.close();
			}*/
			InputStream inputStream = connection.getInputStream();
			byte buff[] = new byte[64*1024];
			int count = inputStream.read(buff);
			System.out.println(new String(buff,0,count));
			inputStream.close();

		}catch (Exception e){
			System.out.println("Error: "+e.toString());
		}
	}

	//создание своего подкласса исключений со следующими двумя конструкторами
	class LastFMException extends Exception {
		// классический конструктор с сообщением о характере ошибки
		public LastFMException(String msg) {super(msg);}
		// пустой конструктор
		public LastFMException() {}
	}
}
