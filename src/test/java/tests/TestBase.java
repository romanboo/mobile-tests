package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import drivers.BrowserStackMobileDriver;
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
        if (System.getProperty("device") == null) {
            System.setProperty("device", "android");
        }

        System.out.println("=== BROWSERSTACK TEST CONFIGURATION ===");
        System.out.println("Device: " + System.getProperty("device"));

        Configuration.browser = BrowserStackMobileDriver.class.getName();

        Configuration.browserSize = null;
        Configuration.timeout = 30000;
        Configuration.pageLoadStrategy = "none";
        Configuration.savePageSource = true;
        Configuration.fastSetValue = true;
        Configuration.pageLoadTimeout = 60000;

        System.setProperty("selenide.logs.enabled", "false");

        System.out.println("Timeout: " + Configuration.timeout + "ms");
        System.out.println("==========================");
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true));

        System.out.println("Opening application on BrowserStack...");

        open();

        Selenide.sleep(5000);

        System.out.println("Application ready for testing");
    }

    @AfterEach
    void addAttachments() {
        String sessionId = Selenide.sessionId() != null ? Selenide.sessionId().toString() : "null";
        System.out.println("=== COLLECTING ATTACHMENTS ===");
        System.out.println("Session ID: " + sessionId);

        collectAllAttachments();

        System.out.println("Closing web driver...");
        closeWebDriver();

        if (sessionId != null && !"null".equals(sessionId)) {
            System.out.println("Adding BrowserStack video...");
            try {
                Attach.addVideo(sessionId);
            } catch (Exception e) {
                System.out.println("Failed to add video: " + e.getMessage());
            }
        }

        System.out.println("=== BROWSERSTACK TEST COMPLETE ===");
    }

    private void collectAllAttachments() {
        try { Attach.screenshotAs("Final screenshot"); } catch (Exception e) { logError("screenshot", e); }
        try { Attach.pageSource(); } catch (Exception e) { logError("page source", e); }
        try { Attach.getElementTree(); } catch (Exception e) { logError("element tree", e); }
        try { Attach.getCurrentActivity(); } catch (Exception e) { logError("current activity", e); }
        try { Attach.getSessionInfo(); } catch (Exception e) { logError("session info", e); }
        try { Attach.getDeviceInfo(); } catch (Exception e) { logError("device info", e); }
        try { Attach.getScreenDimensions(); } catch (Exception e) { logError("screen dimensions", e); }
        try { Attach.takeScreenshotWithRetry("Final screenshot with retry"); } catch (Exception e) { logError("retry screenshot", e); }
    }

    private void logError(String attachmentName, Exception e) {
        System.out.println("Failed to collect " + attachmentName + ": " + e.getMessage());
    }

    protected void waitSeconds(int seconds) {
        System.out.println("Waiting " + seconds + " seconds...");
        Selenide.sleep(seconds * 1000);
    }

    protected void takeScreenshot(String name) {
        System.out.println("Taking screenshot: " + name);
        Selenide.screenshot(name);
    }
}