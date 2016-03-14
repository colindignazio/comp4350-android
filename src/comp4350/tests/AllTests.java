package comp4350.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import comp4350.tests.business.SomethingToTestTest;

public class AllTests
{
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("All tests");
        testBusiness();
        return suite;
    }

    private static void testBusiness()
    {
        suite.addTestSuite(SomethingToTestTest.class);
    }
}
