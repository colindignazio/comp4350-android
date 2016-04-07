package comp4350.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllEndToEndTests
{
	public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("All tests");
        testEndToEnd();
        return suite;
    }

    private static void testEndToEnd() {
        suite.addTestSuite(SearchEndToEndTest.class);
        suite.addTestSuite(UserEndToEndTest.class);
        suite.addTestSuite(BeerEndToEndTest.class);
    }
}
