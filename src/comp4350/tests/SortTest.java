package comp4350.tests;

import java.util.HashMap;

import comp4350.boozr.business.APIUtils;
import junit.framework.TestCase;

public class SortTest extends TestCase {
	HashMap<String, String> params;

	public SortTest(String arg0) {
		super(arg0);
	}
	
	public void setUp() throws Exception {
		params = new HashMap<String, String>();
	}
	
	public void test_paramsToString() throws Exception {
		params.clear();
		params.put("id", "42");
		params.put("username", "testuser");
		params.put("date", "07/12/16");
		
		assertEquals("id=42&username=testuser&date=07%2F12%2F16", APIUtils.paramsToString(params));
	}
}

