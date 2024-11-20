package selenium;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class GmailIT {
    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        //System.setProperty("webdriver.chrome.driver", "/home/runner/work/Grupp1/Grupp1/drivers/chromedriver");
    }

    @BeforeEach
    public void setup() {
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--remote-allow-origins=*");
        option.addArguments("incognito");
        option.addArguments("--headless");
        option.addArguments("--no-sandbox");
        option.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(option);
    }

    @Test
    public void checkTitle() {
        try {
            // Navigate to Gmail
            driver.get("https://www.gmail.com");

            // Get the title of the page
            String title = driver.getTitle();

            // Assert that the title is correct
            assertEquals("Gmail", title);

            System.out.println("Test Passed! Title is correct: " + title);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
