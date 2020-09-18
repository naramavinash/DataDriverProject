package com.project.testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.project.base.TestBase;

public class BankManagerLoginTest extends TestBase {

	@Test
	public void loginAsBankManager() throws InterruptedException, IOException {

		verifyEquals("abc", "abc");
		log.debug("Inside loginAsBankManager Test!!!");

		click("bmlBtn_CSS");
		// driver.findElement(By.cssSelector(OR.getProperty("bmlBtn"))).click();
		log.debug("Clicked bank manager button");
		Thread.sleep(3000);

		Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustBtn_CSS"))));

		//Assert.fail("Test case failed");

		log.debug("Login succesfully");
		Reporter.log("Login succesfully");

		// Assert.fail("Test case failed");

	}

}
