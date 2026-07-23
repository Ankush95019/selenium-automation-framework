import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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



    @AfterMethod
    public void tearDown(){

        if(driver != null){

            driver.quit();

        }

    }

}