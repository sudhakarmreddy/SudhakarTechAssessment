package common;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverUtilities {
	WebDriver driver;
	WebDriverWait wait;
	Properties prop;
	String folderPath = System.getProperty("user.dir");
	String resourcesPath = folderPath+"/src/test/resources/";
	public static final Logger logger = LogManager.getLogger();
	
	
	// Creates WebDriver instance based on the browser specified in config file
	public void initializeDriver() throws IOException {
		if(driver == null) {
			String BROWSER = getProperty("browser");
			
			if(BROWSER.equalsIgnoreCase("chrome")) {
	        	this.driver = startChromeDriver();
			}
	        else if(BROWSER.equalsIgnoreCase("firefox")) {
	        	this.driver = startFirefoxDriver();
	        }
	        else if(BROWSER.equalsIgnoreCase("edge")) {
	        	this.driver = startEdgeDriver();
	        }
	        else if(BROWSER.equalsIgnoreCase("iexplore")) {
	        	this.driver = startIEDriver();
	        }
	        else {
	        	logger.error("Browser " + BROWSER + " not supported");
	        	throw new IllegalArgumentException("Browser " + BROWSER + " not supported");
			}
			
			logger.info("Driver successfully initiated for browser : "+BROWSER);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			
			wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
		}
	}
	
	// Supporting Methods
	public WebDriver startIEDriver() {
		System.setProperty("webdriver.ie.driver", resourcesPath+"IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		return driver;
	}
	
	public WebDriver startEdgeDriver() {
		System.setProperty("webdriver.edge.driver", resourcesPath+"msedgedriver.exe");
		driver = new EdgeDriver();
		return driver;
	}
	
	public WebDriver startFirefoxDriver() {
		System.setProperty("webdriver.gecko.driver", resourcesPath+"geckodriver.exe");
		driver = new FirefoxDriver();
		return driver;
	}
	
	public WebDriver startChromeDriver() {
		System.setProperty("webdriver.chrome.driver", resourcesPath+"chromedriver.exe");
		driver = new ChromeDriver();
		return driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void quitBrowser() {
		if(driver != null) {
			logger.info("Quit Browser Session");
			driver.quit();
		}
	}
	
	
	// Utility Methods
	public void loadPage(String url) throws IOException {
		logger.info("Loading URL: "+url);
		driver.get(url);
	}
	
	public String getProperty(String key) throws IOException {
		prop = new Properties();
		FileReader fr = new FileReader(resourcesPath+"configFiles/config.properties");
		prop.load(fr);
		return prop.getProperty(key);
	}
	
	public WebElement findElement(By locator) {
		return driver.findElement(locator);
	}
	
	public List<WebElement> findElements(By locator) {
		return driver.findElements(locator);
	}
	
	public void click(By locator) {
		try {
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			
			if(element != null) {
				element.click();
			}
		} catch(TimeoutException e) {
			logger.error("TimeoutException : "+e.getMessage());
		}
	}
	
	public void submit(By locator) {
		WebElement element = findElement(locator);
		element.submit();
	}
	
	public void hover(By locator) {
		WebElement ele = driver.findElement(locator);

		Actions action = new Actions(driver);
		action.moveToElement(ele).perform();
	}
	
	public void hoverAndClick(By locator) {
		WebElement ele = driver.findElement(locator);

		Actions action = new Actions(driver);
		action.moveToElement(ele).perform();
		action.click().build().perform();
	}
	
	public void scrollAndClickLink(By locator) {
		WebElement element = driver.findElement(locator);
		
		JavascriptExecutor js = (JavascriptExecutor) driver; 
		js.executeScript("arguments[0].scrollIntoView();", element);
		
		String url = element.getAttribute("href");
		try{
			driver.navigate().to(url);
		} catch(Exception e) {
			logger.error("Unable to navigate to url : "+url);
		}
	}
	
	public void nextTab() {
		Set<String> handles=driver.getWindowHandles();
		String currentHandle= driver.getWindowHandle();
		
		for(String actual: handles) {
			if(!actual.equalsIgnoreCase(currentHandle)) {
				driver.switchTo().window(actual);
			}
		}
	}
	
	public void scrollToEndOfPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver; 
		
		long prev_height = -1;
		int max_scrolls = 15;
		int scroll_count = 0;
				
		while(scroll_count < max_scrolls) {
		    js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
		    long new_height = (Long) js.executeScript("return document.documentElement.scrollHeight");
		    
		    if(new_height == prev_height) {
		        break;
		    }
		    
		    prev_height = new_height;
		    scroll_count += 1;
		}
	}
	
	public void writeToFile(String filePath, String data) throws IOException {
		  try {
			  FileWriter writer = new FileWriter(filePath, true); //true would append data to file
			  writer.write(data);
			  writer.append('\n');
			  writer.close();
		  } catch(Exception e) {
			  logger.error("Failure : Write to file. "+e.getMessage());
		  }
	}
	
	public Logger getLogger() {
		return logger;
	}
}
