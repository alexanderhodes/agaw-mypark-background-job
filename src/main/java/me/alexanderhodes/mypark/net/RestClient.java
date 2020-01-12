package me.alexanderhodes.mypark.net;

import me.alexanderhodes.mypark.helper.UrlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration()
public class RestClient {

    @Autowired
    private UrlHelper urlHelper;

    public RestClient() {

    }

    public void sendGet(RestTemplate restTemplate) {

    }

    public void post(RestTemplate restTemplate) {

    }

}
