package org.alb.tools.file.rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.alb.tools.file.CommandLineBuilder;
import org.alb.tools.file.rule.exception.ItemActionException;
import org.alb.tools.file.rule.exception.RuleException;
import org.alb.tools.file.util.Functions;
import org.apache.log4j.Logger;

import org.alb.tools.cli.console.ui.ProgressBar;

public class RuleWorker {
	
	public static final Logger logger = Logger.getLogger(RuleWorker.class);
	private boolean disableCheck = false;
	private CommandLineBuilder cmd;
	private ItemStore itemStore;
	private ArrayList<Rule> store;
	private HashMap<Long,Item> rulesHits;
	private HashMap<Long,Item> itemsToProcess;
	
	public RuleWorker() {
		this.store = new ArrayList<Rule>();
		this.rulesHits = new HashMap<Long,Item>();
		this.itemStore = new FileItemStore();
		this.itemsToProcess = new HashMap<Long,Item>();
	}
	
	public void add(Rule rule) {
		store.add(rule);
	}
	
	public void addRule(String rule, boolean disableCheck) throws  RuleException {
		store.add(RuleBuilder.createRule(rule,disableCheck));
	}
	
	protected void addRule(String rule) throws RuleException {
		store.add(RuleBuilder.createRule(rule,this.disableCheck));
	}
	
	protected void addRule(ArrayList<String> rules) throws RuleException {
		for (String rule : rules) {
			store.add(RuleBuilder.createRule(rule,this.disableCheck));
		}
	}
	
	protected void addRule(ArrayList<String> rules, boolean disableCheck) throws RuleException {
		for (String rule : rules) {
			store.add(RuleBuilder.createRule(rule,disableCheck));
		}
	}
	
	protected void createItemStore(CommandLineBuilder cmd) throws ParseException{
		FileItemStore fileStore = new FileItemStore();
		this.cmd = cmd;
		fileStore.createStore(cmd);
		this.itemStore = fileStore;
	}
	
	public boolean isDisableCheck() {
		return disableCheck;
	}

	public void setDisableCheck(boolean disableCheck) {
		this.disableCheck = disableCheck;
	}
	
	public ItemStore getItemStore() {
		return itemStore;
	}

	public void setItemStore(ItemStore itemStore) {
		this.itemStore = itemStore;
	}

	public void displayStore() {
		for (Rule rule : store) {
			logger.info(rule.getRuleInfo());
		}
		if (itemStore != null) {
			itemStore.displayStore();
		}
	}
	
	public void displayRuleStore() {
		for (Rule rule : store) {
			logger.info(rule.getRuleInfo());
		}
	}
	
	public void displayMatches() {
		for (Rule rule : store) {
			logger.info(rule.displayMatches());
		}
	}
	
	public void displayRulesFinalHits() {
		for (Rule rule : store) {
			logger.info(rule.displayFinalHits());
		}
	}
	
	public void displayFinalHits() {
		Item[] items = new Item[this.rulesHits.size()];
		Item[] iat =  this.rulesHits.values().toArray(items);
		List<Item> it = Arrays.asList(iat);
		Collections.sort(it);
		for (Item i : it) {
			logger.info(i.display());
		}
	}
	
	public void displayItemsToProcess() {
		Item[] items = new Item[this.itemsToProcess.size()];
		Item[] iat =  this.itemsToProcess.values().toArray(items);
		List<Item> it = Arrays.asList(iat);
		Collections.sort(it);
		for (Item i : it) {
			logger.info(i.display());
		}
	}
	
	protected void runInit() {
		for (Rule rule : store) {
			rule.initScale();
		}
	}
	
	protected void runSelect() throws ParseException, RuleException {
		for (Entry<Long, Item> entry : itemStore.getStore().entrySet()) {
			for (Rule rule : store) {
				rule.run(entry);
			}
		}
		for (Rule rule : store) {
			if (rule.isMatch()) {
				rule.select(this.cmd.getAutoresolve());
				this.rulesHits.putAll(rule.getFinalHits());				
			} else {
				logger.warn("Rule "+rule.getRawrule()+" Does not match any file !");
			}
		}
		itemsToProcess = new HashMap<Long,Item>();
		itemsToProcess.putAll(this.itemStore.getStore());
		itemsToProcess = Functions.removeAll(itemsToProcess, this.rulesHits);
	}
	
	public void run(CommandLineBuilder cmd) throws NoSuchMethodException, ParseException, RuleException, ItemActionException, IOException {
		addRule(cmd.getRules());
		createItemStore(cmd);
		runInit();
		runSelect();
		displayRulesReport();
		performAction();		
	}
	
	public void performAction() throws NoSuchMethodException, ItemActionException, IOException {
		if (this.cmd.getAction() && this.itemsToProcess.size() > 0) {
			logger.info("------------------------------------------------------------------------");
			logger.info("------------------------------------------------------------------------");
			logger.info("--------------               PROCESSING FILES               ------------");
			logger.info("------------------------------------------------------------------------");
			logger.info("------------------------------------------------------------------------");
			logger.info("Zip : "+(this.cmd.getZipAction()?this.cmd.getZipActionPath():"no"));
			logger.info("Move : "+(this.cmd.getMoveAction()?this.cmd.getMoveActionPath():"no"));
			logger.info("Delete : "+(this.cmd.getForceDeleteAction()?"yes":"no"));
			logger.info("--> "+this.itemsToProcess.size()+" files to process");
			if (!cmd.getForce()) {
				 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			     System.out.print("Do you want to proceed ? y/(n) : ");
			     String s = br.readLine();
			     if (!Constants.CONFIRM_Y.equalsIgnoreCase(s)) {
			    	 logger.info("Action canceled by user");
			    	 return;
			     } else {
			    	 logger.info("Starting - Action confirmed by user");
			     }
			} else {
				logger.info("Starting - Force Mode set by user");
			}
			int start = 0;
			int end = this.itemsToProcess.size();
			StringBuffer buf = new StringBuffer();
			ProgressBar bar = new ProgressBar(end, org.alb.tools.file.Constants.FIXED_WIDTH, "|", "|", "=", "=>", "Done!");
			for (Item i : this.itemsToProcess.values()) {
				buf.append(String.format(" -"+cmd.getActionsStr()+"- "+start+"/"+end+" --> "+i.performAction(this.cmd)+"%n"));
				start += 1;
				bar.show(System.out, start);
			}
			logger.info(" - RESULTS -");
			logger.info(buf);
			logger.info(" SUCCESS ");
		}
	}

	public void displayRulesReport() {
		logger.info("------------------------------------------------------------------------");
		logger.info("--------------");
		logger.info("Results : ");
		logger.info("");
		logger.info(""+this.itemStore.getAll().size()+" file(s) found in ");
		logger.info(""+this.cmd.getPath()+(this.cmd.isSub()?" (with subfolders)":""));
		logger.info("and "+this.itemStore.getStore().size()+" file(s) selected to apply rules once filtered with ("+this.cmd.getName()+") regex on file name");
		logger.info(" ");
		logger.info(" "+this.store.size()+" rules select "+this.rulesHits.size()+ " file(s) to preserve from deletion");
		logger.info(" ");
		logger.info("--------------");
		logger.info("Rule(s) applied ("+this.store.size()+"): ");
		logger.info(" ");
		displayRuleStore();
		logger.info(" ");
		logger.info("--------------");
		logger.info("Preserved file(s) ("+this.rulesHits.size()+"): ");
		displayFinalHits();
		logger.info(" ");
		logger.info("--------------");
		logger.info("File(s) to process ("+this.itemsToProcess.size()+"): ");
		displayItemsToProcess();
		logger.info(" ");
		logger.info("                            ------------------                          ");
		logger.info(" ");
	}
	
	
	
	
}
