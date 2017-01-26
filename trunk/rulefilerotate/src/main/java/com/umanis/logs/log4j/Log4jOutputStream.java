package com.umanis.logs.log4j;

import java.io.OutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class logs all bytes written to it as output stream with a specified logging level.
 *
 * @version 1.0
 */
public class Log4jOutputStream extends OutputStream {
    /** The logger where to log the written bytes. */
    private Logger logger;

    /** The level. */
    private Level level;

    /** The internal memory for the written bytes. */
    private String mem;

    public Log4jOutputStream(Logger logger2, Level level2) {
    	setLogger (logger2);
        setLevel (level2);
        mem = "";
	}

    /**
     * Sets the logger where to log the bytes.
     *
     * @param logger the logger
     */
    public void setLogger (Logger logger) {
        this.logger = logger;
    }

    /**
     * Returns the logger.
     *
     * @return DOCUMENT ME!
     */
    public Logger getLogger () {
        return logger;
    }

    /**
     * Sets the logging level.
     *
     * @param level DOCUMENT ME!
     */
    public void setLevel (Level level) {
        this.level = level;
    }

    /**
     * Returns the logging level.
     *
     * @return DOCUMENT ME!
     */
    public Level getLevel () {
        return level;
    }

    /**
     * Writes a byte to the output stream. This method flushes automatically at the end of a line.
     *
     * @param b DOCUMENT ME!
     */
    public void write (int b) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (b & 0xff);
        mem = mem + new String(bytes);

//        if (mem.endsWith ("\n")) {
//            mem = mem.substring (0, mem.length () - 1);
//            flush ();
//        }
    }

    /**
     * Flushes the output stream.
     */
    public void flush () {
        logger.log(level, mem);
        mem = "";
    }
}

