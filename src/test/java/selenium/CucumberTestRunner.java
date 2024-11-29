package selenium;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features", // Path to feature files
    glue = "selenium", // Package containing step definitions
    plugin = {"pretty", "html:target/cucumber-reports.html"} // Generate reports
)
public class CucumberTestRunner {
}
