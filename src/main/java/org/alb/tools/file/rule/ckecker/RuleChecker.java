package org.alb.tools.file.rule.ckecker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.alb.tools.file.rule.CompiledRule;
import org.alb.tools.file.rule.Rule;
import org.alb.tools.file.rule.RuleDynDate;
import org.alb.tools.file.rule.exception.RuleCheckException;
import org.alb.tools.file.util.Functions;
import org.apache.log4j.Logger;

public class RuleChecker implements RuleCheckInt {

	public static final Logger logger = Logger.getLogger(RuleChecker.class);
	
	public RuleChecker() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Rule check(CompiledRule rule) throws RuleCheckException {
		Rule result = new Rule(rule);
		boolean res = true;
		long days = Math.abs(Functions.getDateDiff(rule.getFromDate(), rule.getToDate(), TimeUnit.DAYS));
		if (rule.getEachCalVal().equals(Calendar.DATE) && days < 1) {
			logger.warn("match period is lesser than select period, ie from-date to to-date time interval is "+days+" day(s) and does not contain a "+rule.getEachStr());
		} else if (rule.getEachCalVal().equals(Calendar.MONTH) && days < 28) {
			logger.warn("match period is lesser than select period, ie from-date to to-date time interval is "+days+" day(s) and does not contain a "+rule.getEachStr());
		} else if (rule.getEachCalVal().equals(Calendar.YEAR) && days < 365) {
			logger.warn("match period is lesser than select period, ie from-date to to-date time interval is "+days+" day(s) and does not contain a "+rule.getEachStr());
		}
		if (rule.getDates()!=null && rule.getDates().size()>0) {
			for (RuleDynDate date : rule.getDates()) {
				if (date.isFixed()) {
					if (!( (date.getDate().compareTo(rule.getFromDate()) > 1 && date.getDate().compareTo(rule.getToDate()) <= 0) || (date.getDate().compareTo(rule.getFromDate()) <=0  && date.getDate().compareTo(rule.getToDate()) > 1) ) ) {
						throw new RuleCheckException("Date "+date+" is not included in from-date to to-date time interval");
					}
				}
			}
			if (rule.getDates().size() < rule.getSelect()) {
				throw new RuleCheckException("Number of date in AT clause must at least equals to SELECT clause");
			}
		}
			
		return result;
	}
	
	public Rule check(CompiledRule rule, boolean disableCheck) throws RuleCheckException {
		if (!disableCheck) {
			return this.check(rule);
		} else {
			return new Rule(rule);
		}
	}
}
