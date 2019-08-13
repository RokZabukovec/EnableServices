# Junior Software Engineer Expertise Test: Fun7 API

Spring Boot API for Fun7 game backend services: multiplayer, ads and customer support.

The API is built using Java Spring Boot Framework and exposes single endpoint which accepts
three parameters:

- timezone: timezone of the user
- userid: string id of the user
- cc: country code of the user

- API ENDPOINT: {BASE_URL}/api/services?timezone={timezone}&userid={userid}?cc={country code}
- TYPE_OF_RESPONSE_DATA: JSON {“multiplayer”: true, ”userSupport”: false, “ads”: true}

_INFO_ HTTP request to the API endpoint results in json response with statuses of the game services.

## Requirements

- Java 8+
- Maven
- Appengine Standard Java 8+
- Spring boot 2.x.x

## Getting Started

I assume that you have installed the Java SE 8 Development Kit (JDK). Otherwise download it on
[JDK website](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

Next, you must install the Cloud SDK and then set up a GCP project for App Engine.

1. [Download](https://cloud.google.com/sdk/docs/) and install Cloud SDK

   or update it with `cloud components update`

2. Create a new project:

   `gcloud projects create [YOUR_PROJECT_ID] --set-as-default`

   Verify the project was created:

   `gcloud projects describe [YOUR_PROJECT_ID]`

   ````createTime: year-month-hour
    lifecycleState: ACTIVE
    name: project-name
    parent:
    id: '433637338589'
    type: organization
    projectId: project-name-id
    projectNumber: 499227785679```
   ````

3. Initialize your App Engine app with your project and choose its region:

   `gcloud app create --project=[YOUR_PROJECT_ID]`

4. Install the following prerequisites:

- Install the App Engine component:
  `gcloud components install app-engine-java`
- [Install Maven: version 3.5 or greater](http://maven.apache.org/)

### Cloning the application to your local machine

1. Clone the repository containing the project
   `git clone https://github.com/RokZabukovec/EnableServices.git`

2. Change to the directory that contains the sample code:

   `cd EnableServices`

### Running application on your local machine

1. Start the local web server using the app engine Maven plugin:
   Make sure that you don't have another server running on the port: 8080

   `mvn appengine:run`

2. In your browser navigate to the address url with test parameter data
   <http://localhost:8080?timezone=Europe/London&userid=user1&cc=us>

### Deploying and running Fun7 API on App Engine

1. You can deploy application to App Engine from EnableServices directory by running:

   `mvn appengine:deploy`

2. Now the applocation is deployed to Google App Engine server on URL: http://YOUR_PROJECT_ID.appspot.com.

   You can view it by running:

   `mvn appengine:browse`

## Classes

#### Controller: ./EnableServiceController

In the EnableServiceController class, which is annotated with @Validated and @RestController.

GET request is mapped by the public method services with mathod parameters annotated with @RequestParam. The value of @RequestParam is name of the parameter passed throu URl. Parameters are validated with Anotations @NotBlank, @NotNull and @Size

Method return type is ResponseEntity which is a shorthand for creating a response with the body and a status code.

```Java
    @GetMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> services(@RequestParam(value = "timezone")
                                               @NotBlank(message = "You must provide timezone.")
                                               @NotNull(message = "Timezone can not be empty.") String timezone ,

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
```

#### Models: models/User.java

Containes getter and setters for the users properties:

```Java
    private String userid;
    private Long numberOfApiCalls;
```

Containes three constructors for generating an object in three different ways:

```Java
// Generate object from method  parameters
    public User(String userid, Long numberOfApiCalls) {
        this.userid = userid;
        this.numberOfApiCalls = numberOfApiCalls;
    }
// Generate object with Builder class chaining methods
    private User(Builder builder) {
        this.userid = builder.userid;
        this.numberOfApiCalls = builder.numberofApiCalls;
    }
    // Generate object with setter methods
    public User() {}
```

Containes static class Builder which is used in creating user object with chaining methods:

```Java
    public static class Builder{
        private String userid;
        private Long numberofApiCalls;


        public Builder userId(String userid) {
            this.userid = userid;
            return this;
        }

        public Builder numberOfApiCalls(Long numberofApiCalls) {
            this.numberofApiCalls = numberofApiCalls;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
```

#### DataStore: DataStore.java

DataStore is a public class which is responsible for manipulating with user entities in the Google App Engine Datastore(NoSQL)

public class DataStore containes a constructor:

```Java
    public DataStore() {
        datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
    }
```

- Long createUser(Entity userEntity) - creates a new Entity
- public Entity getUser(String userid) - returns Entity object from a string userid. If user not found
  it's calling createUser method.
- public User entityToUser(Entity entity) - uses Builder class for transforming Entity to User object
- public void newApiCall - updates the users a number of api calls info.

#### DataStore: EnableServices.java

EnableServices is a public class which is responsible for manipulation of passed parameters and returning response base on the logic.

**Multiplayer**

Multiplayer is a feature that is available only for more skilled players so it should be enabled if user has used “Fun7” game more than 5 times (based on the number of API calls). Also our multiplayer server infrastructure is located in the US so it should be enabled only if the user comes from the US.

**Customer Support**

Customer support should be enabled only on work days between 9:00 - 15:00 Ljubljana time, because only then support personal is available.

**Ads**

Ads in the game are served by the external partner so this service should be enabled only if our external partner supports user device. And to know it, we must call partner’s public API which is already provided and is secured by basic access authentication (via HTTP header).

Outgoing API request in ads method is using Basic Authentication with username and password.

Author: **Rok Zabukovec**
