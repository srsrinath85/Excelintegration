package pages;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.CredsLoader;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final String mainWindowHandle;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.mainWindowHandle = driver.getWindowHandle();
    }

    public boolean isLoginSuccessful() {
        // Check if login was successful
        // For example, check if the user is redirected to the homepage or if an error
        // message is displayed
        try {
            return driver.findElement(By.id("someElementThatIndicatesLoginSuccess")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public double calculatePercentage(double base, double percentage) {
        double value = (base * percentage) / 100;
        System.out.println("The percentage is " + roundToTwoDecimals(value));
        return roundToTwoDecimals(value);
    }

    public boolean verifyTitle(By elementLocator, String expectedTitle) {
        try {
            String actualTitle = driver.findElement(elementLocator).getText().trim();
            return actualTitle.equals(expectedTitle.trim());
        } catch (Exception e) {
            System.out.println("Exception in verifyTitle: " + e.getMessage());
            return false;
        }
    }

    public double roundToTwoDecimals(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // Round to 2 decimal places
        return bd.doubleValue();
    }

    public String getTaxAmount(String input) {
        String amount = "";
        Pattern pattern = Pattern.compile("Rs\\.(\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            amount = matcher.group(1); // Get the matched group
            System.out.println("Extracted amount: " + amount);
        } else {
            System.out.println("No amount found.");
        }
        return amount;
    }

    public void clickAgain(By element) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        for (int i = 0; i < 4; i++) {
            try {
                Thread.sleep(1000);
                driver.findElement(element).click();
                Thread.sleep(1000);
                if (!driver.findElement(element).isDisplayed()) {
                    break;
                } else {
                    Thread.sleep(1000);
                    driver.findElement(element).click();

                }
            } catch (Exception e) {

            }
        }
    }

    public String getTaxPercentage(String input) {
        String percentage = "";
        Pattern pattern = Pattern.compile("(\\d+)%");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            percentage = matcher.group(1); // Get the matched group
            System.out.println("Extracted percentage: " + percentage);
        } else {
            System.out.println("No percentage found.");
        }
        return percentage;
    }

    public void openDuplicateTab(String url) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open(arguments[0], '_blank');", url);
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }

    public void enterTextForDisabled(By element) {

        // String locator = "document.querySelector(\"#rowid_22145522 > td:nth-child(14)
        // > input\").removeAttribute('disabled');;";
        WebElement data = driver.findElement(element);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].removeAttribute('disabled');", data);

    }

    public void goTo(String url) {
        driver.get(url);
        waitFor();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(10000));
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void clickOnElement(By element) {

        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(2000));
        wait1.ignoring(TimeoutException.class).until(ExpectedConditions.visibilityOfElementLocated(element));
        driver.findElement(element).click();
    }

    public void enterTextOnElement(By element, String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).clear();
        driver.findElement(element).sendKeys(value);
    }

    public void enterTextOnElementWithoutClear(By element, String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).sendKeys(value);
    }

    public void switchToNewTabAndClose() {

        Set<String> windowHandles = driver.getWindowHandles();

        for (String handle : windowHandles) {
            if (!handle.equals(mainWindowHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        driver.close();

        driver.switchTo().window(mainWindowHandle);
    }

    public void clickElementUsingJS(By element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        waitUntilElementIsDisplayed(element);
        WebElement e = driver.findElement(element);
        jsExecutor.executeScript("arguments[0].click();", e);

    }

    public WebDriver openNewIncognitoWindow(WebDriver driver) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open();");

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        WebDriver newTab = driver.switchTo().window(tabs.get(tabs.size() - 1));

        return newTab;
    }

    public void clickElementById(String elementId) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById(arguments[0]).click();", elementId);
        } catch (Exception e) {
            System.out.println("Element with ID " + elementId + " not found or not clickable.");
            e.printStackTrace();
        }
    }

    public void clickedit(By element) {

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            System.out.println("Element with ID " + element + " not found or not clickable.");
            e.printStackTrace();
        }
    }

    public void clickElementByIdAndIndex(String elementId, int index) {
        try {

            List<WebElement> elements = driver.findElements(By.id(elementId));

            if (index < 0 || index >= elements.size()) {
                System.out.println("Index out of bounds. Available elements: " + elements.size());
                return;
            }

            elements.get(index).click();
        } catch (Exception e) {
            System.out.println("Error clicking element with ID " + elementId + " at index " + index);
            e.printStackTrace();
        }
    }

    public String getPlaceholderText(WebDriver driver, String elementId) {
        WebElement inputField = driver.findElement(By.id(elementId));
        return inputField.getAttribute("placeholder");
    }

    public String getEnteredTextFromInputField(By element) {
        return driver.findElement(element).getAttribute("value");
    }

    public void moveToElementAndClick(By element) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        WebElement webElement = driver.findElement(element);
        Actions act = new Actions(driver);
        act.moveToElement(webElement).click().build().perform();
    }

    public void moveToElement(By element) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        WebElement webElement = driver.findElement(element);
        Actions act = new Actions(driver);
        act.moveToElement(webElement).build().perform();
    }

    public String getTextOnElement(By element) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        return driver.findElement(element).getText();
    }

    public void scrollToElement(By element) {
        WebElement webelement = driver.findElement(element);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webelement);
    }

    public boolean verifyUrlPath(String expectedUrl, String currentUrl) {

        try {
            System.out.println(driver.getCurrentUrl());
            if (expectedUrl.contains(currentUrl)) {

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void scrollToElement(WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void waitUntilElementIsDisplayed(By element) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        } catch (Exception e) {

        }
    }

    public void selectFromDropDown(By dropDown, String value) {

        waitUntilElementIsDisplayed(dropDown);
        Select objSelect = new Select(driver.findElement(dropDown));
        objSelect.selectByVisibleText(value);
    }

    public void selectFromDropDownByIndex(By dropDown, String value) {

        waitUntilElementIsDisplayed(dropDown);
        Select objSelect = new Select(driver.findElement(dropDown));
        objSelect.selectByVisibleText(value);
    }

    public boolean isDisplayed(By element) {
        try {
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(10));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            waitUntilElementIsDisplayed(element);
            if (driver.findElement(element).isDisplayed()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOverRideDisplayed(By element) {
        try {
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(1));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            waitUntilElementIsDisplayed(element);
            if (driver.findElement(element).isDisplayed()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String getTodayDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return currentDate.format(formatter);
    }

    public void waitForAllElementIsDisplayed(By element) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
    }

    public void clickAction(By element, By elementTobeDisplayed) {
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(10));
        for (int i = 0; i < 4; i++) {
            try {
                Thread.sleep(1000);
                wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
                driver.findElement(element).click();
                Thread.sleep(1000);
                if (driver.findElement(elementTobeDisplayed).isDisplayed()) {
                    break;

                }

            } catch (Exception e) {
            }
        }
    }

    public boolean verifyAlertMessage(String textMessage) {
        boolean result = false;
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait1.until(ExpectedConditions.alertIsPresent());
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        String textmessage = driver.switchTo().alert().getText();
        System.out.println("the text present in alert is:" + textmessage);
        if (textmessage.trim().equalsIgnoreCase(textMessage.trim())) {
            result = true;
        }
        driver.switchTo().alert().accept();
        return result;
    }

    public void datePicker(By date, String datetext) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1]", driver.findElement(date), datetext);
    }

    public String dynamicLocators(String path, String value) {
        return String.format(path, value);
    }

    public void scroolInToView(By element) {
        JavascriptExecutor jc = (JavascriptExecutor) driver;
        jc.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String todaysDate() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return today.format(formatter);
    }

    public String getDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = currentDate.plusDays(2);
        return date.format(formatter);
    }

    public boolean comapreDateAndTime(String userdateandtime, String currentdateandtime) {
        boolean result = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime userDateTime = LocalDateTime.parse(userdateandtime, formatter);
        LocalDateTime currentDateTime = LocalDateTime.parse(currentdateandtime, formatter);
        System.out.println("userdateandtime" + userdateandtime);
        System.out.println("currentdateandtime" + currentdateandtime);
        if (Math.abs(ChronoUnit.MINUTES.between(currentDateTime, userDateTime)) <= 1) {
            result = true;
        }
        return result;
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    public void doubleClick(By element) throws InterruptedException {
        Actions ag = new Actions(driver);
        WebElement link = driver.findElement(element);
        ag.moveToElement(link).build().perform();
        Thread.sleep(2000);
        ag.doubleClick(link).build().perform();
    }

    public static String decrypt(String encryptedMobile) throws Exception {
        String SECRET_KEY = "abcdefghijklmnopqrstuvwx";
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMobile));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String getMobileNumber() {
        Random rand = new Random();
        StringBuilder mobileNumber = new StringBuilder("");

        int startDigit = rand.nextInt(4) + 6;
        mobileNumber.append(startDigit);

        for (int i = 0; i < 9; i++) {
            mobileNumber.append(rand.nextInt(10));
        }

        return mobileNumber.toString();
    }

    public static String getRandomName() {
        Faker faker = new Faker();
        String randomName = faker.name().firstName();
        System.out.println("Generated Random Name: " + randomName);
        return randomName;
    }

    // public static String generateRandomGSTIN() {
//        Random rand = new Random();
//
//        String stateCode = String.format("%02d", rand.nextInt(37));
//
//        StringBuilder panNumber = new StringBuilder();
//        panNumber.append((char) (rand.nextInt(26) + 'A'));
//        for (int i = 0; i < 4; i++) {
//            panNumber.append((char) (rand.nextInt(26) + 'A'));
//        }
//        panNumber.append(rand.nextInt(10));
//
//        String entityCode = String.format("%02d", rand.nextInt(100));
//
//        String gstinWithoutChecksum = stateCode + panNumber.toString() + entityCode;
//        int[] weights = {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2};
//        int total = 0;
//        for (int i = 0; i < gstinWithoutChecksum.length(); i++) {
//            int digit = Character.getNumericValue(gstinWithoutChecksum.charAt(i));
//            total += (digit / 10 + digit % 10) * weights[i];
//        }
//        int checksum = (10 - (total % 10)) % 10;
//
//        return stateCode + panNumber.toString() + entityCode + checksum;
//    }
    public static String generateRandomGSTIN() {
        Random rand = new Random();

        String stateCode = String.format("%02d", rand.nextInt(37) + 1); // State codes are 01-37

        StringBuilder panNumber = new StringBuilder();
        panNumber.append((char) (rand.nextInt(26) + 'A')); // First letter (A-Z)
        for (int i = 0; i < 4; i++) {
            panNumber.append((char) (rand.nextInt(26) + 'A')); // Next 4 letters (A-Z)
        }
        panNumber.append(rand.nextInt(10)); // 1st digit (0-9)
        panNumber.append(rand.nextInt(10)); // 2nd digit (0-9)
        panNumber.append(rand.nextInt(10)); // 3rd digit (0-9)
        panNumber.append(rand.nextInt(10)); // 4th digit (0-9)
        panNumber.append((char) (rand.nextInt(26) + 'A')); // Last letter (A-Z)

        String entityCode = String.format("%02d", rand.nextInt(100)); // Entity codes are 00-99

        String gstinWithoutChecksum = stateCode + panNumber.toString() + entityCode;

        int[] weights = { 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1 }; // Adjust weights to match length of GSTIN
        int total = 0;

        for (int i = 0; i < gstinWithoutChecksum.length(); i++) {
            int digit = Character.getNumericValue(gstinWithoutChecksum.charAt(i));
            if (digit >= 0) {
                total += (digit / 10 + digit % 10) * weights[i]; // Split digits for Luhn's algorithm
            }
        }

        int checksum = (10 - (total % 10)) % 10;

        return gstinWithoutChecksum + checksum;
    }

    public void enterTextOnElementUsingJS(By element, String value) {
        WebElement e = driver.findElement(element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + value + "';", e);

    }

    public void waitForElementToBeClickabe(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        wait.until(ExpectedConditions.visibilityOfElementLocated((element)));

    }

    public void doubleClickUsingActions(By element) {
        Actions a = new Actions(driver);
        WebElement e = driver.findElement(element);
        a.doubleClick(e).build().perform();
    }

    public void closeWindowHandle() {
        String mainWindow = driver.getWindowHandle();

        Set<String> allWindows = driver.getWindowHandles();

        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(mainWindow)) {

                driver.switchTo().window(windowHandle);
                driver.close();
                driver.switchTo().window(mainWindow);
            }
        }
    }

    public void switchToWindowHandle() {
        String mainWindow = driver.getWindowHandle();

        Set<String> allWindows = driver.getWindowHandles();

        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(mainWindow)) {

                driver.switchTo().window(windowHandle);

            }
        }
    }

    public boolean verifyIfTextExists(By element, String expected) {
        waitFor();
        List<WebElement> elements = driver.findElements(element);
        for (WebElement e : elements) {
            String actual = e.getText();
            if (actual.equals(expected)) {
                return true;
            }

        }

        return false;
    }

    public static String getRandomEmail() {
        Faker faker = new Faker();
        String randomEmail = faker.internet().emailAddress();
        return randomEmail;
    }

    public void redirectToPage(String pagename) {
        switch (pagename) {
            case "HOME_DASHBOARD":
                driver.get(new CredsLoader().getProperty("Homedashboard"));
                waitFor();
                break;

            case "CREATEPARTS":
                driver.get(new CredsLoader().getProperty("CREATE_ORDER"));
                break;
            default:
                System.out.println("Invalid page name");
        }

    }

    public void enterTextOnElementUsingActions(By element, String value) throws InterruptedException, AWTException {

        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).clear();
        WebElement e = driver.findElement(element);
        Actions a = new Actions(driver);
        a.sendKeys(e, value).build().perform();
        Thread.sleep(3000);
        a.sendKeys(Keys.DOWN).build().perform();
        a.sendKeys(Keys.ENTER).build().perform();

    }

    public void setDate(By e, String date) {
        WebElement element = driver.findElement(e);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].value='" + date + "';", element);
    }

    public void clearTextFromTextboxUsingJs(By path) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement textbox = driver.findElement(path);
        js.executeScript("arguments[0].value = '';", textbox);
    }

    public void waitFor() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
    }

    public void selectFromDropDownByValue(By dropDown, String index) {

        waitUntilElementIsDisplayed(dropDown);
        Select objSelect = new Select(driver.findElement(dropDown));
        objSelect.selectByValue(index);
        System.out.println("Dropdown selected");
    }

    public void selectFromDropDownByIndex(By dropDown, int index) {

        waitUntilElementIsDisplayed(dropDown);
        Select objSelect = new Select(driver.findElement(dropDown));
        objSelect.selectByIndex(index);
        System.out.println("Dropdown selected");
    }

    public void selectByVisibleText(By dropDown, String text) {

        waitUntilElementIsDisplayed(dropDown);
        Select objSelect = new Select(driver.findElement(dropDown));
        objSelect.selectByVisibleText(text);
        System.out.println("Dropdown selected");
    }

    public WebElement waitForElementVisible(By xpath) {
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait1.until(ExpectedConditions.visibilityOfElementLocated(xpath));
    }

    public void selectByIndex(By dropDown, int index) {

        waitUntilElementIsDisplayed(dropDown);
        Select objSelect = new Select(driver.findElement(dropDown));
        objSelect.selectByIndex(index);
        System.out.println("Dropdown selected");
    }

    public boolean isElementDisplayed(By element) {
        waitUntilElementIsDisplayed(element);
        WebElement webElement = driver.findElement(element);
        return webElement.isDisplayed();

    }

    public void handleLoading() {
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait1.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                .equals("complete"));
    }

    public String getSelectedOption(By xpathdata) {
        Select dropdown = new Select(driver.findElement(xpathdata));
        WebElement element = dropdown.getFirstSelectedOption();
        return element.getText();
    }

    public static String generateRandomEmailId() {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789";
        int localPartMaxLength = 15;
        String domain = "@asia.com";

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        int localPartLength = Math.min(localPartMaxLength - domain.length(), 10); // Ensure local part is within limits
        for (int i = 0; i < localPartLength; i++) {
            int index = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(index));
        }

        sb.append(domain);

        return sb.toString();
    }

    public boolean verifyUrl(String pageUrl) {
        String url = driver.getCurrentUrl();
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(10));
        wait1.until(ExpectedConditions.urlToBe(pageUrl));
        boolean result = false;
        if (url.equals(pageUrl)) {
            result = true;
        }
        return result;
    }

    public String getValueFromTextBox(WebElement textBox) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value = (String) js.executeScript("return arguments[0].value;", textBox);
        return value;
    }

    public String getValueFromTextBoxUsingJs(By textBox) {
        WebElement inputValue = driver.findElement(textBox);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value = (String) js.executeScript("return arguments[0].value;", inputValue);
        return value;
    }

}
