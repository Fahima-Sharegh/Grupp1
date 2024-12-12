package steps;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
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
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {
    private final String URL = "https://webshop-agil-testautomatiserare.netlify.app/";
    private  WebDriverWait wait;
    private WebDriver driver;
    // Suppress Selenium warnings


    @Before //Written by Anders
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);
        driver = new ChromeDriver(options);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After //Written by Anders
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    //<editor-fold desc="Written by Carl-Johan">
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
    //</editor-fold>

    //<editor-fold desc="Written by Anders">
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
        driver.get("https://webshop-agil-testautomatiserare.netlify.app/checkout");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("li")));
    }

    @And("the cart is empty")
    public void theCartIsEmpty() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("cartSize"), ""));
    }

    @When("I navigate to the product page")
    public void iNavigateToTheProductPage() throws InterruptedException {
        driver.get("https://webshop-agil-testautomatiserare.netlify.app/products");
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

    @Then("the cart item counter on the product page should show {string} items")
    public void theCartCounterOnTheProductsPageShouldShow(String quantity) {
        assertEquals(quantity, driver.findElement(By.id("buttonSize")).getText());
    }

    @Then("the cart on the checkout page should have {string} items")
    public void theCartOnTheCheckoutPageShouldHaveItem(String quantity) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("cartSize"), quantity));
        assertEquals(quantity, driver.findElement(By.id("cartSize")).getText());
    }
    //</editor-fold>

    //<editor-fold desc="Written by Fahima">
    @When("user clicks on the {string} button")
    public void the_user_click_on_the_button(String string) throws InterruptedException {
        WebElement shopButton = driver.findElement(By.cssSelector("header[class='p-3 bg-dark text-white'] li:nth-child(2) a:nth-child(1)"));
        shopButton.click();
        Thread.sleep(1000);
    }

    @And("user clicks the {string} button")
    public void userClicksOnAllButton(String arg0) throws InterruptedException {
        WebElement allButton = driver.findElement(By.xpath("//a[normalize-space()='All']"));
        Thread.sleep(2000);
        allButton.click();
        Thread.sleep(2000);
    }

    @Then("all products should be visible")
    public void the_user_should_see_all_the_products() throws InterruptedException {
        WebElement allProducts = driver.findElement(By.xpath("//a[normalize-space()='All']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", new Object[]{allProducts});
        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "https://webshop-agil-testautomatiserare.netlify.app/products#";
        assertEquals(expectedUrl, currentUrl);
    }

    @Then("the user types {string} into the search bar and presses the {string} key")
    public void theUserTypesIntoTheSearchBarAndPressesTheKey(String productName, String enter) throws InterruptedException {
        WebElement searchButton = driver.findElement(By.cssSelector("#search"));
        searchButton.sendKeys("Mens Cotton Jacket");
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("shop"), "The user was navigated to the Shop page!");
    }

    @And("the user adds a product to the basket")
    public void theUserAddsToTheBasket() throws InterruptedException {
        Thread.sleep(2000);
        WebElement backPack = driver.findElement(By.xpath("/html/body/main/div[1]/div/div/button"));
        backPack.click();
    }

    @And("the user clicks on the Checkout button")
    public void theUserClicksOnTheButton() throws InterruptedException {
        WebElement checkCart = driver.findElement(By.xpath("//a[contains(text(),'\uD83D\uDED2 Checkout')]"));
        checkCart.click();
        Thread.sleep(2000);
    }

    @Then("the user removes the product from the shopping cart")
    public void theUserRemovesTheProductFromTheShoppingCart() throws InterruptedException {
        WebElement removeProduct = driver.findElement(By.xpath("//button[normalize-space()='Remove']"));
        removeProduct.click();
        Thread.sleep(2000);
        WebElement emptyCartMessage = driver.findElement(By.xpath("/html[1]/body[1]/header[1]/div[1]/div[1]/div[1]/a[1]"));
        Thread.sleep(2000);
        assertTrue(emptyCartMessage.isDisplayed(), "The cart is not empty after removing the product!");
        Thread.sleep(2000);
    }

    @And("the user clicks on the Continue to Checkout button")
    public void theUserClicksOnTheContinueButton() throws InterruptedException {
        WebElement continueButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);
        Thread.sleep(2000);
        continueButton.click();
    }

    @Then("the user should see the error message under each empty required fields")
    public void theUserShouldSeeTheErrorMessageUnderEachEmptyRequiredField() throws InterruptedException {

        WebElement firstNameError = driver.findElement(By.xpath("//div[normalize-space()='Valid first name is required.']"));
        WebElement lastNameError = driver.findElement(By.xpath("//div[normalize-space()='Valid last name is required.']"));
        WebElement emailError = driver.findElement(By.xpath("//div[normalize-space()='Please enter a valid email address for shipping updates.']"));
        WebElement addressError = driver.findElement(By.xpath("//div[normalize-space()='Please enter your shipping address.']"));
        WebElement countryError = driver.findElement(By.xpath("/html/body/main/div[2]/div[2]/form//div[normalize-space()='Please select a valid country.']"));
        WebElement cityError = driver.findElement(By.xpath("//div[normalize-space()='Please provide a valid state.']"));
        WebElement zipError = driver.findElement(By.xpath("//div[normalize-space()='Zip code required.']"));
        WebElement cardError = driver.findElement(By.xpath("//div[normalize-space()='Name on card is required']"));
        WebElement expirationDateError = driver.findElement(By.xpath("//div[normalize-space()='Expiration date required']"));

        String expectedFirstNameError = "Valid first name is required.";
        String expectedLastNameError = "Valid last name is required.";
        String expectedEmailError = "Please enter a valid email address for shipping updates.";
        String expectedAddressError = "Please enter your shipping address.";
        String expectedCountryError = "Please select a valid country.";
        String expectedCityError = "Please provide a valid state.";
        String expectedZipError = "Zip code required.";
        String expectedCardError = "Name on card is required";
        String expectedExpirationDateError = "Expiration date required";

        assertEquals(expectedFirstNameError, firstNameError.getText());
        assertEquals(expectedLastNameError, lastNameError.getText());
        assertEquals(expectedEmailError, emailError.getText());
        assertEquals(expectedAddressError, addressError.getText());
        assertEquals(expectedCountryError, countryError.getText());
        assertEquals(expectedCityError, cityError.getText());
        assertEquals(expectedZipError, zipError.getText());
        assertEquals(expectedCardError, cardError.getText());
        assertEquals(expectedExpirationDateError, expirationDateError.getText());
    }

    @And("the user fills in all required fields")
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
    }

    @Then("the user should be navigated to the Shop page")
    public void theUserShouldBeNavigatedToTheShopPage() {}

    //</editor-fold>

    //<editor-fold desc="Written by Baraa">
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
    //</editor-fold>
}
