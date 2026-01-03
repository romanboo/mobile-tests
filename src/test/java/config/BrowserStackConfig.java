package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "classpath:${device}.properties",
        "file:~/${device}.properties"
})
public interface BrowserStackConfig extends Config {

    @DefaultValue("bs://1f014cc65a5a246fcf31a453ae00dbe64c294925")  // Ваше приложение
    String browserstackApp();

    @DefaultValue("Google Pixel 7")
    String browserstackDevice();

    @DefaultValue("13.0")
    String browserstackPlatform();

    @DefaultValue("Appium Java Project")
    String browserstackProject();

    @DefaultValue("browserstack-build-1")
    String browserstackBuild();

    @DefaultValue("Wikipedia app tests")
    String browserstackName();

    String browserstackUrl();
}
