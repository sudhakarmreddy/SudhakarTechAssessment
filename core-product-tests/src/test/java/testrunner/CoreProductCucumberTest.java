package testrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(

		tags = "@CoreProduct",

		features = { "src/test/resources/features/CoreProduct.feature" }, glue = { "stepdefinitions", "testrunner" },

		plugin = { "pretty", "json:core-product-tests/target/cucumber-reports/cucumber.json",
				"html:core-product-tests/target/cucumber-reports/cucumberreport.html" },

		monochrome = true)
public class CoreProductCucumberTest extends AbstractTestNGCucumberTests {
	// @Override
	// @DataProvider(parallel = true)
	// public Object[][] scenarios() {
	// return super.scenarios();
	// }
}
