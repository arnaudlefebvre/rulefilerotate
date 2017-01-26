package com.umanis.tools.file.rule;

import java.util.ArrayList;
import java.util.Date;

public class CompiledRule extends ParsedRule {


	protected Date fromDate;
	protected Date toDate;
	protected Integer select;
	protected ArrayList<RuleDynDate> dates;
	protected Integer eachCalVal;
	protected String selectOpt;
	
	public CompiledRule() {
		// TODO Auto-generated constructor stub
	}
	
	public CompiledRule(ParsedRule parsedrule) {
		super();
		this.rawrule = parsedrule.rawrule;
		this.fromStr = parsedrule.fromStr;
		this.toStr = parsedrule.toStr;
		this.selectstr = parsedrule.selectstr;
		this.eachStr = parsedrule.eachStr;
		this.atStr = parsedrule.atStr;
		this.selectOptstr = parsedrule.selectOptstr;
		this.selectOpt = parsedrule.selectOptstr;
	}
	

	public CompiledRule(String rawrule, String fromStr, String toStr,
			String selectstr, String eachStr, String atStr, String selectOptstr) {
		super(rawrule, fromStr, toStr, selectstr, eachStr, atStr, selectOptstr);
		this.selectOpt = selectOptstr;
		// TODO Auto-generated constructor stub
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getSelect() {
		return select;
	}

	public void setSelect(Integer select) {
		this.select = select;
	}

	public ArrayList<RuleDynDate> getDates() {
		return dates;
	}

	public void setDates(ArrayList<RuleDynDate> dates) {
		this.dates = dates;
	}

	public Integer getEachCalVal() {
		return eachCalVal;
	}

	public void setEachCalVal(Integer eachCalVal) {
		this.eachCalVal = eachCalVal;
	}

	public String getSelectOpt() {
		return selectOpt;
	}

	public void setSelectOpt(String selectOpt) {
		this.selectOpt = selectOpt;
	}

	@Override
	public String toString() {
		return "CompiledRule [fromDate=" + fromDate + ", toDate=" + toDate
				+ ", select=" + select + ", dates=" + dates + ", eachCalVal="
				+ eachCalVal + ", select=" + select + ", rawrule=" + rawrule
				+ ", fromStr=" + fromStr + ", toStr=" + toStr + ", selectstr="
				+ selectstr + ", eachStr=" + eachStr + ", atStr=" + atStr
				+ ", selectstr=" + selectstr + "]";
	}
	
	
}
