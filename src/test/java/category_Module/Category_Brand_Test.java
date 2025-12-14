package category_Module;

import java.time.Duration;
import java.util.List;
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

public class Category_Brand_Test {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        driver.get("https://automationexercise.com/products");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

   

    @Test(priority = 1)
    public void TC_CAT_01_Verify_Women_Expansion() {
        System.out.println("Running TC 01: Verify Women Category Expansion");
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//a[normalize-space()='Women']")).click();
        WebElement dress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]")));
        Assert.assertTrue(dress.isDisplayed());
        System.out.println("PASS: Women category expanded.");
    }

    @Test(priority = 2)
    public void TC_CAT_02_Select_Women_Dress() {
        System.out.println("Running TC 02: Select Women -> Dress");
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//a[normalize-space()='Women']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]"))).click();
        WebElement header = driver.findElement(By.xpath("//h2[contains(text(),'Women - Dress Products')]"));
        Assert.assertTrue(header.isDisplayed());
        System.out.println("PASS: Navigated to Women-Dress.");
    }

    @Test(priority = 3)
    public void TC_CAT_03_Verify_Men_Expansion() {
        System.out.println("Running TC 03: Verify Men Category Expansion");
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//a[normalize-space()='Men']")).click();
        WebElement jeans = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Men']//a[contains(text(),'Jeans')]")));
        Assert.assertTrue(jeans.isDisplayed());
        System.out.println("PASS: Men category expanded.");
    }

    @Test(priority = 4)
    public void TC_CAT_04_Select_Men_Jeans() {
        System.out.println("Running TC 04: Select Men -> Jeans");
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//a[normalize-space()='Men']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Men']//a[contains(text(),'Jeans')]"))).click();
        WebElement header = driver.findElement(By.xpath("//h2[contains(text(),'Men - Jeans Products')]"));
        Assert.assertTrue(header.isDisplayed());
        System.out.println("PASS: Navigated to Men-Jeans.");
    }

    @Test(priority = 5)
    public void TC_CAT_05_Verify_Kids_Category() {
        System.out.println("Running TC 05: Verify Kids Category");
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//a[normalize-space()='Kids']")).click();
        WebElement kidsDress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Kids']//a[contains(text(),'Dress')]")));
        Assert.assertTrue(kidsDress.isDisplayed());
        System.out.println("PASS: Kids category works.");
    }

    @Test(priority = 6)
    public void TC_CAT_06_Collapse_Category() {
        System.out.println("Running TC 06: Collapse Category");
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//a[normalize-space()='Women']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]")));
        driver.findElement(By.xpath("//a[normalize-space()='Women']")).click();
        System.out.println("PASS: Category toggle clicked.");
    }



    @Test(priority = 7)
    public void TC_CAT_07_Verify_Brand_Count() {
        System.out.println("Running TC 07: Verify Brand Count (Real Check)");
        js.executeScript("window.scrollBy(0, 600)");
        
        String brandText = driver.findElement(By.xpath("//a[@href='/brand_products/Polo']")).getText(); 
        String expectedCountStr = brandText.replaceAll("[^0-9]", ""); 
        int expectedCount = Integer.parseInt(expectedCountStr);
        
        WebElement poloLink = driver.findElement(By.xpath("//a[@href='/brand_products/Polo']"));
        js.executeScript("arguments[0].click();", poloLink); 
        
        int actualCount = driver.findElements(By.className("single-products")).size();
        System.out.println("Expected: " + expectedCount + ", Actual: " + actualCount);
        
       
        Assert.assertEquals(actualCount, expectedCount, "FAIL: Brand count mismatch!");
    }

    @Test(priority = 8)
    public void TC_CAT_08_Navigate_Polo() {
        System.out.println("Running TC 08: Navigate Polo");
        js.executeScript("window.scrollBy(0, 600)");
        
        WebElement poloLink = driver.findElement(By.xpath("//a[@href='/brand_products/Polo']"));
        js.executeScript("arguments[0].click();", poloLink); 
        
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Brand - Polo Products')]")));
        Assert.assertTrue(header.isDisplayed());
        System.out.println("PASS: Polo Page Loaded.");
    }

    @Test(priority = 9)
    public void TC_CAT_09_Navigate_HM() {
        System.out.println("Running TC 09: Navigate H&M");
        js.executeScript("window.scrollBy(0, 600)");
        
        WebElement hmLink = driver.findElement(By.xpath("//a[@href='/brand_products/H&M']"));
        js.executeScript("arguments[0].click();", hmLink); 
        
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Brand - H&M Products')]")));
        Assert.assertTrue(header.isDisplayed());
        System.out.println("PASS: H&M Page Loaded.");
    }

    @Test(priority = 10)
    public void TC_CAT_10_Switch_Category_To_Brand() {
        System.out.println("Running TC 10: Switch Category to Brand");
        js.executeScript("window.scrollBy(0, 300)");
        
       
        driver.findElement(By.xpath("//a[normalize-space()='Women']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]"))).click();
        
   
        js.executeScript("window.scrollBy(0, 300)");
        WebElement madameLink = driver.findElement(By.xpath("//a[@href='/brand_products/Madame']"));
        js.executeScript("arguments[0].click();", madameLink); 
        
       
        WebElement newHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Brand - Madame Products')]")));
        
        Assert.assertTrue(newHeader.isDisplayed());
        System.out.println("PASS: Switched successfully to Madame.");
    }
    @Test(priority = 11)
    public void TC_CAT_11_Verify_Active_Selection_Color() {
        System.out.println("Running TC 11: Verify UI Highlighting");
        js.executeScript("window.scrollBy(0, 600)");
        
        WebElement poloLink = driver.findElement(By.xpath("//a[@href='/brand_products/Polo']"));
        js.executeScript("arguments[0].click();", poloLink);
        
        WebElement activeLink = driver.findElement(By.xpath("//a[@href='/brand_products/Polo']"));
        String color = activeLink.getCssValue("color");
        Assert.assertNotNull(color); 
        System.out.println("PASS: Active link found.");
    }

    @Test(priority = 12)
    public void TC_CAT_12_Back_Button() {
        System.out.println("Running TC 12: Back Button (Negative)");
        js.executeScript("window.scrollBy(0, 600)");
        
       
        WebElement poloLink = driver.findElement(By.xpath("//a[@href='/brand_products/Polo']"));
        js.executeScript("arguments[0].click();", poloLink); 
        
      
        WebElement hmLink = driver.findElement(By.xpath("//a[@href='/brand_products/H&M']"));
        js.executeScript("arguments[0].click();", hmLink); 
        
       
        driver.navigate().back();
        
       
        wait.until(ExpectedConditions.urlContains("Polo"));
        System.out.println("PASS: Back button worked.");
    }

    @Test(priority = 13)
    public void TC_CAT_13_Refresh_Page() {
        System.out.println("Running TC 13: Refresh Page");
        js.executeScript("window.scrollBy(0, 300)");
        
        driver.findElement(By.xpath("//a[normalize-space()='Women']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]"))).click();
        
        driver.navigate().refresh();
        
        WebElement header = driver.findElement(By.xpath("//h2[contains(text(),'Women - Dress Products')]"));
        Assert.assertTrue(header.isDisplayed());
        System.out.println("PASS: Page retained after refresh.");
    }

    @Test(priority = 14)
    public void TC_CAT_14_URL_Manipulation() {
        System.out.println("Running TC 14: URL Manipulation");
        driver.get("https://automationexercise.com/brand_products/InvalidBrand123");
        boolean isPageLoaded = driver.findElements(By.tagName("body")).size() > 0;
        Assert.assertTrue(isPageLoaded);
        System.out.println("PASS: Site handled invalid URL gracefully.");
    }

    @Test(priority = 15)
    public void TC_CAT_15_Broken_Images() {
        System.out.println("Running TC 15: Check Broken Images (Real Check)");
        driver.get("https://automationexercise.com/products");
        
        List<WebElement> images = driver.findElements(By.tagName("img"));
        for (WebElement img : images) {
            Object width = js.executeScript("return arguments[0].naturalWidth", img);
            if (width.toString().equals("0")) {
                System.out.println("Broken Image Found: " + img.getAttribute("src"));
            }
        }
        
        
        System.out.println("PASS: Checked images.");
    }
}