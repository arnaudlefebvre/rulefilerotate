package com.umanis.tools.file.rule.compiler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.umanis.tools.file.rule.CompiledRule;
import com.umanis.tools.file.rule.Constants;
import com.umanis.tools.file.rule.ParsedRule;
import com.umanis.tools.file.rule.RuleDynDate;
import com.umanis.tools.file.rule.exception.RuleCompileException;

public class RuleCompiler implements RuleCompilerInt {
	
	public static final Logger logger = Logger.getLogger(RuleCompiler.class);
	
	public RuleCompiler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public CompiledRule compile(ParsedRule rule) throws RuleCompileException {
		
			CompiledRule crule = new CompiledRule(rule);
			crule.setFromDate(parseDateOrPeriod(rule.getFromStr()).getDate());
			crule.setToDate(parseDateOrPeriod(rule.getToStr(),crule.getFromDate()).getDate());
			crule.setEachCalVal(convertPeriod(rule.getEachStr()));
			if (Constants.SELECT_ALL.equals(rule.getSelectstr())) {
				crule.setSelect(Integer.MAX_VALUE);
			} else {
				try { 
					crule.setSelect(Integer.parseInt(rule.getSelectstr()));
				} catch (NumberFormatException nfe) {
					throw new RuleCompileException("Number of files to select must be greater than 0");				
				}
			}
			if (crule.getSelect() <=0) {
				throw new RuleCompileException("Number of files to select must be greater than 0");
			}
			if (rule.getAtStr() != null) {
				String[] tmp = rule.getAtStr().split(",");
				ArrayList<RuleDynDate> dates = new ArrayList<RuleDynDate>();
				for (int i = 0; i< tmp.length ; i++ ) {
					RuleDynDate tmpd = parsedate(tmp[i],true);
					if (tmpd != null) {
						dates.add(tmpd);
					} else {
						throw new RuleCompileException("Wrong date ("+tmp[i]+") in AT clause : "+rule.getAtStr());
					}
				}
				crule.setDates(dates);
			}
			normalizeFromTo(crule);
			logger.debug("Created "+crule.toString());
			return crule;

		}
	
		private void normalizeFromTo(CompiledRule rule) {
			if (rule.getFromDate().compareTo(rule.getToDate()) > 0) {
				Date tmp = rule.getToDate();
				rule.setToDate(rule.getFromDate());
				rule.setFromDate(tmp);
			}
			
		}
	
		private RuleDynDate parseDateOrPeriod(String clause) throws RuleCompileException {
			return parseDateOrPeriod(clause,null);
		}
		
		private RuleDynDate parseDateOrPeriod(String clause, Date fromDate) throws RuleCompileException {
			Date result = null;
			RuleDynDate res = null;
			try {
				res = parsedate(clause,false);
				if (res == null) {
					result = parseperiod(clause,fromDate);
					if (result != null) {
						res = new RuleDynDate(result);
					}
				}
			} catch (Exception e) {
				//logger.debug("Unable to parse input " + clause);
				throw new RuleCompileException("Unable to parse input " + clause, e);
			}
			if (res != null) {
				return res;
			} else {
				throw new RuleCompileException("Unable to parse input " + clause);
			}
		}
		
		private RuleDynDate parsedate(String dateclause, Boolean isAt) {
			Date result = null;
			RuleDynDate res = null;
			for (String formatString : Constants.FIXED_DATE_FORMATS) {
				try {
					result = new SimpleDateFormat(formatString).parse(dateclause);
					res = new RuleDynDate(result);
				} catch (java.text.ParseException e) {
					logger.trace("Unable to parse input (" + dateclause+ ") with format " + formatString);
				}
			}
			if (isAt) {		
				for (String formatString : Constants.DYN_DATE_FORMATS) {
					Pattern pattern;
					Matcher matcher;
					pattern = Pattern.compile(formatString);
					matcher = pattern.matcher(dateclause);
					if (matcher.matches()) {
						res = new RuleDynDate(dateclause);
					}					
				}
			}
			return res;
		}
		
		private Date parseperiod(String periodclause) throws RuleCompileException {
			return parseperiod(periodclause,null);
		}

		private Date parseperiod(String periodclause, Date fromDate) throws RuleCompileException {
			Date result = null;
			boolean isFromDate = false;
			if (Constants.KEYWD_TODAY.equals(periodclause)) {
				Calendar cal = Calendar.getInstance();
				return cal.getTime();
			} else if (periodclause.startsWith(Constants.KEYWD_TODAY)) {
				periodclause = periodclause.substring(Constants.KEYWD_TODAY.length());
			} else if (periodclause.startsWith(Constants.KEYWD_FROM)) {
				periodclause = periodclause.substring(Constants.KEYWD_FROM.length());
				isFromDate = true;
			} else {
				throw new RuleCompileException("Must start with keyword "+Constants.KEYWD_FROM+" OR "+Constants.KEYWD_TODAY);
			}
			//logger.debug("period to compile : " + periodclause);
			Pattern pattern;
			Matcher matcher;
			pattern = Pattern.compile(Constants.REG_PARSE_INTERVAL);
			matcher = pattern.matcher(periodclause);
			Integer add = 0;
			Integer addtype = 0;
			Calendar cal = Calendar.getInstance();
			if (isFromDate) {
				cal.setTime(fromDate);
			}
			result = cal.getTime();
			int i = 0;
			while (matcher.find()) {
				//logger.debug("Groupe " + i + " : " + matcher.group());
				add = Integer.parseInt(matcher.group(1));
				addtype = convertPeriod(matcher.group(2));
				cal.setTime(result);
				cal.add(addtype, add);
				result = cal.getTime();
				i++;
			}
			if (i == 0) {
				throw new RuleCompileException("Unable to calculate date from period "+periodclause);
			}
			return result;
		}

	private Integer convertPeriod(String period) throws RuleCompileException {
			if (Constants.KEYWD_DAY.equals(period.trim())) {
				return Calendar.DATE;
			} else if (Constants.KEYWD_MONTH.equals(period.trim())) {
				return Calendar.MONTH;
			} else if (Constants.KEYWD_YEAR.equals(period.trim())) {
				return Calendar.YEAR;
			} else if (Constants.KEYWD_HOUR.equals(period.trim())) {
				return Calendar.HOUR;
			} else {
				throw new RuleCompileException("Wrong period keyword " + period);
			}
		}


}
