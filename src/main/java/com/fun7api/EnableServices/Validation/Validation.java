package com.fun7api.EnableServices.Validation;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Validation {

    @NotBlank(message = "User id can not be empty.")
    @NotNull(message = "You must provide a user id.")
    private String userid;


    @NotBlank(message = "You must provide country code.")
    @NotNull(message = "Country code can not be empty.")
    @Size(min = 2, max = 3, message = "Country code must be at least 2 character and not longer than 3.")
    private String cc;

    @NotBlank(message = "You must provide timezone.")
    @NotNull(message = "Timezone can not be empty.")
    private String timezone;
}
