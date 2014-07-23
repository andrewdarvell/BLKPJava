package ru.darvell.blkp.lastfm;

import ru.darvell.blkp.lastfm.Parsers.Parsers;
import ru.darvell.blkp.lastfm.model.AuthResp;
import ru.darvell.blkp.lastfm.model.NowPlngResp;
import ru.darvell.blkp.utils.MD5;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	private static final String UPD_NOW_PLNG_METHOD = "track.updateNowPlaying";
	private static final String GET_TOKEN_METHOD = "auth.getToken";

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


	String getSignature(Map<String, String> params){
		Set<String> sortedParams = new TreeSet<>(params.keySet());
		StringBuilder stringBuilder = new StringBuilder();
		for (String s :sortedParams){
			stringBuilder.append(s);
			stringBuilder.append(params.get(s));
		}
		stringBuilder.append(API_SECRET);
		return MD5.getMd5(stringBuilder.toString());
	}

	String buildPostBody(Map<String,String> params){
		StringBuilder postBody = new StringBuilder();
		Set<String> sortedParams = new TreeSet<>(params.keySet());
		for (String s:sortedParams){
			postBody.append("&");
			postBody.append(s);
			postBody.append("=");
			postBody.append(params.get(s).replace("\\s","%20"));
		}
		return postBody.toString();
	}

	HttpsURLConnection getConnectionSSL (URL url,Map<String,String> parameters){
		try {
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			byte[] postDataBytes = buildPostBody(parameters).getBytes("UTF-8");
			connection.getOutputStream().write(postDataBytes);

			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			if(responseCode == 200 ){
				return connection;
			}else{
				InputStream is = connection.getErrorStream();
				byte[] buff = new byte[64*1024];
				int i = is.read(buff);
				String mess = new String(buff,0,i);
				System.out.println("Error LOGIN code = "+responseCode+" "+mess);
				return null;
			}
		}catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
			return null;
		}
	}

	HttpURLConnection getConnection (URL url,String parameters){
		try {

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			byte[] postDataBytes = parameters.getBytes("UTF-8");
			connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));



			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);


			connection.getOutputStream().write(postDataBytes);
			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			/*
			if(responseCode == 200 ){
				return connection;
			}else return null;
			*/
			return connection;

		}catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
			return null;
		}
	}


	String getAuth(){
		String key = null;
		try{
			URL url = new URL(null, MAIN_URL,new sun.net.www.protocol.https.Handler());

			Map<String,String> params = new HashMap<>();
			params.put("api_key",API_KEY);
			params.put("method", AUTH_METOD);
			params.put("password",usrpasswd);
			params.put("username",usrname);
			params.put("api_sig",getSignature(params));

			HttpsURLConnection connection = getConnectionSSL(url,params);
			if(connection != null ){
				AuthResp authResp = Parsers.parseAuthResp(connection.getInputStream());
				key = authResp.getKey();
			}
			connection.disconnect();
		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}
		return key;
	}



	public void sendNowPlay(String artist, String track){
		try{

			URL url = new URL(MAIN_URL);
			//URL url = new URL(null, MAIN_URL,new sun.net.www.protocol.http.Handler());
			String parameters = "method="+ UPD_NOW_PLNG_METHOD
								+"&artist="+artist
								+"&track="+track
								+"&sk="+sessionKey
								+"&api_key="+API_KEY
								+"&api_sig="+MD5.getMd5("api_key"+API_KEY+"artist"+artist+"method"+UPD_NOW_PLNG_METHOD+"sk"+sessionKey+"track"+track+API_SECRET);
								;

			System.out.println(MAIN_URL);
			System.out.println(parameters);
			HttpURLConnection connection = getConnection(url,parameters);

			if(connection != null ){
				InputStream is = connection.getInputStream();
				NowPlngResp nowPlngResp = Parsers.parseNowPlngResp(is);
				System.out.println(nowPlngResp.getArtist());
				is.close();
			}


			/*
			InputStream inputStream = connection.getErrorStream();
			byte buff[] = new byte[64*1024];
			int count = inputStream.read(buff);
			System.out.println(new String(buff,0,count));
			inputStream.close();
            */
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
