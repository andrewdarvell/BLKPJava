package ru.darvell.blkp;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 16:58
 * Uses to storing commands
 */
public class Heap {
	private static Logger log = Logger.getLogger(Heap.class.getName());

	private ArrayList<Map<String,String>> commands;

	public Heap(){
		System.out.println("heap create");
		this.commands = new ArrayList<Map<String, String>>();
	}

	synchronized public boolean addCommand(Map<String,String> newCmd){
		commands.add(newCmd);
		return true;
	}

	synchronized public Map<String,String> getCommand(){
		if(this.commands.size()>0){
			Map<String,String> cmd = commands.get(commands.size()-1);
			commands.remove(commands.size()-1);
			return cmd;
		}
		return null;
	}

	synchronized int getCount(){
		return commands.size();
	}
}
