package comp4350.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("All tests");
        testEndToEnd();
        return suite;
    }

    private static void testEndToEnd()
    {
        suite.addTestSuite(SearchTest.class);
        suite.addTestSuite(UserTest.class);
        suite.addTestSuite(BeerTest.class);
    }
}
