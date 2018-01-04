package org.alb.tools.file.util;

import org.alb.tools.file.util.Functions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FunctionsTest extends TestCase {

	 /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FunctionsTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( FunctionsTest.class );
        //suite.addTest(test);
        return suite;
    }

    /**
     * Rigourous Test :-)
     */
    public void testFunctions()
    {
        assertTrue( true );
        System.out.println("test !");
    }
    
    public void testRoundTimeMillis() {
    	assertEquals(125035.0,Functions.roundMiliTime(125035, 0));
    	assertEquals(12503.5,Functions.roundMiliTime(125035, 1));
    	assertEquals(1250.35,Functions.roundMiliTime(125035, 2));
    	assertEquals(125.035,Functions.roundMiliTime(125035, 3));
    	assertEquals(0.125035,Functions.roundMiliTime(125035, 6));
    }

	public void testGetFilePathWithoutExtension() {
		fail("Not yet implemented");
	}

	public void testGetFilePath() {
		fail("Not yet implemented");
	}

	public void testGetFileNameFromPath() {
		fail("Not yet implemented");
	}

	public void testZipFiles() {
		fail("Not yet implemented");
	}

	public void testGetStackTrace() {
		fail("Not yet implemented");
	}

	public void testListFilesString() {
		fail("Not yet implemented");
	}

	public void testListFilesStringBoolean() {
		fail("Not yet implemented");
	}

	public void testGetDateDiff() {
		fail("Not yet implemented");
	}

	public void testGetDateHashCode() {
		fail("Not yet implemented");
	}

	public void testGethaschCodeStep() {
		fail("Not yet implemented");
	}

	public void testGethaschCodeDateformat() {
		fail("Not yet implemented");
	}

	public void testRemoveAll() {
		fail("Not yet implemented");
	}



}
