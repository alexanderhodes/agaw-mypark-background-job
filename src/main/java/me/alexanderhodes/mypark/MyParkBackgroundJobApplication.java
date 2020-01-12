package me.alexanderhodes.mypark;

import me.alexanderhodes.mypark.auth.JobAuthentication;
import me.alexanderhodes.mypark.businesslogic.Housekeeping;
import me.alexanderhodes.mypark.businesslogic.ParkingSpaceAssignment;
import me.alexanderhodes.mypark.helper.UrlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class MyParkBackgroundJobApplication {

    private static final Logger log = LoggerFactory.getLogger(MyParkBackgroundJobApplication.class);

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
            log.info("Started at {}", new Date());
            // AUTHENTICATION
            this.jobAuthentication.authenticate();

            LocalDateTime localDateTime = LocalDateTime.now();

            int dayOfTheWeek = localDateTime.getDayOfWeek().getValue();

            if (dayOfTheWeek <= 5) {
                localDateTime = dayOfTheWeek == 5 ? localDateTime.plusDays(1) : localDateTime.plusDays(3);


            }

            // HOUSEKEEPING
            this.housekeeping.doTheJob();

            // PARKINGSPACE ASSIGNMENT
            this.parkingSpaceAssignment.doTheMagic();

            // Montag -> 1, Sonntag -> 7
        };
    }

}
