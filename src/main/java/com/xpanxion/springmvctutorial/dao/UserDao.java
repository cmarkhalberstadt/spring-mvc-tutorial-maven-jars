package com.xpanxion.springmvctutorial.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.xpanxion.springmvctutorial.dto.beans.UserBean;
import com.xpanxion.springmvctutorial.dto.entity.UserEntity;

/**
 * Interface for the User Dao
 * 
 * @author mhalberstadt
 *
 */
public interface UserDao {
	/**
     * Returns all of the User entities.
     * 
     * @return all of the User entities. 
     */
    List<UserEntity> getAllItems();
    
    
    /**
   	 * returns a UserBean for a user in the database with the given username.
   	 * @param Username - username to be found in the database
   	 * @return - Userbean object of user in the database
   	 */
    public UserBean getUserWithUsername(String Username);
    
    /**
	 * Changes the password of a given user in the database.
	 * @param Username - username of user to change password for
	 * @param newPassword - new password to be set
	 */
    public void changePasswordOfUser(String Username, String newPassword);
    
    /**
	 * adds a new user to the database.
	 * @param Username - Username to be added to the database
	 * @param Password - password of the user to be added into the database
	 */
    public void addUserToDatabase(String Username, String Password);
    
    /**
	 * Deletes a given user from the database.
	 * @param Username - username of the user to be deleted
	 */
    public void deleteUserFromDatabase(String Username);
    
    /**
	 * Returns true if a given user is in the database. Otherwise, returns false.
	 * @param Username - username to check if it is in the database
	 * @return - boolean value - true if username is in the database - false otherwise
	 */
    public boolean isUsernameInDatabase(String Username);
    
    /**
	 * Returns true if the username and password match for a given user record in the database. 
	 * @param Username - Username to be checked
	 * @param Password - password to be checked
	 * @return - boolean value - true if username and password match for the user record in the database - false otherwise
	 */
    public boolean isPasswordCorrectForGivenUsername(String Username, String Password);
}
