package com.fun7api.EnableServices;


import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.fun7api.EnableServices.models.User;
import javax.servlet.ServletException;
import java.util.Base64;


public class DataStore{
    private DatastoreService datastore;
    private static final String USER_ENTITY = "Users";

    public DataStore() {
        datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
    }


    public User createUser(String userid, int numOfApiCalls) {
        try{
            User newUser = new User(userid, numOfApiCalls);
            Entity userEntity = new Entity(USER_ENTITY);  // Key will be assigned once written
            userEntity.setProperty(User.USER_ID, newUser.getUserid());
            userEntity.setProperty(User.NUMBER_OF_API_CALLS, newUser.getNumberOfCalls());
            Key userKey = datastore.put(userEntity); // Save the Entity
            return newUser;
        }catch (DatastoreFailureException e){
            System.out.println("Datastore exception creating a user: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Datastore exception creating a user: " + e.getMessage());
            return null;
        }

    }

    public User getUser(String userid){
        try{
            Query q = new Query(USER_ENTITY).addFilter("userid", FilterOperator.EQUAL, userid);
            PreparedQuery pq = datastore.prepare(q);
            Entity user = pq.asSingleEntity();
            if(user != null){
                User foundUser = new User();
                foundUser.setUserid((String)user.getProperty("userid"));
                foundUser.setNumberOfCalls(Math.toIntExact((Long) user.getProperty("apiCalls")));
                return foundUser;
            }else{
                return this.createUser(userid, 0);
            }
        } catch (DatastoreTimeoutException e) {
           return null;
        } catch (Exception e) {
            return null;
        }


    }

    public void updateUser(User user) {
            Key key = KeyFactory.createKey(USER_ENTITY, user.getUserid());  // From a user, create a Key
            Entity newUser = new Entity(key);         // Convert user to an Entity
            newUser.setProperty(User.USER_ID, user.getUserid());
            newUser.setProperty(User.NUMBER_OF_API_CALLS, user.getNumberOfCalls() + 1);
            datastore.put(newUser);                   // Update the Entity

    }


}
