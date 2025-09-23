package pages.Customer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

import java.time.Duration;
import java.util.Map;

public class CustomerPage extends BasePage {

    private final By icon = By.xpath("//img[@id='headermenuicon']");
    private final By customerDropdown = By.xpath("//h4//b[text()='Customer']");
    private final By customer = By.xpath("//th[text()='Customer ']");
    private final By customerButton = By.id("addCustomerButton");
    private final By name = By.id("txtName");
    private final By KooversCategory = By.id("kCategory");
    private final By customerContactNumber = By.id("txtContactNumber");
    private final By newKooversCategory = By.id("custType");
    private final By city = By.id("cityDLmain");
    private final By Status = By.id("status");
    private final By address = By.id("txtAddrLine1");
    private final By State = By.id("stateList");
    private final By pinCode = By.id("txtPinCode");
    private final By mobileNumber = By.id("txtMobileNumber");
    private final By shippingAddressCheckbox = By.id("addId");
    private final By saveButton = By.id("btnSave");
    private final By vehicleSegment = By.id("evSegment");
    private final By contact = By.id("nav-contactsTab-tab");
    private final By contactIcon = By.id("btnAddContact");
    private final By firstName = By.id("txtFirstNamepop");
    private final By mobNumber = By.id("txtMobliepop");
    private final By addButton = By.id("btnAdd");

    public CustomerPage(WebDriver driver) {
        super(driver);
    }


    public void Hamburger_icon() {
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(5000));
        try {
            clickOnElement(icon);
        } catch (Exception e1) {

            for (int i = 0; i <= 3; i++) {
                try {
                    wait1.until(ExpectedConditions.visibilityOfElementLocated(customerDropdown));
                    if (isDisplayed(customerDropdown)) {
                        break;
                    }
                } catch (Exception e) {
                    clickOnElement(icon);
                }
            }
        }

    }

    public void clickCustomer() {
        try {
            clickOnElement(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void customerBtn() {
        waitFor();
        clickOnElement(customerButton);
    }

    public void clickCustomerButton() {
        try {
            clickOnElement(customerDropdown);
        } catch (Exception e) {

            for (int i = 0; i <= 3; i++) {
                try {
                    if (isDisplayed(customer)) {
                        break;
                    } else {
                        clickOnElement(customer);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
    public void enterMandatoryBasicDetails(String Name,String kCategory,String cnumber, String NkCategory, String City) {

        enterTextOnElement(name, Name);
        selectFromDropDown(KooversCategory, kCategory);
        enterTextOnElement(customerContactNumber, cnumber);
        selectFromDropDown(newKooversCategory, NkCategory);
        selectFromDropDown(city, City);
        System.out.println("the customer name is " + Name);
    }




    public void enterMandatoryAddressDetails(String Address, String state, String pinCode,String mnumber,String vSegment,
                                             String status) {
        enterTextOnElement(address, Address);
        selectFromDropDown(State, state);
        enterTextOnElement(this.pinCode, pinCode);
        enterTextOnElement(mobileNumber, mnumber);
        selectFromDropDown(vehicleSegment, vSegment);
        selectFromDropDown(Status, status);

    }
    public void clickCheckbox() {
        clickOnElement(shippingAddressCheckbox);
    }

    public void clickContactButton() {
        clickOnElement(contact);
    }

    public void clickContactIcon() {
        clickOnElement(contactIcon);
    }
    public void enterContactMandatoryDetails(String fname,String mnum) {
        enterTextOnElement(firstName, fname);
        enterTextOnElement(mobNumber, mnum);

    }
    public void addButton() {
        clickOnElement(addButton);
    }
    public void clickSave() {
        clickOnElement(saveButton);
    }













}
