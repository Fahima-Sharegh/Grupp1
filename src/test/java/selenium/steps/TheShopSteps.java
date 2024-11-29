package selenium.steps;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;

@CucumberContextConfiguration
@SpringBootTest
public class TheShopSteps {

    private WebDriver driver;
    private static final String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @Given("I open the web shop")
    public void iOpenTheWebShop() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options for headless testing
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--headless",
            "--no-sandbox", 
            "--disable-dev-shm-usage", 
            "--remote-allow-origins=*",
            "incognito"
        );
        
        // Initialize the driver
        driver = new ChromeDriver(options);
        driver.get(URL);
    }

    @When("I check the page title")
    public void iCheckThePageTitle() {
        // This step can remain empty if it's just a placeholder
    }

    @Then("the title should be {string}")
    public void theTitleShouldBe(String expectedTitle) {
        String actualTitle = driver.getTitle();
        Assertions.assertEquals(expectedTitle, actualTitle, "Page title does not match!");
    }

    @When("I look for the {string} button")
    public void iLookForTheButton(String buttonText) {
        // This step can remain empty if it's just a placeholder
    }

    @Then("the {string} button should be visible")
    public void theButtonShouldBeVisible(String buttonText) {
        String actualText = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/button")).getText();
        Assertions.assertEquals(buttonText, actualText, "Button text does not match!");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}