package com.fun7api.EnableServices;

import com.fun7api.EnableServices.models.User;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class EnableServices {
    private boolean multiplayer;
    private boolean ads;
    private boolean customerSupport;

    public EnableServices() {
        this.multiplayer = false;
        this.ads = false;
        this.customerSupport = false;
    }

    /*
     * Changes the default false multiplayer to enabled based on location and number
     * of API calls fro user.
     *
     * @param String userID - string representation of user id.
     *
     * @param String countryCode - Country code for where the user makes the call.
     *
     */
    public void setMultiplayer(String userid, String countryCode){
        int numOfApi;
        DataStore db = new DataStore();
        User user = db.getUser(userid);
        if(user != null){
            if (user.getNumberOfCalls() < 4) {
                db.updateUser(user);
            }else if(user.getNumberOfCalls() >= 4 && countryCode.equals("us")){
                this.multiplayer = true;
            }
        }


    }

    /*
     * Calls partners api endpoint to check if device is supported.
     *
     * @param String cc - Country code of the user.
     */
    public ResponseEntity<String> setAds(String countryCode) {
        if (this.isValidCountryCode(countryCode)) {


            final String API_URL = "https://us-central1-o7tools.cloudfunctions.net/fun7-ad-partner?countryCode="
                    + countryCode;
            final String REQUEST_METHOD = "GET";
            final String USERNAME = "fun7user";
            final String PASSWORD = "fun7pass";


            HttpsURLConnection connection = null;
            try {
                URL url = new URL(API_URL);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setRequestProperty("Accept", "application/json");
                String usernameColonPassword = USERNAME + ":" + PASSWORD;
                String basicAuthPayload = "Basic "  + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
                // Include the HTTP Basic Authentication payload
                connection.addRequestProperty("Authorization", basicAuthPayload);
                // Read response from web server, which will trigger HTTP Basic Authentication
                // request to be sent.

                BufferedReader httpResponseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String response;
                while ((response = httpResponseReader.readLine()) != null) {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("ads")) {
                        String ads = jsonResponse.get("ads").toString();
                        if (ads.equals("sure, why not!")) {
                            this.ads = true;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }finally {
                connection.disconnect();
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
     * calculates the time in Ljubljana-Slovenia based on the timezone.
     *
     * @param String timezone -
     * http://tutorials.jenkov.com/java-date-time/java-util-timezone.html
     *
     */
    public void setCustomerSupport(String timezone) {
        if (this.isSupportWorking(timezone)) {
            this.customerSupport = true;
        }
    }


    public boolean isSupportWorking(String timezone) {
        // Time zone codes
        final ZoneId supportTeamLocation = ZoneId.of("Europe/Ljubljana");
        if(isValidTimeZone(timezone)){
            ZoneId usersLocation = ZoneId.of(timezone);
            // Calculates the time in Ljubljana based on the time zone of the user.
            LocalDateTime zonetimeLj = LocalDateTime.now(supportTeamLocation);
            LocalDateTime zonetimeUser = LocalDateTime.now(usersLocation);
            int diff = (int) Duration.between(zonetimeLj, zonetimeUser).toHours();
            LocalTime timeInLj = LocalTime.from(zonetimeUser.minusHours(diff));

            // Customer support working hours.
            LocalTime workStart = LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime workEnd = LocalTime.parse("15:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));

            // Checking if users time in working hours of support team in Ljubljana.
            boolean start = workStart.isBefore(timeInLj);
            boolean end = workEnd.isAfter(timeInLj);
            boolean isSupportWorking = start && end;
            return isSupportWorking;
        }else{
            System.out.println("Timezone is not valid!");
            return false;
        }
    }

    /*
     * Checks if the string parameter cc is a valid two country code
     *
     * @param String cc - two letter country code
     *
     * @return boolean isValid - returns true if supplied country code is valid.
     */
    public boolean isValidCountryCode(String cc) {
        boolean isValid = false;
        // Map ISO countries to custom country object
        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);
            String code = locale.getCountry();
            if (countryCode.equals(cc.toUpperCase())) {
                System.out.println(code);
                isValid = true;
            }
        }
        return isValid;
    }

    public boolean isValidTimeZone(String timezone) {
        return Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone);
    }


}
