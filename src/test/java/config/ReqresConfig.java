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
    @DefaultValue("https://reqres.in/api")
    String baseUrl();

    @Key("api.key")
    @DefaultValue("reqres-free-v1")
    String apiKey();

    @Key("email")
    @DefaultValue("eve.holt@reqres.in")
    String email();

    @Key("password")
    @DefaultValue("cityslicka")
    String password();
}