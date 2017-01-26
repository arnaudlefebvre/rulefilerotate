package com.umanis.tools.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;

import com.umanis.logs.log4j.SystemOutToSlf4j;


public class CommandLineBuilder {
	
	private ArrayList<String> rules;
	private String path;
	private boolean sub;
	private String name;
	private String key;
	private String dformat;
	private String dreplace;
	private String dpattern;
	private Boolean zipAction;
	private Boolean moveAction;
	private Boolean forceDeleteAction;
	private Boolean action;
	private String zipActionPath;
	private String moveActionPath;
	private Boolean overwrite;
	private Boolean force;
	private Boolean autoresolve;
	private Boolean skipError;
	private String actionsStr;
	
	private static final Logger logger = Logger.getLogger(CommandLineBuilder.class);
	public static final String helpTxt = "java [jvm opts] -jar rulefilerotate-"+CommandLineBuilder.class.getPackage().getImplementationVersion()+".jar -p path [-r rule | -f file] [options] ";
	public static final Comparator<Option> OptionComparator = new Comparator<Option>() {
		public int compare(Option o1, Option o2) {
    		if (o1 instanceof com.umanis.tools.cli.Option && o2 instanceof com.umanis.tools.cli.Option) {
    			com.umanis.tools.cli.Option uo1 = (com.umanis.tools.cli.Option) o1;
    			com.umanis.tools.cli.Option uo2 = (com.umanis.tools.cli.Option) o2;
    			if (uo1.getImportance() < uo2.getImportance() ) {
    				return -1;
    			} else if (uo1.getImportance() > uo2.getImportance() ) {
    				return 1;
    			} else {
	    			if ((o1.isRequired() && o2.isRequired()) || (!o1.isRequired() && !o2.isRequired())) {
	    				if (uo1.getOrder() < uo2.getOrder())  {
							return -1;
	    				} else if (uo1.getOrder() > uo2.getOrder()) {
	    					return 1;
	    				} else {
	    					return o1.getOpt().compareTo(o2.getOpt());
	    				}
	    			} else if (o1.isRequired()) {
	    				return -1;
	    			} else {
	    				return 1;
	    			}
    			}
    		}
    		logger.error("CANNOT COMPARE OPTIONS");
    		return 0;
		}
	};
	
	
	public CommandLineBuilder() {
		this.sub = false;
		this.name = ".*";
		this.actionsStr = "";
		this.key = com.umanis.tools.file.rule.Constants.FILE_ITEM_OPT_MODIF;
		this.rules = new ArrayList<String>();// TODO Auto-generated constructor stub
		this.zipAction = false;
		this.moveAction = false;
		this.forceDeleteAction = false;
		this.overwrite = false;
		this.action = false;
		this.force = false;
		this.skipError = false;
		this.autoresolve = true;
		File cur = new File("");
		this.zipActionPath  = cur.getAbsolutePath();
		//logger.info(this.zipActionPath);
		this.moveActionPath = cur.getAbsolutePath();		
	}
	
	public boolean parseForHelp(Options help,Options options, String[] args, boolean stop) throws ParseException {
		CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
		CommandLine cmd = parser.parse(help, args, true);
		if (help.hasOption(Constants.H_OPT)) {
			if (cmd.hasOption(Constants.H_OPT)) {
				printHelp(options);
				return true;
			}
		} 
		if (help.hasOption(Constants.MAN_OPT)) {
			if (cmd.hasOption(Constants.MAN_OPT)) {				
				try {					
				    logger.info(readMan("/"+Constants.MAN_FILE_FR));
				    printHelp(options);
					return true;
				} catch (IOException io) {
					throw new ParseException("Wrong  -"+Constants.MAN_OPT+" option. Unreadable file ? "+io);
				}
			}
		}
		return false;
	}
	
	public CommandLine parseAndCheck(Options options, String[] args, boolean stop) throws ParseException {
		CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
		CommandLine cmd = parser.parse(options, args, true);		
		parser = new org.apache.commons.cli.DefaultParser();
		cmd = parser.parse(options, args, false);
		if (!cmd.hasOption(Constants.RULE_OPT) && !cmd.hasOption(Constants.RULE_FILE_OPT)) {
			throw new ParseException("Missing -"+Constants.RULE_OPT+" or -"+Constants.RULE_FILE_OPT+" option. ");
		} else if (!cmd.hasOption(Constants.PATH_OPT)) {
			throw new ParseException("Missing -"+Constants.PATH_OPT+" option. ");
		}
		if (cmd.hasOption(Constants.RULE_OPT)) {
			this.rules.add(cmd.getOptionValue(Constants.RULE_OPT));
		} else {
			loadRulesFromFile(cmd);
		}
		this.path = cmd.getOptionValue(Constants.PATH_OPT);
		if (cmd.hasOption(Constants.SUB_OPT)) {
			this.sub = true;
		}
		if (cmd.hasOption(Constants.NAME_OPT)) {
			this.name = cmd.getOptionValue(Constants.NAME_OPT);
		}
		if (cmd.hasOption(Constants.KEY_OPT)) {
			this.key = cmd.getOptionValue(Constants.KEY_OPT);
			if (!com.umanis.tools.file.rule.Constants.FILE_ITEM_OPT_MODIF.equals(this.key) && ! com.umanis.tools.file.rule.Constants.FILE_ITEM_OPT_NAME.equals(this.key)) {
				throw new ParseException("Wrong  -"+Constants.PATH_OPT+" option. ");
			} else if (com.umanis.tools.file.rule.Constants.FILE_ITEM_OPT_NAME.equals(this.key)) {
				if (!cmd.hasOption(Constants.DFORMAT_OPT)) {
					this.dformat = Constants.DFORMAT_OPT_DEFAULT;
				} else {
					this.dformat = cmd.getOptionValue(Constants.DFORMAT_OPT);
				}
				if (!cmd.hasOption(Constants.DPATTERN_OPT)) {
					throw new ParseException("Missing -"+Constants.DPATTERN_OPT+" option. ");
				} else if (!cmd.hasOption(Constants.DREPLACE_OPT)) {
					throw new ParseException("Missing -"+Constants.DREPLACE_OPT+" option. ");
				}					
				this.dpattern = cmd.getOptionValue(Constants.DPATTERN_OPT);
				this.dreplace = cmd.getOptionValue(Constants.DREPLACE_OPT);
			}
		}
		this.zipAction = cmd.hasOption(Constants.ACTION_ZIP_OPT);
		this.moveAction = cmd.hasOption(Constants.ACTION_MOV_OPT);
		this.forceDeleteAction = cmd.hasOption(Constants.ACTION_FD_OPT);
		this.overwrite = cmd.hasOption(Constants.OVER_OPT);
		this.autoresolve = !cmd.hasOption(Constants.AUTO_OPT);
		this.skipError = cmd.hasOption(Constants.SKIPERR_OPT);
		if (this.overwrite && ! (this.zipAction || this.moveAction)) {
			throw new ParseException("Wrong -"+Constants.OVER_OPT+" option. ");
		}
		if (this.forceDeleteAction && ! (this.zipAction || this.moveAction)) {
			logger.info("-WARNING- File will be deleted with no backup ! ");
		}
		if (this.zipAction) {
			this.zipActionPath = cmd.getOptionValue(Constants.ACTION_ZIP_OPT);
			this.actionsStr += "Z";
		}
		if (this.moveAction) {
			this.moveActionPath = cmd.getOptionValue(Constants.ACTION_MOV_OPT);
			this.actionsStr += "M";
		}
		if (this.forceDeleteAction) {			
			this.actionsStr += "D";
		}
		if (!(this.zipAction || this.moveAction || this.forceDeleteAction)) {
			logger.info("NO ACTION : Select Mode ");
		} else {
			this.action = true;
		}
		this.force = cmd.hasOption(Constants.FORCE_OPT);
		return cmd;
	}

	private void loadRulesFromFile(CommandLine cmd) throws ParseException {
		String fpath = cmd.getOptionValue(Constants.RULE_FILE_OPT);
		try {
			File f = new File(fpath);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
		    while ((line = br.readLine()) != null) {
		       this.rules.add(line);
		    }
		} catch (IOException io) {
			throw new ParseException("Wrong  -"+Constants.RULE_FILE_OPT+" option. Unreadable file ? "+io);
		}
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(Constants.FIXED_WIDTH);
		options.addOption(Constants.HELP);
		options.addOption(Constants.MAN);
		formatter.setOptionComparator(OptionComparator);				
		SystemOutToSlf4j.enableForClass(CommandLineBuilder.class);
		formatter.printHelp(helpTxt, options);		
		SystemOutToSlf4j.disable();
	}
	
	private String readMan(String path) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer man = new StringBuffer();
	    while ((line = br.readLine()) != null) {
	    	man.append(WordUtils.wrap(line,Constants.FIXED_WIDTH)).append(String.format("%n"));
	    }
	    return man.toString();
	}
	
	public ArrayList<String> getRules() {
		return rules;
	}

	public String getPath() {
		return path;
	}


	public boolean isSub() {
		return sub;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getDformat() {
		return dformat;
	}


	public String getDreplace() {
		return dreplace;
	}

	public String getDpattern() {
		return dpattern;
	}

	public Boolean getZipAction() {
		return zipAction;
	}

	public Boolean getMoveAction() {
		return moveAction;
	}


	public Boolean getForceDeleteAction() {
		return forceDeleteAction;
	}


	public Boolean getAction() {
		return action;
	}

	public String getZipActionPath() {
		return zipActionPath;
	}


	public String getMoveActionPath() {
		return moveActionPath;
	}


	public Boolean getOverwrite() {
		return overwrite;
	}

	public Boolean getForce() {
		return force;
	}

	public Boolean getAutoresolve() {
		return autoresolve;
	}

	public Boolean getSkipError() {
		return skipError;
	}
	
	public String getActionsStr() {
		return this.actionsStr;
	}
}
