package org.alb.tools.file;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.alb.tools.file.rule.RuleWorker;
import org.alb.tools.file.util.Functions;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RuleFileRotate {
	
	public static final String SYS_PROP_LOG4J = "log4j";
	public static final String SYS_PROP_LOG4J_DEF = "/log4j.properties";
	public static final String SYS_PROP_LOGDATE = "current.date";
	public static final String SYS_PROP_LOGDATE_DEF = "yyyyMMddhhmmssSSS";
	public static final String SYS_PROP_LOGPATH = "logpath";
	public static final String SYS_PROP_LOGPATH_DEF = "./";
	public static final String SYS_PROP_LOGFNAME = "logfilename";
	public static final String SYS_PROP_LOGFNAME_DEF = "rulefilerotate_";
	public static final String SYS_PROP_LOGLEVEL = "loglevel";
	public static final String SYS_PROP_LOGLEVEL_DEF = "INFO";
	
	static{
		if (System.getProperty(RuleFileRotate.SYS_PROP_LOG4J) == null) {
	    	System.setProperty(RuleFileRotate.SYS_PROP_LOG4J, RuleFileRotate.SYS_PROP_LOG4J_DEF);
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(RuleFileRotate.SYS_PROP_LOGDATE_DEF);
		    System.setProperty(RuleFileRotate.SYS_PROP_LOGDATE, dateFormat.format(new Date()));
		    if (System.getProperty(RuleFileRotate.SYS_PROP_LOGPATH) == null) {
		    	System.setProperty(RuleFileRotate.SYS_PROP_LOGPATH, RuleFileRotate.SYS_PROP_LOGPATH_DEF);
		    }
		    if (System.getProperty(RuleFileRotate.SYS_PROP_LOGFNAME) == null) {
		    	System.setProperty(RuleFileRotate.SYS_PROP_LOGFNAME, RuleFileRotate.SYS_PROP_LOGFNAME_DEF);
		    }		    
		    if (System.getProperty(RuleFileRotate.SYS_PROP_LOGLEVEL) == null) {
		    	System.setProperty(RuleFileRotate.SYS_PROP_LOGLEVEL,RuleFileRotate.SYS_PROP_LOGLEVEL_DEF);
		    }
		    System.out.println("Logging to : "+(new File(System.getProperty(RuleFileRotate.SYS_PROP_LOGPATH)+System.getProperty(RuleFileRotate.SYS_PROP_LOGFNAME)+System.getProperty(RuleFileRotate.SYS_PROP_LOGDATE)+".log").getAbsolutePath()));
	    }		
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		URL url = RuleFileRotate.class.getResource(System.getProperty(RuleFileRotate.SYS_PROP_LOG4J));
		PropertyConfigurator.configure(url);
		Logger logger = Logger.getLogger(RuleFileRotate.class);
		
		//Help
		Options help = new Options();
		help.addOption(Constants.HELP);
		help.addOption(Constants.MAN);

		// Definition des parametres d'entrees
		Options options = new Options();
		options.addOption(Constants.RULE); //requis (ou fichier de règles )
		options.addOption(Constants.RULE_FILE); // requis (ou règle)
		options.addOption(Constants.PATH); // requis
		options.addOption(Constants.SUB); //facultatif
		options.addOption(Constants.NAME); //facultatif
		options.addOption(Constants.KEY); //facultatif
		options.addOption(Constants.DFORMAT); //facultatif
		options.addOption(Constants.DREPLACE); //facultatif
		options.addOption(Constants.DPATTERN); //facultatif
		options.addOption(Constants.ACTION_FD); //facultatif
		options.addOption(Constants.ACTION_MOV); //facultatif
		options.addOption(Constants.ACTION_ZIP); //facultatif
		options.addOption(Constants.OVER); //facultatif
		options.addOption(Constants.FORCE); // facultatif
		options.addOption(Constants.AUTO); // facultatif
		options.addOption(Constants.SKIPERR); // facultatif
		
		logger.info("------------------------------------------------------------------------");
		logger.info("--                          RuleFileRotate                            --");
		logger.info("------------------------------------------------------------------------");

		try {
			// On parse les parametres d'entree
			CommandLineBuilder cmd = new CommandLineBuilder();
			if(cmd.parseForHelp(help, options, args, true)) {
				return;
			}			
			cmd = new CommandLineBuilder();
			cmd.parseAndCheck(options, args, true);
			RuleWorker worker = new RuleWorker();
			worker.run(cmd);
			
		} catch (ParseException exp) {
			logger.error("Wrong options. See -h  or --help for more information : " + exp.getMessage());
		} catch (Exception e) {
			logger.error("Une erreur est survenue",e);			
		} finally {
			logger.info("------------------------------------------------------------------------");
			logger.info("-- End. Total Time : "+Functions.roundMiliTime((System.currentTimeMillis() - start), 3) + "s --");
			logger.info("------------------------------------------------------------------------");
		}

	}

}
