package com.umanis.tools.file.rule;

import java.util.Date;

public class RuleDynDate {
	
	private String dateStr;
	private Date date;
	private boolean fixed;
	private boolean dyn;
	
	public RuleDynDate() {
		this.fixed = false;
		this.dyn = false;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public RuleDynDate(String dateStr, Date date) {
		super();
		this.fixed = true;
		this.dyn = false;
		this.dateStr = dateStr;
		this.date = date;
	}
	
	public RuleDynDate(Date date) {
		super();
		this.fixed = true;
		this.dyn = false;
		this.date = date;
	}
	
	public RuleDynDate(String dateStr) {
		super();
		this.fixed = false;
		this.dyn = true;
		this.dateStr = dateStr;
	}

	@Override
	public String toString() {
		return "RuleDynDate [dateStr=" + dateStr + ", date=" + (date!=null?Constants.sdfout.format(date):"") + "]";
	}

	public boolean isFixed() {
		return fixed;
	}

	public boolean isDyn() {
		return dyn;
	}
	
	public String getDateStrToRegex() {
		String result = null;
		if (dateStr!=null) {
			result =  dateStr.replaceAll("\\*+","(.+)");
			result = result.replaceAll("([0-9]+)", "($1)")+"(.*)";
		}
		return result;
	}
}
