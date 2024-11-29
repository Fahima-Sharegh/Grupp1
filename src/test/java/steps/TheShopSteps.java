package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheShopSteps {
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

    @Then("the 'All products' button text should be {string}")
    public void verifyAllProductsButtonText(String expectedText) {
        String actualText = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/button")).getText();
        assertEquals(expectedText, actualText);
    }
}
