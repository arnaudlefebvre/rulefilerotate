package com.umanis.tools.file.rule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.umanis.tools.file.rule.exception.RuleException;
import com.umanis.tools.file.util.Functions;

public class Rule extends CompiledRule {
	
	public static final Logger logger = Logger.getLogger(Rule.class);
	private HashMap<Long,HashMap<Long,Item>> hitsByScale;
	private ArrayList<Item> hits;
	private HashMap<Long,Item> finalHits;
	private boolean match;
	
	public Rule() {
		this.match = false;
	}
	
	public Rule(CompiledRule compiledrule) {		
		super(compiledrule.rawrule, compiledrule.fromStr, compiledrule.toStr, compiledrule.selectstr, compiledrule.eachStr, compiledrule.atStr, compiledrule.selectOpt);
		this.fromDate = compiledrule.fromDate;
		this.toDate = compiledrule.toDate;
		this.select = compiledrule.select;
		this.dates = compiledrule.dates;
		this.eachCalVal = compiledrule.eachCalVal;
		this.select = compiledrule.select;
		this.hits = new ArrayList<Item>();
		this.finalHits = new HashMap<Long,Item>();
		this.hitsByScale = new  HashMap<Long,HashMap<Long,Item>>();
		this.match = false;
	}
	
	@Override
	public String toString() {
		return "Rule [fromDate=" + fromDate + ", toDate=" + toDate + ", select="
				+ select + ", dates=" + dates + ", eachCalVal=" + eachCalVal
				+ ", select=" + select + ", rawrule=" + rawrule + ", fromStr="
				+ fromStr + ", toStr=" + toStr + ", selectstr=" + selectstr
				+ ", eachStr=" + eachStr + ", atStr=" + atStr + ", selectstr="
				+ selectstr + "]";
	}
	
	public String toInfoString() {
		return "Rule [fromDate=" + fromDate + ", toDate=" + toDate + ", select="
				+ select + ", dates=" + dates + ", eachCalVal=" + eachCalVal
				+ ", select=" + select + "]";
	}
	
	public String getRuleInfo()
	{
		String result = String.format(this.toInfoString()+"%n"+"This rule will match objects by age from "+Constants.sdfout.format(fromDate)+" to "+Constants.sdfout.format(toDate)+" and select "+select+" objects each "+eachStr);
		if (dates != null  && dates.size() > 0) {
			result += String.format("%n using dates "+dates);
		}
		if ("KEYWD_LAST".equals(select)) {
			result += String.format("%n and filter to last matching object ");
		}
		if ("KEYWD_FIRST".equals(select)) {
			result += String.format("%n and filter to first matching object ");
		}
		if ("KEYWD_RANDOM".equals(select)) {
			result += String.format("%n and filter to random matching object ");
		}
		result += String.format("%n scales found "+this.hitsByScale.size());
		return result;
	}
	
	public String getRuleDebugInfo()
	{
		String result = String.format(this.toInfoString()+"%n"+"This rule will match objects by age from "+fromDate+" to "+toDate+" and select "+select+" objects each "+eachStr);
		if (dates != null  && dates.size() > 0) {
			result += String.format("%n using dates "+dates);
		}
		if ("KEYWD_LAST".equals(select)) {
			result += String.format("%n and filter to last matching object ");
		}
		if ("KEYWD_FIRST".equals(select)) {
			result += String.format("%n and filter to first matching object ");
		}
		if ("KEYWD_RANDOM".equals(select)) {
			result += String.format("%n and filter to random matching object ");
		}
		result += String.format("%n scales found "+this.hitsByScale.size()+" "+eachStr+" List[scale] : "+this.hitsByScale);
		return result;
	}
	
	public void initScale() {
		if (this.hitsByScale.isEmpty()) {
			Date start = fromDate;
			while (start.compareTo(toDate) <= 0) {
				this.hitsByScale.put(Functions.getDateHashCode(start, eachCalVal),new HashMap<Long,Item>());
				Calendar cal = Calendar.getInstance();
				cal.setTime(start);
				cal.add(this.eachCalVal,1);
				start = cal.getTime();
			}
			if (!this.hitsByScale.containsKey(Functions.getDateHashCode(toDate, eachCalVal))) {
				this.hitsByScale.put(Functions.getDateHashCode(toDate, eachCalVal),new HashMap<Long,Item>());
			}
		}
	}
	
	public void run(Entry<Long,Item> e) {
		if (e.getValue() instanceof FileItem) {
			FileItem f = (FileItem) e.getValue();			
			if (checkIn(f.getKey())) {
				Long hash = Functions.getDateHashCode(f.getKey(), eachCalVal);
				HashMap<Long,Item> itemLst = this.hitsByScale.get(hash);
				itemLst.put((long) f.hashCode(),f);
				hits.add(f);
				this.hitsByScale.put(hash,itemLst);
				this.match = true;
			}
		}
	}
	
	public String displayMatches() {
		String result = this.rawrule+" NO MATCH " ;
		if (match) {
			result = String.format(this.rawrule+" MATCHES : " );
			for (Entry<Long, HashMap<Long,Item>> e : this.hitsByScale.entrySet()) {
				if (!e.getValue().isEmpty()) {
					result += String.format("%n - Match for "+e.getKey()+" : "+e.getValue().values());
				}
			}
		}
		return result;
	}
	
	public String displayFinalHits() {
		String result = this.rawrule+" has selected : " ;
		for (Item i : finalHits.values()) {
			result += String.format("%n - "+i);
		}
		return result;
	}
	
	private boolean groupisFixed (String group) {
		boolean result = false;
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("[0-9]+");
		matcher = pattern.matcher(group);
		if (matcher.matches()) {
			return true;
		}
		return result;
	}
	
	private Boolean isFuckingWrongAutoresolve(FileItem random) {
		logger.debug("isFuckingWrongAutoresolve");
		boolean result = false;
		for (RuleDynDate rdd : this.getDates()) {
			Boolean ruleMatch = false;
			if (rdd.isDyn()) {	
				Calendar cal = Calendar.getInstance();
				cal.setTime(random.getKey());
				logger.debug("isFuckingWrongAutoresolve Current : "+Constants.sdfout.format(cal.getTime())+", rdd : "+rdd.getDateStr()+", regex : "+rdd.getDateStrToRegex());
				Pattern pattern;
				Matcher matcher;
				pattern = Pattern.compile(rdd.getDateStrToRegex());
				matcher = pattern.matcher(rdd.getDateStr());
				while (matcher.find()) {
					for (int i = 0; i < matcher.groupCount(); i++) {
						if (groupisFixed(matcher.group(i))) {
							cal.set(matchGroupIdxToCal(i),Integer.parseInt(matcher.group(i)));
						}										
					}									
				}
				logger.debug("isFuckingWrongAutoresolve Compare file : "+Constants.sdfout.format((random).getKey())+", computed : "+Constants.sdfout.format(cal.getTime()));
				if (cal.getTime().compareTo((random).getKey()) == 0) {
					if (!ruleMatch) {
						result = true;
						logger.debug("isFuckingWrongAutoresolve WINNER : "+Constants.sdfout.format((random).getKey()));
					}							
				}
				
			} else {
				Date dt = (random).getKey();
				if (dt.compareTo(rdd.getDate()) == 0) {								
					if (!ruleMatch) {
						result = true;
						logger.debug("isFuckingWrongAutoresolve WINNER : "+Constants.sdfout.format((random).getKey()));
					}
					
				}
			}
		}
		return result;
	}
	
	public void selectByDates(boolean autoresolve) throws RuleException{
		HashMap<Long,Item> tmphits = new HashMap<Long,Item>();
		for (Entry<Long, HashMap<Long,Item>> e : this.hitsByScale.entrySet()) {			
			if (!e.getValue().isEmpty()) {				
				for (RuleDynDate rdd : this.getDates()) {
					HashMap<Long,Item> tmp = (HashMap<Long,Item>) e.getValue();
					tmphits = new HashMap<Long,Item>();
					Boolean ruleMatch = false;
					for (Entry<Long,Item> en : tmp.entrySet()) {						
						if (rdd.isDyn()) {							
							Calendar cal = Calendar.getInstance();
							cal.setTime(((FileItem)en.getValue()).getKey());
							logger.debug("Current : "+Constants.sdfout.format(cal.getTime())+", rdd : "+rdd.getDateStr()+", regex : "+rdd.getDateStrToRegex());
							Pattern pattern;
							Matcher matcher;
							pattern = Pattern.compile(rdd.getDateStrToRegex());
							matcher = pattern.matcher(rdd.getDateStr());
							while (matcher.find()) {
								for (int i = 0; i < matcher.groupCount(); i++) {
									if (groupisFixed(matcher.group(i))) {
										cal.set(matchGroupIdxToCal(i),Integer.parseInt(matcher.group(i)));
									}										
								}									
							}
							logger.debug("Compare file : "+Constants.sdfout.format(((FileItem)en.getValue()).getKey())+", computed : "+Constants.sdfout.format(cal.getTime()));
							if (cal.getTime().compareTo(((FileItem)en.getValue()).getKey()) == 0) {
								if (!ruleMatch) {
									tmphits.put(en.getKey(),en.getValue());
									ruleMatch = true;
									logger.debug("WINNER : "+Constants.sdfout.format(((FileItem)en.getValue()).getKey()));
								}							
							}
							
						} else {
							Date dt = ((FileItem)en).getKey();
							if (dt.compareTo(rdd.getDate()) == 0) {								
								if (!ruleMatch) {
									tmphits.put(en.getKey(),en.getValue());
									ruleMatch = true;
									logger.debug("WINNER : "+Constants.sdfout.format(((FileItem)en.getValue()).getKey()));
								}
								
							}
						}
					}
					if (tmphits.size() < 1 && ! autoresolve ) {
						throw new RuleException("Select by date incomplete : lesser file selected than select clause imposes in "+e.getKey()+" for "+rdd+")");
					} else if (tmphits.size() < 1 && autoresolve ) {						
						if (e.getValue().size() <= 1) {
							logger.info("Auto-resolve : Select by date incomplete : in "+e.getKey()+" for "+rdd+") - Select all (only one result in "+e.getKey()+")");
							finalHits.putAll(e.getValue());
						} else {
							//TODO select exact number, one for each rule "random"
							//The best is to select nearby files according to dates.
							if (e.getValue().size() <= this.dates.size()) {
								logger.info("Auto-resolve : Select by date incomplete : in "+e.getKey()+" for "+rdd+") - Select all (less results in "+e.getKey()+") than number of files to select");
								finalHits.putAll(e.getValue());
							} else {
								Random randomizer = new Random();
								logger.info("Auto-resolve : Select by date incomplete : in "+e.getKey()+" for "+rdd+") - Select random (more results in "+e.getKey()+") than number of files to select");
								int randIdx = randomizer.nextInt(e.getValue().values().size());
								Item random = (Item) e.getValue().values().toArray()[randIdx];
								logger.debug("FinaHits : "+finalHits);
								logger.debug("random : "+random+", hascode : "+random.hashCode());
								while (finalHits.containsValue(random) || isFuckingWrongAutoresolve((FileItem)random) ) {
									randIdx = randomizer.nextInt(e.getValue().values().size());
									random = (Item) e.getValue().values().toArray()[randIdx];
									logger.debug("new random : "+random+", hascode : "+random.hashCode());
								}
								logger.debug("Auto-resolve : WINNER in "+e.getKey()+" for "+rdd+" : "+Constants.sdfout.format(((FileItem)random).getKey()));
								finalHits.put( new Long(random.hashCode()),random);
							}
						}
					} else{
						finalHits.putAll(tmphits);
					}
				}				
			} else {
				logger.info("0-No Match for step "+e.getKey());
				//throw new RuleException("No Match for step "+e.getKey());
				//TODO throw exception - No value for a step.
			}
		}
	}
	
	public void select(boolean autoresolve) throws ParseException, RuleException {
		if (match) {
			if (this.getDates() != null ) {
				selectByDates(autoresolve);
			}
			else {
				for (Entry<Long, HashMap<Long,Item>> e : this.hitsByScale.entrySet()) {
					if (!e.getValue().isEmpty() && e.getValue().size() >= this.select) {
						if (e.getValue().size() == this.select) { //?
							finalHits.putAll(e.getValue());
						} else {
							if (Constants.KEYWD_LAST.equals(selectOpt)) {
								ArrayList<Item> tmp = (ArrayList<Item>) e.getValue().values();
								Collections.sort(tmp);
								ArrayList<Item> toadd = (ArrayList<Item>) tmp.subList(e.getValue().size()-this.select, e.getValue().size()-1);
								HashMap<Long,Item> hashadd = new HashMap<Long,Item>();
								for (Item i : toadd) hashadd.put((long)i.hashCode(),i);
								finalHits.putAll(hashadd);
							} else if (Constants.KEYWD_FIRST.equals(selectOpt)) {
								ArrayList<Item> tmp = (ArrayList<Item>) e.getValue().values();
								Collections.sort(tmp);
								ArrayList<Item> toadd = (ArrayList<Item>) tmp.subList(0,e.getValue().size()-this.select);
								HashMap<Long,Item> hashadd = new HashMap<Long,Item>();
								for (Item i : toadd) hashadd.put((long)i.hashCode(),i);
								finalHits.putAll(hashadd);
							} else if (Constants.KEYWD_DEF.equals(selectOpt)) {
								finalHits.putAll(selectDefault(e));											
						    } else if (Constants.KEYWD_RANDOM.equals(selectOpt)) {
								Random randomizer = new Random();
								int randIdx = randomizer.nextInt(e.getValue().values().size());
								Item random = (Item) e.getValue().values().toArray()[randIdx];
								HashMap<Long,Item>  tmpHits = new HashMap<Long,Item>(this.select);
								tmpHits.put((long) random.hashCode(),random);
								while (tmpHits.size() < this.select) {
									randomizer = new Random();
									while (tmpHits.containsValue(random)) {
										randIdx = randomizer.nextInt(e.getValue().values().size());
										random = (Item) e.getValue().values().toArray()[randIdx];								
									}
									tmpHits.put((long) random.hashCode(),random);
								}
								finalHits.putAll(tmpHits);
							} else {
								finalHits.putAll(selectDefault(e));	
							} 
						}
					} else {
						// Si il y en a moins ? on lève une erreur ou on prend tout ?
						if (autoresolve) {
							if (e.getValue().size() == 0 ) {
								logger.info("0-No Match for step "+e.getKey());
							} else {
								//TODO On pourrait tout prendre - au choix de l'utilisateur
								logger.warn("More match for step "+e.getKey()+" than number of files to select");
								finalHits.putAll(e.getValue());
							}
						} else {
							throw new RuleException("Rule as either empty or less results for period "+e.getKey()+" than number of files to select. See -"+com.umanis.tools.file.Constants.AUTO_OPT+" option");
						}
					}
				}
			}
		}
	}
	
	/***
	 * Select Item in scale by maximum space beetwen results
	 * @param e
	 * @return
	 * @throws ParseException
	 */
	private HashMap<Long,Item> selectDefault(Entry<Long,HashMap<Long,Item>> e) throws ParseException {
		HashMap<Long,Item> tmp = (HashMap<Long,Item>) e.getValue();
		//Collections.sort(tmp);
		SimpleDateFormat sdf = new SimpleDateFormat(Functions.gethaschCodeDateformat(e.getKey()));
		Date start = sdf.parse(e.getKey().toString());
		logger.debug("-----BEGIN-----");
		logger.debug("start : "+start);
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.add(Functions.gethaschCodeStep(e.getKey()), 1);
		cal.add(Calendarminus1(), -1);
		Date end = cal.getTime();
		logger.debug("end : "+end);
		long milli = Math.abs(Functions.getDateDiff(start, end, TimeUnit.MILLISECONDS));
		//Ici, il faut peut être diviser uniquement par this.select pour obtenir des résultats avec un espacement identique entre les différents jour,mois,année,etc (each)
		//Et dans ce cas, on peut démarrer de start ou de start + step
		Long step = milli / (this.select + 1);
		ArrayList<Date> divDates = new ArrayList<Date>();
		Calendar tmpcal = Calendar.getInstance();
		tmpcal.setTime(start);
		tmpcal.add(Calendar.MILLISECOND,step.intValue());							
		Date stepDate = tmpcal.getTime();
		logger.debug("step : "+stepDate);
		for (int i = 0; i < this.select; i++) {
			divDates.add(stepDate);
			tmpcal.setTime(stepDate);
			tmpcal.add(Calendar.MILLISECOND,step.intValue());
			stepDate = tmpcal.getTime();
		}							
		logger.debug("divDates : "+divDates);
		HashMap<Long,Item>  tmpHits = new HashMap<Long,Item>(this.select);
		for (Date st : divDates) {
			long winHash = Integer.MIN_VALUE;
			Item winItem = null;
			logger.debug("Current Step Date : "+Constants.sdfout.format(st));
			for (Entry<Long,Item> en : tmp.entrySet()) {
				if (checkIn(((FileItem)en.getValue()).getKey(), start, end)) {
					long hh = (en.getValue()).calculateDistance(st,tmpHits);
					logger.debug("winhash : "+winHash+", hh : "+hh+", winItem : "+(winItem!=null?winItem.getName():"null")+", currentItem : "+en.getValue().getName());
					if (winHash < hh ) {
						winHash = hh;
						winItem = en.getValue();
					}
				}
				
			}
			if (winItem != null ) {
				tmpHits.put(winHash,winItem);
				logger.debug("FINAL winhash : "+winHash+", winItem : "+winItem);
			} else {
				logger.debug("NO MATCH ! ");
			}
		}
		logger.debug("-----END-----");
		return tmpHits;
	}
	
	
	
	private int Calendarminus1() {
		if (this.eachCalVal.equals(Calendar.YEAR)||this.eachCalVal.equals(Calendar.MONTH)) {
			return Calendar.SECOND;
		} else if (this.eachCalVal.equals(Calendar.DATE)||this.eachCalVal.equals(Calendar.HOUR)||this.eachCalVal.equals(Calendar.MINUTE)) {
			return Calendar.SECOND;
		}
		return 0;
	}
	
	private int matchGroupIdxToCal(int i) {
		switch (i) {
			case 1: return Calendar.YEAR;
			case 2: return Calendar.MONTH;
			case 3: return Calendar.DATE; 
			case 4: return Calendar.HOUR; 
			case 5: return Calendar.MINUTE; 
			case 6: return Calendar.SECOND; 
			default: return -1;
		}
	}
	
	
	public boolean checkIn(Date date) {
		return (date.compareTo(this.getFromDate()) >= 1 && date.compareTo(this.getToDate()) <= 0) || (date.compareTo(this.getFromDate()) <=0  && date.compareTo(this.getToDate()) >= 1) ; 
	}
	
	public boolean checkIn(Date date, Date start, Date end) {
		return (date.compareTo(start) >= 1 && date.compareTo(end) <= 0) || (date.compareTo(start) <=0  && date.compareTo(end) >= 1) ; 
	}
	
	public void reset() {
		this.match = false;
		this.hitsByScale = new  HashMap<Long,HashMap<Long,Item>>();
	}

	public HashMap<Long,Item> getFinalHits() {
		return finalHits;
	}

	public boolean isMatch() {
		return match;
	}

}
