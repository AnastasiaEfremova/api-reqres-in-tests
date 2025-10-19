package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:properties/reqres.properties"
})
public interface ReqresConfig extends Config {

    @Key("base.url")
    @DefaultValue("https://reqres.in")
    String baseUrl();

    @Key("api.key")
    @DefaultValue("reqres-free-v1")
    String apiKey();

    @Key("default.email")
    @DefaultValue("eve.holt@reqres.in")
    String defaultEmail();

    @Key("default.password")
    @DefaultValue("cityslicka")
    String defaultPassword();

    @Key("test.user.id")
    @DefaultValue("2")
    String testUserId();
}