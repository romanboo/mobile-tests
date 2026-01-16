package drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class LocalAndroidDriver implements WebDriverProvider {

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();


        options.setPlatformName("Android");
        options.setPlatformVersion("11.0"); // API 30 = Android 11.0
        options.setDeviceName("Pixel_4_API_30");
        options.setAutomationName("UIAutomator2");

        options.setAppPackage("org.wikipedia.alpha");
        options.setAppActivity("org.wikipedia.main.MainActivity");

        boolean cleanStart = Boolean.parseBoolean(System.getProperty("clean.start", "true"));

        if (cleanStart) {
            System.out.println("CLEAN START: Clearing app data for fresh onboarding");
            options.setNoReset(false); // false = очистить данные приложения
            options.setFullReset(false); // false = не удалять приложение
            options.setCapability("fastReset", true); // true = очистить данные приложения
            options.setCapability("clearSystemFiles", true); // очистить системные файлы
        } else {
            System.out.println("CONTINUE SESSION: Using existing app data");
            options.setNoReset(true); // true = не очищать данные
            options.setFullReset(false);
        }

        options.setAutoGrantPermissions(true);
        options.setNewCommandTimeout(Duration.ofSeconds(300));
        options.setCapability("appWaitActivity", "org.wikipedia.*");
        options.setCapability("appWaitDuration", 30000);
        options.setCapability("appWaitForLaunch", true);

        options.setCapability("printPageSourceOnFindFailure", true);
        options.setCapability("adbExecTimeout", 60000);
        options.setCapability("ignoreUnimportantViews", true);
        options.setCapability("disableAndroidWatchers", true);

        System.out.println("========================================");
        System.out.println("LOCAL EMULATOR CONFIGURATION:");
        System.out.println("  Device: Pixel 4 API 30");
        System.out.println("  Android: 11.0");
        System.out.println("  App: Wikipedia Alpha (installed)");
        System.out.println("  Package: org.wikipedia.alpha");
        System.out.println("  Activity: org.wikipedia.main.MainActivity");
        System.out.println("  Clean start: " + cleanStart + " (clears app data)");
        System.out.println("  Appium Server: http://localhost:4723");
        System.out.println("========================================");

        try {
            URL appiumUrl = new URL("http://localhost:4723/wd/hub");
            AndroidDriver driver = new AndroidDriver(appiumUrl, options);
            System.out.println("✓ Android driver created successfully");
            System.out.println("✓ Session ID: " + driver.getSessionId());
            return driver;
        } catch (MalformedURLException e) {
            System.err.println("✗ ERROR: Cannot connect to Appium server");
            System.err.println("  Make sure Appium is running: appium");
            System.err.println("  Check: http://localhost:4723");
            throw new RuntimeException("Failed to connect to Appium server", e);
        }
    }
}