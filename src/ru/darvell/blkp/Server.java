package ru.darvell.blkp;

import ru.darvell.blkp.lastfm.Lastfm;
import ru.darvell.blkp.serialport.Arduino;

import java.net.ServerSocket;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 13:39
 * LastFM plugin for BLKPlayer.
 */

public class Server {
	static int port = 3425;
	public static void main(String args[]){

		try{
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("server is started");
			Heap heap = new Heap();

			//new Arduino(heap);

			Lastfm lastfm = new Lastfm("RottenDarvell", "zghjcnjvjcmrf");
			System.out.println(lastfm.getSessionKey());
			lastfm.sendNowPlay("Letzte%20Instanz","Winterträn");
			new Worker(heap,lastfm);

			while(true){
				new SocketWorker(serverSocket.accept(), heap);
			}
		}catch (Exception e){
			System.out.println("Error "+e.getMessage());
		}
	}
}