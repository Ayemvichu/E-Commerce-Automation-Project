package Login_Module;

import java.time.Duration;
import java.util.Random; // Random number generate cheyyan

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select; 
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Registration_Test {
    
    WebDriver driver;
    
    @BeforeClass
    public void LaunchBrowser() {
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @BeforeMethod
    public void navigateToSignupPage() {
        driver.get("https://automationexercise.com/login");
        
       
        try {
            WebElement logoutBtn = driver.findElement(By.xpath("//a[contains(text(),'Logout')]"));
            if(logoutBtn.isDisplayed()) {
                System.out.println("User was logged in. Logging out to see Signup form...");
                
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", logoutBtn);
                
                
                driver.get("https://automationexercise.com/login");
            }
        } catch (Exception e) {
            
        }
    }
    @Test(priority = 1)
    public void TC_REG_01_VerifyNavigateToSignUp() {
        System.out.println("Running TC_01: Verify Signup Section Visibility");
        
        boolean isSignupVisible = driver.findElement(By.xpath("//h2[contains(text(),'New User Signup!')]")).isDisplayed();
        Assert.assertTrue(isSignupVisible, "Signup Section not found!");
    }

   
    @Test(priority = 2)
    public void TC_REG_02_ValidRegistration() {
        System.out.println("Running TC_02: Valid Registration with Dynamic Email");
        
        
        Random random = new Random();
        int randomNumber = random.nextInt(9000); 
        String dynamicEmail = "ayemvichu" + randomNumber + "@gmail.com";
        System.out.println("Testing with Email: " + dynamicEmail);

        
        driver.findElement(By.xpath("//input[@data-qa='signup-name']")).sendKeys("Ayemvichu Test");
        driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys(dynamicEmail);
        driver.findElement(By.xpath("//button[@data-qa='signup-button']")).click();
        
        
        driver.findElement(By.id("id_gender1")).click(); 
        driver.findElement(By.id("password")).sendKeys("123456");
        
        
        new Select(driver.findElement(By.id("days"))).selectByVisibleText("10");
        new Select(driver.findElement(By.id("months"))).selectByVisibleText("May");
        new Select(driver.findElement(By.id("years"))).selectByVisibleText("2000");
        
       
        driver.findElement(By.id("first_name")).sendKeys("Ayemvichu");
        driver.findElement(By.id("last_name")).sendKeys("Tester");
        driver.findElement(By.id("address1")).sendKeys("Padur, Kerala");
        
        new Select(driver.findElement(By.id("country"))).selectByVisibleText("India");
        driver.findElement(By.id("state")).sendKeys("Kerala");
        driver.findElement(By.id("city")).sendKeys("Kochi");
        driver.findElement(By.id("zipcode")).sendKeys("682030");
        driver.findElement(By.id("mobile_number")).sendKeys("9876543210");
        
        WebElement createAccountBtn = driver.findElement(By.xpath("//button[@data-qa='create-account']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", createAccountBtn);
        
       
        boolean isAccountCreated = driver.findElement(By.xpath("//b[contains(text(), 'Account Created')]")).isDisplayed();
        Assert.assertTrue(isAccountCreated, "Registration Failed!");
        
        
        driver.findElement(By.xpath("//a[@data-qa='continue-button']")).click();
        
    }

    
    @Test(priority = 3)
    public void TC_REG_03_ExistingEmail() {
        System.out.println("Running TC_03: Check Existing Email");
        
        driver.findElement(By.xpath("//input[@data-qa='signup-name']")).sendKeys("Ayemvichu");
        
        
        driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys("ayemvichu@gmail.com");
        
        driver.findElement(By.xpath("//button[@data-qa='signup-button']")).click();
        
        
        String errorText = driver.findElement(By.xpath("//p[contains(text(),'already exist')]")).getText();
        Assert.assertEquals(errorText, "Email Address already exist!");
    }

    
    @Test(priority = 4)
    public void TC_REG_04_InvalidEmailFormat() {
        System.out.println("Running TC_04: Invalid Email Format");
        
        driver.findElement(By.xpath("//input[@data-qa='signup-name']")).sendKeys("Ayemvichu");
        driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys("ayemvichu.com"); 
        driver.findElement(By.xpath("//button[@data-qa='signup-button']")).click();
        
        
        boolean isStillOnSignup = driver.findElement(By.xpath("//button[@data-qa='signup-button']")).isDisplayed();
        Assert.assertTrue(isStillOnSignup, "System accepted invalid email!");
    }

   
    @Test(priority = 5)
    public void TC_REG_05_EmptyFields() {
        System.out.println("Running TC_05: Empty Fields Submit");
        
        
        driver.findElement(By.xpath("//button[@data-qa='signup-button']")).click();
        
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "System accepted empty fields!");
    }

    @AfterClass
    public void tearDown() {
        if(driver != null) {
            
        }
    }
}