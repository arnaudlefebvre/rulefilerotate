package org.alb.tools.file.rule;

import java.text.SimpleDateFormat;

public final class Constants {

	public Constants() {
		// TODO Auto-generated constructor stub
	}

	public static final String REG_PARSE_ALL = "FROM (.*) TO (.*) SELECT (.*) EACH (.*)";
	//Dates : Remember this for select each file, number of date must be equals to SELECT CLAUSE 
	//If dynamic date is used, and match several file, one of them will be selected randomly.
	//PS : If you use dynamic date, remember that file must provide complete date key, especially if you use name and does not extract, hour, min, sec etc.
	//either yyyy/MM/dd hh:mm:ss, yyyy/MM/dd, yyyy/**/**, yyyy/mm/**, ****/**/dd, ****/**/** hh:**:**, ****/**/** hh:mm:**, ****/**/** **:mm:**, ****/**/** **:**:ss
	//A "merger avec la date du fichier. Les * prennent la valeur de la date du fichier
	//Par exemple sion veut un fichier le 15 de chaque mois, on utilise ****/**/15
	//Si pour chaque mois j'ai 3 fichier, le 15, le 20 et le 5, le système va itérer sur les 3 fichier de chaque mois,
	//Et pour chaque fichier, créer une date à partir de la date du fichier et changer dans celle - ci le jour par 15
	//Si la date du fichier est équivalente à cette date modifiée alors il y a match.
	//Dans le cas de période dynamique, il est possible qu'une unité de temps (EACH) complète ne soit pas comprise aux bornes de la période.
	// Par conséquent il est possible qu'il n'y est pas de match. Si c'est le cas, (TODO option fs forceselect) le premier fichier sera sélectionner
	// Sinon erreur.(TODO option skipErrors)
	public static final String REG_PARSE_OPT = "(.*) AT (.*)";
	public static final String REG_PARSE_INTERVAL = "([+\\-][0-9]+)([^0-9\\-+]+)";
	
	public static final String[] FIXED_DATE_FORMATS = { "dd/MM/yyyy", "dd/MM/yyyy HH:mm:ss" };
	public static final String[] DYN_DATE_FORMATS = { "([0-9\\*]{4})\\/([0-9\\*]{2})\\/([0-9\\*]{2})", "([0-9\\*]{4})\\/([0-9\\*]{2})\\/([0-9\\*]{2}) ([0-9\\*]{2}):([0-9\\*]{2}):([0-9\\*]{2})" };
	
	public static final String SELECT_ALL = "ALL";
	
	public static final String KEYWD_LAST = "LAST";
	public static final String KEYWD_FIRST = "FIRST";
	public static final String KEYWD_RANDOM = "RANDOM";
	public static final String KEYWD_DEF = "DEFAULT";
	
	public static final String KEYWD_FROM = "FROMDATE";
	public static final String KEYWD_TODAY = "TODAY";
	public static final String KEYWD_MONTH = "MONTH";
	public static final String KEYWD_YEAR = "YEAR";
	public static final String KEYWD_DAY = "DAY";
	public static final String KEYWD_HOUR= "HOUR";
	
	public static final String FILE_ITEM_OPT_CREATE = "CREATE";
	public static final String FILE_ITEM_OPT_MODIF = "MODIF";
	public static final String FILE_ITEM_OPT_NAME = "NAME";
	public static final String FILE_ITEM_OPT_ = "";
	
	public static final String CONFIRM_Y = "y";
	
	public static final SimpleDateFormat sdfout = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

}
