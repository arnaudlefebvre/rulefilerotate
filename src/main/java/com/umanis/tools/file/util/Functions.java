package com.umanis.tools.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.umanis.tools.file.rule.Item;

public final class Functions {
	public static final Logger logger = Logger.getLogger(Functions.class);
	public Functions() {
		// TODO Auto-generated constructor stub
	}

	public static double roundMiliTime(long ms, int n) {
		return Math.round(((ms)) * Math.pow(10, 3)) / Math.pow(10, 3 + n);
	}
	
	public static String getFilePathWithoutExtension(String path) {
		int pos = path.lastIndexOf(".");
		return path.substring(0, pos);
	}
	
	public static String getFilePath(String path) {
		int pos = -1;
		//try {pos = path.lastIndexOf(FileSystems.getDefault().getSeparator());} catch (Exception e) {};
		try {pos = path.lastIndexOf(System.getProperty("file.separator"));} catch (Exception e) {};
		if (pos == -1) {
			pos = path.lastIndexOf("\\");
		}
		if (pos == -1) {
			pos = path.lastIndexOf("/");
		}
		return path.substring(0,pos);
	}
	
	public static String getFileNameFromPath(String path) {
		int pos = -1;
		//try {pos = path.lastIndexOf(FileSystems.getDefault().getSeparator());} catch (Exception e) {};
		try {pos = path.lastIndexOf(System.getProperty("file.separator"));} catch (Exception e) {};
		if (pos == -1) {
			pos = path.lastIndexOf("\\");
		}
		if (pos == -1) {
			pos = path.lastIndexOf("/");
		}
		return path.substring(pos+1,path.length());
	}
	
	/**
     * Créé un ZIP des fichiers passés en paramètres
     * 
     * @param files les fichiers à zipper
     * @param directory le dossier de destination du zip
     * @param zipFileName le nom du fichier
     * @return le fichier zip
     */
	public static File zipFiles(List<File> files, String directory, String zipFileName, boolean overwrite) throws IOException {
        FileInputStream inputStream;

        // Construction du nom de l'archive générée
        String zipFilePath = directory + File.separatorChar + zipFileName;

        // Create a buffer for reading the files
        byte [] buffer = new byte [1024];

        // Construction de l'archive
        File zipFile = new File(zipFilePath);
        File zipfolder = new File(directory);
        if (!zipfolder.exists()) {
        	zipfolder.mkdirs();
        }
        if (zipFile.exists() && !overwrite) {
        	logger.error("File "+zipFile+" already exists : SKIPPED");
        	return null;
        }
        ZipOutputStream zipOutputStream = null;
        
        zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

        // compress the files
        for (File file : files) {
            // Au cas où on tente de zipper le fichier zip qu'on est entrain de créer
            if (!file.getName().equals(zipFileName)) {

                inputStream = new FileInputStream(file);

                // Add ZIP entry to output stream.
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                // Complete the entry
                zipOutputStream.closeEntry();
                inputStream.close();
            }
        }
        zipOutputStream.close();
        return zipFile;
    }
	 
    public static String getStackTrace(Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            throwable.printStackTrace(pw);
            return sw.getBuffer().toString();
    }
    
    
    /**
     * List all files contained in directoryPath. 
     * Subdirectories are note listed and included in result
     * @param directoryPath
     * @return List of file contained in directoryPath
     */
    public static ArrayList<File> listFiles(String directoryPath) {
    	return listFiles(directoryPath, false);
    }
    
    /**
     * List all files contained in directoryPath. 
     * @param directoryPath path of directory
     * @param withSubDirectories flag to include files from subdirectories
     * @return List of file contained in directoryPath
     */
    public static ArrayList<File> listFiles(String directoryPath, Boolean withSubDirectories) {
    	ArrayList<File> files = new ArrayList<File>();
    	File directory = new File(directoryPath);
        File[] fList = directory.listFiles();
        if (fList != null) { 
	        for (File file : fList) {
	            if (file.isFile()) {
	                files.add(file);
	            } else if (file.isDirectory()) {
	            	if (withSubDirectories) {
	            		files.addAll(listFiles(file.getAbsolutePath(),withSubDirectories));
	            	} 
	            }
	        }
        }
        return files;
    }
    
    /**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	public static Long getDateHashCode(Date date,int scale) {
		//Number for US date format, ie : 20161011
		switch (scale) {
			case Calendar.DATE : return Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(date));
			case Calendar.MONTH : return Long.parseLong(new SimpleDateFormat("yyyyMM").format(date));
			case Calendar.YEAR : return Long.parseLong(new SimpleDateFormat("yyyy").format(date));
			case Calendar.HOUR : return Long.parseLong(new SimpleDateFormat("yyyyMMddhh").format(date));
			case Calendar.MINUTE : return Long.parseLong(new SimpleDateFormat("yyyyMMddhhmm").format(date));
			case Calendar.SECOND : return Long.parseLong(new SimpleDateFormat("yyyyMMddhhmmss").format(date));
		}
		//You jerk
		return 0L;
	}
	
	public static int gethaschCodeStep(Long h) {
		if (h.toString().length()<=4) {
			return Calendar.YEAR;
		} else if (h.toString().length()<=6) {
			return Calendar.MONTH;
		} else if (h.toString().length()<=8) {
			return Calendar.DATE;
		} else if (h.toString().length()<=10) {
			return Calendar.HOUR;
		} else if (h.toString().length()<=12) {
			return Calendar.MINUTE;
		} else if (h.toString().length()<=14) {
			return Calendar.SECOND;
		}
		else return -1;
	}
	
	public static String gethaschCodeDateformat(Long h) {
		if (h.toString().length()<=4) {
			return "yyyy";
		} else if (h.toString().length()<=6) {
			return "yyyyMM";
		} else if (h.toString().length()<=8) {
			return "yyyyMMdd";
		} else if (h.toString().length()<=10) {
			return "yyyyMMddhh";
		} else if (h.toString().length()<=12) {
			return "yyyyMMddhhmm";
		} else if (h.toString().length()<=14) {
			return "yyyyMMddhhmmss";
		}
		else return "-1";
	}

	public static HashMap<Long,Item> removeAll(HashMap<Long,Item> in, HashMap<Long,Item> toremove) {
		Collection<Item> inC = in.values();
		Collection<Item> toRemC = toremove.values();
		Collection<Item> result = new ArrayList<Item>();
		for (Item i : inC) {
			if (!toRemC.contains(i)) {
				result.add(i);
			}
		}
		HashMap<Long,Item> hashadd = new HashMap<Long,Item>();
		for (Item i : result) hashadd.put((long)i.hashCode(),i);
//		HashMap<Long,Item> hMap = new HashMap<Long,Item>();
//		hMap.putAll(in);
//		for (Iterator<Map.Entry<Long,Item>> it = hMap.entrySet().iterator(); it.hasNext();) {
//			Map.Entry<Long,Item> e = it.next();
//			for (Iterator<Map.Entry<Long,Item>> jt = toremove.entrySet().iterator(); it.hasNext();) {
//				 Map.Entry<Long,Item> f = it.next();
//				 if (e.getValue().equals(f.getValue())) {
//					 it.remove();
//				 }
//			 }
//		}
		return hashadd;
	}
}
