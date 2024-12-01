package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

public class IssuesFromCarl {
    private WebDriver driver;
    private final String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("incognito");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("the user navigates to the shop homepage")
    public void navigateToHomepage() {
        driver.get(URL);
    }

    @Then("the page title should be {string}")
    public void verifyPageTitle(String expectedTitle) {
        assertEquals(expectedTitle, driver.getTitle());
    }

    @When("the user clicks on the {string} link")
    public void clickOnLink(String linkText) {
        WebElement link = driver.findElement(By.linkText(linkText));
        link.click();
    }

    @Then("the page route should be {string}")
    public void verifyPageRoute(String expectedRoute) {
        String currentUrl = driver.getCurrentUrl();
        String currentRoute = currentUrl.replace(URL, "");
        assertEquals(expectedRoute, currentRoute);
    }

    @When("the user searches for {string}")
    public void searchForProduct(String productName) {
        // Navigate to the shop homepage
        driver.get(URL+"products");

        By searchBoxLocator = By.id("search");

        // First, locate the WebElement using the locator
        WebElement searchBox = driver.findElement(searchBoxLocator);
        
        // Then, use `sendKeys` on the WebElement
        searchBox.sendKeys(productName + Keys.RETURN);
    }

    @Then("the search results should display {string}")
    public void verifySearchResults(String productName) {
        // Initialize WebDriverWait with a 10-second timeout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    
        // Define a precise XPath to locate the <h3> element with the product name
        String xpathExpression = "//h3[contains(@class, 'card-title') and contains(text(), '" + productName + "')]";
    
        // Wait until the <h3> element is visible
        WebElement searchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression)));
        
        // Assert that the element is displayed
        assertTrue(searchResult.isDisplayed(), "Search result not displayed for: " + productName);
    }
}
