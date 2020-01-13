package me.alexanderhodes.mypark;

import me.alexanderhodes.mypark.auth.JobAuthentication;
import me.alexanderhodes.mypark.businesslogic.Housekeeping;
import me.alexanderhodes.mypark.businesslogic.ParkingSpaceAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@SpringBootApplication
public class MyParkBackgroundJobApplication {

    private static final Logger log = LoggerFactory.getLogger(MyParkBackgroundJobApplication.class);

    @Autowired
    private JobAuthentication jobAuthentication;
    @Autowired
    private Housekeeping housekeeping;
    @Autowired
    private ParkingSpaceAssignment parkingSpaceAssignment;

    @Value("${mypark.job.parkingspace}")
    private int hourParkingSpaceAssignment;
    @Value("${mypark.job.housekeeping}")
    private int hourHousekeeping;

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
            LocalTime localTime = localDateTime.toLocalTime();

            int dayOfTheWeek = localDateTime.getDayOfWeek().getValue();
            log.info("for day of the week {}", dayOfTheWeek);

            if (dayOfTheWeek <= 5) {
                localDateTime = dayOfTheWeek == 5 ? localDateTime.plusDays(1) : localDateTime.plusDays(3);

                if (localTime.getHour() == this.hourParkingSpaceAssignment) {
                    log.info("in ParkingSpaceAssignment {}", new Date());
                    // PARKINGSPACE ASSIGNMENT
                    this.parkingSpaceAssignment.doTheMagic(localDateTime);
                }
            }

            if (localTime.getHour() == this.hourHousekeeping) {
                log.info("in Housekeeping {}", new Date());
                // HOUSEKEEPING
                this.housekeeping.doTheJob();
            }
        };
    }

}
