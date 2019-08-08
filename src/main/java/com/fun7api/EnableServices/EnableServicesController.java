package com.fun7api.EnableServices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class EnableServicesController {

    @RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public String services(@RequestParam(value = "timezone") String timezone,
            @RequestParam(value = "userid") String userid, @RequestParam(value = "cc") String cc)
            throws JsonProcessingException {
        EnableServices services = new EnableServices();
        services.setMultiplayer(userid, cc);
        services.setAds(cc);
        services.setCustomerSupport(timezone);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(services);
        return jsonString;
    }

    @RequestMapping(value = "/hello", produces = MediaType.TEXT_HTML_VALUE)
    public String services() throws JsonProcessingException {
        String hello = "Hello";
        return hello;
    }

}