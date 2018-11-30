package testproject_selenium;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * COMP10066 - Assignment#6 starter code
 *
 * @author mark.yendt@mohawkcollege.ca
 */
public class TestProject_SeleniumTest {

    enum Browser {
        FIREFOX, CHROME, IE
    };

    Browser browser = Browser.IE;

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    private WebDriver startBrowser(Browser browser) throws Exception {
        WebDriver tempDriver = null;
        // Tested at College on Nov 19,2018
        if (browser == Browser.CHROME) {
            System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
            tempDriver = new ChromeDriver();
        } else if (browser == Browser.FIREFOX) {
            System.setProperty("webdriver.gecko.driver", "resources\\geckodriver.exe");
            // May need to do this if not in path
            // String pathToBinary = "C:\\Program Files\\Mozilla Firefox 61-32-bit\\firefox.exe";
            // System.setProperty("webdriver.firefox.driver",pathToBinary);
            // FirefoxProfile firefoxProfile = new FirefoxProfile();
            tempDriver = new FirefoxDriver();
        } else if (browser == Browser.IE) {
            System.setProperty("webdriver.ie.driver", "resources\\IEDriverServer.exe");
            tempDriver = new InternetExplorerDriver();
        }
        return tempDriver;
    }

    private void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }


    /*
     Example Test case - driver is assummed to be set-up
     Your other test cases need to resemble these 
     Obtain more test cases by recording scripts using Katalon and exporting to
     Java - Junit 4 Web-Driver format
     */
    public void testLoginAdmin() throws Exception {

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://csunix.mohawkcollege.ca/tooltime/comp10066/A3/login.php");
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("adminP6ss");
        driver.findElement(By.name("Submit")).click();
        driver.findElement(By.linkText("User Admin")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("loginname")).click();
        try {
            assertEquals("Not Logged In", driver.findElement(By.id("loginname")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    /**
     * This code will run all of the test cases - DO NOT CHANGE except to add
     * more test cases as shown below:
     */
    @Test
    public void testRunner() {
        // Will run through asll enum cases 
        for (Browser browser : Browser.values()) {
            try {

                driver = startBrowser(browser);

                //----------------------------------------
                // PUT YOUR TEST CASES AFTER this line 
                testLoginAdmin();

                // DO NOT MODIFY below line
                // ----------------------------------------
                closeBrowser();
                
            } catch (Exception ex) {
                System.err.println("Exception caught starting " + browser + " " + ex.getMessage());
            }

        }
    }
}
