package ru.darvell.blkp;




import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ru.darvell.blkp.lastfm.Lastfm;
import ru.darvell.blkp.serialport.Arduino;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import java.net.Socket;
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

			new Worker(heap,lastfm);
			//new Worker(heap,lastfm,arduino);
			/*
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String s="hello";
			Socket socket = new Socket("localhost",3426);
			socket.getOutputStream().write(s.getBytes());
			socket.close();
            */

			while(true){
				new SocketWorker(serverSocket.accept(), heap);
			}


		}catch (Exception e){
			log.error(e.toString());
		}
	}
}