package selenium;

import io.cucumber.java.BeforeAll;
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
    WebDriver driver;
    String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--remote-allow-origins=*");
        option.addArguments("incognito");
        option.addArguments("--headless");
        option.addArguments("--no-sandbox");
        option.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(option);
    }

    @Given("I open the web shop")
    public void iOpenTheWebShop() {
        driver.get(URL);
    }

    @Then("the title should be {string}")
    public void theTitleShouldBe(String expectedTitle) {
        assertEquals(expectedTitle, driver.getTitle());
        driver.quit();
    }

    @Then("the {string} button should be visible")
    public void theButtonShouldBeVisible(String buttonText) {
        String actualText = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/button")).getText();
        assertEquals(buttonText, actualText);
        driver.quit();
    }
}
