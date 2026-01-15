package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:localAndroid.properties"
})
public interface LocalAndroidConfig extends Config {
    
    @Key("app.path")
    String appPath();
    
    @Key("app.package")
    String appPackage();
    
    @Key("app.activity")
    String appActivity();
    
    @DefaultValue("Android")
    String platformName();
    
    @DefaultValue("11.0")  // API 30 = Android 11.0
    String platformVersion();
    
    @DefaultValue("Pixel_4_API_30")  // Ваш эмулятор
    String deviceName();
    
    @DefaultValue("http://localhost:4723/wd/hub")
    String appiumUrl();
    
    @DefaultValue("UIAutomator2")
    String automationName();
    
    @DefaultValue("portrait")
    String deviceOrientation();
    
    @DefaultValue("true")
    boolean autoGrantPermissions();
    
    @DefaultValue("false")
    boolean noReset();
    
    @DefaultValue("false")
    boolean fullReset();
    
    @DefaultValue("300")
    int newCommandTimeout();
}