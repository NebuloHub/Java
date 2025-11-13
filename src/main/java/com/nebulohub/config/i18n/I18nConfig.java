// File: src/main/java/com/nebulohub/config/i18n/I18nConfig.java (UPDATED)
package com.nebulohub.config.i18n;

import org.springframework.context.MessageSource; // <-- IMPORT ADDED
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource; // <-- IMPORT ADDED
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Configures the Internationalization (i18n) beans.
 * Based on the OTMAV project.
 */
@Configuration
public class I18nConfig {

    /**
     * **NEW BEAN**
     * Configures the source for message properties, forcing UTF-8 encoding.
     * This fixes character issues with Português (e.g., "ltimas").
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8"); // <-- THE FIX
        return messageSource;
    }

    /**
     * Creates a LocaleResolver that stores the user's selected language
     * in a cookie named "localeInfo".
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver("localeInfo");
        resolver.setDefaultLocale(new Locale("en")); // Default language is English
        return resolver;
    }

    /**
     * Creates an interceptor that will switch the language when it
     * sees a URL parameter named "lang".
     * e.g., /posts?lang=pt
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
}