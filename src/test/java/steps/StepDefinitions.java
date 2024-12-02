package steps;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class StepDefinitions {
    private WebDriver driver;
    private final String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("incognito");
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

    @When("the user clicks on the {string} button")
    public void clickOnButton(String buttonText) {
        // Locate the button by its visible text and class
        WebElement button = driver.findElement(By.xpath("//a[contains(@class, 'btn') and contains(text(), '" + buttonText + "')]"));
        button.click();
    }

    @Then("the page route should be {string}")
    public void verifyPageRoute(String expectedRoute) {
        // Get the current URL
        String currentUrl = driver.getCurrentUrl();
        // Extract the route by removing the base URL
        String currentRoute = currentUrl.replace(URL, "");
        // Verify the extracted route matches the expected route
        assertEquals(expectedRoute, currentRoute, "The page route did not match the expected route.");
    }

    @When("the user searches for {string}")
    public void searchForProduct(String productName) {
        driver.get(URL + "products");

        By searchBoxLocator = By.id("search");
        WebElement searchBox = driver.findElement(searchBoxLocator);
        searchBox.sendKeys(productName + Keys.RETURN);
    }

    @Then("the search results should display {string}")
    public void verifySearchResults(String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String xpathExpression = "//h3[contains(@class, 'card-title') and contains(text(), '" + productName + "')]";
        WebElement searchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression)));
        assertTrue(searchResult.isDisplayed(), "Search result not displayed for: " + productName);
    }
}
