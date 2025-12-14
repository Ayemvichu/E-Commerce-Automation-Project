package Product_Module;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Product_Details_Test {
    
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        // Browser Setup
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
      
        driver.get("https://automationexercise.com/");
    }

    @Test(priority = 1)
    public void TC_PROD_01_VerifyAllProductsPage() {
        System.out.println("Running TC_PROD_01: Verify All Products Page Navigation");
        
       
        driver.findElement(By.xpath("//a[@href='/products']")).click();
        
       
        String actualTitle = driver.findElement(By.xpath("//h2[contains(text(),'All Products')]")).getText();
        Assert.assertEquals(actualTitle, "ALL PRODUCTS", "Failed to navigate to Products page!");
        
        System.out.println("Status: PASS - Navigated to All Products Page.");
    }

    @Test(priority = 2)
    public void TC_PROD_02_VerifyProductListView() {
        System.out.println("Running TC_PROD_02: Verify Product List");
        
        
        driver.findElement(By.xpath("//a[@href='/products']")).click();

       
        List<WebElement> products = driver.findElements(By.className("features_items"));
        Assert.assertTrue(products.size() > 0, "No products are displayed!");
        
        System.out.println("Status: PASS - Products are listed correctly.");
    }

    @Test(priority = 3)
    public void TC_PROD_03_VerifyProductDetails() {
        System.out.println("Running TC_PROD_03: Verify Product Details Page");
        
        
        driver.findElement(By.xpath("//a[@href='/products']")).click();

       
        WebElement viewProductBtn = driver.findElement(By.xpath("(//a[contains(text(),'View Product')])[1]"));
        
       
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", viewProductBtn);

       
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("product_details"), "Not on Product Details page!");

        
        boolean isNameVisible = driver.findElement(By.xpath("//div[@class='product-information']/h2")).isDisplayed();
        boolean isCategoryVisible = driver.findElement(By.xpath("//p[contains(text(),'Category')]")).isDisplayed();
        boolean isPriceVisible = driver.findElement(By.xpath("//span[contains(text(),'Rs.')]")).isDisplayed();
        boolean isAvailabilityVisible = driver.findElement(By.xpath("//b[contains(text(),'Availability')]")).isDisplayed();

        Assert.assertTrue(isNameVisible, "Product Name is missing!");
        Assert.assertTrue(isCategoryVisible, "Category is missing!");
        Assert.assertTrue(isPriceVisible, "Price is missing!");
        Assert.assertTrue(isAvailabilityVisible, "Availability status is missing!");
        
        System.out.println("Status: PASS - All Product Details are visible.");
    }

    @AfterMethod
    public void tearDown() {
     
        if(driver != null) {
            driver.quit(); 
        }
    }
}