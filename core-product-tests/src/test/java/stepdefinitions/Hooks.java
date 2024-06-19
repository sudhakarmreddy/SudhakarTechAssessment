package stepdefinitions;

import java.nio.file.Files;
import java.nio.file.Paths;

import common.DriverUtilities;
import io.cucumber.java.After;

public class Hooks extends DriverUtilities {

	static DriverUtilities util = new DriverUtilities();

	@After
	public static void afterScenario(io.cucumber.java.Scenario sc) throws Exception {
		// Attach jacketDetails text file to cucumber report
		String jacketDetailsFilePath = util.getProperty("jacketDetailsFilePath");
		byte[] fileContent = Files.readAllBytes(Paths.get(jacketDetailsFilePath));
		sc.attach(fileContent, "text/plain", "Jacket Details Text File");
	}

}
