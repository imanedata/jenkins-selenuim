package com.logwire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.logwire.pages.CartPage;
import com.logwire.pages.CheckoutPage;
import com.logwire.pages.InventoryPage;
import com.logwire.pages.LoginPage;

@Tag("checkout")
public class ChekoutPageTest {
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    @BeforeEach
    public void setUp(){
        String browser = System.getProperty("browser","chrome");

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                //ChromeOptions options = new ChromeOptions();
                //options.addArguments("--headless=new");
                break;
            
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                driver = new ChromeDriver(); 
                //ChromeOptions options = new ChromeOptions();
                //options.addArguments("--headless=new");
                break;
        }
 
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://www.saucedemo.com/");
        loginPage.login("standard_user", "secret_sauce");


    }

    @AfterEach
    public void tearDown(){
        if (driver != null){
            driver.close();
            driver.quit();
            driver = null;
        }
    }

    @Tag("valideContinuTest")
    @Test
    public void valideCheckoutTest(){
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        cartPage.checkout();
        checkoutPage.saisirFirstName("user");
        checkoutPage.saisirLastName("user");
        checkoutPage.saisirZipCode("200");
        checkoutPage.continueClick();
        assertTrue(true);
    }

    @Tag("invalidContinuTest")
    @ParameterizedTest
    @CsvFileSource(resources = "data/invalideCheckoutInfo.csv", numLinesToSkip = 1)
    public void invalidContinuTest(String firstName, String lastName, String zipCode){
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        cartPage.checkout();
        checkoutPage.saisirFirstName(firstName);
        checkoutPage.saisirLastName(lastName);
        checkoutPage.saisirZipCode(zipCode);
        checkoutPage.continueClick();
        String classe;
        try {
            classe = checkoutPage.getErrorElement().getDomAttribute("class");
        } catch (Exception e) {
            // TODO: handle exception
            classe = null;
        }
        assertEquals("error-message-container error", classe);
    }
}
