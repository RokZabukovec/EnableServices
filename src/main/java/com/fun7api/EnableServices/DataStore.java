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


    public Long createUser(Entity userEntity) {
        try{
             // Key will be assigned once written
            userEntity.setProperty(User.USER_ID, userEntity.getProperty("userid"));
            userEntity.setProperty(User.NUMBER_OF_API_CALLS, 1);
            Key userKey = datastore.put(userEntity); // Save the Entity
            return userKey.getId();
        }catch (DatastoreFailureException e){
            System.out.println("Datastore exception creating a user: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Datastore exception creating a user: " + e.getMessage());
            return null;
        }

    }


    public Entity getUser(String userid){
        try{
            Query q = new Query(USER_ENTITY).addFilter("userid", FilterOperator.EQUAL, userid);
            PreparedQuery pq = datastore.prepare(q);
            Entity user = pq.asSingleEntity();
            if(user != null){
                return user;
            }else{
                Entity userEntity = new Entity(USER_ENTITY);  // Key will be assigned once written
                userEntity.setProperty(User.USER_ID, userid);
                userEntity.setProperty(User.NUMBER_OF_API_CALLS, 1);
                this.createUser(userEntity);
            }
        } catch (DatastoreTimeoutException e) {
            return null;
        }
        return null;
    }
    public User entityToUser(Entity entity) {
        return new User.Builder()
                .numberOfApiCalls((Long) entity.getProperty(User.NUMBER_OF_API_CALLS))
                .userId((String) entity.getProperty(User.USER_ID))
                .build();
    }

    public void updateUser(String userid){
        Entity founduser = this.getUser(userid);
//        Long prevNumOfcalls = user.getNumberOfCalls();
        Long currentNumOfcalls = Long.sum((Long) founduser.getProperty("apiCalls"), 1);
        founduser.setProperty("apiCalls", currentNumOfcalls);
        datastore.put(founduser);

    }


}