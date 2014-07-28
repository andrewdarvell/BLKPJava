package ru.darvell.blkp;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 14:53
 * Work with new connected client
 */
public class SocketWorker extends Thread{

	private static Logger log = Logger.getLogger(SocketWorker.class.getName());

	Socket socket;
	Heap heap;

	public SocketWorker(Socket socket, Heap heap){
		this.socket = socket;
		this.heap = heap;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try{

			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();


			byte buf[] = new byte[64*1024];
			int len = inputStream.read(buf);


			String message = new String(buf,0,len-1);

			if (message.length() != 0){
				String resp = "Ok";
				outputStream.write(resp.getBytes());
				outputStream.close();
				log.info("got command "+message);

				if(Pattern.matches("^P_.*$",message)){

					Map<String,String> map = new HashMap();
					String[] commands = message.split("P_");

					map.put("time",commands[1]);
					map.put("id",commands[1]);
					String[] artist_track = commands[2].split("\\s-\\s");

					map.put("artist", artist_track[0]);
					map.put("track", artist_track[1]);
					map.put("command","play");
					heap.addCommand(map);
				}
				if(Pattern.matches("^S_.*$",message)){
					Map<String,String> map = new HashMap();
					String[] command = message.split("_");
					map.put("id",command[1]);
					map.put("command","stop");
					heap.addCommand(map);
				}
			}
			inputStream.close();
			socket.close();
		}catch (Exception e){
			log.error(e.toString());
		}
	}
}
