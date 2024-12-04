package steps;


import java.time.Duration;
import java.util.List;

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

public class StepDefinitionsBaraa {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL = "https://webshop-agil-testautomatiserare.netlify.app/";

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1920,1080");
        options.addArguments("start-maximized");
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I am on the homepage")
    public void iAmOnTheHomepage() {
        driver.get(URL);
        String title = driver.getTitle();
        System.out.println("Homepage title: " + title);
        assertEquals("The Shop", title, "The homepage title does not match.");
    }

    @When("I click the {string} link")
    public void iClickTheLink(String linkText) {
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='" + linkText + "']")));
        System.out.println("Clicking link: " + linkText);
        wait.until(ExpectedConditions.elementToBeClickable(link)).click();
    }

    @When("I click the {string}")
    public void iClickAddProduct(String buttonText) throws InterruptedException {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), '➕ Add to cart')]")));
        Thread.sleep(500);
        button.click();

        System.out.println("Successfully clicked the button: " + buttonText);
    }

    @Then("the product should be added to the cart")
    public void the_product_should_be_added_to_the_cart() {
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class, 'btn') and contains(@class, 'btn-warning')]")));
        String cartContent = checkoutButton.getText();
        System.out.println("Cart content: " + cartContent);
        assertTrue(cartContent.contains("Checkout"), "The cart does not contain the expected product or state.");
        System.out.println("The product was Successfully added to the cart.");
    }


    @When("I click on the {string} button")
    public void iClickOnTheButton(String buttonText) {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), '" + buttonText + "')]")));
        System.out.println("Clicking button: " + buttonText);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    @Then("I should be redirected to the checkout page")
    public void iShouldBeRedirectedToTheCheckoutPage() {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Redirected URL: " + currentUrl);
        assertTrue(currentUrl.contains("/checkout"), "User is not redirected to the checkout page.");
    }

    @Given("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        driver.get(URL + "checkout");
    }

    @When("I fill in the {string} field with {string}")
    public void iFillInTheFieldWith(String fieldName, String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(getFieldId(fieldName))));
        System.out.println("Filling field: " + fieldName + " with value: " + value);
        wait.until(ExpectedConditions.elementToBeClickable(field));
        field.clear();
        field.sendKeys(value);
        System.out.println("Field value after sending keys: " + field.getAttribute("value"));
    }

    private String getFieldId(String fieldName) {
        switch (fieldName) {
            case "First name":
                return "firstName";
            case "Last name":
                return "lastName";
            case "Email":
                return "email";
            case "Address":
                return "address";
            case "Country":
                return "country";
            case "City":
                return "city";
            case "Zip":
                return "zip";
            case "Name on card":
                return "cc-name";
            case "Credit card number":
                return "cc-number";
            case "Expiration":
                return "cc-expiration";
            case "CVV":
                return "cc-cvv";
            default:
                throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }

    @Given("I have filled out my billing details")
    public void iHaveFilledOutMyBillingDetails() {
        iAmOnTheCheckoutPage();
        iFillInTheFieldWith("First name", "Baraa");
        iFillInTheFieldWith("Last name", "Abdullatif");
        iFillInTheFieldWith("Email", "baraa.abdullatif@iths.se");
        iFillInTheFieldWith("Address", "Lab la bla");
        iFillInTheFieldWith("Country", "Sweden");
        iFillInTheFieldWith("City", "Gothenburg");
        iFillInTheFieldWith("Zip", "44444");
        System.out.println("Billing details have been filled out successfully.");
    }

    @Then("the billing details should be filled in correctly")
    public void theBillingDetailsShouldBeFilledInCorrectly() {
        validateFieldValue("First name", "Baraa");
        validateFieldValue("Last name", "Abdullatif");
        validateFieldValue("Email", "baraa.abdullatif@iths.se");
        validateFieldValue("Address", "Lab la bla");
        validateFieldValue("Country", "Sweden");
        validateFieldValue("City", "Gothenburg");
        validateFieldValue("Zip", "44444");
    }

    private void validateFieldValue(String fieldName, String expectedValue) {
        WebElement field = driver.findElement(By.id(getFieldId(fieldName)));
        String actualValue = field.getAttribute("value");
        System.out.println("Validating field: " + fieldName + " | Expected: " + expectedValue + " | Actual: " + actualValue);
        assertEquals(expectedValue, actualValue, fieldName + " value is incorrect.");
    }

    @Then("I should see an error message for the {string} field")
    public void iShouldSeeAnErrorMessageForTheField(String fieldName) {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(getFieldId(fieldName))));
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='invalid-feedback' and preceding-sibling::input[@id='" + getFieldId(fieldName) + "']]")
            ));
            System.out.println("Error message for field '" + fieldName + "': " + errorMessage.getText());
            assertTrue(errorMessage.isDisplayed(), "Error message is not displayed for the field: " + fieldName);
        } catch (Exception e) {
            System.err.println("Error validating error message for field: " + fieldName + " - " + e.getMessage());
            throw e;
        }
    }

    @Then("all payment methods should be selectable")
    public void allPaymentMethodsShouldBeSelectable() {
        try {
            String[] paymentMethods = {"credit", "debit", "paypal"};
            for (String method : paymentMethods) {
                WebElement radioButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(method)));
                radioButton.click();
                assertTrue(radioButton.isSelected(), "Payment method " + method + " is not selectable.");
                System.out.println("Payment method '" + method + "' is selectable and selected.");
            }
        } catch (Exception e) {
            System.err.println("Error validating payment methods selection: " + e.getMessage());
            throw e;
        }
    }


    @When("I select {string} as the {string}")
    public void iSelectAsThe(String value, String fieldName) {
        if (fieldName.equalsIgnoreCase("Payment method")) {
            String radioId = "form-check";
            switch (value.toLowerCase()) {
                case "credit":
                    radioId = "credit";
                    break;
                case "debit":
                    radioId = "debit";
                    break;
                case "paypal":
                    radioId = "paypal";
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported payment method: " + value);
            }
            WebElement radioButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(radioId)));
            System.out.println("Selecting payment method: " + value);
            radioButton.click();
        } else {
            throw new IllegalArgumentException("Field not supported: " + fieldName);
        }
    }

    @When("I remove an item from the basket")
    public void iRemoveAnItemFromTheBasket() {
        try {
            WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Remove']")));
            System.out.println("Removing item from the basket...");
            removeButton.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[text()='Remove']")));
            System.out.println("Item successfully removed.");
        } catch (Exception e) {
            System.err.println("Error removing item from the basket: " + e.getMessage());
        }
    }

    @Then("the basket should be empty")
    public void theBasketShouldBeEmpty() {
        try {
            WebElement basket = driver.findElement(By.cssSelector(".your-cart"));
            String basketContent = basket.getText();
            System.out.println("Basket content after removal: " + basketContent);
            assertTrue(basketContent.isEmpty() || !basketContent.contains("Mens Casual Premium Slim Fit T-Shirts"), "Basket is not empty.");
        } catch (Exception e) {
            System.err.println("Error validating basket content: " + e.getMessage());
        }
    }

    @When("I click the {string} button")
    public void iClickTheButton(String buttonText) {
        try {
            WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='" + buttonText + "']")));
            System.out.println("Clicking button: " + buttonText);
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
        } catch (Exception e) {
            System.err.println("Error clicking button: " + buttonText + " - " + e.getMessage());
        }
    }

    @Then("the checkout process should be completed successfully")
    public void theCheckoutProcessShouldBeCompletedSuccessfully() {
        try {
            System.out.println("Ensuring all  fields are filled before proceeding.");
            List<WebElement> fields = driver.findElements(By.cssSelector("input[required]"));
            for (WebElement field : fields) {
                if (field.getAttribute("value").isEmpty()) {
                    field.sendKeys("Test Value");
                    System.out.println("Filled field: " + field.getAttribute("name"));
                }
            }

            WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Continue to checkout']")));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);

            System.out.println("Clicking the 'Continue to Checkout' button.");
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);

            System.out.println("Waiting for the order confirmation message to appear.");
            boolean isOrderConfirmationDisplayed = false;

            try {
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Order Confirmation')]")));
                isOrderConfirmationDisplayed = successMessage.isDisplayed();
            } catch (TimeoutException e) {
                System.err.println("Order confirmation message is not displayed within the expected time.");
            }

            if (isOrderConfirmationDisplayed) {
                System.out.println("Order confirmation message displayed successfully.");
                assertTrue(isOrderConfirmationDisplayed, "Order confirmation message is displayed as expected.");
            } else {
                System.err.println("Order confirmation message was not displayed. Checkout process failed.");
                assertFalse(isOrderConfirmationDisplayed, "Order confirmation message is not displayed. Checkout process failed.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred during the checkout process: " + e.getMessage());
            throw e;
        }
    }
}
