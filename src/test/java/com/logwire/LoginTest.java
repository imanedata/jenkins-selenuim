package com.logwire;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;

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

@Tag("login")
public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;

    private String userDataDir;

    @BeforeEach
    public void setUp() throws Exception {
        String browser = System.getProperty("browser", "chrome");

        // Création d'un répertoire temporaire unique pour chaque test
        userDataDir = "/tmp/chrome_user_data_" + System.nanoTime();
        Files.createDirectories(new File(userDataDir).toPath());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-data-dir=" + userDataDir);  // Répertoire unique

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver(options);  // Chrome avec options
                break;
            
            case "firefox":
                driver = new FirefoxDriver();  // Firefox sans options
                break;
            
            default:
                driver = new ChromeDriver(options);  // Chrome par défaut avec options
                break;
        }

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://www.saucedemo.com/");
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.close();
            driver.quit();
            driver = null;
        }

        // Suppression du répertoire des données utilisateur après chaque test
        File userData = new File(userDataDir);
        if (userData.exists()) {
            deleteDirectory(userData);
        }
    }

    // Méthode pour supprimer un répertoire et son contenu
    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    @Tag("positifLogin")
    @ParameterizedTest
    @CsvFileSource(resources = "data/validUsernamesPassword.csv", numLinesToSkip = 1)
    public void logincorrect(String username, String password) {
        loginPage.saisirUsername(username);
        loginPage.saisirPassword(password);
        loginPage.clickLogin();
        assertTrue((inventoryPage.getNbrProduits() > 0) && driver.getCurrentUrl().contains("/inventory"));
    }

    @Tag("negatifLogin")
    @Test
    public void usernameIncorrectPasswordCorrect() {
        loginPage.saisirUsername("user");
        loginPage.saisirPassword("secret_sauce");
        loginPage.clickLogin();
        assertTrue(loginPage.getError());
    }

    @Tag("negatifLogin")
    @Test
    public void usernameCorrectPasswordInorrect() {
        loginPage.saisirUsername("standard_user");
        loginPage.saisirPassword("secret");
        loginPage.clickLogin();
        assertTrue(loginPage.getError());
    }
}
