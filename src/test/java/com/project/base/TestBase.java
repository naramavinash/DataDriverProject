package com.project.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.project.listeners.CustomListeners;
import com.project.utilities.ExcelReader;
import com.project.utilities.ExtentManager;
import com.project.utilities.TestUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	/*
	 * WebDriver, Properties, Logs, Log4j jar, .log, log4j.properties, Logger,
	 * ExtentReports, DB, Excel, Mail, ReportNG, Jenkins
	 * 
	 */

	public static WebDriver driver;
	public static String browser;
	public static FileInputStream fis;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;

	@BeforeSuite
	public void setUp() {

		if (driver == null) {

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties");
				log.debug("File Input Stream for config file initilized");

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

				config.load(fis);
				log.debug("Config file loaded !!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
				log.debug("File Input Stream for config file initilized");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				OR.load(fis);
				log.debug("OR file loaded !!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		browser = config.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			log.debug("Chrome driver initilized!!!");
		} else if (browser.equalsIgnoreCase("FireFox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			log.debug("Firefox driver initilized!!!");

		} else if (browser.equalsIgnoreCase("IE")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			log.debug("IE driver initilized!!!");

		} else if (browser.equalsIgnoreCase("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			log.debug("Edge driver initilized!!!");

		}

		driver.get(config.getProperty("testsiteurl"));
		log.debug("URL launched..Navigated to!!!" + config.getProperty("testsiteurl"));
		driver.manage().window().maximize();
		log.debug("Window maximised!!!");
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("ImplicitWait")),
				TimeUnit.SECONDS);
		log.debug("Implicit wait set!!!");
		wait = new WebDriverWait(driver, 5);

	}

	public void click(String locator) {

		if (locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locator))).click();
		}
		CustomListeners.testReport.get().log(Status.INFO, "Clicking on : " + locator);
	}

	public void type(String locator, String value) {

		if (locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
		}

		CustomListeners.testReport.get().log(Status.INFO, "Typing in : " + locator + " entered value as " + value);

	}
	
	
	
	static WebElement dropdown;

	public void select(String locator, String value) {

		if (locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
		} else if (locator.endsWith("_ID")) {
			dropdown = driver.findElement(By.id(OR.getProperty(locator)));
		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);

		CustomListeners.testReport.get().log(Status.INFO, "Selecting from dropdown : " + locator + " value as " + value);

	}
	

	public static void verifyEquals(String expected, String actual) throws IOException {

		try {

			Assert.assertEquals(actual, expected);

		} catch (Throwable t) {

			TestUtil.captureScreenshots();
			// ReportNG
			Reporter.log("<br>" + "Verification failure : " + t.getMessage() + "<br>");
			Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName
					+ " height=200 width=200></img></a>");
			Reporter.log("<br>");
			Reporter.log("<br>");
			// Extent Reports
			CustomListeners.testReport.get().log(Status.FAIL,
					" Verification failed with exception : " + t.getMessage());
			
			//CustomListeners.testReport.get().log(Status.FAIL,CustomListeners.testReport.get().addScreenCaptureFromPath(TestUtil.screenshotName));
			CustomListeners.testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(System.getProperty("user.dir")+TestUtil.screenshotPath+"\\"+TestUtil.screenshotName)
							.build());
		}

	}

	public boolean isElementPresent(By by) {

		try {
			driver.findElement(by);
			log.debug("Element is Present!!!" + by);
			return true;
		} catch (NoSuchElementException e) {
			log.debug("Element is Not Present!!!" + by);
			return false;
		}

	}

	@AfterSuite
	public void tearDown() {

		try {
			if (driver != null) {
				driver.quit();
				log.debug("Test Execution Completed!!!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fis.close();
			log.debug("File Input Stream Closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
