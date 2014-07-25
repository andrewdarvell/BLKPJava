package ru.darvell.blkp.serialport;


import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import ru.darvell.blkp.Heap;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 14.07.14
 * Time: 16:28
 * Module SEND and GET commands from Arduino
 */
public class Arduino implements Runnable{

	CommPortIdentifier portId;
	String command;
	boolean runnig;
	Heap heap;
	Thread t;

	public Arduino(Heap heap) throws NoSuchPortException {

		//System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/tty0");
		//portId = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0");
		portId = CommPortIdentifier.getPortIdentifier("/dev/tty0");
		this.heap = heap;
		t = Thread.currentThread();
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		try{
			SerialPort serialPort =(SerialPort) portId.open("Demo application", 9600);
			runnig = true;
			OutputStream outstream = serialPort.getOutputStream();
			InputStream inputStream = serialPort.getInputStream();
			Thread.sleep(5000);

			outstream.write("s100200p".getBytes());
			Thread.sleep(3000);

			while (runnig){
				byte buff[] = new byte[64*1024];
				int length = inputStream.read(buff);
				//heap.addCommand(new String(buff,0,length));
				if (!command.equals("")){
					System.out.println("doCommand");
					setCommand("");
				}
				t.wait(500);
			}
			serialPort.close();

		}catch (Exception e){
			System.out.println("Serial error: "+e.getMessage());
		}
	}

	synchronized public void setCommand(String command){
		this.command = command;
	}
}
