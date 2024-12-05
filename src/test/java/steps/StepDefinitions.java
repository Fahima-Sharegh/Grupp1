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

import io.cucumber.java.en.And;
import org.openqa.selenium.*;
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
                    if (attr == null) {
                        return null;
                    }
                    if (attr.contains("\"http")) {
                        return baseURL + attr.split("\"")[1];
                    } else if (attr.contains("'http")) {
                        return baseURL + attr.split("'")[1];
                    } else if (attr.contains(".html'")) {
                        return baseURL + attr.split("'")[1];
                    } else if (attr.contains(".html\"")) {
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
            } else
                System.out.println("\u001B[32mOK! (" + responseCode + ")\u001B[0m");
        }
        if (testFail) {
            fail("There were broken links on the page");
        }
    }
        @Given("user navigates to the shop homepage")
        public void the_user_navigates_to_the_shop_homepage() {
            driver.manage().window().maximize();
            driver.get(URL);
        }

        @When("user clicks on the {string} button")
        public void the_user_click_on_the_button(String string) throws InterruptedException {
            WebElement shopButton = driver.findElement(By.cssSelector("header[class='p-3 bg-dark text-white'] li:nth-child(2) a:nth-child(1)"));
            shopButton.click();
            Thread.sleep(1000);
    } @And("user clicks the {string} button")
    public void userClicksOnAllButton(String arg0) throws InterruptedException {
        WebElement allButton = driver.findElement(By.xpath("//a[normalize-space()='All']"));
        Thread.sleep(2000);
        allButton.click();
        Thread.sleep(2000);
    }

    @Then("all products should be visible")
    public void the_user_should_see_all_the_products() throws InterruptedException {
        WebElement allProducts = driver.findElement(By.xpath("//a[normalize-space()='All']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", new Object[]{allProducts});
        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "https://webshop-agil-testautomatiserare.netlify.app/products#";
        assertEquals(expectedUrl, currentUrl);
    }

    @Then("the user types {string} into the search bar and presses the {string} key")
    public void theUserTypesIntoTheSearchBarAndPressesTheKey(String productName, String enter) throws InterruptedException {
        WebElement searchButton = driver.findElement(By.cssSelector("#search"));
        searchButton.sendKeys("Mens Cotton Jacket");
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("shop"), "The user was navigated to the Shop page!");

    }

    @When("the user adds {string} to the basket")
    public void theUserAddsToTheBasket(String product) throws InterruptedException {

        Thread.sleep(2000);
        WebElement backPack = driver.findElement(By.xpath("/html/body/main/div[1]/div/div/button"));
        backPack.click();
    }

    @And("the user clicks on the Checkout button")
    public void theUserClicksOnTheButton() throws InterruptedException {
        WebElement checkCart = driver.findElement(By.xpath("//a[contains(text(),'\uD83D\uDED2 Checkout')]"));
        checkCart.click();
        Thread.sleep(2000);

    }

    @And("the user removes the product from the shopping cart")
    public void theUserRemovesTheProductFromTheShoppingCart() throws InterruptedException {
        WebElement removeProduct = driver.findElement(By.xpath("//button[normalize-space()='Remove']"));
        removeProduct.click();
        Thread.sleep(2000);
        WebElement emptyCartMessage = driver.findElement(By.xpath("/html[1]/body[1]/header[1]/div[1]/div[1]/div[1]/a[1]"));
        Thread.sleep(2000);
        assertTrue(emptyCartMessage.isDisplayed(), "The cart is not empty after removing the product!");
        Thread.sleep(2000);
    }

    @And("the user clicks on the Continue to Continue button")
    public void theUserClicksOnTheContinueButton() throws InterruptedException {
        WebElement continueButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);
        Thread.sleep(2000);
        continueButton.click();
    }

    @Then("the user should see the error message under each empty required fields")
    public void theUserShouldSeeTheErrorMessageUnderEachEmptyRequiredField() throws InterruptedException {
        WebElement errorElement = driver.findElement(By.xpath("/html/body/main/div[2]/div[2]/form"));
        Thread.sleep(2000);
        String expectedErrorMessage = "Valid first name is required.";
        Thread.sleep(2000);
        assertEquals("Valid first name is required.", expectedErrorMessage, errorElement.getText());
        Thread.sleep(2000);


    }

    @When("the user fills in all required fields")
    public void fillInRequiredFields() throws InterruptedException {
        driver.findElement(By.id("firstName")).sendKeys("Fahima");
        driver.findElement(By.id("lastName")).sendKeys("Sharegh");
        driver.findElement(By.id("email")).sendKeys("fahima.sharegh@iths.se");
        driver.findElement(By.id("address")).sendKeys("Dengata 123");
        driver.findElement(By.id("country")).sendKeys("Sweden");
        driver.findElement(By.id("city")).sendKeys("Stockholm");
        driver.findElement(By.id("zip")).sendKeys("14321");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement payment = driver.findElement(By.xpath("//label[normalize-space()='Debit card']"));
        js.executeScript("arguments[0].click();", payment);
        driver.findElement(By.id("cc-name")).sendKeys("what");
        driver.findElement(By.id("cc-number")).sendKeys("1432177777");
        driver.findElement(By.id("cc-expiration")).sendKeys("26/26");
        driver.findElement(By.id("cc-cvv")).sendKeys("123");
        Thread.sleep(2000);
        //String expectedUrl = "https://webshop-agil-testautomatiserare.netlify.app/checkout?paymentMethod=on";
        // assertEquals(expectedUrl, driver.getCurrentUrl());
    }

    @Then("the user ended up in the same page")
    public void endedUpInSamePage() throws InterruptedException {
        String expectedUrl = "https://webshop-agil-testautomatiserare.netlify.app/checkout?paymentMethod=on";
        assertEquals(expectedUrl, driver.getCurrentUrl());

    }

}
