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

@Tag("checkoutOverviewTest")
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
        String browser = System.getProperty("browser", "chrome");

        // Options de Chrome sans le user-data-dir
        ChromeOptions options = new ChromeOptions();
        // Pas de --user-data-dir ici
        options.addArguments("--headless");  // Facultatif : pour exécuter Chrome sans UI
        options.addArguments("--disable-gpu"); // Facultatif : pour les environnements sans GPU

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver(options); // Utilisation des options pour Chrome
                break;
            
            case "firefox":
                driver = new FirefoxDriver(); // Utilisation de Firefox sans options
                break;
            
            default:
                driver = new ChromeDriver(options); // Utilisation par défaut de Chrome sans options
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
