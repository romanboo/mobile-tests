package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "classpath:${device}.properties",
        "file:~/${device}.properties"
})
public interface BrowserStackConfig extends Config {

    @DefaultValue("bs://f3b370e92873f9032c4c5c247deb604a539b1f95")
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
