package com.xpanxion.springmvctutorial.dao;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.xpanxion.springmvctutorial.dto.beans.ChangeUserPasswordBean;
import com.xpanxion.springmvctutorial.dto.beans.UserBean;
import com.xpanxion.springmvctutorial.dto.entity.UserEntity;

/**
 * Implementation of the UsersRESTDao interface. 
 * 
 * @author mhalberstadt
 *
 */
@Repository
public class RestApiDaoImpl implements UserDao {
	
	// need the complete URL to access the rest service
	private final String startingURL = "http://localhost:8080/restservice";
	
	// the rest template object used to make rest calls
	@Autowired
	private RestTemplate restTemplate;
	
	/*
	*//**
	 * Sets the restTemplate object
	 * @param restTemplate - Rest Template object to be used
	 *//*
	@Resource
	public void setRestTemplate(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}
	
	*/
	
	
	/**
	 * This method returns a list of all the users in the database through a call to the Rest API
	 * @return - List of UserEntities for all users in the database
	 */
	@Override
	public List<UserEntity> getAllItems() {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("All users list obtained through REST API");
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.startingURL);
		stringBuffer.append("/api/users");
		String url = stringBuffer.toString();
		
		ArrayList output = this.restTemplate.getForObject(url, ArrayList.class);
		ArrayList<UserEntity> retval = new ArrayList<UserEntity>();
		
		// declare variables outside of FOR loop
		UserEntity entityToAddToReturnList = null;
		UserBean  userBeanRestCallOutput= null;
		for (Object objectToBeConvertedToUserBean: output){
			userBeanRestCallOutput = mapper.convertValue(objectToBeConvertedToUserBean, UserBean.class);
			entityToAddToReturnList = new UserEntity();
			entityToAddToReturnList.setId(userBeanRestCallOutput.getId());
			entityToAddToReturnList.setPassword(userBeanRestCallOutput.getPassword());
			entityToAddToReturnList.setUsername(userBeanRestCallOutput.getUsername());
			retval.add(entityToAddToReturnList);
		}
		return retval;
		
	}
	
	/**
	 * returns a UserBean for a user in the database with the given username through a call to the Rest API.
	 * @param Username - username to be found in the database
	 * @return - Userbean object of user in the database
	 */
	@Override
	public UserBean getUserWithUsername(String Username) {
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.startingURL);
		stringBuffer.append("/api/user/");
		stringBuffer.append(Username);
		String url = stringBuffer.toString();
		
		return this.restTemplate.getForObject(url, UserBean.class);
	}
	
	/**
	 * Changes the password of a given user in the database through a call to the rest API
	 * @param Username - username of user to change password for
	 * @param newPassword - new password to be set
	 */
	@Override
	public void changePasswordOfUser(String Username, String newPassword) {
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.startingURL);
		stringBuffer.append("/api/user/");
		stringBuffer.append(Username);
		String url = stringBuffer.toString();
		
		ChangeUserPasswordBean b = new ChangeUserPasswordBean();
		b.setNewpassword(newPassword);
		UserBean fromDB = this.getUserWithUsername(Username);
		b.setOldpassword(fromDB.getPassword());
		this.restTemplate.put(url, b);
	}
	
	/**
	 * adds a new user to the database through a call to the Rest API
	 * @param Username - Username to be added to the database
	 * @param Password - password of the user to be added into the database
	 */
	@Override
	public void addUserToDatabase(String Username, String Password) {
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.startingURL);
		stringBuffer.append("/api/user");
		String url = stringBuffer.toString();
		
		UserBean toAdd = new UserBean();
		toAdd.setPassword(Password);
		toAdd.setUsername(Username);
		this.restTemplate.postForEntity(url, toAdd, UserBean.class);
	}
	
	/**
	 * Deletes a given user from the database through a call to the Rest API
	 * @param Username - username of the user to be deleted
	 */
	@Override
	public void deleteUserFromDatabase(String Username) {
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.startingURL);
		stringBuffer.append("/api/user/");
		stringBuffer.append(Username);
		String url = stringBuffer.toString();
		
		this.restTemplate.delete(url);
	}
	
	/**
	 * Returns true if a given user is in the database. Otherwise, returns false.
	 * Executed through a call to the Rest API
	 * @param Username - username to check if it is in the database
	 * @return - boolean value - true if username is in the database - false otherwise
	 */
	@Override
	public boolean isUsernameInDatabase(String Username) {
		UserBean fromDatabase = this.getUserWithUsername(Username);
		if (fromDatabase == null){
			return false;
		} else {
			String passwordFromDatabase = fromDatabase.getPassword();
			if (passwordFromDatabase.isEmpty()){
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Returns true if the username and password match for a given user record in the database. 
	 * Executed through a call to the Rest API
	 * @param Username - Username to be checked
	 * @param Password - password to be checked
	 * @return - boolean value - true if username and password match for the user record in the database - false otherwise
	 */
	@Override
	public boolean isPasswordCorrectForGivenUsername(String Username,
			String Password) {
		UserBean fromDatabase = this.getUserWithUsername(Username);
		if (fromDatabase == null){
			return false;
		} else {
			String passwordFromDatabase = fromDatabase.getPassword();
			if (passwordFromDatabase.isEmpty()){
				return false;
			} else {
				if (passwordFromDatabase.equals(Password)){
					return true;
				} else {
					return false;
				}
			}
		}
	}

	
	
}
