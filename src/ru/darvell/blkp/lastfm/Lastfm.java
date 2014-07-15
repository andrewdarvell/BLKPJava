package ru.darvell.blkp.lastfm;

import ru.darvell.blkp.utils.MD5;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;

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
	private static final String AUTH_METOD = "?&method=auth.getMobileSession";
	private final String USER_AGENT = "Mozilla/5.0";

	String sessionKey = "";
	String apiSignature = "";
	String usrname = "";
	String usrpasswd = "";

	public Lastfm(String usrname, String usrpasswd){
		this.usrname = usrname;
		this.usrpasswd = usrpasswd;
		getAuth();
	}


	String getAuth(){
		String key = null;
		try{

			apiSignature = getSignature();
			//URL url = new URL(MAIN_URL+AUTH_METOD);
			URL url = new URL(null, MAIN_URL+AUTH_METOD ,new sun.net.www.protocol.https.Handler());

			String parameters = "username="+usrname+"&password="+usrpasswd+"&api_key="+API_KEY+"&api_sig="+apiSignature;

			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);


		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}

		return key;
	}

	String getSignature(){
		String sign = "";
		sign = MD5.getMd5("api_key"+API_KEY+"methodauth.getMobileSessionpassword"+usrname+"username"+usrname+API_SECRET);
		return sign;
	}

}
