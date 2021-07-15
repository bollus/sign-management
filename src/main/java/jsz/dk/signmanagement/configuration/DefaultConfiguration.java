package jsz.dk.signmanagement.configuration;

import jsz.dk.signmanagement.interceptor.advice.BaseResponseAdvice;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.configuration
 * @ClassName: DefaultConfiguration
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/13 19:46
 */
@Configuration
@EnableConfigurationProperties(DefaultProperties.class)
@PropertySource(value = "classpath:dispose.properties", encoding = "UTF-8")
public class DefaultConfiguration {
    @Bean
    public BaseResponseAdvice commonResponseDataAdvice(DefaultProperties defaultProperties) {
        return new BaseResponseAdvice(defaultProperties);
    }
}
