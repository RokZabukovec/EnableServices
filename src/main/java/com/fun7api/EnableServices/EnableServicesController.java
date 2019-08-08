package com.fun7api.EnableServices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

@RestController
public class EnableServicesController {

    @RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> services(@RequestParam(value = "timezone") String timezone,
                                           @RequestParam(value = "userid") String userid,
                                           @RequestParam(value = "cc") String cc)
            throws JsonProcessingException {

            EnableServices services = new EnableServices();
            services.setMultiplayer(userid, cc);
            services.setAds(cc);
            services.setCustomerSupport(timezone);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String jsonString = mapper.writeValueAsString(services);

            return new ResponseEntity<String>(jsonString, HttpStatus.OK);
        }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String missingParameters = "{\"status\": 400, \"message\": \"Missing parameter " + name + "\"}";
        return new ResponseEntity<String>(missingParameters, HttpStatus.BAD_REQUEST);

    }

}