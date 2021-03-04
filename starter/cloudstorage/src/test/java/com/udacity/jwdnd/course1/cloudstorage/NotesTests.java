package com.udacity.jwdnd.course1.cloudstorage;


import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesTests {

    @LocalServerPort
    private int port;

    @FindBy(css="#add-note")
    private WebElement addNote;

    @FindBy(css="#nav-notes-tab")
    private WebElement navNotesTab;


    @FindBy(css="#save-note-button")
    private WebElement noteSaveButton;

    @FindBy(xpath=".//*[@id=\"userTable\"]/tbody/tr/th")
    private WebElement noteTableRow;

    @FindBy(css="#note-title")
    private WebElement noteTitle;

    @FindBy(css="#note-description")
    private WebElement noteDescription;

    @FindBy(css="#save-note-button")
    private WebElement noteSave;

    @FindBy(xpath=".//*[@id=\"userTable\"]/tbody/tr/td[2]/button")
    private WebElement noteEditButton;

    @FindBy(xpath=".//*[@id=\"userTable\"]/tbody/tr/td[1]/form/button")
    private WebElement noteDeleteButton;

    @FindBy(xpath=".//*[@id=\"userTable\"]/tbody/tr/th")
    private WebElement noteTitleField;

    @FindBy(xpath=".//*[@id=\"userTable\"]/thead")
    private WebElement noteTableHead;

    private WebDriver driver;

    private String baseURL;

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

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
    @WithMockUser(username = "username", authorities = { "ADMIN", "USER" })
    public void addNoteTest() throws InterruptedException {

        String username = "username";
        String password = "password";
        Integer userID = 1;

        User userWithCredentials = new User(userID,username,"salt",password,"fname","lname");
        userService.createUser(userWithCredentials);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        PageFactory.initElements(driver, this);

        driver.get("http://localhost:" + this.port + "/home");

        String newNoteTitle = "noteTitleTest";
        String newnoteDisc = "noteDiscTest";

        navNotesTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.elementToBeClickable(addNote));
        addNote.click();

        wait.until(ExpectedConditions.elementToBeClickable(noteSaveButton));
        addNote(newNoteTitle,newnoteDisc);

        driver.get("http://localhost:" + this.port + "/home");

        navNotesTab.click();

        wait.until(ExpectedConditions.visibilityOf(noteTableRow));
        String actualNoteTitle = noteTableRow.getText();
        assertEquals(newNoteTitle,actualNoteTitle);

        //cleanup
        User currentUser = userService.getCurrentUser();
        Note note = userService.getUserNotes(currentUser.getUserId()).get(0);
        noteService.removeNote(note.getNoteId());
        userService.removeUser(currentUser);
    }


    @Test
    @WithMockUser(username = "username", authorities = { "ADMIN", "USER" })
    public void editNoteTest() throws InterruptedException {

        String username = "username";
        String password = "password";
        Integer userID = 1;
        Integer noteID = 1;

        User userWithCredentials = new User(userID,username,"salt",password,"fname","lname");
        userService.createUser(userWithCredentials);
        Note note = new Note(noteID,"noteTitle","noteDescription",userID);
        noteService.addNote(note);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        PageFactory.initElements(driver, this);

        driver.get("http://localhost:" + this.port + "/home");

        String newNoteTitle= "newNoteTitle";
        String newNoteDisc= "newNoteDisc";

        navNotesTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(noteTitleField));
        noteEditButton.click();

       wait.until(ExpectedConditions.elementToBeClickable(noteSaveButton));

       setNotes(newNoteTitle,newNoteDisc);

       driver.get("http://localhost:" + this.port + "/home");

       navNotesTab.click();

       wait.until(ExpectedConditions.visibilityOf(noteTitleField));
       String actualNoteTitleField = noteTitleField.getText();
       assertEquals(newNoteTitle,actualNoteTitleField);

       //cleanup
       noteService.removeNote(note.getNoteId());
       userService.removeUser(userWithCredentials);

    }


    @Test
    @WithMockUser(username = "username", authorities = { "ADMIN", "USER" })
    public void deleteNoteTest() throws InterruptedException {

        String username = "username";
        String password = "password";
        Integer userID = 1;
        Integer noteID = 1;

        User userWithCredentials = new User(userID,username,"salt",password,"fname","lname");
        userService.createUser(userWithCredentials);
        Note note = new Note(noteID,"noteTitle","noteDescription",userID);
        noteService.addNote(note);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        PageFactory.initElements(driver, this);

        driver.get("http://localhost:" + this.port + "/home");

        navNotesTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(noteTitleField));

        noteDeleteButton.click();

        driver.get("http://localhost:" + this.port + "/home");

        navNotesTab.click();

        wait.until(ExpectedConditions.visibilityOf(noteTableHead));

        assertEquals(0,driver.findElements(By.xpath(".//*[@id=\"userTable\"]/tbody/tr/th")).size());

        //cleanup
        userService.removeUser(userWithCredentials);
    }

    void setNotes(String noteTitle, String noteDiscription){

        testUtils.clearFields(this.noteDescription, this.noteTitle);

        this.noteTitle.sendKeys(noteTitle);
        this.noteDescription.sendKeys(noteDiscription);
        this.noteSave.click();
    }

    void addNote(String noteTitle,String noteDiscription){
        this.noteTitle.sendKeys(noteTitle);
        this.noteDescription.sendKeys(noteDiscription);
        this.noteSave.click();
    }

}
