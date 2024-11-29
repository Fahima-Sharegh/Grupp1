// File: src/test/java/selenium/CucumberIT.java
package selenium;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // Path to feature files relative to resources
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "selenium.steps") // Package containing step definitions
public class CucumberIT {
    // This class remains empty. It serves as an entry point for Cucumber tests.
}
