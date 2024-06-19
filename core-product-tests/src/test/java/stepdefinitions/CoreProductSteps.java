package stepdefinitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import common.DriverUtilities;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobject.CoreProductPage;
import pageobject.NewsAndFeaturesPage;
import pageobject.ShopMensPage;

public class CoreProductSteps {

	CoreProductPage coreProduct = new CoreProductPage();
	NewsAndFeaturesPage newsAndFeatures = new NewsAndFeaturesPage();
	ShopMensPage mensPage = new ShopMensPage();
	DriverUtilities util = new DriverUtilities();

	List<String> jacketDetails = new ArrayList<String>();
	List<Integer> videoDurations = new ArrayList<Integer>();
	
	Logger logger = util.getLogger();

	@Given("User is on Core Product Home Page")
	public void user_is_on_core_product_home_page() throws IOException {
		String Core_Product_HomePage_Url = util.getProperty("Core_Product_HomePage_Url");
		util.initializeDriver();
		util.loadPage(Core_Product_HomePage_Url);

		// Close banners if any
		util.click(coreProduct.closeTicketAccessBanner);
		util.click(coreProduct.declineCookies);
	}

	@When("Navigate to Men's Page")
	public void navigate_to_men_s_page() {
		util.hover(coreProduct.shopMenu);
		util.hoverAndClick(coreProduct.mens);

		// Close banners if any
		util.click(mensPage.closeFreeShipping);

		// As Men's page opens in a new tab, switch tab
		util.nextTab();
	}

	@And("Find all Jackets")
	public void find_all_jackets() {
		util.scrollAndClickLink(mensPage.jacketsRadioButton);
	}

	@And("For each Jacket, retrieve Price, Title and Top Seller Message \\(if any)")
	public void for_each_jacket_retrieve_price_title_and_top_seller_message_if_any() {
		String price;
		String title;
		String topSellerMsg = "Nil";
		String jacketDetail;

		while (true) {
			List<WebElement> jacketElements = util.findElements(mensPage.productCardData);

			for (WebElement jacket : jacketElements) {
				price = jacket.findElement(mensPage.productPrice).getText();
				title = jacket.findElement(mensPage.productTitle).getText();

				// TODO Step taking long time to execute
				// try {
				// topSellerMsg = jacket.findElement(mensPage.productTopSellerMsg).getText();
				// } catch(Exception e) {}

				jacketDetail = "Price: " + price + ", Title: " + title + ", topSellerMessage: " + topSellerMsg;
				jacketDetails.add(jacketDetail);

				price = "";
				title = "";
				topSellerMsg = "";
			}

			WebElement nextButton = util.findElement(mensPage.nextPage);
			if (nextButton.getAttribute("aria-disabled").equalsIgnoreCase("false")) {
				util.scrollAndClickLink(mensPage.nextPage);
			} else {
				break;
			}
		}
	}

	@When("Store the Jacket details in a file")
	public void store_the_jacket_details_in_a_file() throws IOException {
		String filePath = util.getProperty("jacketDetailsFilePath");

		for (String jacket : jacketDetails) {
			try {
				util.writeToFile(filePath, jacket);
			} catch (Exception e) {
				logger.error("Failure : Write to file."+e.getMessage());
			}
		}

		// Note : This file would be attached to Cucumber reports using After Hook.
	}

	@When("Navigate to News and Features Page")
	public void navigate_to_news_and_features_page() {
		// Hover on the ... and click 'News and Features' entry
		util.hover(coreProduct.ellipsisMenu);
		util.hoverAndClick(coreProduct.newsAndFeatures);
	}

	@And("Identify and count the number of Video feeds")
	public void identify_and_count_the_number_of_video_feeds() {
		List<WebElement> videoFeedElements = util.findElements(newsAndFeatures.timeValuesOfVideos);

		for (WebElement video : videoFeedElements) {
			// Retrieve video duration
			String videoDuration = video.getText();

			// Store number of days for each video in a list
			Pattern p = Pattern.compile("(\\d+).?d"); // Search for strings like 3d, 7d, etc
			Matcher m = p.matcher(videoDuration);

			if (m.find()) {
				int days = Integer.parseInt(m.group(1));
				videoDurations.add(days);
			} else {
				videoDurations.add(1); // These videos have duration in hours, which is less than 1 day
			}	
		}
		
		System.out.println("\n***********Count of Video Feeds : " + videoDurations.size());
		logger.info("Count of Video Feeds : "+videoDurations.size());
	}

	@And("Find the count of Videos which are older than 3d")
	public void find_the_count_of_videos_which_are_older_than_3_days() {
		int count = 0;

		for (int days : videoDurations) {
			if (days > 3) {
				count++;
			}
		}

		System.out.println("\n***********Count of videos greater than 3d: " + count);
		logger.info("Count of videos greater than 3d : "+videoDurations.size());
	}

	@Then("Validate that list of Video feeds is not empty")
	public void validate_that_list_of_video_feeds_is_not_empty() {
		Assert.assertTrue(videoDurations.size() > 0);
	}
	
	@After
	public void close_browser_session() {
		util.quitBrowser();
	}
}
