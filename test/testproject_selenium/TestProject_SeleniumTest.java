package testproject_selenium;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author 000734787@mohawkcollege.ca
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

        loginAsAdmin();
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
     * test create - creates a new non administrator user
     */
    public void testCreate() {
        loginAsAdmin();
        driver.findElement(By.linkText("User Admin")).click();
        //enter username and password
        driver.findElement(By.name("username")).sendKeys("TomasRuizTest1");
        driver.findElement(By.name("password")).sendKeys("Test@pass1");
        // select radio button to activate account
        List<WebElement> activateRadioButton = driver.findElements(By.name("activate"));
        for (int i = 0; i < activateRadioButton.size(); i++) {
            String radioValue = activateRadioButton.get(i).getAttribute("value");
            if (radioValue.equalsIgnoreCase("Y")) {
                activateRadioButton.get(i).click();
            }
        }
        //select radio button for admin - non admin
        List<WebElement> isAdminRadioButton = driver.findElements(By.name("admin"));
        for (int i = 0; i < isAdminRadioButton.size(); i++) {
            String radioValue = isAdminRadioButton.get(i).getAttribute("value");
            if (radioValue.equalsIgnoreCase("N")) {
                isAdminRadioButton.get(i).click();
            }
        }
        // set email
        driver.findElement(By.name("email")).sendKeys("tomazruiztest@gmail.com");
        //click on add new member button
        driver.findElement(By.name("Add New Member")).click();
        //logout
        driver.findElement(By.linkText("Logout")).click();

        //login again usgin the provided credentials to confirm the created user
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("TomasRuizTest1");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("Test@pass1");
        driver.findElement(By.name("Submit")).click();

        // confirm the created user
        String actualUsername = driver.findElement(By.id("loginname")).getText();
        driver.findElement(By.linkText("Logout")).click();
        assertEquals("User: TomasRuizTest1", actualUsername);
    }

    /**
     * testDelete -  deletes the created user by testCreate
     */
    public void testDelete() {
        loginAsAdmin();
        driver.findElement(By.linkText("User Admin")).click();
        // find and click on the delete button
        driver.findElement(By.xpath("//td[@id='TomasRuizTest1']//a//img[@src='images/delete.gif']")).click();
        //confirm the deletion
        driver.findElement(By.linkText("here")).click();
        //check for the delete message
        String deleteMessage = driver.findElement(By.xpath("//div[@class='error']")).getText();
        //logout
        driver.findElement(By.linkText("Logout")).click();
        assertEquals("User TomasRuizTest1 was successfully deleted.", deleteMessage.trim());
    }

    /**
     * testCityDirectory - counts the number of results when selected from
     * the drop down
     * @param city the city that has to be selected from the drop down
     * @param expectedCount expected number of results of the given city
     */
    public void testCityDirectory(String city, int expectedCount) {
        loginAsAdmin();
        driver.findElement(By.linkText("Directory")).click();
        // click on the drop down
        Select select = new Select(driver.findElement(By.id("city")));
        //select the given city
        select.selectByValue(city);
        //click on submit
        driver.findElement(By.name("submit")).click();
        //count the results
        List<WebElement> results = driver.findElements(By.xpath("//div[@class='listresults']//ul[@class='companylist']"));
        //logout
        driver.findElement(By.linkText("Logout")).click();
        assertEquals(expectedCount, results.size());
    }
    
    /**
     * loginAsAdmin - this function logs the user as an administrator.
     * function is used to reduce code redundancy.
     */
    private void loginAsAdmin() {
        // login as admin
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://csunix.mohawkcollege.ca/tooltime/comp10066/A3/login.php");
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("adminP6ss");
        driver.findElement(By.name("Submit")).click();
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
                testCreate();
                testDelete();
                testCityDirectory("Calgary", 4);
                testCityDirectory("Guelph", 6);
                testCityDirectory("Hamilton", 7);
                testCityDirectory("Kingsville", 1);

                // DO NOT MODIFY below line
                // ----------------------------------------
                closeBrowser();

            } catch (Exception ex) {
                System.err.println("Exception caught starting " + browser + " " + ex.getMessage());
            }

        }
    }
}
