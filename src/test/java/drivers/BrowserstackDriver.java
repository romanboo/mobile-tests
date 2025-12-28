package drivers;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;

public class BrowserstackDriver implements WebDriverProvider {

    private static final String USERNAME = "romanboo_IA4dnz";
    private static final String ACCESS_KEY = "y7tQnHW9e42oNtYRUGRs";
    private static final String APP = "bs://sample.app"; // Демо приложение

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        System.out.println("=== Creating BrowserStack Session (Simple) ===");

        MutableCapabilities caps = new MutableCapabilities();

        // 1. Базовые Appium capabilities
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Google Pixel 7");
        caps.setCapability("platformVersion", "13.0");
        caps.setCapability("app", APP);
        caps.setCapability("automationName", "UIAutomator2");

        // 2. BrowserStack authentication
        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        // 3. BrowserStack project info
        caps.setCapability("project", "Mobile Tests");
        caps.setCapability("build", "Test Build");
        caps.setCapability("name", "Wikipedia Test");

        // 4. Дополнительные настройки
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("browserstack.networkLogs", "true");
        caps.setCapability("browserstack.idleTimeout", "300");

        try {
            System.out.println("Using device: Google Pixel 7 (Android 13.0)");
            System.out.println("App: " + APP);

            RemoteWebDriver driver = new RemoteWebDriver(
                    new URL("https://hub.browserstack.com/wd/hub"), caps);

            System.out.println("✅ BrowserStack session created!");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create BrowserStack driver", e);
        }
    }
}