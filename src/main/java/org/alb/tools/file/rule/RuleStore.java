package org.alb.tools.file.rule;

import java.util.ArrayList;

import org.alb.tools.file.rule.exception.RuleException;
import org.apache.log4j.Logger;

public class RuleStore {
	
	public static final Logger logger = Logger.getLogger(RuleStore.class);
	private boolean disableCheck = false;
	
	private ArrayList<Rule> store;
	
	public RuleStore() {
		this.store = new ArrayList<Rule>(); 
	}
	
	public void add(Rule rule) {
		store.add(rule);
	}
	
	public void add(String rule, boolean disableCheck) throws  RuleException {
		store.add(RuleBuilder.createRule(rule,disableCheck));
	}
	
	public void add(String rule) throws RuleException {
		store.add(RuleBuilder.createRule(rule,this.disableCheck));
	}
	
	public void displayStore() {
		for (Rule rule : store) {
			logger.info(rule.getRuleInfo());
		}
	}
	
	public void run() {
		for (Rule rule : store) {
			//logger.info("Go Go gadget au ...");
			rule.initScale();
			//Provide objects here !
			rule.run(null);
		}
	}

	public boolean isDisableCheck() {
		return disableCheck;
	}

	public void setDisableCheck(boolean disableCheck) {
		this.disableCheck = disableCheck;
	}
	
	
}
