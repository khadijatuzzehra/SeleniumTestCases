package com.example.eshoptestcases;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPageTest {
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());

        Configuration.headless = true;
        Configuration.timeout = 60;
    }

    @Test
    public void RegisterUser(){
        //Create Random Email
        Random r = new Random();
        int low = 10;
        int high = 100;
        int result = r.nextInt(high-low) + low;
        String email="test_user" + result+"@gmail.com";

        //Testing
        Selenide.open("http://ec2-13-230-127-101.ap-northeast-1.compute.amazonaws.com:8181/register");
        $(By.id("name")).sendKeys(new CharSequence[]{"User"});
        $(By.id("email")).sendKeys(new CharSequence[]{email});
        $(By.id("password")).sendKeys(new CharSequence[]{"user1234"});
        $(By.id("password-confirm")).sendKeys(new CharSequence[]{"user1234"});
        $(By.xpath("/html/body/div/main/div/div/div/div/div[2]/form/div[5]/div/button")).click();

        String message= Selenide.$(By.xpath("/html/body/div[1]/div/div/div/div/div[2]")).getText();
        Assertions.assertEquals("You are logged in!",message);

        webdriver().driver().clearCookies();
    }

    @Test
    public void RegisterPasswordMismatch(){
        //If the password doesn't match, displays error
        open("http://ec2-13-230-127-101.ap-northeast-1.compute.amazonaws.com:8181/register");
        $(By.id("name")).sendKeys(new CharSequence[]{"User"});
        $(By.id("email")).sendKeys(new CharSequence[]{"user9090@gmail.com"});
        $(By.id("password")).sendKeys(new CharSequence[]{"user1234"});
        $(By.id("password-confirm")).sendKeys(new CharSequence[]{"user123"});
        $(By.xpath("/html/body/div/main/div/div/div/div/div[2]/form/div[5]/div/button")).click();
        String message= $(By.xpath("/html/body/div/main/div/div/div/div/div[2]/form/div[3]/div/span/strong")).getText();
        Assertions.assertEquals("The password confirmation does not match.",message);
        webdriver().driver().clearCookies();
    }
    @Test
    public void LoginUser(){
        //Login with valid credentials
        open("http://ec2-13-230-127-101.ap-northeast-1.compute.amazonaws.com:8181/login");
        $(By.id("email")).sendKeys(new CharSequence[]{"user12@gmail.com"});
        $(By.id("password")).sendKeys(new CharSequence[]{"user1234"});
        $(By.xpath("/html/body/div[1]/main/div/div/div/div/div[2]/form/div[4]/div/button")).click();
        String message= $(By.xpath("/html/body/div[2]/div/div[1]")).getText();
        Assertions.assertEquals("Logged in successfully",message);
        $(By.xpath("/html/body/div[2]/div/div[2]/div/button")).click();
        webdriver().driver().clearCookies();
    }
    @Test
    public void LoginWrongCredentials(){
        //Login with Invalid Password
        open("http://ec2-13-230-127-101.ap-northeast-1.compute.amazonaws.com:8181/login");
        $(By.id("email")).sendKeys("user12@gmail.com");
        $(By.id("password")).sendKeys("user12");
        $(By.xpath("/html/body/div[1]/main/div/div/div/div/div[2]/form/div[4]/div/button")).click();
        String message= $(By.xpath("/html/body/div[1]/main/div/div/div/div/div[2]/form/div[1]/div/span/strong")).getText();
        Assertions.assertEquals("These credentials do not match our records.",message);
        webdriver().driver().clearCookies();
    }

    @Test
    public void DashboardAccess(){
        //Login User
        open("http://ec2-13-230-127-101.ap-northeast-1.compute.amazonaws.com:8181/login");
        $(By.id("email")).sendKeys(new CharSequence[]{"user12@gmail.com"});
        $(By.id("password")).sendKeys(new CharSequence[]{"user1234"});
        $(By.xpath("/html/body/div[1]/main/div/div/div/div/div[2]/form/div[4]/div/button")).click();

        //Normal user can't access admin dashboard
        $(By.xpath("/html/body/div[2]/div/div[2]/div/button")).click();
        $(By.xpath("/html/body/nav/div/div/ul[1]/li[4]/a")).click();
        $(By.xpath("/html/body/div[2]/div/div[2]/div/button")).click();
        String message= $(By.xpath("/html/body/div[1]/div/div/div/div/div[2]/div")).getText();
        Assertions.assertEquals("Access Denied! as you are not as admin",message);
        webdriver().driver().clearCookies();
    }

}
