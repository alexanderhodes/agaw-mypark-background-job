package me.alexanderhodes.mypark;

import me.alexanderhodes.mypark.auth.JobAuthentication;
import me.alexanderhodes.mypark.businesslogic.Housekeeping;
import me.alexanderhodes.mypark.businesslogic.ParkingSpaceAssignment;
import me.alexanderhodes.mypark.helper.UrlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MyParkBackgroundJobApplication {

    @Autowired
    private UrlHelper urlHelper;
    @Autowired
    private JobAuthentication jobAuthentication;
    @Autowired
    private Housekeeping housekeeping;
    @Autowired
    private ParkingSpaceAssignment parkingSpaceAssignment;

    public static void main(String[] args) {
        SpringApplication.run(MyParkBackgroundJobApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner commandLineRunner(RestTemplate restTemplate) {
        return args -> {
            this.jobAuthentication.authenticate();
            System.out.println("token: " + this.jobAuthentication.getToken());

            this.parkingSpaceAssignment.doTheMagic();

            // Montag -> 1, Sonntag -> 7

//            String url = this.urlHelper.getBackendUrl();
//            System.out.println("Backend Url: " + url);
//
//            ResponseEntity<Auth> response = this.jobAuthentication.authenticate();
//
//            System.out.println("Status Code: " + response.getStatusCode().name());
//            System.out.println("Response: " + response.getBody());
//            Auth auth = response.getBody();
//            System.out.println("token: " + auth.getToken());
//            String token = this.jobAuthentication.getToken();
//
//            System.out.println("jobAuthentication-token: " + token);
//
//            parkingSpaceAssignment.requestSeriesBookings();
//            parkingSpaceAssignment.requestSeriesAbsences();
//            parkingSpaceAssignment.requestFreeParkingSpaces();
//            parkingSpaceAssignment.requestBookings();
        };
    }

}
