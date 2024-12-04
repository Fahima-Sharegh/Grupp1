package steps;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {
    private WebDriver driver;
    private final String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
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



    @Given("I am on the page {string}")
    public void navigateToHomePage(String homepageURL) {
        driver.get(homepageURL);
    }

    @Then("I should not get an HTTP response error for any of the links")
    public void iClickOnEachOfTheLinksOnThePage() throws IOException, InterruptedException {

        // Get urls from a-elements
        List<String> urls = new ArrayList<>(driver
                .findElements(By.tagName("a"))
                .stream()
                .map(element -> element.getAttribute("href"))
                .toList());

        // Add urls from buttons with onclick event
        String baseURL = driver.getCurrentUrl();
        urls.addAll(driver
                .findElements(By.tagName("button"))
                .stream()
                .map(button -> button.getAttribute("onclick"))
                .map(attr -> {
                    if (attr == null) { return null; }
                    if (attr.contains("\"http")) {
                        return baseURL + attr.split("\"")[1];
                    }
                    else if (attr.contains("'http")) {
                        return baseURL + attr.split("'")[1];
                    }
                    else if (attr.contains(".html'")) {
                        return baseURL + attr.split("'")[1];
                    }
                    else if (attr.contains(".html\"")) {
                        return baseURL + attr.split("\"")[1];
                    }
                    return null;
                }).toList());

        boolean testFail = false;
        for (String url : urls) {
            if (url == null || !url.startsWith("http")) {
                continue;
            }
            System.out.print("Testing link:\t" + url + "\t");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();
            if ((responseCode < 200 || responseCode > 399) && responseCode != 999) {
                testFail = true;
                System.out.println("\u001B[31mFAIL! (" + responseCode + ")\u001B[0m");
            }
            else
                System.out.println("\u001B[32mOK! (" + responseCode + ")\u001B[0m");
        }
        if (testFail) {
            fail("There were broken links on the page");
        }
    }
}
