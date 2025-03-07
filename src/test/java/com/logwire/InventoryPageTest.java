package com.logwire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.logwire.pages.InventoryPage;
import com.logwire.pages.LoginPage;

@Tag("produits")
public class InventoryPageTest {
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;

    @BeforeEach
    public void setUp(){
        String browser = System.getProperty("browser","chrome");

        switch (browser.toLowerCase()) {
            case "chrome":
                // Spécification du répertoire de données utilisateur unique pour éviter le conflit
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--user-data-dir=/tmp/chrome-user-data");  // Spécifier un répertoire unique
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

    @Tag("ajoutProduitTest")
    @Test
    public void ajoutProduitTest(){
        inventoryPage.addProduct();
        assertEquals(6,inventoryPage.getRemoveBoutons().size());
    }

    @Tag("retraitProduitTest")
    @Test
    public void retraitProduitTest(){
        inventoryPage.addProduct();
        inventoryPage.removeProduct();
        assertEquals(0, inventoryPage.getRemoveBoutons().size());
    }

    @Tag("logoutTest")
    @Test
    public void logoutTest(){
        inventoryPage.clickmenu();
        inventoryPage.logout();
        assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl());
    }

    @Tag("filtresTest")
    @ParameterizedTest
    @CsvFileSource(resources = "data/filtres.csv", numLinesToSkip = 1)
    public void filtreProduitsTest(String filtre){
        inventoryPage.ordreProduits(filtre);
        if (filtre == "Name (A to Z)") {
            List<String> nomList = inventoryPage.getNomList();
            List<String> nomListTriee = new ArrayList<>(nomList);
            Collections.sort(nomListTriee);
            assertEquals(nomList, nomListTriee);
        }else if(filtre == "Name (Z to A)"){
            List<String> nomList = inventoryPage.getNomList();
            List<String> nomListTriee = new ArrayList<>(nomList);
            Collections.sort(nomListTriee, Collections.reverseOrder());
            assertEquals(nomList, nomListTriee);
        }else if(filtre == "Price (low to high)"){
            List<Float> prixListOriginale = inventoryPage.getPrixList();
            List<Float> prixListTriee = new ArrayList<>(prixListOriginale);
            Collections.sort(prixListTriee);
            assertTrue(prixListOriginale.equals(prixListTriee));
        }else if(filtre == "Price (high to low)"){
            List<Float> prixListOriginale = inventoryPage.getPrixList();
            List<Float> prixListTriee = new ArrayList<>(prixListOriginale);
            Collections.sort(prixListTriee, Collections.reverseOrder());
            assertTrue(prixListOriginale.equals(prixListTriee));
        }else{
            assertTrue(true);
        }
    }

    @Tag("resetBoutonTest")
    @Test
    public void resetBoutonTest(){
        inventoryPage.addProduct();
        inventoryPage.clickmenu();
        inventoryPage.reset();
        assertTrue(inventoryPage.getRemoveBoutons().size()==0);
    }

    @Tag("resetCartTest")
    @Test
    public void resetCartTest(){
        inventoryPage.addProduct();
        inventoryPage.clickmenu();
        inventoryPage.reset();
        assertEquals(0, inventoryPage.getBadgeNumber());
    }

}
