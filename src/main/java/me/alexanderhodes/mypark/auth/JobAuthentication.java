package me.alexanderhodes.mypark.auth;

import me.alexanderhodes.mypark.helper.UrlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JobAuthentication {

    private Auth auth;

    @Autowired
    private UrlHelper urlHelper;
    @Autowired
    private RestTemplate restTemplate;

    public JobAuthentication() {

    }

    public ResponseEntity<Auth> authenticate() {
        String url = this.urlHelper.createUrlForResource("authenticate");
        String body = this.createBody();

        ResponseEntity<Auth> responseEntity = restTemplate.postForEntity(url, body, Auth.class);
        this.auth = responseEntity.getBody();
        return responseEntity;
    }

    public String getToken() {
        return this.auth.getToken();
    }

    private String createBody() {
        String body = "------WebKitFormBoundary1cxxATRkbOxavsxS\n" +
                "Content-Disposition: form-data; name=\"username\"\n" +
                "\n" +
                "system\n" +
                "------WebKitFormBoundary1cxxATRkbOxavsxS\n" +
                "Content-Disposition: form-data; name=\"password\"\n" +
                "\n" +
                "Telekom123\n" +
                "------WebKitFormBoundary1cxxATRkbOxavsxS--";
        return body;
    }

}
