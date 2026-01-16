package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import drivers.LocalAndroidDriver;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.*;

public class LocalTestBase {

    @BeforeAll
    static void beforeAll() {
        System.out.println("=== LOCAL EMULATOR TEST CONFIGURATION ===");
        System.out.println("Device: Pixel 4 API 30 (Emulator)");
        System.out.println("App: Wikipedia Alpha (installed)");


        Configuration.browser = LocalAndroidDriver.class.getName();

        // Настройки для локального эмулятора
        Configuration.browserSize = null;
        Configuration.timeout = 60000;
        Configuration.pageLoadStrategy = "none";
        Configuration.savePageSource = true;
        Configuration.fastSetValue = true;

        Configuration.pageLoadTimeout = 0;

        boolean debugMode = Boolean.parseBoolean(System.getProperty("debug", "false"));
        if (debugMode) {
            Configuration.screenshots = true;
            Configuration.savePageSource = true;
        }

        System.out.println("Timeout: " + Configuration.timeout + "ms");
        System.out.println("Clean start: " + System.getProperty("clean.start", "true"));
        System.out.println("==========================");
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true));

        System.out.println("Launching Wikipedia Alpha app on emulator...");

        try {

            open();

            System.out.println("Waiting for app to launch (5 seconds)...");
            Selenide.sleep(1000);

            System.out.println("App launch attempt complete");

        } catch (Exception e) {
            System.err.println("Error launching app: " + e.getMessage());
            System.err.println("Troubleshooting steps:");
            System.err.println("1. Make sure emulator is running: Pixel 4 API 30");
            System.err.println("2. Make sure Appium server is running: appium");
            System.err.println("3. Make sure Wikipedia is installed on emulator");
            System.err.println("4. Try: adb shell pm list packages | grep wikipedia");
            throw e;
        }

        System.out.println("Local app ready for testing");
    }

    @AfterEach
    void addAttachments() {
        System.out.println("=== COLLECTING LOCAL TEST ATTACHMENTS ===");

        try {
            // Делаем финальный скриншот
            System.out.println("Taking final screenshot...");
            Attach.screenshotAs("Final screenshot - Local Emulator");

            // Сохраняем source страницы
            System.out.println("Saving page source...");
            Attach.pageSource();

            // Пробуем получить информацию об устройстве
            try {
                Attach.getDeviceInfo();
            } catch (Exception e) {
                System.out.println("Could not get device info: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Failed to collect attachments: " + e.getMessage());
        }

        System.out.println("Closing local driver...");
        try {
            closeWebDriver();
        } catch (Exception e) {
            System.out.println("Error closing driver: " + e.getMessage());
        }

        System.out.println("=== LOCAL TEST COMPLETE ===");
    }

    // Вспомогательные методы для тестов
    protected void waitSeconds(int seconds) {
        System.out.println("Waiting " + seconds + " seconds...");
        Selenide.sleep(seconds * 1000);
    }

    protected void takeScreenshot(String name) {
        try {
            System.out.println("Taking screenshot: " + name);
            Selenide.screenshot(name);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}