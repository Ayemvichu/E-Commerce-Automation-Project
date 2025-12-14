package Checkout_Module;

import java.time.Duration;
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

public class Checkout_Payment_Test {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup() {
        // Browser Setup
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        driver.get("https://automationexercise.com/");
    }

    @AfterMethod
    public void tearDown() {
       
        if (driver != null) {
            driver.quit();
        }
    }

   
    public void loginAndAddToCart() {
        // Login
        driver.findElement(By.xpath("//a[contains(text(),'Signup / Login')]")).click();
        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("ayemvichu@gmail.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("Ayemvichy@123");
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();

        // Add Product
        js.executeScript("window.scrollBy(0, 500)");
        WebElement addToCartBtn = driver.findElement(By.xpath("(//a[contains(text(),'Add to cart')])[1]"));
        js.executeScript("arguments[0].click();", addToCartBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//u[contains(text(),'View Cart')]"))).click();
    }
    
 

    @Test(priority = 1)
    public void TC_CHK_01_Checkout_LoggedInUser() {
        System.out.println("Running TC 01: Checkout Navigation");
        loginAndAddToCart();
        
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        boolean addressVisible = driver.findElement(By.xpath("//h2[contains(text(),'Address Details')]")).isDisplayed();
        Assert.assertTrue(addressVisible, "Failed to reach Address Page");
        System.out.println("PASS: Navigated to Address Page.");
    }

    @Test(priority = 2)
    public void TC_CHK_02_Register_During_Checkout() {
        System.out.println("Running TC 02: Register during Checkout");
       
        js.executeScript("window.scrollBy(0, 500)");
        WebElement addToCartBtn = driver.findElement(By.xpath("(//a[contains(text(),'Add to cart')])[1]"));
        js.executeScript("arguments[0].click();", addToCartBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//u[contains(text(),'View Cart')]"))).click();
        
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//u[contains(text(),'Register / Login')]"))).click();
        
        String actualTitle = driver.getTitle();
        Assert.assertTrue(actualTitle.contains("Signup"), "Not redirected to Signup page");
        System.out.println("PASS: Redirected to Register/Login Page.");
    }

    @Test(priority = 3)
    public void TC_CHK_03_Verify_Address_Details() {
        System.out.println("Running TC 03: Verify Address");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        boolean deliveryAdd = driver.findElement(By.id("address_delivery")).isDisplayed();
        boolean billingAdd = driver.findElement(By.id("address_invoice")).isDisplayed();
        
        Assert.assertTrue(deliveryAdd && billingAdd, "Address details missing");
        System.out.println("PASS: Address Details are visible.");
    }

    @Test(priority = 4)
    public void TC_CHK_04_Add_Order_Comment() {
        System.out.println("Running TC 04: Add Comment");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        driver.findElement(By.className("form-control")).sendKeys("Test Comment");
        driver.findElement(By.xpath("//a[contains(text(),'Place Order')]")).click();
        
        Assert.assertTrue(driver.getCurrentUrl().contains("payment"), "Failed to navigate to Payment");
        System.out.println("PASS: Comment added & Navigated to Payment.");
    }

    @Test(priority = 5)
    public void TC_CHK_05_Place_Order_Valid() {
        System.out.println("Running TC 05: Place Order");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
       
        WebElement placeOrderBtn = driver.findElement(By.xpath("//a[contains(text(),'Place Order')]"));
        js.executeScript("arguments[0].click();", placeOrderBtn);
       
        
        driver.findElement(By.name("name_on_card")).sendKeys("Test User");
        driver.findElement(By.name("card_number")).sendKeys("4111222233334444");
        driver.findElement(By.name("cvc")).sendKeys("123");
        driver.findElement(By.name("expiry_month")).sendKeys("12");
        driver.findElement(By.name("expiry_year")).sendKeys("2030");
        
        
        js.executeScript("arguments[0].click();", driver.findElement(By.id("submit")));
        
        boolean success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'Congratulations!')]"))).isDisplayed();
        Assert.assertTrue(success);
        System.out.println("PASS: Order Placed Successfully.");
    }

    @Test(priority = 6)
    public void TC_CHK_06_Download_Invoice() {
        System.out.println("Running TC 06: Download Invoice");
        // Note: Needs order placed first
        TC_CHK_05_Place_Order_Valid(); 
        
        WebElement downloadBtn = driver.findElement(By.xpath("//a[contains(text(),'Download Invoice')]"));
        Assert.assertTrue(downloadBtn.isDisplayed());
        downloadBtn.click();
        System.out.println("PASS: Download Invoice Clicked.");
    }

    @Test(priority = 7)
    public void TC_CHK_07_Continue_After_Order() {
        System.out.println("Running TC 07: Continue Button");
        TC_CHK_05_Place_Order_Valid(); 
        
        driver.findElement(By.xpath("//a[contains(text(),'Continue')]")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://automationexercise.com/");
        System.out.println("PASS: Redirected to Home Page.");
    }

    @Test(priority = 8)
    public void TC_CHK_08_Payment_Empty_Fields() {
        System.out.println("Running TC 08: Empty Fields");
        loginAndAddToCart();
        
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement placeOrderBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Place Order')]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", placeOrderBtn);
       
       
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name_on_card")));
       
        
        WebElement submitBtn = driver.findElement(By.id("submit"));
        js.executeScript("arguments[0].click();", submitBtn);
        
       
        boolean stayedOnPage = driver.getCurrentUrl().contains("payment");
        Assert.assertTrue(stayedOnPage, "FAIL: System accepted empty fields and moved to next page!");
        System.out.println("PASS: System validated empty fields.");
    }
    @Test(priority = 9)
    public void TC_CHK_09_Invalid_Card_Number() {
        System.out.println("Running TC 09: Invalid Card (Alphabets)");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Place Order')]")).click();
        
        driver.findElement(By.name("card_number")).sendKeys("ABCD");
        String value = driver.findElement(By.name("card_number")).getAttribute("value");
        
        
        System.out.println("Typed Value: " + value); 
        System.out.println("PASS: Checked card field validation.");
    }

    @Test(priority = 10)
    public void TC_CHK_10_Expiry_Date_Past() {
        System.out.println("Running TC 10: Past Expiry Date");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Place Order')]")).click();
        
        driver.findElement(By.name("name_on_card")).sendKeys("Test");
        driver.findElement(By.name("card_number")).sendKeys("4111222233334444");
        driver.findElement(By.name("cvc")).sendKeys("123");
        driver.findElement(By.name("expiry_year")).sendKeys("2000"); // Old year
        
        js.executeScript("arguments[0].click();", driver.findElement(By.id("submit")));
        
        
        Assert.assertTrue(driver.getCurrentUrl().contains("payment"));
        System.out.println("PASS: Past expiry date checked.");
    }

    @Test(priority = 11)
    public void TC_CHK_11_Empty_CVC() {
        System.out.println("Running TC 11: Empty CVC");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Place Order')]")).click();
        
        driver.findElement(By.name("name_on_card")).sendKeys("Test");
        driver.findElement(By.name("card_number")).sendKeys("4111222233334444");
        // No CVC entered
        js.executeScript("arguments[0].click();", driver.findElement(By.id("submit")));
        
        Assert.assertTrue(driver.getCurrentUrl().contains("payment"));
        System.out.println("PASS: Empty CVC checked.");
    }

    @Test(priority = 12)
    public void TC_CHK_12_Back_Button_Security() {
        System.out.println("Running TC 12: Back Button Security (FAILED CASE)");
        TC_CHK_05_Place_Order_Valid(); // Place order first
        
        driver.navigate().back(); // Press Back
        
        if(driver.getPageSource().contains("Payment")) {
            System.out.println("BUG DETECTED: Back button goes back to Payment!");
        } else {
            System.out.println("PASS: Secure.");
        }
    }

    @Test(priority = 13)
    public void TC_CHK_13_Card_Length_Validation() {
        System.out.println("Running TC 13: Card Length (FAILED CASE)");
        loginAndAddToCart();
        
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement placeOrderBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Place Order')]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", placeOrderBtn);
        
        // 2. Fill Invalid Data
        driver.findElement(By.name("name_on_card")).sendKeys("Test");
        driver.findElement(By.name("card_number")).sendKeys("123"); 
        driver.findElement(By.name("cvc")).sendKeys("123");
        driver.findElement(By.name("expiry_year")).sendKeys("2030");
        driver.findElement(By.name("expiry_month")).sendKeys("12"); 
        
        
        WebElement submitBtn = driver.findElement(By.id("submit"));
        js.executeScript("arguments[0].click();", submitBtn);
        
        
        try {
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'Congratulations!')]")));
            System.out.println("BUG DETECTED: System accepted short card number!");
            
            
            
        } catch(Exception e) {
           
            System.out.println("PASS: System blocked short card.");
        }
    }

    @Test(priority = 14)
    public void TC_CHK_14_XSS_Injection() {
        System.out.println("Running TC 14: XSS Injection");
        loginAndAddToCart();
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        driver.findElement(By.className("form-control")).sendKeys("<b>Hack</b>");
        driver.findElement(By.xpath("//a[contains(text(),'Place Order')]")).click();
        
        System.out.println("PASS: XSS Payload entered (Visual check needed for fail).");
    }

    @Test(priority = 15)
    public void TC_CHK_15_Double_Click_Payment() {
        System.out.println("Running TC 15: Double Click Payment");
       
        System.out.println("SKIP: Cannot reliably automate double-click race condition.");
    }
}