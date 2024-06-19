package stepdefinitions;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import common.DriverUtilities;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobject.DP1HomePage;

public class DP1Steps {

	DP1HomePage dp1_homepage = new DP1HomePage();
	DriverUtilities util = new DriverUtilities();
	Map<String, Integer> slideDetails = new HashMap<String, Integer>();
	Map<String, Integer> slideDetailsTestdata = new HashMap<String, Integer>();
	Logger logger = util.getLogger();

	String DP1_HomePage_Url;

	@Given("User is on DP1 Home Page")
	public void user_is_on_dp1_home_page() throws IOException {
		DP1_HomePage_Url = util.getProperty("DP1_HomePage_Url");
		util.initializeDriver();
		util.loadPage(DP1_HomePage_Url);

		// Close banners if any
		//util.click(dp1_homepage.declineCookies);

		// Read testdata from Json file
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(
					new FileReader(System.getProperty("user.dir")+util.getProperty("testdataFilePath")));
			JSONArray slideDetails = (JSONArray) obj;
			Iterator<JSONObject> iterator = slideDetails.iterator();
			while (iterator.hasNext()) {
				JSONObject slide = iterator.next();
				slideDetailsTestdata.put(slide.get("slide").toString(),
						Integer.parseInt(slide.get("duration").toString()));
			}
		} catch (Exception e) {
			logger.error("Failed to read testdata file : "+e.getMessage());
		}
	}

	@When("Retrieve slide count, title and duration")
	public void calculate_the_duration_of_each_slide() {
		// Measure the duration of each slide
		List<By> slideLocators = new ArrayList<By>();
		slideLocators.add(dp1_homepage.slide1);
		slideLocators.add(dp1_homepage.slide2);
		slideLocators.add(dp1_homepage.slide3);
		slideLocators.add(dp1_homepage.slide4);

		for (int i = 0; i < slideLocators.size(); i++) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(util.getDriver()).withTimeout(Duration.ofSeconds(35))
					.pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.visibilityOfElementLocated(slideLocators.get(i)));

			long startTime = System.currentTimeMillis();
			String key = util.findElement(slideLocators.get(i)).getText();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(slideLocators.get(i)));

			long endTime = System.currentTimeMillis();
			double durationInSeconds = (double) (endTime - startTime) / 1000.0;

			slideDetails.put(key, (int) Math.round(durationInSeconds));

			System.out.println("\n\n**************" + key + ", Duration: " + slideDetails.get(key));
			logger.info("Slide: "+ key + ", Duration: " + slideDetails.get(key));
		}
	}

	@Then("Validate slide titles and duration with testdata")
	public void validate_slide_titles_with_testdata() {
		for (Entry<String, Integer> slideEntry : slideDetailsTestdata.entrySet()) {
			String key = slideEntry.getKey();
			int value1 = slideEntry.getValue();
			
			int value2 = slideDetails.get(key);

			if (((value2 - value1) * 100 / value1) > 20) {// Deviation is more than 20%
				logger.error("Actual value: "+value2+", Expected value: "+value1);
				Assert.assertTrue(false);
			}
		}
	}
	
	@After
	public void close_browser_session() {
		util.quitBrowser();
	}
}
