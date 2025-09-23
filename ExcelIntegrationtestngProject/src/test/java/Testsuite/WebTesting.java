package Testsuite;

import dbhelper.CreateCustomerDB;
import factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.Customer.CustomerPage;
import pages.Login.LoginPage;
import utilities.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class WebTesting{
    WebDriver driver;
    public ConfigLoader configLoader=new ConfigLoader();
    public BasePage basePage;
    public LoginPage loginPage;
    public CustomerPage customerPage;
    public CredsLoader credsLoader=new CredsLoader();

    @BeforeMethod
    public void BrowserExecution() throws MalformedURLException {
    DriverFactory driverFactory=new DriverFactory();
    driver=driverFactory.initializeDriver();
    basePage=new BasePage(driver);
    basePage.goTo(configLoader.getProperty("url"));
        System.out.println("browser is executing");
        loginPage=new LoginPage(driver);
        loginPage.loginToApplication(credsLoader.getProperty("username1"),credsLoader.getProperty("password1"),credsLoader.getProperty("code1"));
        loginPage.clickSubmit();
        basePage.waitFor();
}



@Test(dataProvider = "excelDataProvider",dataProviderClass = utilities.dataexcelmain.class)
public void createCustomer(Map<String, String> data) throws IOException {
    customerPage=new CustomerPage(driver);
    customerPage.Hamburger_icon();
    customerPage.clickCustomerButton();
    customerPage.clickCustomer();
    basePage.goTo(configLoader.getProperty("Customer_url"));
    customerPage.customerBtn();
    customerPage.enterMandatoryBasicDetails(data.get("Name"),data.get("Koovers Category")
            ,data.get("Contact Number"),data.get("New koovers Category"),data.get("City"));

    customerPage.enterMandatoryAddressDetails(data.get("Address"),data.get("state"),data.get("pin code")
            ,data.get("phone number"),data.get("vehicleSegment")
            ,data.get("status"));
    customerPage.clickCheckbox();
    customerPage.clickContactButton();
    customerPage.clickContactIcon();
    customerPage.enterContactMandatoryDetails(data.get("first_name"),data.get("mobile number"));
    customerPage.addButton();
    customerPage.clickSave();
    customerPage.verifyAlertMessage(configLoader.getProperty("customer.alertmessage"));
    basePage.waitFor();

}

@AfterClass
public void createDb() throws SQLException, IOException {
    new CreateCustomerDB().createdbcon();
    basePage.waitFor();
    new DBfetchdataexcel().exportFilteredDataByNames(configLoader.getProperty("InputExcelpath"),
            configLoader.getProperty("OutputexcelpathDB"));
    basePage.waitFor();
    new DBfetchdataexcel().ExceltoLocalhost();
}

@AfterMethod
    public void webclose(){
        driver.close();
}


}
