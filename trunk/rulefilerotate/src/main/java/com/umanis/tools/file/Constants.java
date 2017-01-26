package com.umanis.tools.file;

import com.umanis.tools.cli.Option;

public final class Constants {
	public static final Integer FIXED_WIDTH = 100;
	public static final String MAN_FILE_FR = "man-fr.txt";
	
	/** INPUT PARAMETERS **/
	public static final String RULE_OPT = "r";
	public static final String RULE_LONGOPT = "rule";
	public static final String RULE_ARGNAME = "";
	public static final String RULE_OPT_DESC = "REQUIRED : rule to select files, use double-quotes";
	
	public static final String RULE_FILE_OPT = "f";
	public static final String RULE_FILE_LONGOPT = "rules-file";
	public static final String RULE_FILE_ARGNAME = "file";
	public static final String RULE_FILE_OPT_DESC = "REQUIRED : file of rules, one rule by line";
	
	public static final String H_OPT = "h";
	public static final String H_LONGOPT = "help";
	public static final String H_OPT_DESC = "Print this message";
	
	public static final String MAN_OPT = "man";
	public static final String MAN_LONGOPT = "manual";
	public static final String MAN_OPT_DESC = "Print manual";
	
	public static final String PATH_OPT = "p";
	public static final String PATH_LONGOPT = "path";
	public static final String PATH_ARGNAME = "folder path";
	public static final String PATH_OPT_DESC = "REQUIRED : Folder path to scan, use double-quotes if spaces";
	
	public static final String SUB_OPT = "R";
	public static final String SUB_LONGOPT = "recursive";
	public static final String SUB_OPT_DESC = "OPTIONAL : Scan subfolders (default : no)";
	
	public static final String NAME_OPT = "n";
	public static final String NAME_LONGOPT = "name";
	public static final String NAME_ARGNAME = "file name regex";
	public static final String NAME_OPT_DESC = "OPTIONAL : Filter files by name in path (using regex)";
	
	public static final String KEY_OPT = "k";
	public static final String KEY_LONGOPT = "key";
	public static final String KEY_ARGNAME = "MODIF|NAME";
	public static final String KEY_OPT_DESC = "OPTIONAL : MODIF(default) or NAME : File key is file last modif date (MODIF) or extracted from file name (NAME)";
	
	public static final String DFORMAT_OPT = "d";
	public static final String DFORMAT_LONGOPT = "df";
	public static final String DFORMAT_ARGNAME = "java date pattern";
	public static final String DFORMAT_OPT_DESC = "MANDATORY if key is NAME : Simple Date format used to create date object from date extracted on file name. (Default : yyyyMMdd). See -"+Constants.DPATTERN_OPT+" and -"+Constants.DREPLACE_OPT+" options. See https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html";
	public static final String DFORMAT_OPT_DEFAULT = "yyyyMMdd";
	
	public static final String DREPLACE_OPT = "c";
	public static final String DREPLACE_LONGOPT = "dreplace";
	public static final String DREPLACE_ARGNAME = "regex replace";
	public static final String DREPLACE_OPT_DESC = "MANDATORY if key is NAME : Use matching group on file name with extract regex option. See -"+Constants.DPATTERN_OPT+" and -"+Constants.DFORMAT_OPT+" options."
			+ " Example For myapp_x_20161229_y.log : pattern=myapp_.*_([0-9]{4})([0-9]{2})([0-9]{2})_.*.log   and replace=$1$2$3";
	
	public static final String DPATTERN_OPT = "e";
	public static final String DPATTERN_LONGOPT = "extract";
	public static final String DPATTERN_ARGNAME = "regex pattern";
	public static final String DPATTERN_OPT_DESC = "MANDATORY if key is NAME : Simple date extractor from file name. Use regex and groups to extract date. See -"+Constants.DREPLACE_OPT+" and -"+Constants.DFORMAT_OPT+" options."
			+ " Example For myapp_x_20161229_y.log : pattern=myapp_.*_([0-9]{4})([0-9]{2})([0-9]{2})_.*.log   and replace=$1$2$3";
		
	public static final String ACTION_FD_OPT = "fd";
	public static final String ACTION_FD_LONGOPT = "force-delete";
	public static final String ACTION_FD_OPT_DESC = "OPTIONAL :/!\\Delete all files to process (ie:not selected by rules)";
	
	public static final String ACTION_ZIP_OPT = "z";
	public static final String ACTION_ZIP_LONGOPT = "zip";
	public static final String ACTION_ZIP_ARGNAME = "target folder";
	public static final String ACTION_ZIP_OPT_DESC = "OPTIONAL : Create zipfile in <target folder> for each file to process (ie : not matched by rules). See -"+Constants.ACTION_FD_OPT+" option to delete original file";
	
	public static final String ACTION_MOV_OPT = "m";
	public static final String ACTION_MOV_LONGOPT = "move";
	public static final String ACTION_MOV_ARGNAME = "target folder";
	public static final String ACTION_MOV_OPT_DESC = "OPTIONAL : Move files to process to <target folder> (ie : not matched by rules). See -"+Constants.ACTION_FD_OPT+" option to delete original file";
	
	
	public static final String OVER_OPT = "o";
	public static final String OVER_LONGOPT = "overwrite";
	public static final String OVER_OPT_DESC = "OPTIONAL : Overwrite target files if already exist";
	
	public static final String FORCE_OPT = "F";
	public static final String FORCE_LONGOPT = "force";
	public static final String FORCE_OPT_DESC = "OPTIONAL : Force apply action on files to process without confirmation";
	
	public static final String AUTO_OPT = "da";
	public static final String AUTO_LONGOPT = "dis-autoresolve";
	public static final String AUTO_OPT_DESC = "OPTIONAL : Disable autoresolve for rule files selection";
	
	public static final String SKIPERR_OPT = "skip";
	public static final String SKIPERR_LONGOPT = "skip-error";
	public static final String SKIPERR_OPT_DESC = "OPTIONAL : Skip error when performing actions on file. Files will not be deleted (-"+Constants.ACTION_FD_OPT+" option) if previous action failed";
	
	public static final String FAILRULE_OPT = "failrule";
	public static final String FAILRULE_LONGOPT = "fail-on-empty-rule";
	public static final String FAILRULE_OPT_DESC = "OPTIONAL : Throw error if one rule as less match than SELECT clause in one of its period";
	
//	@SuppressWarnings("static-access")
//	public static final Option RULE = new Option(OptionBuilder.withDescription(Constants.RULE_OPT_DESC).hasArg()
//			.withArgName(Constants.RULE_ARGNAME).withLongOpt(Constants.RULE_LONGOPT).create(Constants.RULE_OPT),Option.REQUIRED,1);
//	@SuppressWarnings("static-access")
//	public static final Option RULE_FILE = new Option(OptionBuilder.withDescription(Constants.RULE_FILE_OPT_DESC).hasArg()
//			.withArgName(Constants.RULE_FILE_ARGNAME).withLongOpt(Constants.RULE_FILE_LONGOPT).create(Constants.RULE_FILE_OPT),Option.REQUIRED,2);
//	@SuppressWarnings("static-access")
//	public static final Option HELP = new Option(OptionBuilder.withDescription(Constants.H_OPT_DESC).withLongOpt(Constants.H_LONGOPT)
//			.create(Constants.H_OPT),Option.HELP,3);
//	@SuppressWarnings("static-access")
//	public static final Option MAN = new Option(OptionBuilder.withDescription(Constants.MAN_OPT_DESC).withLongOpt(Constants.MAN_LONGOPT)
//			.create(Constants.MAN_OPT),Option.HELP,4);
//	@SuppressWarnings("static-access")
//	public static final Option PATH = new Option(OptionBuilder.withDescription(Constants.PATH_OPT_DESC).hasArg().isRequired()
//			.withArgName(Constants.PATH_ARGNAME).withLongOpt(Constants.PATH_LONGOPT).create(Constants.PATH_OPT),Option.REQUIRED,5);
//	@SuppressWarnings("static-access")
//	public static final Option SUB = new Option(OptionBuilder.withDescription(Constants.SUB_OPT_DESC)
//			.withLongOpt(Constants.SUB_LONGOPT).create(Constants.SUB_OPT),Option.OPTIONNAL);
//	@SuppressWarnings("static-access")
//	public static final Option NAME = new Option(OptionBuilder.withDescription(Constants.NAME_OPT_DESC).hasArg()
//			.withArgName(Constants.NAME_ARGNAME).withLongOpt(Constants.NAME_LONGOPT).create(Constants.NAME_OPT),Option.OPTIONNAL);
//	@SuppressWarnings("static-access")
//	public static final Option KEY = new Option(OptionBuilder.withDescription(Constants.KEY_OPT_DESC).hasArg()
//			.withArgName(Constants.KEY_ARGNAME).withLongOpt(Constants.KEY_LONGOPT).create(Constants.KEY_OPT),Option.OPTIONNAL);
//	@SuppressWarnings("static-access")
//	public static final Option DFORMAT = new Option(OptionBuilder.withDescription(Constants.DFORMAT_OPT_DESC).hasArg()
//			.withArgName(Constants.DFORMAT_ARGNAME).withLongOpt(Constants.DFORMAT_LONGOPT).create(Constants.DFORMAT_OPT),Option.MANDATORY,9);
//	@SuppressWarnings("static-access")
//	public static final Option DREPLACE = new Option(OptionBuilder.withDescription(Constants.DREPLACE_OPT_DESC).hasArg()
//			.withArgName(Constants.DREPLACE_ARGNAME).withLongOpt(Constants.DREPLACE_LONGOPT).create(Constants.DREPLACE_OPT),Option.MANDATORY,10);
//	@SuppressWarnings("static-access")
//	public static final Option DPATTERN = new Option(OptionBuilder.withDescription(Constants.DPATTERN_OPT_DESC).hasArg()
//			.withArgName(Constants.DPATTERN_ARGNAME).withLongOpt(Constants.DPATTERN_LONGOPT).create(Constants.DPATTERN_OPT),Option.MANDATORY,11);
//	@SuppressWarnings("static-access")
//	public static final Option ACTION_ZIP = new Option(OptionBuilder.withDescription(Constants.ACTION_ZIP_OPT_DESC).hasArg()
//			.withArgName(Constants.ACTION_ZIP_ARGNAME).withLongOpt(Constants.ACTION_ZIP_LONGOPT).create(Constants.ACTION_ZIP_OPT),Option.OPT_CAT1);
//	@SuppressWarnings("static-access")
//	public static final Option ACTION_MOV = new Option(OptionBuilder.withDescription(Constants.ACTION_MOV_OPT_DESC).hasArg()
//			.withArgName(Constants.ACTION_MOV_ARGNAME).withLongOpt(Constants.ACTION_MOV_LONGOPT).create(Constants.ACTION_MOV_OPT),Option.OPT_CAT1);
//	@SuppressWarnings("static-access")
//	public static final Option ACTION_FD = new Option(OptionBuilder.withDescription(Constants.ACTION_FD_OPT_DESC)
//			.withLongOpt(Constants.ACTION_FD_LONGOPT).create(Constants.ACTION_FD_OPT),Option.OPT_CAT1);
//	@SuppressWarnings("static-access")
//	public static final Option OVER = new Option(OptionBuilder.withDescription(Constants.OVER_OPT_DESC)
//			.withLongOpt(Constants.OVER_LONGOPT).create(Constants.OVER_OPT),Option.OPTIONNAL);
//	@SuppressWarnings("static-access")
//	public static final Option FORCE = new Option(OptionBuilder.withDescription(Constants.FORCE_OPT_DESC)
//			.withLongOpt(Constants.FORCE_LONGOPT).create(Constants.FORCE_OPT),Option.OPT_CAT3);
//	@SuppressWarnings("static-access")
//	public static final Option AUTO = new Option(OptionBuilder.withDescription(Constants.AUTO_OPT_DESC)
//			.withLongOpt(Constants.AUTO_LONGOPT).create(Constants.AUTO_OPT),Option.OPT_CAT3);
//	@SuppressWarnings("static-access")
//	public static final Option SKIPERR = new Option(OptionBuilder.withDescription(Constants.SKIPERR_OPT_DESC)
//			.withLongOpt(Constants.SKIPERR_LONGOPT).create(Constants.SKIPERR_OPT),Option.OPT_CAT3);
//	@SuppressWarnings("static-access")
//	public static final Option FAILRULE = new Option(OptionBuilder.withDescription(Constants.FAILRULE_OPT_DESC)
//			.withLongOpt(Constants.FAILRULE_LONGOPT).create(Constants.FAILRULE_OPT),Option.OPT_CAT3);
	@SuppressWarnings("static-access")
	public static final Option RULE = new Option(org.apache.commons.cli.Option.builder(Constants.RULE_OPT).desc(Constants.RULE_OPT_DESC).hasArg()
			.argName(Constants.RULE_ARGNAME).longOpt(Constants.RULE_LONGOPT).build(),Option.REQUIRED,1);
	@SuppressWarnings("static-access")
	public static final Option RULE_FILE = new Option(org.apache.commons.cli.Option.builder(Constants.RULE_FILE_OPT).desc(Constants.RULE_FILE_OPT_DESC).hasArg()
			.argName(Constants.RULE_FILE_ARGNAME).longOpt(Constants.RULE_FILE_LONGOPT).build(),Option.REQUIRED,2);
	@SuppressWarnings("static-access")
	public static final Option HELP = new Option(org.apache.commons.cli.Option.builder(Constants.H_OPT).desc(Constants.H_OPT_DESC).longOpt(Constants.H_LONGOPT)
			.build(),Option.HELP,3);
	@SuppressWarnings("static-access")
	public static final Option MAN = new Option(org.apache.commons.cli.Option.builder(Constants.MAN_OPT).desc(Constants.MAN_OPT_DESC).longOpt(Constants.MAN_LONGOPT)
			.build(),Option.HELP,4);
	@SuppressWarnings("static-access")
	public static final Option PATH = new Option(org.apache.commons.cli.Option.builder(Constants.PATH_OPT).desc(Constants.PATH_OPT_DESC).hasArg().required()
			.argName(Constants.PATH_ARGNAME).longOpt(Constants.PATH_LONGOPT).build(),Option.REQUIRED,5);
	@SuppressWarnings("static-access")
	public static final Option SUB = new Option(org.apache.commons.cli.Option.builder(Constants.SUB_OPT).desc(Constants.SUB_OPT_DESC)
			.longOpt(Constants.SUB_LONGOPT).build(),Option.OPTIONNAL);
	@SuppressWarnings("static-access")
	public static final Option NAME = new Option(org.apache.commons.cli.Option.builder(Constants.NAME_OPT).desc(Constants.NAME_OPT_DESC).hasArg()
			.argName(Constants.NAME_ARGNAME).longOpt(Constants.NAME_LONGOPT).build(),Option.OPTIONNAL);
	@SuppressWarnings("static-access")
	public static final Option KEY = new Option(org.apache.commons.cli.Option.builder(Constants.KEY_OPT).desc(Constants.KEY_OPT_DESC).hasArg()
			.argName(Constants.KEY_ARGNAME).longOpt(Constants.KEY_LONGOPT).build(),Option.OPTIONNAL);
	@SuppressWarnings("static-access")
	public static final Option DFORMAT = new Option(org.apache.commons.cli.Option.builder(Constants.DFORMAT_OPT).desc(Constants.DFORMAT_OPT_DESC).hasArg()
			.argName(Constants.DFORMAT_ARGNAME).longOpt(Constants.DFORMAT_LONGOPT).build(),Option.MANDATORY,9);
	@SuppressWarnings("static-access")
	public static final Option DREPLACE = new Option(org.apache.commons.cli.Option.builder(Constants.DREPLACE_OPT).desc(Constants.DREPLACE_OPT_DESC).hasArg()
			.argName(Constants.DREPLACE_ARGNAME).longOpt(Constants.DREPLACE_LONGOPT).build(),Option.MANDATORY,10);
	@SuppressWarnings("static-access")
	public static final Option DPATTERN = new Option(org.apache.commons.cli.Option.builder(Constants.DPATTERN_OPT).desc(Constants.DPATTERN_OPT_DESC).hasArg()
			.argName(Constants.DPATTERN_ARGNAME).longOpt(Constants.DPATTERN_LONGOPT).build(),Option.MANDATORY,11);
	@SuppressWarnings("static-access")
	public static final Option ACTION_ZIP = new Option(org.apache.commons.cli.Option.builder(Constants.ACTION_ZIP_OPT).desc(Constants.ACTION_ZIP_OPT_DESC).hasArg()
			.argName(Constants.ACTION_ZIP_ARGNAME).longOpt(Constants.ACTION_ZIP_LONGOPT).build(),Option.OPT_CAT1);
	@SuppressWarnings("static-access")
	public static final Option ACTION_MOV = new Option(org.apache.commons.cli.Option.builder(Constants.ACTION_MOV_OPT).desc(Constants.ACTION_MOV_OPT_DESC).hasArg()
			.argName(Constants.ACTION_MOV_ARGNAME).longOpt(Constants.ACTION_MOV_LONGOPT).build(),Option.OPT_CAT1);
	@SuppressWarnings("static-access")
	public static final Option ACTION_FD = new Option(org.apache.commons.cli.Option.builder(Constants.ACTION_FD_OPT).desc(Constants.ACTION_FD_OPT_DESC)
			.longOpt(Constants.ACTION_FD_LONGOPT).build(),Option.OPT_CAT1);
	@SuppressWarnings("static-access")
	public static final Option OVER = new Option(org.apache.commons.cli.Option.builder(Constants.OVER_OPT).desc(Constants.OVER_OPT_DESC)
			.longOpt(Constants.OVER_LONGOPT).build(),Option.OPTIONNAL);
	@SuppressWarnings("static-access")
	public static final Option FORCE = new Option(org.apache.commons.cli.Option.builder(Constants.FORCE_OPT).desc(Constants.FORCE_OPT_DESC)
			.longOpt(Constants.FORCE_LONGOPT).build(),Option.OPT_CAT3);
	@SuppressWarnings("static-access")
	public static final Option AUTO = new Option(org.apache.commons.cli.Option.builder(Constants.AUTO_OPT).desc(Constants.AUTO_OPT_DESC)
			.longOpt(Constants.AUTO_LONGOPT).build(),Option.OPT_CAT3);
	@SuppressWarnings("static-access")
	public static final Option SKIPERR = new Option(org.apache.commons.cli.Option.builder(Constants.SKIPERR_OPT).desc(Constants.SKIPERR_OPT_DESC)
			.longOpt(Constants.SKIPERR_LONGOPT).build(),Option.OPT_CAT3);
	@SuppressWarnings("static-access")
	public static final Option FAILRULE = new Option(org.apache.commons.cli.Option.builder(Constants.FAILRULE_OPT).desc(Constants.FAILRULE_OPT_DESC)
			.longOpt(Constants.FAILRULE_LONGOPT).build(),Option.OPT_CAT3);
	
	public static final String MANUAL = String.format("Manual : %nRuleFileRotate is a tool designed to extend commons log/file rotator products (logrotate, log4j, logbacks, etc."
			+"It provides ability to select files based on a date (last file modif or date contained in name) and to writes rules to select more or less depending on their age"
			+"The main goal is to facilitate system administration on production environment where somes files (logs, database backups, etc) must be saved for a time and then moved, archive, or even deleted"
			+"For example : If a database dump is created each day on a production environment, and if you have disk usage restrictions, you will have to find a way to keep a limited number of this dumps."
			+"Usually you will want to keep a maximum number of theses backups for a defined time and then delete it or move it to another storage."
			+"%nThis tool offers you to write rules to select files to save and then delete, move or archive other files. Rules were basically create to let you express something like :"
			+"For 2 days from today, keep all files. For 2 month from todays keep at least 1 file per week, etc."
			+"Rules use a custom syntax describes below"
			+"Rule [%nFROM (startdate) %nTO (enddate) %nSELECT (number of files) %nEACH (period) %n[AT (dates)] %n(select option)]"
			+" FROM (startdate) : startdate can be a fixed date (FORMAT) or a dynamic date constructed using ");
	
			
	
	
}
