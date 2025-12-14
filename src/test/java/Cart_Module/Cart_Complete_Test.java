package Cart_Module;

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

public class Cart_Complete_Test {
    
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://automationexercise.com/");
    }

    
    public void addBlueTopToCart() {
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500)");
        
        
        WebElement addToCartBtn = driver.findElement(By.xpath("(//a[contains(text(),'Add to cart')])[1]"));
        
        
        js.executeScript("arguments[0].click();", addToCartBtn);
        
        
        WebElement viewCartLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//u[contains(text(),'View Cart')]")));
        viewCartLink.click();
    }

    
    @Test(priority = 1)
    public void TC_CART_01_AddProductAndVerifyPrice() {
        System.out.println("Running TC_CART_01: Add Single Product");
        addBlueTopToCart();
        
        
        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not redirected to Cart Page!");
        
      
        String price = driver.findElement(By.xpath("//td[@class='cart_price']/p")).getText();
        Assert.assertEquals(price, "Rs. 500", "Unit Price mismatch!");
        System.out.println("PASS: Product added and Price verified.");
    }

    
    @Test(priority = 2)
    public void TC_CART_03_AddDuplicateProduct() {
        System.out.println("Running TC_CART_03: Add Duplicate Product");
        
     
        addBlueTopToCart();
        
       
        driver.findElement(By.xpath("//a[contains(text(),'Home')]")).click();
        addBlueTopToCart();
        
        
        String quantity = driver.findElement(By.xpath("//td[@class='cart_quantity']/button")).getText();
        Assert.assertEquals(quantity, "2", "Quantity did not update to 2 for duplicate item!");
        System.out.println("PASS: Duplicate item increased quantity correctly.");
    }

   
    @Test(priority = 3)
    public void TC_CART_07_UpdateQuantityAndVerifyTotal() {
        System.out.println("Running TC_CART_07 & 08: Update Quantity & Check Total");
        addBlueTopToCart();
        
       
        WebElement qtyField = driver.findElement(By.xpath("//td[@class='cart_quantity']/button"));
        Assert.assertEquals(qtyField.getText(), "1", "Default Quantity is not 1");

        
        driver.findElement(By.xpath("//a[contains(text(),'Home')]")).click();
        addBlueTopToCart(); 
        
        
        String total = driver.findElement(By.xpath("//p[@class='cart_total_price']")).getText();
        Assert.assertEquals(total, "Rs. 1000", "Total calculation incorrect!");
        System.out.println("PASS: Total calculated correctly for 2 items.");
    }

    
    @Test(priority = 4)
    public void TC_CART_09_RemoveItem() {
        System.out.println("Running TC_CART_09: Remove Item");
        addBlueTopToCart();
        
       
        driver.findElement(By.className("cart_quantity_delete")).click();
        
        
        WebElement emptyMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[contains(text(),'Cart is empty!')]")));
        Assert.assertTrue(emptyMsg.isDisplayed(), "Cart is not empty!");
        System.out.println("PASS: Item removed successfully.");
    }

   
    @Test(priority = 5)
    public void TC_CART_11_ProceedToCheckout() {
        System.out.println("Running TC_CART_11: Proceed to Checkout");
        addBlueTopToCart();
        
        driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
        
        
        WebElement loginModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//u[contains(text(),'Register / Login')]")));
        Assert.assertTrue(loginModal.isDisplayed(), "Login prompt did not appear!");
        System.out.println("PASS: Proceed to checkout triggered login prompt.");
    }

   
    @Test(priority = 6)
    public void TC_CART_12_CheckoutEmptyCart() {
        System.out.println("Running TC_CART_12: Checkout Empty Cart");
        
        
        driver.findElement(By.xpath("(//a[@href='/view_cart'])[1]")).click();
        
        
        WebElement emptyMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[contains(text(),'Cart is empty!')]")));
        Assert.assertTrue(emptyMsg.isDisplayed(), "Cart is not initially empty!");
        
        System.out.println("PASS: Cart is empty verified.");
    }

    @AfterMethod
    public void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }
}