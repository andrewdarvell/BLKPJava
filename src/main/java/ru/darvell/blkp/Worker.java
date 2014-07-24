package ru.darvell.blkp;

import org.apache.log4j.Logger;
import ru.darvell.blkp.lastfm.Lastfm;

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
	Boolean play = false;
	Map<String,String> nowPlay;

	Worker(Heap heap,Lastfm lastfm){
		//System.out.println("worker create");
		this.heap = heap;
		this.lastfm = lastfm;
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
						case "stop": doStop();
							break;
					}


				}
				t.sleep(700);
			}
		}catch (Exception e){
			log.error(e.toString());
		}
	}

	void doPlay(Map<String,String> command){
		log.info("doPlay");
		lastfm.sendNowPlay(command.get("artist"),command.get("track"));
		nowPlay = command;
		play = true;
	}

	void doStop(){
		if(play && nowPlay != null){
			log.info("doStop");
			lastfm.sendScrobble(nowPlay.get("artist"),nowPlay.get("track"),Integer.parseInt(nowPlay.get("time")));
			play = false;
			nowPlay = null;
		}
	}
}
