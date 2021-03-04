package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginSignUpTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private String baseURL;

	@Autowired
	UserService userService;

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
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void HomePageNotAccessibleWithoutLogin() {
		driver.get("http://localhost:" + this.port + "/home");

		String actualUrl = driver.getCurrentUrl();
		String urlToCheck = baseURL + "/home";

		assertNotEquals(actualUrl,urlToCheck);
	}

	@Test
	public void testUserSignupLogin() {
		String username = "userNameTest";
		String password = "passwordTest";


		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("firstNameTest", "lastNameTest", username, password);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		driver.get(baseURL + "/home");

		String expectedUrl = baseURL + "/home";
		String actualUrl = driver.getCurrentUrl();

		assertEquals(expectedUrl,actualUrl);

		HomePage homePage = new HomePage(driver);
		homePage.logOut();

		driver.get(baseURL + "/home");

		String urlToCheck = baseURL + "/home";
		actualUrl = driver.getCurrentUrl();


		assertNotEquals(actualUrl,urlToCheck);

	}



}
