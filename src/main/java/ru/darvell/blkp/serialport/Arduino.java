package ru.darvell.blkp.serialport;


import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import org.apache.log4j.Logger;
import ru.darvell.blkp.Heap;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 14.07.14
 * Time: 16:28
 * Module SEND and GET commands from Arduino
 */
public class Arduino extends Thread{

    private static Logger log = Logger.getLogger(Arduino.class.getName());

	CommPortIdentifier portId;
	ArrayList<Map<String,String>> commands;
	boolean runnig;
	Heap heap;
	Thread t;

	public Arduino(Heap heap) throws NoSuchPortException {

		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		//System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/tty0");
		portId = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0");
		//portId = CommPortIdentifier.getPortIdentifier("/dev/tty0");
		this.heap = heap;
		commands = new ArrayList<>();
		t = this;
		t.setDaemon(true);
		t.start();
        log.info("Arduino create");

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
				//int length = inputStream.read(buff);
				//heap.addCommand(new String(buff,0,length));
				Map<String,String> command = getCommand();
				if(command!=null){
				    String commandName = command.get("name");
					switch (commandName){
						case "show":doShow(command, outstream);
							break;
					}
				}
				t.wait(500);
			}
			serialPort.close();

            log.info("djskd");
		}catch (Exception e){
			log.error(e.toString());
		}
	}

	synchronized public void addCommand(Map<String,String> command){
		commands.add(command);
	}

	Map<String,String> getCommand(){
		if (commands.size()>0){
			Map<String,String> command = commands.get(0);
			command.remove(0);
			return command;
		}
		return null;
	}

	void doShow(Map<String,String> command,OutputStream outputStream){
		try{
			String strCommand = "s"+command.get("artist")+"&"+command.get("track");
			outputStream.write(strCommand.getBytes());

		}catch (Exception e){
			log.error(e.toString());
		}

	}

}
