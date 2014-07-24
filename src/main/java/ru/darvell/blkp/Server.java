package ru.darvell.blkp;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ru.darvell.blkp.lastfm.Lastfm;
import ru.darvell.blkp.serialport.Arduino;

import java.net.ServerSocket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


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

			Lastfm lastfm = new Lastfm("RottenDarvell", "zghjcnjvjcmrf");

			GregorianCalendar gregorianCalendar = new GregorianCalendar();


			int timestamp = (int) (new Date().getTime()/1000);


			lastfm.sendScrobble("LetzteInstanz","Winterträn",timestamp);
			new Worker(heap,lastfm);

			while(true){
				new SocketWorker(serverSocket.accept(), heap);
			}
		}catch (Exception e){
			log.error(e.getMessage());
		}
	}
}