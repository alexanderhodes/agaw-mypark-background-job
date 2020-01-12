package me.alexanderhodes.mypark.businesslogic;

import me.alexanderhodes.mypark.auth.JobAuthentication;
import me.alexanderhodes.mypark.helper.UrlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Configuration()
public class Housekeeping {

    private static final Logger log = LoggerFactory.getLogger(Housekeeping.class);

    @Autowired
    private JobAuthentication jobAuthentication;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UrlHelper urlHelper;

    public Housekeeping() {
    }

    public void doTheJob() {
        log.info("started housekeeping");
        this.removeBookings();
        this.removeAbsences();
        this.removeTokens();
        this.updateParkingSpaces();
        log.info("ended housekeeping");
    }

    // clean booking
    private boolean removeBookings() {
        log.info("started bookings");
        String url = this.urlHelper.createUrlForResource("bookings/system");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, body, String.class);
        log.info("ended bookings");
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    // clean absence
    private boolean removeAbsences() {
        log.info("started absences");
        String url = this.urlHelper.createUrlForResource("absences/system");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, body, String.class);
        log.info("ended absences");
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    // clean token
    private boolean removeTokens() {
        log.info("started tokens");
        String url = this.urlHelper.createUrlForResource("token/system");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, body, String.class);
        log.info("ended tokens");
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    private boolean updateParkingSpaces() {
        log.info("started update parkingspaces");
        String url = this.urlHelper.createUrlForResource("parkingspaces/system/update");

        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, body, String.class);
        log.info("ended update parkingspaces");
        return response.getStatusCode().equals(HttpStatus.OK);
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
