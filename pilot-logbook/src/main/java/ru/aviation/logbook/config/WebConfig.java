package ru.aviation.logbook.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

@Configuration
public class WebConfig {

    /**
     * Должен выполняться раньше Spring Security, иначе тело POST (заметки и т.д.)
     * читается не в UTF-8 и в БД попадают «ÐŸÐµÑ‚Ñ€Ð¾Ð²».
     */
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> utf8CharacterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true);
        filter.setForceRequestEncoding(true);
        filter.setForceResponseEncoding(true);

        FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
