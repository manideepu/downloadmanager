package com.deepu.downloadmanager;

import java.io.IOException;
import java.util.List;

import com.deepu.downloadmanager.MultiThreadedApp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple MyDownloadManagerTest.
 */
public class MultiThreadedAppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MultiThreadedAppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MultiThreadedAppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws IOException
	 */
	public void testMyApp() throws IOException {
		MultiThreadedApp app = new MultiThreadedApp();

		List<String> inputList = app.readInputTextFile();
		assertNotNull(inputList);
		assertTrue(inputList.size() > 0);
		assertTrue(inputList.size() == 18);

		app.fetchFiles(inputList);
		assertTrue(true);
	}
}
