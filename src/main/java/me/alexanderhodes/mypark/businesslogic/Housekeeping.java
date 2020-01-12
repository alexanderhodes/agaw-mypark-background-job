package me.alexanderhodes.mypark.businesslogic;

import me.alexanderhodes.mypark.auth.JobAuthentication;
import me.alexanderhodes.mypark.helper.UrlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Configuration()
public class Housekeeping {

    @Autowired
    private JobAuthentication jobAuthentication;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UrlHelper urlHelper;

    public Housekeeping() {
    }

    // clean booking
    public boolean removeBookings() {
        String url = this.urlHelper.createUrlForResource("bookings/system");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, body, String.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    // clean absence
    public boolean removeAbsences() {
        String url = this.urlHelper.createUrlForResource("absences/system");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, body, String.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    // clean token
    public boolean removeTokens() {
        String url = this.urlHelper.createUrlForResource("token/system");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, body, String.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    public void updateParkingSpaces() {

    }

    private void sendRequest() {

    }

    private HttpEntity<String> prepareHeader() {
        String token = this.jobAuthentication.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> body = new HttpEntity("", headers);
        return body;
    }

}
