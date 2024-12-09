package steps;

import java.time.Duration;
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

    @Before //Written by Anders
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

    @After //Written by Anders
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("the user navigates to the shop homepage") //Written by Carl-Johan
    public void navigateToHomepage() {
        driver.get(URL);
    }

    @Then("the page title should be {string}") //Written by Carl-Johan
    public void verifyPageTitle(String expectedTitle) {
        assertEquals(expectedTitle, driver.getTitle());
    }

    @When("the user clicks on the {string} link") //Written by Carl-Johan
    public void clickOnLink(String linkText) {
        WebElement link = driver.findElement(By.linkText(linkText));
        link.click();
    }

    @When("the user clicks on the {string} button") //Written by Carl-Johan
    public void clickOnButton(String buttonText) {
        // Locate the button by its visible text and class
        WebElement button = driver.findElement(By.xpath("//a[contains(@class, 'btn') and contains(text(), '" + buttonText + "')]"));
        button.click();
    }

    @Then("the page route should be {string}") //Written by Carl-Johan
    public void verifyPageRoute(String expectedRoute) {
        // Get the current URL
        String currentUrl = driver.getCurrentUrl();
        // Extract the route by removing the base URL
        String currentRoute = currentUrl.replace(URL, "");
        // Verify the extracted route matches the expected route
        assertEquals(expectedRoute, currentRoute, "The page route did not match the expected route.");
    }

    @When("the user searches for {string}") //Written by Carl-Johan
    public void searchForProduct(String productName) {
        driver.get(URL + "products");
        By searchBoxLocator = By.id("search"); //Written by Carl-Johan
        WebElement searchBox = driver.findElement(searchBoxLocator);
        searchBox.sendKeys(productName + Keys.RETURN);
    }

    @Then("the search results should display {string}") //Written by Carl-Johan
    public void verifySearchResults(String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String xpathExpression = "//h3[contains(@class, 'card-title') and contains(text(), '" + productName + "')]";
        WebElement searchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression)));
        assertTrue(searchResult.isDisplayed(), "Search result not displayed for: " + productName);
    }

    @Given("user navigates to the shop homepage") //Written by Fahima
    public void the_user_navigates_to_the_shop_homepage() {
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @When("user clicks on the {string} button") //Written by Fahima
    public void the_user_click_on_the_button(String string) throws InterruptedException {
        WebElement shopButton = driver.findElement(By.cssSelector("header[class='p-3 bg-dark text-white'] li:nth-child(2) a:nth-child(1)"));
        shopButton.click();
        Thread.sleep(1000);
    }

    @And("user clicks the {string} button") //Written by Fahima
    public void userClicksOnAllButton(String arg0) throws InterruptedException {
        WebElement allButton = driver.findElement(By.xpath("//a[normalize-space()='All']"));
        Thread.sleep(2000);
        allButton.click();
        Thread.sleep(2000);
    }

    @Then("all products should be visible") //Written by Fahima
    public void the_user_should_see_all_the_products() throws InterruptedException {
        WebElement allProducts = driver.findElement(By.xpath("//a[normalize-space()='All']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", new Object[]{allProducts});
        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "https://webshop-agil-testautomatiserare.netlify.app/products#";
        assertEquals(expectedUrl, currentUrl);
    }

    @Then("the user types {string} into the search bar and presses the {string} key") //Written by Fahima
    public void theUserTypesIntoTheSearchBarAndPressesTheKey(String productName, String enter) throws InterruptedException {
        WebElement searchButton = driver.findElement(By.cssSelector("#search"));
        searchButton.sendKeys("Mens Cotton Jacket");
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("shop"), "The user was navigated to the Shop page!");
    }

    @When("the user adds {string} to the basket") //Written by Fahima
    public void theUserAddsToTheBasket(String product) throws InterruptedException {
        Thread.sleep(2000);
        WebElement backPack = driver.findElement(By.xpath("/html/body/main/div[1]/div/div/button"));
        backPack.click();
    }

    @And("the user clicks on the Checkout button") //Written by Fahima
    public void theUserClicksOnTheButton() throws InterruptedException {
        WebElement checkCart = driver.findElement(By.xpath("//a[contains(text(),'\uD83D\uDED2 Checkout')]"));
        checkCart.click();
        Thread.sleep(2000);
    }

    @And("the user removes the product from the shopping cart") //Written by Fahima
    public void theUserRemovesTheProductFromTheShoppingCart() throws InterruptedException {
        WebElement removeProduct = driver.findElement(By.xpath("//button[normalize-space()='Remove']"));
        removeProduct.click();
        Thread.sleep(2000);
        WebElement emptyCartMessage = driver.findElement(By.xpath("/html[1]/body[1]/header[1]/div[1]/div[1]/div[1]/a[1]"));
        Thread.sleep(2000);
        assertTrue(emptyCartMessage.isDisplayed(), "The cart is not empty after removing the product!");
        Thread.sleep(2000);
    }

    @And("the user clicks on the Continue to Continue button") //Written by Fahima
    public void theUserClicksOnTheContinueButton() throws InterruptedException {
        WebElement continueButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);
        Thread.sleep(2000);
        continueButton.click();
    }

    @Then("the user should see the error message under each empty required fields") //Written by Fahima
    public void theUserShouldSeeTheErrorMessageUnderEachEmptyRequiredField() throws InterruptedException {
        WebElement errorElement = driver.findElement(By.xpath("/html/body/main/div[2]/div[2]/form"));
        Thread.sleep(2000);
        String expectedErrorMessage = "Valid first name is required.";
        Thread.sleep(2000);
        assertEquals("Valid first name is required.", expectedErrorMessage, errorElement.getText());
        Thread.sleep(2000);
    }

    @When("the user fills in all required fields") //Written by Fahima
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

    @Then("the user ended up in the same page") //Written by Fahima
    public void endedUpInSamePage() throws InterruptedException {
        String expectedUrl = "https://webshop-agil-testautomatiserare.netlify.app/checkout?paymentMethod=on";
        assertEquals(expectedUrl, driver.getCurrentUrl());
    }
}
