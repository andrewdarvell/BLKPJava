package ru.darvell.blkp;

import org.apache.log4j.Logger;
import ru.darvell.blkp.lastfm.Lastfm;
import ru.darvell.blkp.serialport.Arduino;

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
					switch (command.get("command")){
						case "play": doPlay(command);
							break;
						case "stop": doStop(command);
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
			Map<String,String> arduinoCommand = new HashMap<>();
			arduinoCommand.put("name","show");
			arduinoCommand.put("artist",command.get("artist"));
			arduinoCommand.put("track",command.get("track"));
			arduino.addCommand(arduinoCommand);
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
		}
	}
	void update(){
		if (play){
			lastfm.sendNowPlay(nowPlay.get("artist"),nowPlay.get("track"));
		}
	}
}
