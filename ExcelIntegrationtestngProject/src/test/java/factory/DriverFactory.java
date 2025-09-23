package factory;

import constants.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    static {
        // JVM shutdown hook - kills browser instances if still alive
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            WebDriver drv = driver.get();
            if (drv != null) {
                try {
                    drv.quit();
                } catch (Exception e) {
                    System.err.println("⚠️ Failed to quit WebDriver during shutdown: " + e.getMessage());
                } finally {
                    driver.remove();
                }
            }
        }));
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public void setDriver(WebDriver driver2) {
        driver.set(driver2);
    }

    public WebDriver initializeDriver() throws MalformedURLException {
        String browser = System.getProperty("browser", "chrome");

        switch (Browser.valueOf(browser.toUpperCase())) {
            case CHROME -> {
                WebDriverManager.chromedriver().clearResolutionCache().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-save-password-bubble");
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--disable-browser-side-navigation");
                chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-software-rasterizer");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-renderer-backgrounding");
                chromeOptions.addArguments("--disable-background-timer-throttling");
                chromeOptions.addArguments("--disable-backgrounding-occluded-windows");

                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.default_content_setting_values.notifications", 2);
                chromeOptions.addArguments("--disable-features=PasswordManagerOnboarding,PasswordLeakDetection");
                chromeOptions.setExperimentalOption("prefs", prefs);

                chromeOptions.setExperimentalOption("excludeSwitches",
                        Arrays.asList("enable-automation", "disable-popup-blocking"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);

                if (System.getProperty("browserMode", "normal").equalsIgnoreCase("headless")) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("window-size=1920,1080");
                }

                setDriver(new ChromeDriver(chromeOptions));
                getDriver().manage().deleteAllCookies();

            }

            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (System.getProperty("browserMode", "normal").equalsIgnoreCase("headless")) {
                    firefoxOptions.addArguments("--headless");
                }
                setDriver(new FirefoxDriver(firefoxOptions));
                getDriver().manage().deleteAllCookies();
            }

            default -> throw new RuntimeException("Invalid Browser: " + browser);
        }

        getDriver().manage().window().maximize();
        return getDriver();
    }

    public void closeWebDriver() {
        WebDriver drv = getDriver();
        if (drv != null) {
            try {
                drv.quit();
            } catch (Exception e) {
                System.err.println("⚠️ Error quitting WebDriver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}
