package me.alexanderhodes.mypark.businesslogic;

import me.alexanderhodes.mypark.auth.JobAuthentication;
import me.alexanderhodes.mypark.helper.UrlHelper;
import me.alexanderhodes.mypark.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Configuration()
public class ParkingSpaceAssignment {

    private static final Logger log = LoggerFactory.getLogger(ParkingSpaceAssignment.class);

    @Autowired
    private JobAuthentication jobAuthentication;
    @Autowired
    private UrlHelper urlHelper;
    @Autowired
    private RestTemplate restTemplate;

    private BookingStatus[] bookingStatuses;

    public void doTheMagic(LocalDateTime localDateTime) {
        log.info("started parkingspace assignment for {} and weekday {}", localDateTime.toString(),
                localDateTime.getDayOfWeek().getValue());

        int dayOfTheWeek = localDateTime.getDayOfWeek().getValue();
        String date = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString();

        // 0. request bookingstatus
        this.requestBookingStatus();

        // 1. Serienbuchungen abfragen und Buchungen erzeugen
        SeriesBooking[] seriesBookings = this.requestSeriesBookings(dayOfTheWeek);

        for (SeriesBooking seriesBooking : seriesBookings) {
            this.createBooking(seriesBooking);
        }

        // 2. Serienabwesenheiten abfragen und Abwesenheiten erzeugen
        SeriesAbsence[] seriesAbsences = this.requestSeriesAbsences(dayOfTheWeek);

        for (SeriesAbsence seriesAbsence : seriesAbsences) {
            this.createAbsence(seriesAbsence);
        }

        // 5. Parkplätze vergeben
        this.assignParkingSpaces(date);
    }

    // 1. Serienbuchungen abfragen und Buchungen erzeugen
    public SeriesBooking[] requestSeriesBookings(int weekDay) {
        String url = this.urlHelper.createUrlForResource("seriesbookings/system/" + weekDay);
        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<SeriesBooking[]> response = restTemplate.exchange(url, HttpMethod.GET, body,
                SeriesBooking[].class);
        System.out.println("length: " + response.getBody().length);
        return response.getBody();
    }

    public void createBooking(SeriesBooking seriesBooking) {
        Booking booking = new Booking();

        LocalDate localDate = this.createLocalDateForDayOfTheWeek(seriesBooking.getWeekday());
        LocalDateTime localDateTime = localDate.atStartOfDay();
        localDateTime = localDateTime.withHour(seriesBooking.getTime().getHour());
        localDateTime = localDateTime.withMinute(seriesBooking.getTime().getMinute());

        booking.setUser(seriesBooking.getUser());
        booking.setParkingSpace(null);
        booking.setBookingStatus(null);
        booking.setDate(localDateTime);

        String url = this.urlHelper.createUrlForResource("bookings");
        HttpEntity<Booking> body = this.prepareHeader(booking);
        ResponseEntity<Booking> responseEntity = restTemplate.exchange(url, HttpMethod.POST, body, Booking.class);
        System.out.println("CREATE BOOKING - StatusCode: " + responseEntity.getStatusCode().value());
    }

    // 2. Serienabwesenheiten abfragen und Abwesenheiten erzeugen
    public SeriesAbsence[] requestSeriesAbsences(int weekDay) {
        String url = this.urlHelper.createUrlForResource("seriesabsences/system/" + weekDay);
        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<SeriesAbsence[]> response = restTemplate.exchange(url, HttpMethod.GET, body,
                SeriesAbsence[].class);
        System.out.println("length: " + response.getBody().length);
        return response.getBody();
    }

    public void createAbsence(SeriesAbsence seriesAbsence) {
        Absence absence = new Absence();

        LocalDate localDate = this.createLocalDateForDayOfTheWeek(seriesAbsence.getWeekday());

        absence.setUser(seriesAbsence.getUser());
        absence.setStart(localDate);
        absence.setEnd(localDate);

        String url = this.urlHelper.createUrlForResource("absences");
        HttpEntity<Absence> body = this.prepareHeader(absence);
        ResponseEntity<Absence> responseEntity = restTemplate.exchange(url, HttpMethod.POST, body, Absence.class);
        System.out.println("CREATE ABSENCE - StatusCode: " + responseEntity.getStatusCode().value());
    }

    // 3. Freie Parkplätze ermitteln
    public ParkingSpace[] requestFreeParkingSpaces(String day) {
        String url = this.urlHelper.createUrlForResource("parkingspaces/system/free/" + day);
        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<ParkingSpace[]> response = restTemplate.exchange(url, HttpMethod.GET, body, ParkingSpace[].class);
        System.out.println("length: " + response.getBody().length);
        return response.getBody();
    }

    // 4. Buchungen ermitteln
    public Booking[] requestBookings(String day) {
        String url = this.urlHelper.createUrlForResource("bookings/system/" + day);
        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<Booking[]> response = restTemplate.exchange(url, HttpMethod.GET, body, Booking[].class);
        System.out.println("length: " + response.getBody().length);
        return response.getBody();
    }

    // 5. Parkplätze vergeben
    private void assignParkingSpaces(String date) {
        // 3. Freie Parkplätze ermitteln
        ParkingSpace[] parkingSpaces = this.requestFreeParkingSpaces(date);
        // 4. Buchungen ermitteln
        Booking[] bookings = this.requestBookings(date);

        if (parkingSpaces.length > bookings.length) {
            // es gibt mehr freie Parkplätze als Buchungen
            for (int i = 0; i < parkingSpaces.length && i < bookings.length; i++) {
                BookingStatus bookingStatus = this.getBookingStatus(BookingStatus.CONFIRMED);

                bookings[i].setParkingSpace(parkingSpaces[i]);
                // set booking status
                bookings[i].setBookingStatus(bookingStatus);
            }

            // send to backend
            for (Booking booking : bookings) {
                this.updateBooking(booking);
            }
        } else {
            // es gibt weniger freie Parkplätze als Buchungen -> random sort
            int i;

            for (i = 0; i < parkingSpaces.length; i++) {
                int index = createRandomIndex(bookings.length);

                while (bookings[index].getParkingSpace() != null) {
                    index = createRandomIndex(bookings.length);
                }

                BookingStatus bookingStatus = this.getBookingStatus(BookingStatus.CONFIRMED);
                bookings[index].setParkingSpace(parkingSpaces[i]);
                // set booking status
                bookings[index].setBookingStatus(bookingStatus);
            }

            for (int j = 0; j < bookings.length; j++) {
                if (bookings[i].getParkingSpace() == null) {
                    BookingStatus bookingStatus = this.getBookingStatus(BookingStatus.DECLINED);
                    // set booking status
                    bookings[i].setBookingStatus(bookingStatus);
                }
            }

            // send to backend
            for (Booking booking : bookings) {
                this.updateBooking(booking);
            }
        }
    }

    private int createRandomIndex(int max) {
        Random random = new Random();
        int index = random.nextInt(max);
        return index;
    }

    private void updateBooking(Booking booking) {
        String url = this.urlHelper.createUrlForResource("bookings/" + booking.getId());

        HttpEntity<Booking> body = this.prepareHeader(booking);
        ResponseEntity<Booking> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, body, Booking.class);
        System.out.println("UPDATE - StatusCode: " + responseEntity.getStatusCode().value());
    }

    private void requestBookingStatus() {
        String url = this.urlHelper.createUrlForResource("bookingstatus");
        HttpEntity<String> body = this.prepareHeader();
        ResponseEntity<BookingStatus[]> response = restTemplate.exchange(url, HttpMethod.GET, body, BookingStatus[].class);
        System.out.println("length: " + response.getBody().length);
        this.bookingStatuses = response.getBody();
    }

    private HttpEntity<String> prepareHeader() {
        HttpHeaders headers = this.createHttpHeaders();
        return new HttpEntity<String>("", headers);
    }

    private HttpEntity<Booking> prepareHeader(Booking entity) {
        HttpHeaders headers = this.createHttpHeaders();
        return new HttpEntity<Booking>(entity, headers);
    }

    private HttpEntity<Absence> prepareHeader(Absence entity) {
        HttpHeaders headers = this.createHttpHeaders();
        return new HttpEntity<Absence>(entity, headers);
    }

    private HttpHeaders createHttpHeaders() {
        String token = this.jobAuthentication.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        return headers;
    }

    private LocalDate createLocalDateForDayOfTheWeek(int dayOfTheWeek) {
        LocalDate localDate = LocalDate.now();
        while (localDate.getDayOfWeek().getValue() != dayOfTheWeek) {
            localDate = localDate.plusDays(1);
        }
        return localDate;
    }

    private BookingStatus getBookingStatus(String name) {
        if (this.bookingStatuses != null) {
            for (BookingStatus bookingStatus : this.bookingStatuses) {
                if (bookingStatus.getName().equals(name)) {
                    return bookingStatus;
                }
            }
        }
        return null;
    }

}
