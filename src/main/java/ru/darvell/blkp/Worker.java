package ru.darvell.blkp;

import org.apache.log4j.Logger;
import ru.darvell.blkp.lastfm.Lastfm;
import ru.darvell.blkp.serialport.Arduino;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 17:10
 * Class execute commands
 */
public class Worker extends Thread{

	private static Logger log = Logger.getLogger(Worker.class.getName());

	Heap heap;
	Thread t;
	Lastfm lastfm;
	Arduino arduino;
	Boolean play = false;
	Map<String,String> nowPlay;
	int id;

    boolean inArduino = false;

	Worker(Heap heap,Lastfm lastfm,Arduino arduino){
		//System.out.println("worker create");
		this.heap = heap;
		this.lastfm = lastfm;
		id = 0;
		this.arduino = arduino;
		t = this;
		t.setDaemon(true);
		t.start();
	}

	Worker(Heap heap,Lastfm lastfm){
		//System.out.println("worker create");
		this.heap = heap;
		this.lastfm = lastfm;
		this.arduino = null;
		id = 0;
		t = this;
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		try{
			while(true){

				if(heap.getCount()>0){
					Map<String,String> command = heap.getCommand();
					log.info("got command: "+command.get("command"));
					switch (command.get("command")){
						case "play": doPlay(command);
							break;
						case "stop": doStop(command);
							break;
						case "sendPlayerStop": sendCommand("s");
							break;
						case "sendPlayerPlay": sendCommand("p");
							break;
						case "sendPlayerNext": sendCommand("n");
							break;
					}
				}
				t.sleep(1500);
				update();
			}
		}catch (Exception e){
			log.error(e.toString());
		}
	}

	void doPlay(Map<String,String> command){
		log.info("doPlay");
		id = Integer.parseInt(command.get("id"));
		lastfm.sendNowPlay(command.get("artist"),command.get("track"));
		nowPlay = command;
		play = true;
		if (arduino!=null){
            if (!inArduino){
                log.info("send_to_arduino");
                Map<String,String> arduinoCommand = new HashMap<>();
                arduinoCommand.put("name","show");
                arduinoCommand.put("artist",command.get("artist"));
                arduinoCommand.put("track",command.get("track"));
                arduino.addCommand(arduinoCommand);
                inArduino = true;
            }
		}
	}

	void doStop(Map<String,String> command){
		if(play && nowPlay != null){
			log.info("doStop");
			if(id == Integer.parseInt(command.get("id"))){
				log.info("do scrobble "+id);
				lastfm.sendScrobble(nowPlay.get("artist"),nowPlay.get("track"),Integer.parseInt(nowPlay.get("time")));
			}
			play = false;
			nowPlay = null;
            inArduino = false;
		}
	}
	void update(){
		if (play){
			lastfm.sendNowPlay(nowPlay.get("artist"),nowPlay.get("track"));
		}
	}

	void sendCommand(String commandStr){
		try{
			File lock = new File("/home/darvell/CProjects/mediaRPI/CRadio/lock");
			lock.createNewFile();
			if(lock.exists()){
				log.info("lock exist");
			}

			File command = new File("/home/darvell/CProjects/mediaRPI/CRadio/command");
			if (command.exists()){
				command.delete();
			}else {
				command.createNewFile();
			}

			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(command));

			bufferedWriter.write(commandStr);
			bufferedWriter.close();
			lock.delete();
			log.info("Sended command to player: "+commandStr);
		}catch (Exception e){
			log.error(e.toString());
		}
	}
}
