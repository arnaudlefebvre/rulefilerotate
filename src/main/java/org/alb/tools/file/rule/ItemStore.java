package org.alb.tools.file.rule;

import java.util.HashMap;

import org.alb.tools.file.CommandLineBuilder;

public interface ItemStore {
	//public HashMap<Long,Item> createStore(String path);
	public void displayStore();
	public HashMap<Long,Item> getStore();
	public HashMap<Long, Item> createStore(CommandLineBuilder cmd) throws Exception;
	public HashMap<Long,Item> getAll();
	
}
