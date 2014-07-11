package ru.darvell.blkp;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 16:58
 * Uses to storing commands
 */
public class Heap {
	ArrayList<String> commands;

	public Heap(){
		System.out.println("heap create");
		this.commands = new ArrayList<String>();
	}

	synchronized public boolean addCommand(String newCmd){
		commands.add(newCmd);
		return true;
	}

	synchronized public String getCommand(){
		if(this.commands.size()>0){
			String cmd = commands.get(commands.size()-1);
			commands.remove(commands.size()-1);
			return cmd;
		}
		return null;
	}

	synchronized int getCount(){
		return commands.size();
	}
}
