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

        Configuration.browserSize = null;
        Configuration.browser = BrowserStackMobileDriver.class.getName();
        Configuration.timeout = 15000;
        Configuration.pageLoadStrategy = "none";
        Configuration.remoteReadTimeout = 20000;
        Configuration.remoteConnectionTimeout = 20000;
        Configuration.savePageSource = true; // Включаем сохранение page source для отладки
        Configuration.fastSetValue = true;

        // Отключаем лишние логи, чтобы не засорять вывод
        System.setProperty("selenide.logs.enabled", "false");
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
        open();
    }

    @AfterEach
    void addAttachments() {
        String sessionId = Selenide.sessionId().toString();
        System.out.println("=== TEST FINISHED ===");
        System.out.println("Session ID: " + sessionId);

        // Собираем максимум информации для отладки
        try {
            Attach.screenshotAs("Final screenshot");
            Attach.pageSource();
            Attach.getElementTree(); // Добавляем XML дерево элементов
            Attach.getCurrentActivity(); // Добавляем текущую активность
            Attach.getSessionInfo(); // Добавляем информацию о сессии

            // Также можно сохранить дополнительный скриншот с повторными попытками
            Attach.takeScreenshotWithRetry("Final screenshot with retry");

        } catch (Exception e) {
            System.out.println("Failed to collect attachments: " + e.getMessage());
        }

        closeWebDriver();

        if (sessionId != null && !sessionId.equals("null")) {
            Attach.addVideo(sessionId);
        } else {
            System.out.println("No session ID available for video");
        }
    }
}