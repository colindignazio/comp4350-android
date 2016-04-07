package comp4350.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUnitTests
{
	public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("All tests");
        testUnit();
        return suite;
    }
    
    private static void testUnit() {
    	suite.addTestSuite(SortTest.class);
    }
}