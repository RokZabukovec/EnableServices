package com.fun7api.EnableServices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun7api.EnableServices.Validation.Validation;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;

@Validated
@RestController
public class EnableServicesController {

    @RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> services(@RequestParam(value = "timezone")
                                               @NotBlank(message = "You must provide timezone.")
                                               @NotNull(message = "Timezone can not be empty.")
                                                       String timezone ,

                                           @NotBlank(message = "User id can not be empty.")
                                           @NotNull(message = "You must provide a user id.")
                                           @RequestParam(value = "userid") String userid,

                                           @NotBlank(message = "You must provide country code.")
                                               @NotNull(message = "Country code can not be empty.")
                                               @Size(min = 2, max = 3, message = "Country code must be at least 2 character and not longer than 3.")
                                           @RequestParam(value = "cc") String cc)
            throws IOException {

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
        String missingParameters = "{\"status\": 400, \"message\": \"Missing parameter., \"parameter\": " + name + "\"}";
        return new ResponseEntity<String>(missingParameters, HttpStatus.BAD_REQUEST);

    }

}