package pageobject;

import org.openqa.selenium.By;

public class DP1HomePage {

	public By slideTitles = By.cssSelector("div[role=tabpanel] h1");
	public By slide1 = By.xpath("//div[2]/button[1][@aria-selected='true']");
	public By slide2 = By.xpath("//div[2]/button[2][@aria-selected='true']");
	public By slide3 = By.xpath("//div[2]/button[3][@aria-selected='true']");
	public By slide4 = By.xpath("//div[2]/button[4][@aria-selected='true']");

	public By declineCookies = By.id("onetrust-reject-all-handler");
}