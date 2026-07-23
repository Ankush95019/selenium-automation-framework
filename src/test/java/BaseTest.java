import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class BaseTest {


    public WebDriver driver;


    @BeforeMethod
    public void setup(){


        WebDriverManager.chromedriver().setup();


        ChromeOptions options = new ChromeOptions();


        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");


        driver = new ChromeDriver(options);


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
    public void tearDown(){

        if(driver != null){

            driver.quit();

        }

    }

}