package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import drivers.BrowserstackDriver;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class TestBase {
    @BeforeAll
    static void beforeAll() {
        Configuration.browser = BrowserstackDriver.class.getName();
        Configuration.browserSize = null;
        Configuration.timeout = 30000;
        Configuration.pageLoadTimeout = 30000;
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        try {
            open();
        } catch (Exception e) {
            System.err.println("Failed to open browser: " + e.getMessage());
            throw e;
        }
    }

    @AfterEach
    void addAttachments() {
        try {
            // Получаем sessionId только если драйвер существует
            String sessionId = Selenide.sessionId() != null ?
                    Selenide.sessionId().toString() : "NO_SESSION_ID";

            System.out.println("Session ID: " + sessionId);

            // Attach.screenshotAs("Last screenshot");
            Attach.pageSource();

            // Добавляем видео
            Attach.addVideo(sessionId);

        } catch (Exception e) {
            System.err.println("Error in attachments: " + e.getMessage());
        } finally {
            closeWebDriver();
        }
    }
}