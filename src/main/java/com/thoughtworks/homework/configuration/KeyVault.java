package com.thoughtworks.homework.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Getter
@Setter
@Primary
@Configuration
@ConfigurationProperties(prefix = "keyvault")
public class KeyVault {
    private boolean enabled = false;

    private String name;

    private String clientId;

    private String clientSecret;

    private String mysqlHostKey;

    private String mysqlUserKey;

    private String mysqlPasswordKey;

    private String redisKey;

    private String serviceBusConnectionString;

    private String blobConnectionStringKey;

    public boolean isEnabled() {
        return enabled;
    }
}
