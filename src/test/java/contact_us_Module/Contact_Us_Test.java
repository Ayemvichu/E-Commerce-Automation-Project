package contact_us_Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Contact_Us_Test {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    String dummyFilePath;

    @BeforeMethod
    public void setup() throws IOException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        
        
        File file = new File("automation_test_file.txt");
        if (!file.exists()) {
            FileWriter writer = new FileWriter(file);
            writer.write("This is a sample file for Automation Testing.");
            writer.close();
        }
        dummyFilePath = file.getAbsolutePath(); 
        
        driver.get("https://automationexercise.com/");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
   
    public void navigateToContactUs() {
        driver.findElement(By.xpath("//a[contains(text(),'Contact us')]")).click();
    }

  

    @Test(priority = 1)
    public void TC_CON_01_Navigate_To_Contact_Us() {
        System.out.println("Running TC 01: Navigate to Contact Us");
        navigateToContactUs();
        
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Contact Us") || driver.getCurrentUrl().contains("contact_us"));
        System.out.println("PASS: Navigated successfully.");
    }

    @Test(priority = 2)
    public void TC_CON_02_Verify_Page_Elements() {
        System.out.println("Running TC 02: Verify UI Elements");
        navigateToContactUs();
        
        boolean getInTouch = driver.findElement(By.xpath("//h2[contains(text(),'Get In Touch')]")).isDisplayed();
        boolean feedback = driver.findElement(By.xpath("//h2[contains(text(),'Feedback')]")).isDisplayed();
        
        Assert.assertTrue(getInTouch && feedback, "UI Elements missing!");
        System.out.println("PASS: Headings are visible.");
    }

    @Test(priority = 3)
    public void TC_CON_03_Submit_Form_All_Fields() {
        System.out.println("Running TC 03: Submit Form (All Fields)");
        navigateToContactUs();
        
        driver.findElement(By.name("name")).sendKeys("Vishnu Test");
        driver.findElement(By.name("email")).sendKeys("test@mail.com");
        driver.findElement(By.name("subject")).sendKeys("General Query");
        driver.findElement(By.id("message")).sendKeys("Testing Full Submission");
        
       
        driver.findElement(By.name("upload_file")).sendKeys(dummyFilePath);
        
        driver.findElement(By.name("submit")).click();
        
        
        Alert alert = driver.switchTo().alert();
        alert.accept();
        
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'alert-success')]")));
        Assert.assertTrue(successMsg.getText().contains("Success! Your details have been submitted"));
        System.out.println("PASS: Form submitted with file.");
    }

    @Test(priority = 4)
    public void TC_CON_04_Submit_Without_File() {
        System.out.println("Running TC 04: Submit Without File");
        navigateToContactUs();
        
        driver.findElement(By.name("name")).sendKeys("Vishnu NoFile");
        driver.findElement(By.name("email")).sendKeys("test@mail.com");
        driver.findElement(By.name("subject")).sendKeys("No File Test");
        driver.findElement(By.id("message")).sendKeys("Testing without file");
        
        driver.findElement(By.name("submit")).click();
        driver.switchTo().alert().accept();
        
        boolean success = driver.findElement(By.xpath("//div[contains(@class,'alert-success')]")).isDisplayed();
        Assert.assertTrue(success);
        System.out.println("PASS: Submitted without file.");
    }

    @Test(priority = 5)
    public void TC_CON_05_Empty_Form_Submission() {
        System.out.println("Running TC 05: Empty Form (Negative)");
        navigateToContactUs();
        
        driver.findElement(By.name("submit")).click();
        
       
        boolean isSuccessVisible = driver.findElements(By.xpath("//div[contains(@class,'alert-success')]")).size() > 0;
        
        Assert.assertFalse(isSuccessVisible, "FAIL: System accepted empty form!");
        System.out.println("PASS: System blocked empty form submission correctly.");
    }

    @Test(priority = 6)
    public void TC_CON_06_Invalid_Email_Format() {
        System.out.println("Running TC 06: Invalid Email (Negative)");
        navigateToContactUs();
        
        driver.findElement(By.name("name")).sendKeys("Test");
        driver.findElement(By.name("email")).sendKeys("vishnu_test");
        driver.findElement(By.name("subject")).sendKeys("Subject");
        driver.findElement(By.id("message")).sendKeys("Message");
        
        driver.findElement(By.name("submit")).click();
        
        
        WebElement emailField = driver.findElement(By.name("email"));
        String valMsg = emailField.getAttribute("validationMessage");
        
        System.out.println("Validation Says: " + valMsg);
        Assert.assertTrue(valMsg.contains("@") || !valMsg.isEmpty(), "Email validation failed!");
        System.out.println("PASS: System blocked invalid email.");
    }

    @Test(priority = 7)
    public void TC_CON_07_File_Upload_UI_Verification() {
        System.out.println("Running TC 07: File Upload UI Check");
        navigateToContactUs();
        
        driver.findElement(By.name("upload_file")).sendKeys(dummyFilePath);
        
      
        String value = driver.findElement(By.name("upload_file")).getAttribute("value");
        Assert.assertTrue(value.contains("automation_test_file.txt"));
        System.out.println("PASS: File selected successfully: " + value);
    }

    @Test(priority = 8)
    public void TC_CON_08_Cancel_Browser_Alert() {
        System.out.println("Running TC 08: Cancel Alert");
        navigateToContactUs();
        
        driver.findElement(By.name("name")).sendKeys("Cancel Test");
        driver.findElement(By.name("email")).sendKeys("cancel@test.com");
        driver.findElement(By.name("subject")).sendKeys("Cancel Alert");
        driver.findElement(By.id("message")).sendKeys("I will cancel this.");
        
        
        driver.findElement(By.name("submit")).click();
        
       
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
            System.out.println("Action: Alert Dismissed (Clicked Cancel).");
        } catch (Exception e) {
            System.out.println("WARN: Alert did not appear.");
        }
        
       
        boolean isSuccessVisible = driver.findElements(By.xpath("//div[contains(@class,'alert-success')]")).size() > 0;
        
        if (isSuccessVisible) {
        
            Assert.fail("FAIL: Form submitted even after clicking Cancel! (Bug Found)");
        } else {
            System.out.println("PASS: Form was NOT submitted.");
        }
    }

    @Test(priority = 9)
    public void TC_CON_09_Return_Home_Functionality() {
        System.out.println("Running TC 09: Return Home");
        
        TC_CON_03_Submit_Form_All_Fields(); 
        
        driver.findElement(By.xpath("//span[contains(text(),' Home')]")).click();
        
        Assert.assertEquals(driver.getCurrentUrl(), "https://automationexercise.com/");
        System.out.println("PASS: Returned to Home Page.");
    }

    @Test(priority = 10)
    public void TC_CON_10_SQL_Injection_Check() {
        System.out.println("Running TC 10: SQL Injection (Security)");
        navigateToContactUs();
        
        driver.findElement(By.name("name")).sendKeys("Hacker");
        driver.findElement(By.name("email")).sendKeys("hacker@test.com");
        driver.findElement(By.name("subject")).sendKeys("SQL Injection");
        
        
        driver.findElement(By.id("message")).sendKeys("SELECT * FROM users; DROP TABLE users;");
        
        driver.findElement(By.name("submit")).click();
        driver.switchTo().alert().accept();
        
        
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'alert-success')]")));
        Assert.assertTrue(successMsg.isDisplayed());
        System.out.println("PASS: System handled SQL query as plain text.");
    }
}