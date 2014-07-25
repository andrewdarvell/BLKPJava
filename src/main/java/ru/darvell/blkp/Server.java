package ru.darvell.blkp;



import jouvieje.bass.BassInit;
import jouvieje.bass.structures.HSTREAM;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ru.darvell.blkp.lastfm.Lastfm;


import java.net.ServerSocket;

import java.util.Date;
import java.util.GregorianCalendar;


import static jouvieje.bass.Bass.*;


/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 13:39
 * LastFM plugin for BLKPlayer.
 */

public class Server {

	private static Logger log = Logger.getLogger(Server.class.getName());

	static int port = 3425;
	public static void main(String args[]){

		try {
			DOMConfigurator.configure("etc/log4j-config.xml");
		} catch (Exception e) {
			log.error("problem with log4j config file");
		}

		try{
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("server is started");
			Heap heap = new Heap();

			//new Arduino(heap);
			//Player player = new Player(new URL("http://streams.balboatech.com:8000/").openStream());
			//URLConnection urlConnection = new URL("http://radio.flex.ru:8000/radionami").openConnection();
			//URLConnection urlConnection = new URL("http://streams.balboatech.com:8000").openConnection();
			//urlConnection.connect();

			BassInit.loadLibraries();


			if(((BASS_GetVersion() & 0xFFFF0000) >> 16) != BassInit.BASSVERSION()) {
				log.error("An incorrect version of BASS.DLL was loaded");
				return;
			}

			if (!BASS_Init(1, 44100, 0, null, null)) {
				log.error("Can't initialize device");
				return;
			}

			HSTREAM hstream = BASS_StreamCreateURL("http://streams.balboatech.com:8000/",0,0,null,null);
			BASS_ChannelPlay(hstream.asInt(),true);


			Lastfm lastfm = new Lastfm("RottenDarvell", "zghjcnjvjcmrf");
			//lastfm.sendNowPlay("LetzteInstanz","Winterträn");

			GregorianCalendar gregorianCalendar = new GregorianCalendar();


			int timestamp = (int) (new Date().getTime()/1000);


			//lastfm.sendScrobble("LetzteInstanz","Winterträn",timestamp);
			new Worker(heap,lastfm);

			while(true){
				new SocketWorker(serverSocket.accept(), heap);
			}
		}catch (Exception e){
			log.error(e.getMessage());
		}
	}
}