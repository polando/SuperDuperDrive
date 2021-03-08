package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

@Service
public class TestUtils {

    void clearField(WebElement webElement){
        webElement.sendKeys(Keys.CONTROL, Keys.chord("a")); //select all text in textbox
        webElement.sendKeys(Keys.BACK_SPACE);
    }

    void clearFields(WebElement... webElements){
        for (WebElement webElement:webElements) {
            clearField(webElement);
        }
    }
}
