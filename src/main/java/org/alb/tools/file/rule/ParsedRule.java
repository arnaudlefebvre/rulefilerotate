package org.alb.tools.file.rule;


public class ParsedRule {
	
	protected String rawrule;
	protected String fromStr;
	protected String toStr;
	protected String selectstr;
	protected String eachStr;
	protected String atStr;
	protected String selectOptstr;
	
	
	public ParsedRule() {
		// TODO Auto-generated constructor stub
	}
	
	public ParsedRule(ParsedRule parsedrule) {
		super();
		this.rawrule = parsedrule.rawrule;
		this.fromStr = parsedrule.fromStr;
		this.toStr = parsedrule.toStr;
		this.selectstr = parsedrule.selectstr;
		this.eachStr = parsedrule.eachStr;
		this.atStr = parsedrule.atStr;
		this.selectOptstr = parsedrule.selectOptstr;
	}
	
	public ParsedRule(String rawrule, String fromStr, String toStr,
			String selectstr, String eachStr, String atStr, String selectOptstr) {
		super();
		this.rawrule = rawrule;
		this.fromStr = fromStr;
		this.toStr = toStr;
		this.selectstr = selectstr;
		this.eachStr = eachStr;
		this.atStr = atStr;
		this.selectOptstr = selectOptstr;
	}

	public String getRawrule() {
		return rawrule;
	}


	public void setRawrule(String rawrule) {
		this.rawrule = rawrule;
	}


	public String getFromStr() {
		return fromStr;
	}


	public void setFromStr(String fromStr) {
		this.fromStr = fromStr;
	}


	public String getToStr() {
		return toStr;
	}


	public void setToStr(String toStr) {
		this.toStr = toStr;
	}


	public String getSelectstr() {
		return selectstr;
	}


	public void setSelectstr(String selectstr) {
		this.selectstr = selectstr;
	}


	public String getEachStr() {
		return eachStr;
	}


	public void setEachStr(String eachStr) {
		this.eachStr = eachStr;
	}


	public String getAtStr() {
		return atStr;
	}


	public void setAtStr(String atStr) {
		this.atStr = atStr;
	}


	public String getSelectOptstr() {
		return selectOptstr;
	}


	public void setSelectOptstr(String selectOptstr) {
		this.selectOptstr = selectOptstr;
	}

	@Override
	public String toString() {
		return "ParsedRule [rawrule=" + rawrule + ", fromStr=" + fromStr
				+ ", toStr=" + toStr + ", selectstr=" + selectstr + ", eachStr="
				+ eachStr + ", atStr=" + atStr + ", selectstr=" + selectstr
				+ "]";
	}
	
	public Boolean checkUnEmptyRuleClauses() {
		if ("".equalsIgnoreCase(rawrule) || "".equalsIgnoreCase(fromStr)
				|| "".equalsIgnoreCase(toStr) || "".equalsIgnoreCase(selectstr)
				|| "".equalsIgnoreCase(eachStr)) {
			return false;
		}
		return true;
	}
	
}
