package selenium;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.produktapi.ProduktapiApplication;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ProduktapiApplication.class)
@CucumberContextConfiguration
public class TheShopSteps {

    WebDriver driver;
    String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

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
