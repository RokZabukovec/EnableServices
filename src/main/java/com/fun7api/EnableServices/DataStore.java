package com.fun7api.EnableServices;


import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.fun7api.EnableServices.models.User;


public class DataStore{
    private DatastoreService datastore;
    private static final String USER_ENTITY = "Users";

    public DataStore() {
        datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
    }

    /*
     * Name: createUser
     * Description: Inserts new entity in the datastore.
     *
     * @param Entity
     * @return created users key.
     */
    public Long createUser(Entity userEntity) {
        try{
             // Key will be assigned once written
            userEntity.setProperty(User.USER_ID, userEntity.getProperty("userid"));
            userEntity.setProperty(User.NUMBER_OF_API_CALLS, 0);
            Key userKey = datastore.put(userEntity); // Save the Entity
            return userKey.getId();
        }catch (DatastoreFailureException e){
            System.out.println("Datastore exception creating a user: " + e.getMessage());
            return null;
        }

    }

    /*
     * Name: getUser
     * Description: Finds user based on string user id from datastore.
     * If user is nout found it creates a new one.
     *
     * @String userid
     * @return Entity entity.
     */
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
                userEntity.setProperty(User.NUMBER_OF_API_CALLS, 0);
                this.createUser(userEntity);
                return userEntity;
            }
        } catch (DatastoreTimeoutException e) {
            return null;
        }
    }

    /*
     * Name: entityToUser
     * Description: Builds new User object from entity using static class Builder located in User class.
     *
     * @param Entity
     * @return created users key.
     */
    public User entityToUser(Entity entity) {
        return new User.Builder()
                .numberOfApiCalls(((Number) entity.getProperty(User.NUMBER_OF_API_CALLS)).longValue())
                .userId((String) entity.getProperty(User.USER_ID))
                .build();
    }
    /*
     * Name: newApiCall
     * Description: Increments users number of api calls count. Datastore field name: apiCalls.
     *
     * @param String - userid
     * @return void
     */
    public void newApiCall(String userid){
        Entity founduser = this.getUser(userid);
        Long currentNumOfcalls = Long.sum((Long) founduser.getProperty("apiCalls"), 1);
        founduser.setProperty("apiCalls", currentNumOfcalls);
        datastore.put(founduser);

    }


}