package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "classpath:${env}.properties",
        "classpath:android.properties"
})
public interface BrowserstackConfig extends Config {
    String browserstackUser();
    String browserstackKey();
    String app();
    String deviceName();
    String platformVersion();
    String projectName();
    String buildName();
    String name();
    String browserstackUrl();
}