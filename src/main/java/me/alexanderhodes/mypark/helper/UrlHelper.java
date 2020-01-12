package me.alexanderhodes.mypark.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class UrlHelper {

    @Value("${mypark.api.url}")
    private String backendUrl;

    public UrlHelper() {
    }

    public String getBackendUrl() {
        return backendUrl;
    }

    public String createUrlForResource(String resource) {
        return new StringBuilder(this.backendUrl).append(resource).toString();
    }
}
