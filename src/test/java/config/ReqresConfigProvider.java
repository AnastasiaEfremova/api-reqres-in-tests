package config;

import org.aeonbits.owner.ConfigFactory;

public class ReqresConfigProvider {
    public static final ReqresConfig config = ConfigFactory.create(ReqresConfig.class, System.getProperties());
}