package org.alb.tools.file.rule;

import java.util.Date;
import java.util.HashMap;

import org.alb.tools.file.CommandLineBuilder;
import org.alb.tools.file.rule.exception.ItemActionException;


public class Item implements Comparable<Item> {
	
	protected String name;
	protected Object item;
	
	public Item(String name) {
		super();
		this.name = name;
	}
	
	public Item(String name, Object item) {
		super();
		this.name = name;
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public Item() {
		// TODO Auto-generated constructor stub
	}
	
	public String performAction(CommandLineBuilder cmd) throws NoSuchMethodException, ItemActionException {
		throw new NoSuchMethodException();
	}
	
	@Override
	public String toString() {
		return String.format("Item [name=" + name + ", item=" + item + "]");
	}


	public String display() {
		return String.format("Item [name=" + name + ", item=" + item + "]");
	}

	@Override
	public int compareTo(Item o) {
		return this.name.compareTo(o.getName());
	}

	public int compareTo(FileItem o) {
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	public long calculateDistance(Date ref, HashMap<Long,Item> selected) {
		return 0;
	}
	
	
}
