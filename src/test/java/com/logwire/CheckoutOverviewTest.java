package com.logwire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.logwire.pages.CartPage;
import com.logwire.pages.CheckoutFinishPage;
import com.logwire.pages.CheckoutOverviewPage;
import com.logwire.pages.CheckoutPage;
import com.logwire.pages.InventoryPage;
import com.logwire.pages.LoginPage;

@Tag("checkoutoverview")
public class CheckoutOverviewTest {
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CheckoutOverviewPage checkoutOverviewPage;
    CheckoutFinishPage checkoutFinishPage;

    @BeforeEach
    public void setUp(){
        String browser = System.getProperty("browser","chrome");

        switch (browser.toLowerCase()) {
            case "chrome":
                // Spécification du répertoire de données utilisateur unique pour éviter le conflit
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--user-data-dir=/tmp/chrome_" + System.nanoTime());  // Spécifier un répertoire unique
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
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
        checkoutFinishPage = new CheckoutFinishPage(driver);

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

    @Tag("sommeHorsTaxeTest")
    @Test
    public void sommeHorsTaxeTest(){
        Float totalCalcule = 0.0f;
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        cartPage.checkout();
        checkoutPage.saisirFirstName("first");
        checkoutPage.saisirLastName("last");
        checkoutPage.saisirZipCode("100");
        checkoutPage.continueClick();
        for (WebElement prix : checkoutOverviewPage.getPrixUnitaire()) {
            totalCalcule += Float.valueOf(prix.getText().replaceAll("[^0-9.]", ""));
        }
        assertEquals(totalCalcule, Float.valueOf(checkoutOverviewPage.getHtTotal().getText().replaceAll("[^0-9.]", "")));
        
    }

    @Tag("sommeTTCTest")
    @Test
    public void sommeTTCTest(){
        Float totalCalcule = 0.0f;
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        cartPage.checkout();
        checkoutPage.saisirFirstName("first");
        checkoutPage.saisirLastName("last");
        checkoutPage.saisirZipCode("100");
        checkoutPage.continueClick();
        for (WebElement prix : checkoutOverviewPage.getPrixUnitaire()) {
            totalCalcule += Float.valueOf(prix.getText().replaceAll("[^0-9.]", ""));
        }
        totalCalcule += Float.valueOf(checkoutOverviewPage.getTaxe().getText().replaceAll("[^0-9.]", ""));
        assertEquals(totalCalcule, Float.valueOf(checkoutOverviewPage.getTTC().getText().replaceAll("[^0-9.]", "")));
        
    }

    @Tag("finishTest")
    @Test
    public void finishTest(){
        inventoryPage.addProduct();
        inventoryPage.clickPanier();
        cartPage.checkout();
        checkoutPage.saisirFirstName("first");
        checkoutPage.saisirLastName("last");
        checkoutPage.saisirZipCode("100");
        checkoutPage.continueClick();
        checkoutOverviewPage.clickFinish();
        assertTrue(checkoutFinishPage.getCompleteMessage().isDisplayed());
        
    }
}
