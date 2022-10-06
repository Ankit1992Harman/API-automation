package com.mindtickle.qa.apibase;



import com.mindtickle.qa.util.DataProviderUtil;
import com.mindtickle.qa.util.LoggerUtil;
import com.mindtickle.qa.util.LoggerUtil.LogLevel;
import com.mindtickle.qa.util.PropertiesUtil;
import com.mindtickle.qa.util.ReportUtil;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.mindtickle.qa.listener.TestListener;

public class APITestBase implements ITest {

	protected String env = null;
	public static String emailForReporting = null;
	public static String groups = null;
	protected CloseableHttpClient httpClient;
	protected HttpContext httpContext;
	String testName = null;


	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		// apiHelper = new ApiHelper();
		PropertiesUtil.loadConstantFile("constant");
		groups = PropertiesUtil.getConstantProperty("RunningGroup");
	}

	@BeforeTest(alwaysRun = true)
	@Parameters({ "environment"})
	public void setUpEnvironment(@Optional("Beta") String environment)
			throws Exception {
		env = environment;
		String fileName = "env";
		fileName = "user-functional";
		System.out.println("Preparing to Load " + fileName + " file for " + env + "..");
		PropertiesUtil.loadEnvConfigFile(fileName);

		// Connecting DB

		/*
		try {
			DBConnectionUtil.createDBConnectionPetra();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			DBConnectionUtil.createDBConnectionPixie();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * } else { System.out.println("DB Connection for Petra");
		 * DBConnectionUtil.createDBConnectionPetra(); }
		 */
		LoggerUtil.setlog(LogLevel.ONLYLOGS, "DB Connection(s) established successfully");
	}

	@BeforeClass
	@Parameters({ "os" })
	public void beforeEveryClass(@Optional("mac") String os) throws Exception {

	}

	@BeforeMethod
	public void beforeEveryMethod(Object testdata[]) throws SQLException {
		if (testdata != null)
			this.testName = (String) testdata[0];

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		httpClient = HttpClients.custom().setConnectionManager(cm).build();
		CookieStore cookieStore = new BasicCookieStore();
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);


	}

	@AfterMethod
	public void afterEveryMethod() {

		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@AfterClass
	public void afterEveryClass() {
		// clear cache prepared from test data sheet
		DataProviderUtil.clearTestData();
	}

	@AfterTest
	public void afterEachTestCycle() {

		// closing DB connection
//		DBConnectionUtil.closeDBConnection();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {

		File tempFile = null;
		try {
			tempFile = ReportUtil.extractResultTable(env, TestListener.fileName);
			ArrayList<HashMap<String, HashMap<String, Integer>>> testReportCompleteName = TestListener.reportStatus;
			String reportName = "";

			String emailSubject = null;
			// if(testReportCompleteName.size() > 1)
			emailSubject = "hello";
			// else
			// emailSubject = groups.equalsIgnoreCase("smoke") ? "Smoke"
			// +TestListener.subject.substring(10) : TestListener.subject;

			File f = null;
			for (int i = 0; i < testReportCompleteName.size(); i++) {
				for (String reportNameKey : testReportCompleteName.get(i).keySet()) {
					reportName = reportNameKey;
					f = new File(reportName);
					if (!f.exists()) {
						System.out.println("REPORT FILE NOT CREATED" + reportName);
					}
				}
			}
			tempFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tempFile.delete();

		}

	}

	@Override
	public String getTestName() {

		return this.testName;
	}
}
