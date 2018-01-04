package org.alb.tools.file.rule;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alb.tools.file.CommandLineBuilder;
import org.alb.tools.file.RuleFileRotate;
import org.alb.tools.file.rule.exception.FileItemMoveActionException;
import org.alb.tools.file.rule.exception.FileItemZipActionException;
import org.alb.tools.file.rule.exception.ItemActionException;
import org.alb.tools.file.util.Functions;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileItem extends Item implements ItemFileRunable {
	
	public static final Logger logger = Logger.getLogger(FileItem.class);
	
	private static final String ALREADY_EXISTS = "FILE ALREADY EXISTS";
	
	protected File fileItem;
	protected String path;
	protected Date dtCreation;
	protected Date lastModif;
	protected String option;
	protected String dpattern;
	protected String dreplace;
	protected String dformat;	
	protected Date filekey;
	
	public FileItem(File file) throws ParseException {
		super(file.getName(),file);
		this.fileItem = file;
		this.dtCreation = null; //TODO create a method for that
		this.lastModif = new Date(file.lastModified());
		this.dpattern = "";
		this.dreplace = "";
		this.dformat = "";
		this.option = Constants.FILE_ITEM_OPT_MODIF;
		this.path = Functions.getFilePath(file.getAbsolutePath());
		computeKey();
	}
	
	public FileItem(File file, String option, String dpattern, String dreplace, String dformat) throws ParseException {
		super(file.getName(),file);
		this.fileItem = file;
		this.dtCreation = null; //TODO create a method for that
		this.lastModif = new Date(file.lastModified());
		this.dpattern = dpattern;
		this.dreplace = dreplace;
		this.dformat = dformat;
		this.option = option;
		this.path = Functions.getFilePath(file.getAbsolutePath());
		computeKey();
	}
	
	public FileItem(String name, File fileItem, Date dtCreation,
			Date lastModif, String option, String path, String dpattern, String dreplace, String dformat) throws ParseException {
		super(name,fileItem);
		this.fileItem = fileItem;
		this.dtCreation = dtCreation;
		this.lastModif = lastModif;
		this.dpattern = dpattern;
		this.dreplace = dreplace;
		this.dformat = dformat;
		this.option = option;
		this.path = path;
		computeKey();
	}
	
	public FileItem(String name, File fileItem, Date dtCreation,
			Date lastModif, String option, String path) throws ParseException {
		super(name, fileItem);
		this.fileItem = fileItem;
		this.dtCreation = dtCreation;
		this.lastModif = lastModif;
		this.option = option;
		this.path = path;
		computeKey();
	}
	
	public FileItem(String name, File fileItem, Date dtCreation,
			Date lastModif, String path) throws ParseException {
		super(name, fileItem);
		this.fileItem = fileItem;
		this.dtCreation = dtCreation;
		this.lastModif = lastModif;
		this.path = path;
		this.option = Constants.FILE_ITEM_OPT_MODIF;
		computeKey();
	}
	
	private void computeKey() throws ParseException {
		if (Constants.FILE_ITEM_OPT_MODIF.equals(this.option)) {
			this.filekey = this.lastModif;
		} else if (Constants.FILE_ITEM_OPT_NAME.equals(this.option)) {
			try {
				Pattern pattern;
				Matcher matcher;
				pattern = Pattern.compile(this.dpattern);
				matcher = pattern.matcher(this.name);
				if(matcher.find()) {
					String extract = matcher.replaceFirst(this.dreplace);
					SimpleDateFormat sdf = new SimpleDateFormat(this.dformat);
					this.filekey = sdf.parse(extract);
				} else {
					throw new ParseException("Cannot get date from name. Check options.", 0);
				}
			} catch (ParseException p) {
				throw new ParseException("Cannot get date from name. Check options.", 0);
			}
			
		}
	}
	
	private String ZipAction(CommandLineBuilder cmd) throws FileItemZipActionException {
		String result = "";
		ArrayList<File> f = new ArrayList<File>();
		f.add(this.fileItem);		
		try {
			if (Functions.zipFiles(f, cmd.getZipActionPath(), this.fileItem.getName()+".zip",cmd.getOverwrite()) == null && !cmd.getSkipError()) {
				throw new FileItemZipActionException(this.ALREADY_EXISTS);
			}
		} catch (IOException e) {
			throw new FileItemZipActionException(e);
		}
		result = "ZIP -> SUCCESS, ";
		return result;
	}
	
	private String MoveAction(CommandLineBuilder cmd) throws FileItemMoveActionException {
		String result = "";
		try {
			File f = new File(cmd.getMoveActionPath()+System.getProperty("file.separator")+Functions.getFileNameFromPath(this.fileItem.getName()));
			if (f.exists() && ! cmd.getOverwrite()) {
				throw new FileItemMoveActionException(this.ALREADY_EXISTS);
			} else {
				FileUtils.copyFileToDirectory(this.fileItem, new File(cmd.getMoveActionPath()), true );
			}
			
		} catch (IOException e) {
			throw new FileItemMoveActionException(e);
		}
		result = "MOVE -> SUCCESS, ";
		return result;
	}
	
	public String performAction(CommandLineBuilder cmd) throws NoSuchMethodException, ItemActionException {
		StringBuffer log = new StringBuffer();
		if (!System.getProperty(RuleFileRotate.SYS_PROP_LOGLEVEL).equals("DEBUG")) {			
			boolean err = false;
			try  {
				if (cmd.getZipAction()) {
					log.append(ZipAction(cmd));
				}
			} catch (FileItemZipActionException fizae) {
				err = true;
				if (!cmd.getSkipError()) {
					throw new ItemActionException("ZIP FAILED FOR "+this.name+", ",fizae);
				} else  {					
					log.append("ZIP -> FAILED");
					if (this.ALREADY_EXISTS.equals(fizae.getMessage())) {
						log.append("(File already exists)");
					}
					log.append(", ");
				}
			}
			try  {
				if (cmd.getMoveAction()) {
					log.append(this.MoveAction(cmd));
				}
			} catch (FileItemMoveActionException fizae) {
				err = true;
				if (!cmd.getSkipError()) {
					throw new ItemActionException("MOVE FAILED FOR "+this.name+", ",fizae);
				} else  {
					log.append("MOVE -> FAILED");
					if (this.ALREADY_EXISTS.equals(fizae.getMessage())) {
						log.append("(File already exists)");
					}
					log.append(", ");
				}
			}
			try  {
				if (cmd.getForceDeleteAction() && !err) {
					this.fileItem.delete();
					log.append("DELETE -> SUCCESS, ");
				} else if (cmd.getForceDeleteAction() && err) {
					log.append("DELETE -> SKIPPED, ");
				}
			} catch (Exception e) {
				if (!cmd.getSkipError()) {
					throw new ItemActionException("DELETE -> FAILED FOR "+this.name+", ",e);
				} else {
					log.append("DELETE -> FAILED, ");
				}
			}						
			return log.append(" For "+this.fileItem).toString();
		} else {
			return log.append("DEBUG level - No action ! ").append(" for "+this.fileItem).toString();
		}

	}
	
	
	public File getFileItem() {
		return fileItem;
	}
	public void setFileItem(File fileItem) {
		this.fileItem = fileItem;
	}
	public Date getDtCreation() {
		return dtCreation;
	}
	public void setDtCreation(Date dtCreation) {
		this.dtCreation = dtCreation;
	}
	public Date getLastModif() {
		return lastModif;
	}
	public void setLastModif(Date lastModif) {
		this.lastModif = lastModif;
	}
	public String getExtract() {
		return dpattern;
	}
	public void setExtract(String extract) {
		this.dpattern = extract;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public Date getKey() {
		return this.filekey;
	}

	
	@Override
	public String toString() {
		return "FileItem [fileItem=" + fileItem + ", path=" + path
				+ ", dtCreation=" + dtCreation + ", lastModif=" + lastModif
				+ ", option=" + option + ", dpattern=" + dpattern
				+ ", dreplace=" + dreplace + ", dformat=" + dformat
				+ ", filekey=" + filekey + "]";
	}

	public String display() {
		return String.format("FileName : " + fileItem );
				
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileItem == null) ? 0 : fileItem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileItem other = (FileItem) obj;
		if (fileItem == null) {
			if (other.fileItem != null)
				return false;
		} else if (!fileItem.getAbsolutePath().equals(other.fileItem.getAbsolutePath()))
			return false;
		return true;
	}

	@Override
	public int compareTo(FileItem o) {
		return this.getKey().compareTo(o.getKey());
	}
	
	public long calculateDistance(Date ref, HashMap<Long,Item> selected) {
		long result = 0;
		long prox = Math.abs(Functions.getDateDiff(ref, this.getKey(), TimeUnit.MILLISECONDS));
		long minspace = 0;
		for (java.util.Map.Entry<Long, Item> en : selected.entrySet()) {
			long space = Math.abs(Functions.getDateDiff(((FileItem)en.getValue()).getKey(), this.getKey(), TimeUnit.MILLISECONDS));
			if (minspace > space) {
				minspace = space;
			}
		}
		//return Integer.MAX_VALUE- prox - (minspace/2);
		return Integer.MAX_VALUE- Math.abs(prox);
	}
	
	
}
