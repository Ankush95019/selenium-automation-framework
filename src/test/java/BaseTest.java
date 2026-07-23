import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	
	WebDriver driver;

    @BeforeMethod
    public void setup() {

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.manage().window().maximize();
    }

    @Test
    public void verifyGoogleTitle() {

        driver.get("https://www.google.com");

        String title = driver.getTitle();

        System.out.println(title);

        Assert.assertEquals(title, "Google");

    }

    @AfterMethod
    public void tearDown() {

        driver.quit();

    }

}
