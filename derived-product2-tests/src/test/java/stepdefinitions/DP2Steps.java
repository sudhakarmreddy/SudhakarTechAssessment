package stepdefinitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import common.DriverUtilities;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobject.DP2HomePage;

public class DP2Steps {

	DP2HomePage dp2_homepage = new DP2HomePage();
	DriverUtilities util = new DriverUtilities();
	List<String> footerLinkUrls;
	Logger logger = util.getLogger();
	
	@Given("User is on DP2 Home Page")
	public void user_is_on_dp2_home_page() throws IOException {
		String DP2_HomePage_Url = util.getProperty("DP2_HomePage_Url");
		util.initializeDriver();
		util.loadPage(DP2_HomePage_Url);
	}

	@When("User scrolls to the end of the Home Page")
	public void user_scrolls_to_the_end_of_the_home_page() {
		util.scrollToEndOfPage();
	}

	@And("Collects all the footer links")
	public void collects_all_the_footer_links() {
		// Collect all footer links
		List<WebElement> footerLinkElements = util.findElements(dp2_homepage.linksInFooter);
		footerLinkUrls = new ArrayList<String>();

		for (WebElement link : footerLinkElements) {
			String linkUrl = link.getAttribute("href");
			footerLinkUrls.add(linkUrl);
		}
	}

	@And("Writes them into a csv file")
	public void writes_them_into_a_csv_file() throws IOException {
		String filePath = util.getProperty("csvFilePath");

		for (String link : footerLinkUrls) {
			try {
				util.writeToFile(filePath, link);
			} catch (Exception e) {
				logger.error("Failure : Write to file."+e.getMessage());
			}
		}
	}

	@Then("Validate the Urls and report if there are any duplicates")
	public void validate_the_urls_and_report_if_there_are_any_duplicates() {
		List<String> duplicateLinks = new ArrayList<String>();
		HashSet<String> uniqueLinks = new HashSet<String>();

		for (String link : footerLinkUrls) {
			if (!uniqueLinks.add(link)) {
				duplicateLinks.add(link);
			}
		}

		if (duplicateLinks.size() > 0) {
			System.out.println("\n****************Count of Duplicate Links: " + duplicateLinks.size());
			logger.info("Duplicate links present in footer : "+duplicateLinks.size());
		}
	}
	
	@After
	public void close_browser_session() {
		util.quitBrowser();
	}
}
