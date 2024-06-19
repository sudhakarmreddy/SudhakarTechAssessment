package pageobject;

import org.openqa.selenium.By;

public class ShopMensPage {
	public By dropDownItemCount = By.cssSelector("div[aria-label='page-size'] div[class='drop-down-selected']");
	public By nextPage = By.cssSelector("div[class='pagination-component'] a[data-trk-id='next-page']");
	public By nextPageDisabled = By
			.cssSelector("div[class='pagination-component'] a[data-trk-id='next-page'][aria-disabled='true']");

	public By jacketsRadioButton = By.cssSelector("a[data-trk-id='jackets']");
	public By selectedJacketOption = By.cssSelector("a[data-trk-id='jackets'][aria-label='Remove Jackets filter']");

	public By closeFreeShipping = By.cssSelector("i[class='icon icon-close-alt'][aria-label='Close Pop-Up']");

	public By productCardData = By.cssSelector("div[class='columns small-5 medium-12']");
	public By productPrice = By.cssSelector("span[class='price'] span[class='money-value']");
	public By productTitle = By.cssSelector("div[class='product-card-title']");
	public By productTopSellerMsg = By.cssSelector("div[class='product-vibrancy top-seller-vibrancy']");

}