package Product_Module;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Search_Test {
	
	WebDriver driver;
  @BeforeClass
  public void LaunchBrowser() {
	  
	       
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments("--start-maximized");
	        options.addArguments("--disable-notifications"); 

	        driver = new ChromeDriver(options);
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    }

	   
	    @BeforeMethod
	    public void navigateToProductsPage() {
	        driver.get("https://automationexercise.com/");
	        
	       
	        WebElement productsBtn = driver.findElement(By.xpath("//a[@href='/products']"));
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].click();", productsBtn);
	        
	        
	        String title = driver.getTitle();
	        if(title.contains("All Products")) {
	            System.out.println("Navigated to Products Page.");
	        }
	    }

	    
	    @Test(priority = 1)
	    public void TC_SEARCH_01_NavigateToProducts() {
	        System.out.println("Running TC_01: Verify Products Page Load");
	        boolean isHeaderVisible = driver.findElement(By.xpath("//h2[contains(text(),'All Products')]")).isDisplayed();
	        Assert.assertTrue(isHeaderVisible, "Failed to load Products Page!");
	    }

	   
	    @Test(priority = 2)
	    public void TC_SEARCH_02_ValidSearch() {
	        System.out.println("Running TC_02 & 03: Valid Product Search (Tshirt)");
	        
	        driver.findElement(By.id("search_product")).sendKeys("Tshirt");
	        driver.findElement(By.id("submit_search")).click();
	        
	       
	        boolean isSearchedHeaderVisible = driver.findElement(By.xpath("//h2[contains(text(),'Searched Products')]")).isDisplayed();
	        Assert.assertTrue(isSearchedHeaderVisible, "Search Header not visible!");
	        
	        
	        List<WebElement> products = driver.findElements(By.xpath("//div[@class='productinfo text-center']/p"));
	        Assert.assertTrue(products.size() > 0, "No products found for 'Tshirt'!");
	        
	       
	        for(WebElement product : products) {
	            String productName = product.getText().toLowerCase();
	            Assert.assertTrue(productName.contains("shirt"), "Irrelevant product found: " + productName);
	        }
	    }

	    
	    @Test(priority = 3)
	    public void TC_SEARCH_04_EmptySearch() {
	        System.out.println("Running TC_04: Empty Search");
	        
	        driver.findElement(By.id("search_product")).clear();
	        driver.findElement(By.id("submit_search")).click();
	        
	        
	        boolean isHeaderVisible = driver.findElement(By.xpath("//h2[contains(text(),'All Products')]")).isDisplayed();
	        Assert.assertTrue(isHeaderVisible, "Page crashed on empty search!");
	    }

	    
	    @Test(priority = 4)
	    public void TC_SEARCH_05_InvalidSearch() {
	        System.out.println("Running TC_05: Invalid Search (Abcxyz)");
	        
	        driver.findElement(By.id("search_product")).clear();
	        driver.findElement(By.id("search_product")).sendKeys("Abcxyz");
	        driver.findElement(By.id("submit_search")).click();
	        
	        List<WebElement> products = driver.findElements(By.xpath("//div[@class='productinfo text-center']/p"));
	        Assert.assertEquals(products.size(), 0, "System showed products for invalid keyword!");
	    }
	    
	   
	    @Test(priority = 5)
	    public void TC_SEARCH_06_CaseInsensitivity() {
	        System.out.println("Running TC_06: Case Insensitivity (TSHIRT)");
	        
	        driver.findElement(By.id("search_product")).clear();
	        driver.findElement(By.id("search_product")).sendKeys("TSHIRT"); // Uppercase
	        driver.findElement(By.id("submit_search")).click();
	        
	        List<WebElement> products = driver.findElements(By.xpath("//div[@class='productinfo text-center']/p"));
	        Assert.assertTrue(products.size() > 0, "Case sensitivity check failed!");
	    }

	    
	    @Test(priority = 6)
	    public void TC_SEARCH_07_PartialSearch() {
	        System.out.println("Running TC_07: Partial Search (Dre)");
	        
	        driver.findElement(By.id("search_product")).clear();
	        driver.findElement(By.id("search_product")).sendKeys("Dre"); 
	        driver.findElement(By.id("submit_search")).click();
	        
	        List<WebElement> products = driver.findElements(By.xpath("//div[@class='productinfo text-center']/p"));
	        Assert.assertTrue(products.size() > 0, "Partial search failed!");
	        
	        
	        boolean matchFound = false;
	        for(WebElement product : products) {
	            if(product.getText().toLowerCase().contains("dress")) {
	                matchFound = true;
	                break;
	            }
	        }
	        Assert.assertTrue(matchFound, "Partial search did not find 'Dress' products!");
	    }

	    
	    @Test(priority = 7)
	    public void TC_SEARCH_08_SpecialCharSearch() {
	        System.out.println("Running TC_08: Special Char Search (@#$%)");
	        
	        driver.findElement(By.id("search_product")).clear();
	        driver.findElement(By.id("search_product")).sendKeys("@#$%");
	        driver.findElement(By.id("submit_search")).click();
	        
	        List<WebElement> products = driver.findElements(By.xpath("//div[@class='productinfo text-center']/p"));
	        
	        // Expecting Empty List (PASS)
	        Assert.assertEquals(products.size(), 0, "System crashed or showed products for special chars!");
	    }

	   
	    @Test(priority = 8)
	    public void TC_SEARCH_09_SearchPlaceholder() {
	        System.out.println("Running TC_09: Verify Search Placeholder");
	        
	        WebElement searchBox = driver.findElement(By.id("search_product"));
	        String actualPlaceholder = searchBox.getAttribute("placeholder");
	        
	        Assert.assertEquals(actualPlaceholder, "Search Product", "Placeholder text is wrong!");
	    }

	    @AfterClass
	    public void tearDown() {
	        if(driver != null) {
	             
	        }
	    }
	
  }


