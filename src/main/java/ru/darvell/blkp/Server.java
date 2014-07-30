package ru.darvell.blkp;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 30.07.14
 * Time: 14:40
 * Server
 * Listen connections
 */
public class Server extends Thread{

	private static Logger log = Logger.getLogger(Server.class.getName());

	static int port = 3425;

	Thread t;
	ServerSocket serverSocket;
	Heap heap;

	public Server(Heap heap) throws IOException {
		serverSocket = new ServerSocket(port);
		this.heap = heap;
		t = this;
		t.setDaemon(true);
		log.info("Server start");
		t.start();
	}

	@Override
	public void run(){
		while(true){
			try {
				new SocketWorker(serverSocket.accept(), heap);
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
	}

}
