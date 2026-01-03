package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:authData.properties"
})
public interface AuthConfig extends Config {
    @Key("browserStackUser")
    String bsLogin();

    @Key("browserStackPassword")
    String bsPassword();
}
