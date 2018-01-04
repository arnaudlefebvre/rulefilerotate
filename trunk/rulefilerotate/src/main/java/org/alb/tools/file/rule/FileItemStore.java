package org.alb.tools.file.rule;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alb.tools.file.CommandLineBuilder;
import org.alb.tools.file.util.Functions;
import org.apache.log4j.Logger;

public class FileItemStore implements ItemStore {
	
	private static final Logger logger = Logger.getLogger(FileItemStore.class);
	private HashMap<Long, Item> store;
	private HashMap<Long,Item> allFiles;
	private CommandLineBuilder cmd;
	
	public FileItemStore() {
		this.allFiles = new HashMap<Long,Item>();
	}

	@Override
	public HashMap<Long, Item> createStore(CommandLineBuilder cmd) throws ParseException {
		this.cmd = cmd;
		HashMap<Long,Item> result = new HashMap<Long,Item>();
    	ArrayList<File> files = Functions.listFiles(cmd.getPath(),cmd.isSub());
    	for (File file : files) {
    		FileItem fi = new FileItem(file);
    		this.allFiles.put((long) fi.hashCode(),fi);
    		Pattern p = Pattern.compile(cmd.getName());
    		Matcher m = p.matcher(file.getName());
    		if (m.matches()) {
				//Create file with options
	    		FileItem i = new FileItem(file, cmd.getKey(), cmd.getDpattern(),cmd.getDreplace(),cmd.getDformat());
				result.put(Functions.getDateHashCode(i.getKey(),Calendar.SECOND),i);
    		}
		}
    	this.store = result;
    	return this.store;
	}
	
	public void displayStore() {
		for (Entry<Long, Item> entry : store.entrySet()) {
			logger.info("key : "+entry.getKey()+", entry : "+entry.getValue().display());
		}
	}

	public HashMap<Long, Item> getStore() {
		return store;
	}

	public void setStore(HashMap<Long, Item> store) {
		this.store = store;
	}

	public HashMap<Long,Item> getAll() {
		return allFiles;
	}


	
	    	
	    
}
