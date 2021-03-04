package com.udacity.jwdnd.course1.cloudstorage;


import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {

    @FindBy(css="#add-credential")
    private WebElement addCredential;

    @FindBy(css="#nav-credentials-tab")
    private WebElement navCredentialsTab;


    @FindBy(css="#save-credential-button")
    private WebElement credentialSaveButton;


    @FindBy(css="#credential-url")
    private WebElement credentialURL;

    @FindBy(css="#credential-username")
    private WebElement credentialUsername;

    @FindBy(css="#credential-password")
    private WebElement credentialPassword;

    @FindBy(xpath=".//*[@id=\"credentialTable\"]/tbody/tr/td[3]")
    private WebElement credentialTableUserNameField;

    @FindBy(xpath=".//*[@id=\"credentialTable\"]/tbody/tr/td[2]/button")
    private WebElement editCredentialButton;

    @FindBy(xpath=".//*[@id=\"credentialTable\"]/tbody/tr/td[1]/form/button")
    private WebElement deleteCredentialButton;

    @FindBy(xpath=".//*[@id=\"credentialTable\"]/thead/tr/th[4]")
    private WebElement tableHead;


    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseURL;

    @Autowired
    UserService userService;

    @Autowired
    CredentialService credentialService;

    @Autowired
    TestUtils testUtils;


    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();

    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();

        baseURL = "http://localhost:" + port;

    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }



    @Test
    public void addCredentialTest() throws InterruptedException {

        String username = "username";
        String password = "password";

        User user = new User(1,username,"salt",password,"fname","lname");
        userService.createUser(user);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        PageFactory.initElements(driver, this);

        driver.get("http://localhost:" + this.port + "/home");

        String newCredentialURL = "CredentialURLTest";
        String newCredentialUser = "CredentialUserTest";
        String newCredentialPass = "CredentialPassTest";

        navCredentialsTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.elementToBeClickable(addCredential));
        addCredential.click();

        wait.until(ExpectedConditions.elementToBeClickable(credentialSaveButton));
        setCredentials(newCredentialURL,newCredentialUser,newCredentialPass);

        driver.get("http://localhost:" + this.port + "/home");

        navCredentialsTab.click();

        wait.until(ExpectedConditions.visibilityOf(credentialTableUserNameField));
        String actualUserNameField = credentialTableUserNameField.getText();
        assertEquals(newCredentialUser,actualUserNameField);

        //cleanup
        Credential credential = userService.getUserCredentials(user.getUserId()).get(0);
        credentialService.removeCredential(credential.getCredentialId());
        userService.removeUser(user);
    }


    @Test
    @WithMockUser(username = "username", authorities = { "ADMIN", "USER" })
    public void editCredentialTest() throws InterruptedException {

        String username = "username";
        String password = "password";
        Integer userID = 1;

        User userWithCredentials = new User(userID,username,"salt",password,"fname","lname");
        userService.createUser(userWithCredentials);
        Credential credential = new Credential(userID,"url",username,"key",password,userID);
        credentialService.addCredential(credential);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        PageFactory.initElements(driver, this);

        driver.get("http://localhost:" + this.port + "/home");

        String newCredentialURL = "newCredentialURLTest";
        String newCredentialUser = "newCredentialUserTest";
        String newCredentialPass = "newCredentialPassTest";

        navCredentialsTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(credentialTableUserNameField));
        editCredentialButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(credentialSaveButton));

        setCredentials(newCredentialURL,newCredentialUser,newCredentialPass);

        driver.get("http://localhost:" + this.port + "/home");

        navCredentialsTab.click();

        wait.until(ExpectedConditions.visibilityOf(credentialTableUserNameField));
        String actualUserNameField = credentialTableUserNameField.getText();
        assertEquals(newCredentialUser,actualUserNameField);


        //cleanup
        credentialService.removeCredential(credential.getCredentialId());
        userService.removeUser(userWithCredentials);
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ADMIN", "USER" })
    public void deleteCredentialTest() throws InterruptedException {

        String username = "username";
        String password = "password";
        Integer userID = 1;

        User userWithCredentials = new User(userID,username,"salt",password,"fname","lname");
        userService.createUser(userWithCredentials);
        Credential credential = new Credential(userID,"url",username,"key",password,userID);
        credentialService.addCredential(credential);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        PageFactory.initElements(driver, this);

        driver.get("http://localhost:" + this.port + "/home");

        navCredentialsTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(credentialTableUserNameField));

        deleteCredentialButton.click();

        driver.get("http://localhost:" + this.port + "/home");

        navCredentialsTab.click();

        wait.until(ExpectedConditions.visibilityOf(tableHead));

        assertEquals(0,driver.findElements(By.xpath(".//*[@id=\"credentialTable\"]/tbody/tr/td[3]")).size());

        userService.removeUser(userWithCredentials);
    }


    void setCredentials(String URL, String username, String password){

        testUtils.clearFields(this.credentialURL,this.credentialUsername,
                this.credentialPassword, this.credentialSaveButton);

        this.credentialURL.sendKeys(URL);
        this.credentialUsername.sendKeys(username);
        this.credentialPassword.sendKeys(password);
        this.credentialSaveButton.click();
    }





}
