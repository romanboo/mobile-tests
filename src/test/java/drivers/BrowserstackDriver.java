package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.BrowserstackConfig;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserstackDriver implements WebDriverProvider {

    private static final BrowserstackConfig config = ConfigFactory.create(BrowserstackConfig.class);

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull org.openqa.selenium.Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setDeviceName(config.deviceName());
        options.setPlatformVersion(config.platformVersion());
        options.setApp(config.app());

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", config.browserstackUser());
        bstackOptions.put("accessKey", config.browserstackKey());
        bstackOptions.put("projectName", config.projectName());
        bstackOptions.put("buildName", config.buildName());
        bstackOptions.put("sessionName", config.name());

        options.setCapability("bstack:options", bstackOptions);

        try {
            return new RemoteWebDriver(new URL(config.browserstackUrl()), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}