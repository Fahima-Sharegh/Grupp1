package selenium;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TheShopTest {
    WebDriver driver;
    String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--remote-allow-origins=*");
        option.addArguments("incognito");
        option.addArguments("--headless");
        option.addArguments("--no-sandbox");
        option.addArguments("--disable-dev-shm-usage");

        // Disable logging for Google Chrome browser
        option.setCapability("goog:loggingPrefs", java.util.Map.of("browser", "OFF"));
        driver = new ChromeDriver(option);
        driver.get(URL);
    }

    @Test
    public void checkTitle() {
        assertEquals("Webbutiken", driver.getTitle());
        driver.quit();
    }

    @Test
    public void checkAllProductsButton() {
        assertEquals("All products", driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/button")).getText());
    }
}
