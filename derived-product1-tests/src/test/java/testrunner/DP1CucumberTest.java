package testrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(

		tags = "@DP1HomePage",

		features = { "src/test/resources/features/DP1.feature" }, glue = { "stepdefinitions", "testrunner" },

		plugin = { "pretty", "json:core-product-tests/target/cucumber-reports/cucumber.json",
				"html:core-product-tests/target/cucumber-reports/cucumberreport.html" },

		monochrome = true)
public class DP1CucumberTest extends AbstractTestNGCucumberTests {
	// @Override
	// @DataProvider(parallel = true)
	// public Object[][] scenarios() {
	// return super.scenarios();
	// }
}
