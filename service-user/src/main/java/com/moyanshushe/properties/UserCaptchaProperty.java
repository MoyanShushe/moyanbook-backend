package com.moyanshushe.properties;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "moyanshushe.key")
public class UserCaptchaProperty {
    String name;
    String key;
    String secret;
}
