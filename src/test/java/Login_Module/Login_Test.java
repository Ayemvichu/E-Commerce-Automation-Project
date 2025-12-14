package Login_Module;

import java.time.Duration;
import java.util.List; // Added List import
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Login_Test {
	
	WebDriver driver;
	
    @BeforeClass
    public void LaunchBrowser() {
        System.out.println("Launching Chrome browser...");
    	ChromeOptions options = new ChromeOptions();
    	options.setPageLoadStrategy(PageLoadStrategy.EAGER); 
    	options.addArguments("--start-maximized"); 
    	options.addArguments("--remote-allow-origins=*"); 

        driver = new ChromeDriver(options); 
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Reduced to 10s is enough
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }
  
    @BeforeMethod
    public void openWebsite() {
        System.out.println("Opening Automation Exercise website...");
        driver.get("https://automationexercise.com/");
        
        // --- Better Logic to Handle Logout ---
        // Check if 'Logout' button is present using findElements (No Exception if not found)
        List<WebElement> logoutBtns = driver.findElements(By.xpath("//a[contains(text(),'Logout')]"));
        
        if (!logoutBtns.isEmpty()) { 
            System.out.println("User is logged in. Logging out now...");
            logoutBtns.get(0).click(); 
        }
        // -------------------------------------

        driver.findElement(By.xpath("//a[contains(text(), 'Signup / Login')]")).click();
        System.out.println("Navigated to Login Page.");
    }

    @Test(priority = 0)
    public void verifyHomePageTitle() {
        System.out.println("Executing Test: Verify Title");
        
        // Fix: Explicitly go back to Home Page before checking title
        driver.findElement(By.xpath("//a[contains(text(),'Home')]")).click(); 
        
        String actualTitle = driver.getTitle();
        System.out.println("Checking Title: " + actualTitle);
        Assert.assertEquals(actualTitle, "Automation Exercise", "Title does not match!");
    }
        
    @Test(priority = 1)
    public void verifyLoginButtonVisible() {
        System.out.println("Running TC_01: Check Login Button");
        boolean isDisplayed = driver.findElement(By.xpath("//h2[contains(text(),'Login to your account')]")).isDisplayed();
        Assert.assertTrue(isDisplayed, "Login Header is not visible");
    }

    @Test(priority = 2)
    public void verifyValidLogin() {
        System.out.println("Running TC_02: Valid Login Running ---");
        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("ayemvichu@gmail.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("Ayemvichy@123"); 
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();
      
        boolean isLoggedIn = driver.findElement(By.xpath("//li/a[contains(text(), 'Logged in as')]")).isDisplayed();
        Assert.assertTrue(isLoggedIn, "Valid Login Failed!");
        
        // --- Added Logout here to clean up for next test ---
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
        System.out.println("TC_02 Finished. Logged out successfully.");
    }

    @Test(priority = 3)
    public void TC_LOGIN_03_InvalidPassword() {
        System.out.println("Running TC_03: Invalid Password");
        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("ayemvichu@gmail.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("WrongPass123"); 
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();
        
        String errorText = driver.findElement(By.xpath("//p[contains(text(),'incorrect')]")).getText();
        Assert.assertTrue(errorText.contains("incorrect"), "Error message not found!");
    }

    @Test(priority = 4)
    public void TC_LOGIN_04_InvalidUsername() {
        System.out.println("Running TC_04: Invalid Username");
        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("fakeuser@test.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("fake123");
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();
        
        String errorText = driver.findElement(By.xpath("//p[contains(text(),'incorrect')]")).getText();
        Assert.assertTrue(errorText.contains("incorrect"), "Error message not found!");
    }

    @Test(priority = 5)
    public void TC_LOGIN_05_EmptyFields() {
        System.out.println("Running TC_05: Empty Fields");
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "System allowed login with empty fields!");
    }

    @Test(priority = 6)
    public void TC_LOGIN_06_PasswordMasking() {
        System.out.println("Running TC_06: Password Masking");
        WebElement passField = driver.findElement(By.xpath("//input[@data-qa='login-password']"));
        passField.sendKeys("secret123");
        
        String typeAttribute = passField.getAttribute("type");
        Assert.assertEquals(typeAttribute, "password", "Password is NOT masked!");
    }

    @Test(priority = 7)
    public void TC_LOGIN_07_BrowserBackButton() {
        System.out.println("Running TC_07: Browser Back Button Security");
        
        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("ayemvichu@gmail.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("Ayemvichy@123");
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
        logoutBtn.click();
        
        driver.navigate().back();
        
        boolean isDashboardVisible = driver.findElements(By.xpath("//li/a[contains(text(), 'Logged in as')]")).size() > 0;
        Assert.assertFalse(isDashboardVisible, "Security Issue: Back button allows access to dashboard!");
    }

    @Test(priority = 8)
    public void TC_LOGIN_08_VerifyLogout() {
        System.out.println("Running TC_08: Verify Logout Functionality");

        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("ayemvichu@gmail.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("Ayemvichy@123");
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();

        boolean isLoggedIn = driver.findElement(By.xpath("//li/a[contains(text(), 'Logged in as')]")).isDisplayed();
        Assert.assertTrue(isLoggedIn, "Pre-condition Failed: User could not login.");

        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
        System.out.println("Clicked on Logout button.");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "Logout Failed! User not redirected to login page.");
        
        boolean isLoginHeaderVisible = driver.findElement(By.xpath("//h2[contains(text(),'Login to your account')]")).isDisplayed();
        Assert.assertTrue(isLoginHeaderVisible, "Logout Verification Failed!");
    }

    @AfterClass
    public void tearDown() {
        if(driver != null) {
            System.out.println("Closing Browser...");
            driver.quit(); 
        }
    }
}