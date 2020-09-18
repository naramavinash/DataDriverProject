package com.project.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.TestBase;
import com.project.utilities.TestUtil;

public class AddCustomerTest extends TestBase {
	
	
	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void addCustomerTest(Hashtable<String,String> data) throws InterruptedException {
		
       if(!data.get("runmode").equals("Y")){
			
			throw new SkipException("Skipping the test case as the Run mode for data set is NO");
		}
		
		//driver.findElement(By.cssSelector(OR.getProperty("addCustBtn"))).click();
		click("addCustBtn_CSS");
		
		type("firstName_CSS",data.get("firstname"));
		//driver.findElement(By.cssSelector(OR.getProperty("firstName"))).sendKeys(firstName);
		
		Thread.sleep(3000);
		
		type("lastName_CSS",data.get("lastname"));
		//driver.findElement(By.cssSelector(OR.getProperty("lastName"))).sendKeys(lastName);
		
		Thread.sleep(3000);
		
		type("postCode_CSS",data.get("postcode"));
		//driver.findElement(By.cssSelector(OR.getProperty("postCode"))).sendKeys(postCode);
		
		Thread.sleep(3000);
		//driver.findElement(By.cssSelector(OR.getProperty("addBtn"))).click();
		click("addBtn_CSS");
		Thread.sleep(3000);
		
		//Assert.fail("Test case failed");
		
		try{
		
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		
		//alert = driver.switchTo().alert();
		Assert.assertTrue(alert.getText().contains(data.get("alerttext")));
		Thread.sleep(3000);
		alert.accept();
		
		}
		catch (NoAlertPresentException noAlert) {
		noAlert.getMessage();
		}
		
		log.debug("Customer added succesfully");
		Reporter.log("Customer added succesfully");
		
	}
	
	@DataProvider
	public Object[][] getData(){
		
		String sheetName = "AddCustomerTest";
		int rows = excel.getRowCount(sheetName); 
		int cols = excel.getColumnCount(sheetName);
		
		Object[][] data = new Object[rows-1][cols];
		
		for (int rowNum = 2; rowNum <= rows ; rowNum++) {
			for (int colNum = 0; colNum<cols; colNum++) {
				
				data[rowNum-2][colNum] = excel.getCellData(sheetName, colNum, rowNum);
				
			}	
		}
		return data;
		
	}
	
}
