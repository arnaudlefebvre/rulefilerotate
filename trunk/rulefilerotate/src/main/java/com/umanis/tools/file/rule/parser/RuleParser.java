package com.umanis.tools.file.rule.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.umanis.tools.file.rule.Constants;
import com.umanis.tools.file.rule.ParsedRule;
import com.umanis.tools.file.rule.exception.RuleParseException;

public class RuleParser implements RuleParserInt {

	public static final Logger logger = Logger.getLogger(RuleParser.class);
	
	public RuleParser() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ParsedRule parse(String rawrule) throws RuleParseException {
		// TODO Auto-generated method stub
		ParsedRule rule = new ParsedRule();
		String endrule = "";
		
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(Constants.REG_PARSE_ALL);
		matcher = pattern.matcher(rawrule);
		boolean matche = matcher.matches();
		if (matche) {
			// General Parsing
			for (int i = 0; i <= matcher.groupCount(); i++) {
				//logger.debug("Groupe " + i + " : " + matcher.group(i));
				switch (i) {
				case 0:
					rule.setRawrule(matcher.group(i).trim());
					break;
				case 1:
					rule.setFromStr(matcher.group(i).trim());
					break;
				case 2:
					rule.setToStr(matcher.group(i).trim());
					break;
				case 3:
					rule.setSelectstr(matcher.group(i).trim());
					break;
				case 4:
					endrule = matcher.group(i).trim();
					rule.setEachStr(endrule);
					break;
				default:
					break;
				}
			}
			// check well parsed clauses :
			if (!rule.checkUnEmptyRuleClauses()) {
				throw new com.umanis.tools.file.rule.exception.RuleParseException("Invalid rule definition : "
						+ rawrule);
			}
			// Optionnal opt detection
			if (endrule.indexOf(Constants.KEYWD_LAST) != -1) {
				rule.setSelectOptstr(Constants.KEYWD_LAST);
				String neweach = endrule;
				neweach = neweach.substring(0, neweach.indexOf(Constants.KEYWD_LAST))
						+ neweach.substring(
								neweach.indexOf(Constants.KEYWD_LAST) + Constants.KEYWD_LAST.length(),
								neweach.length());
				rule.setEachStr(neweach);
				//logger.debug("Each Str : " + neweach + ", KEYWD_LAST found");
			}
			if (endrule.indexOf(Constants.KEYWD_FIRST) != -1) {
				rule.setSelectOptstr(Constants.KEYWD_FIRST);
				String neweach = endrule;
				neweach = neweach.substring(0, neweach.indexOf(Constants.KEYWD_FIRST))
						+ neweach.substring(
								neweach.indexOf(Constants.KEYWD_FIRST) + Constants.KEYWD_FIRST.length(),
								neweach.length());
				rule.setEachStr(neweach);
				//logger.debug("Each Str : " + neweach + ", KEYWD_FIRST found");
			}
			if (endrule.indexOf(Constants.KEYWD_RANDOM) != -1) {
				rule.setSelectOptstr(Constants.KEYWD_RANDOM);
				String neweach = endrule;
				neweach = neweach.substring(0, neweach.indexOf(Constants.KEYWD_RANDOM))
						+ neweach.substring(
								neweach.indexOf(Constants.KEYWD_RANDOM) + Constants.KEYWD_RANDOM.length(),
								neweach.length());
				rule.setEachStr(neweach);
				//logger.debug("Each Str : " + neweach + ", KEYWD_RANDOM found");
			}
			if (endrule.indexOf(Constants.KEYWD_DEF) != -1) {
				rule.setSelectOptstr(Constants.KEYWD_DEF);
				String neweach = endrule;
				neweach = neweach.substring(0, neweach.indexOf(Constants.KEYWD_DEF))
						+ neweach.substring(
								neweach.indexOf(Constants.KEYWD_DEF) + Constants.KEYWD_DEF.length(),
								neweach.length());
				rule.setEachStr(neweach);
				//logger.debug("Each Str : " + neweach + ", KEYWD_RANDOM found");
			}
			
			pattern = Pattern.compile(Constants.REG_PARSE_OPT);
			matcher = pattern.matcher(rule.getEachStr());
			matche = matcher.matches();
			String neweach = "";
			if (matche) {
				for (int i = 0; i <= matcher.groupCount(); i++) {
					switch (i) {
					case 0:						
						break;
					case 1:
						neweach = matcher.group(i).trim();
						break;
					case 2:
						rule.setAtStr(matcher.group(i).trim());
						break;
					default:
						break;
					}
				}
				rule.setEachStr(neweach);
			} 
			
			
			logger.debug("Created "+rule);
		} else {
			throw new RuleParseException("Invalid rule definition : "
					+ rawrule);
		}	
		return rule;
	}

}
