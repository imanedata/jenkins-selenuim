package com.logwire;

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

import com.logwire.pages.InventoryPage;
import com.logwire.pages.LoginPage;

@Tag("login")
public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;
    @BeforeEach
    public void setUp(){
        String browser = System.getProperty("browser","chrome");

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                driver = new ChromeDriver(); 
                break;
            }
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://www.saucedemo.com/");

    }

    @AfterEach
    public void tearDown(){
        if (driver != null){
            driver.close();
            driver.quit();
            driver = null;
        }
    }


    @Tag("positifLogin")
    @ParameterizedTest
    @CsvFileSource(resources = "data/validUsernamesPassword.csv", numLinesToSkip = 1)
    public void logincorrect(String username, String password){
        loginPage.saisirUsername(username);
        loginPage.saisirPassword(password);
        loginPage.clickLogin();
        assertTrue((inventoryPage.getNbrProduits()>0) && driver.getCurrentUrl().contains("/inventory"));
    }

    @Tag("negatifLogin")
    @Test
    public void usernameIncorrectPasswordCorrect(){
        loginPage.saisirUsername("user");
        loginPage.saisirPassword("secret_sauce");
        loginPage.clickLogin();
        assertTrue(loginPage.getError());
    }

    @Tag("negatifLogin")
    @Test
    public void usernameCorrectPasswordInorrect(){
        loginPage.saisirUsername("standard_user");
        loginPage.saisirPassword("secret");
        loginPage.clickLogin();
        assertTrue(loginPage.getError());
    }
}
