package com.logwire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.logwire.pages.CartPage;
import com.logwire.pages.CheckoutPage;
import com.logwire.pages.InventoryPage;
import com.logwire.pages.LoginPage;

@Tag("panier")
public class CartPageTest {
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
                // Spécification du répertoire de données utilisateur unique pour éviter le conflit
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");  // Spécifier un répertoire unique
                driver = new ChromeDriver(options);
                break;
            
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                // Option par défaut : utiliser Chrome avec un répertoire de données utilisateur unique
                options = new ChromeOptions();
                options.addArguments("--user-data-dir=/tmp/chrome-user-data");  // Spécifier un répertoire unique
                driver = new ChromeDriver(options); 
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

    @Tag("ajoutProduitsCartTest")
    @Test
    public void ajoutProduitsCartTest(){
        inventoryPage.addProduct();
        List<String> addedProductsNames = inventoryPage.getAddedProduct();
        inventoryPage.clickPanier();
        assertEquals(addedProductsNames, cartPage.getAddedProduct());
    }

    @Tag("continueShoppingTest")
    @Test
    public void continueShoppingTest(){
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        List<String> cartProducts = cartPage.getAddedProduct();
        cartPage.continueShopping();
        assertEquals(cartProducts, inventoryPage.getAddedProduct());
    }

    @Tag("valideCheckoutTest")
    @Test
    public void valideCheckoutTest(){
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        cartPage.checkout();
        assertTrue(checkoutPage.getChekoutInfo().isDisplayed());
    }

    @Tag("invalideCheckoutTest")
    @Test
    public void invalideCheckoutTest(){
        inventoryPage.clickPanier();
        cartPage.checkout();
        assertFalse(checkoutPage.getChekoutInfo().isDisplayed());
    }
}
