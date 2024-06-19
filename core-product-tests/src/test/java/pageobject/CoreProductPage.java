package pageobject;

import org.openqa.selenium.By;

public class CoreProductPage {

	public By closeTicketAccessBanner = By.xpath("//div[@class='p-2 absolute right-3 hover:cursor-pointer']");
	public By declineCookies = By.id("onetrust-reject-all-handler");

	public By ticketsMenu = By.xpath("//li[@class='menu-item'][1]");
	public By scheduleMenu = By.xpath("//li[@class='menu-item'][2]");
	public By teamMenu = By.xpath("//li[@class='menu-item'][3]");
	public By shopMenu = By.xpath("//li[@class='menu-item'][4]");
	public By chaseCenterMenu = By.xpath("//li[@class='menu-item'][5]");
	public By myWarriorsAccountMenu = By.xpath("//li[@class='menu-item'][6]");
	public By ellipsisMenu = By.cssSelector("a[class=' style__link_2QXEv']");

	public By mens = By.cssSelector("li[role='menuitem'] li[role='menuitem'] a[title=\"Men's\"]");

	public By newsAndFeatures = By.cssSelector("[role='menuitem'] li[role='menuitem'] a[title='News & Features']");

}