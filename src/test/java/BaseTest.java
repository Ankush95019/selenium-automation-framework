import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;


public class BaseTest {


    public WebDriver driver;


    @BeforeMethod
    public void setup(){


    	ChromeOptions options = new ChromeOptions();


        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");


        driver = new ChromeDriver(options);


        driver.manage().window().maximize();

    }

    
    @Epic("GitHub Actions")
    @Feature("React Build")
    @Story("Verify Home Page")
    @Test
    public void verifyGoogleTitle() {

        driver.get("http://localhost:5173");
        WebElement ele1 = driver.findElement(By.xpath("//a[normalize-space()='GitHub Actions Demo']"));
//        String title = driver.getTitle();
        String heading = ele1.getText();

        System.out.println(heading);

        Assert.assertEquals(heading, "GitHub Actions Demo");

    }
    
    @Epic("Jenkins")
    @Feature("Color of Text")
    @Test
    public void VerifyTextColor1() {
    	System.out.println("Your text is of Red color");
    }
    
    @Test
    public void VerifyTextColor2() {
    	System.out.println("Your text is of Yellow color");
    }
    
    @Test
    public void VerifyTextColor3() {
    	System.out.println("Your text is of Green color");
    }
    
    @Test
    public void VerifyTextColor4() {
    	System.out.println("Your text is of Blue color");
    }


    @AfterMethod
    public void tearDown(){

        if(driver != null){

            driver.quit();

        }

    }

}