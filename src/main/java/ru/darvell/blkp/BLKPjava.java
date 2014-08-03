package ru.darvell.blkp;




import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ru.darvell.blkp.lastfm.Lastfm;
import ru.darvell.blkp.serialport.Arduino;


import java.io.*;
import java.net.ServerSocket;

import java.util.HashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 13:39
 * LastFM plugin for BLKPlayer.
 */

public class BLKPjava {

	private static Logger log = Logger.getLogger(BLKPjava.class.getName());


	public static void main(String args[]){

		try {
			DOMConfigurator.configure("etc/log4j-config.xml");
		} catch (Exception e) {
			log.error("problem with log4j config file");
		}

		try{

			Heap heap = new Heap();

			Arduino arduino = new Arduino(heap);


			Lastfm lastfm = new Lastfm("RottenDarvell", "zghjcnjvjcmrf");

			//new Worker(heap,lastfm);
			new Worker(heap,lastfm,arduino);
			new Server(heap);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				//new SocketWorker(serverSocket.accept(), heap);

				String command = bufferedReader.readLine();
				Map<String,String> map = new HashMap<>();
				map.put("command",command);
				heap.addCommand(map);

			}


		}catch (Exception e){
			log.error(e.toString());
		}
	}
}