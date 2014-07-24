package ru.darvell.blkp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 14:53
 * Work with new connected client
 */
public class SocketWorker extends Thread{

	Socket socket;
	Heap heap;

	public SocketWorker(Socket socket, Heap heap){
		//System.out.println("SocketWorker create");
		this.socket = socket;
		this.heap = heap;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try{
			//System.out.println("Start Thread");
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();


			byte buf[] = new byte[64*1024];
			int len = inputStream.read(buf);
			String message = new String(buf,0,len-1);

			heap.addCommand(message);

			//System.out.println(message);
			String resp = "Ok";
			outputStream.write(resp.getBytes());


			//System.out.println("Stop Thread");
			inputStream.close();
			socket.close();
		}catch (Exception e){
			System.out.println("Exception in Thread: "+e.getMessage());
		}
	}
}
