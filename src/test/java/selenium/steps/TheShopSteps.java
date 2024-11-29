package selenium.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.produktapi.ProduktapiApplication;
import io.github.bonigarcia.wdm.WebDriverManager;

@CucumberContextConfiguration
@SpringBootTest(classes = ProduktapiApplication.class)
public class TheShopSteps {

    private WebDriver driver;
    private String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @Given("I open the web shop")
    public void iOpenTheWebShop() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("incognito");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get(URL);
    }

    @When("I check the page title")
    public void iCheckThePageTitle() {
        // This step can be empty or perform some action
    }

    @Then("the title should be {string}")
    public void theTitleShouldBe(String expectedTitle) {
        String actualTitle = driver.getTitle();
        Assertions.assertEquals(expectedTitle, actualTitle, "Page title does not match!");
        driver.quit();
    }

    @When("I look for the {string} button")
    public void iLookForTheButton(String buttonText) {
        // This step can be empty or perform some action
    }

    @Then("the {string} button should be visible")
    public void theButtonShouldBeVisible(String buttonText) {
        String actualText = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/button")).getText();
        Assertions.assertEquals(buttonText, actualText, "Button text does not match!");
        driver.quit();
    }
}
