package steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitionsAnders {
    private WebDriver driver;
    final private String productPage = "https://webshop-agil-testautomatiserare.netlify.app/products";;
    final private String cartPage = "https://webshop-agil-testautomatiserare.netlify.app/checkout";
    private WebDriverWait wait;


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
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("I am on the page {string}") //Written by Anders
    public void navigateToHomePage(String homepageURL) {
        driver.get(homepageURL);
    }

    @Then("I should not get an HTTP response error for any of the links") //Written by Anders
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

    @Given("I navigate to the checkout page")
    public void iNavigateToTheCheckoutPage() throws InterruptedException {
        driver.get(cartPage);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("li")));
        Thread.sleep(1000);
    }

    @And("the cart is empty")
    public void theCartIsEmpty() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("cartSize"), ""));
    }

    @When("I navigate to the product page")
    public void iNavigateToTheProductPage() throws InterruptedException {
        driver.get(productPage);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("col")));
    }

    @And("I add up to {int} products to the cart")
    public void iAddOneProductToTheCart(int quantity) throws InterruptedException {
        List<WebElement> addToCartButtons = driver.findElements(By.className("col"))
                .stream()
                .map(col -> col.findElement(By.tagName("button")))
                .toList();
        int currentCartCount = driver.findElement(By.id("buttonSize")).getText().isEmpty()
                ? 0 : Integer.parseInt(driver.findElement(By.id("buttonSize")).getText());
        for (int i = 0; i < quantity - currentCartCount; i++) {
            WebElement button = addToCartButtons.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("buttonSize"), Integer.toString(currentCartCount + i + 1)));
        }
    }

    @Then("the cart item counter on the products page should show {string} items")
    public void theCartCounterOnTheProductsPageShouldShow(String quantity) {
        assertEquals(quantity, driver.findElement(By.id("buttonSize")).getText());
    }

    @Then("the cart on the checkout page should have {string} items")
    public void theCartOnTheCheckoutPageShouldHaveItem(String quantity) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("cartSize"), quantity));
        assertEquals(quantity, driver.findElement(By.id("cartSize")).getText());
    }
}