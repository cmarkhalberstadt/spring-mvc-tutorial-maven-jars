package com.xpanxion.springmvctutorial.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.xpanxion.springmvctutorial.dto.beans.UserBean;
import com.xpanxion.springmvctutorial.dto.entity.UserEntity;

/**
 * Implementation of the UserDao interface. 
 * 
 * @author mhalberstadt
 *
 */
@Repository
public class UserDaoImpl implements UserDao  {
	
	private SessionFactory sessionFactory;
	
	private final String SQLQueryForGetAllItems = "SELECT * FROM usernamesandpasswords";
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UserEntity> getAllItems() {
		//return this.sessionFactory.getCurrentSession().getNamedQuery("userNamesAndPasswords.getAll").list();
		System.out.println("All users list obtained directly using hibernate.");
		ArrayList output = this.runSQLQueryAndGetReturnList(this.SQLQueryForGetAllItems);
		ArrayList<UserEntity> retval = new ArrayList<UserEntity>();
		Object[] objArray = null;
		UserEntity toAddToReturnValue = null;
		for (Object fromOutput:output){
			objArray = (Object[])fromOutput;
			toAddToReturnValue = new UserEntity();
			toAddToReturnValue.setUsername((String)objArray[0]);
			toAddToReturnValue.setPassword((String)objArray[1]);
			toAddToReturnValue.setId(((Integer)objArray[2]).longValue());
			retval.add(toAddToReturnValue);
		}
		
		// sort the list based on the ID number
		Collections.sort(retval);
		
		// return the sorted list
		return retval;
	}
	
	/**
	 * Runs a SQL Query and returns an array list of Objects from the database
	 * @param SQLQuery - SQL Query to be run
	 * @return - An ArrayList of Objects returned from the database
	 */
	private ArrayList<Object> runSQLQueryAndGetReturnList(String SQLQuery){
    	Session session = this.sessionFactory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
		} catch (HibernateException ex){
			System.err.println("Exception thrown when beginning transaction: " + ex);
		}
		
		
		
		ArrayList returnValue = (ArrayList) session.createSQLQuery(SQLQuery).list();
		
		try {
			if (tx != null){
				tx.commit();
			}
		} catch (HibernateException ex){
			System.err.println("Exception thrown when committing transaction: " + ex);
			if (tx != null){
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return returnValue;
    }
    
	/**
	 * Runs a SQL query on the database without returning a value from the database.
	 * The int value returned indicates the status of the SQL Query
	 * @param SQLQuery - String of SQL Query to be run
	 * @return - the int value indicating the status of the SQL Query
	 */
    private int runSQLQueryWithNoReturnValue(String SQLQuery){
    	Session session = this.sessionFactory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
		} catch (HibernateException ex){
			System.err.println("Exception thrown when beginning transaction: " + ex);
		}
		
		
		
		int returnValue = session.createSQLQuery(SQLQuery).executeUpdate();
		
		try {
			if (tx != null){
				tx.commit();
			}
		} catch (HibernateException ex){
			System.err.println("Exception thrown when committing transaction: " + ex);
			if (tx != null){
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return returnValue;
    }
    
    /**
     * Returns the id for the given username. If the username is not in the database, -1 is returned
     * @param username - username for which id should be returned
     * @return - long value - the ID for the given username in the database or -1 if the username is not in the database
     */
    private long getIDForGivenUsernameInDatabase(String username){
    	/*String tableName = "usernamesandpasswords";
		String SQLQuery = "SELECT id FROM " + tableName + "\n";
		SQLQuery += "WHERE username=" + "'" + username + "'" + ";";*/
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT id FROM usernamesandpasswords");
		stringBuffer.append("\n");
		stringBuffer.append("WHERE username='");
		stringBuffer.append(username);
		stringBuffer.append("';");
		
		String SQLQuery = stringBuffer.toString();
		
		ArrayList list = this.runSQLQueryAndGetReturnList(SQLQuery);
		
		if (list == null){
			return -1;
		}
		if (list.size() == 0){
			return -1;
		}
		
		for (Object o : list){
			if (o != null){
				Integer i = (Integer)o;
				return i.longValue();
			}
		}
		// should never get to this point.
		return -1;
    }
    
    /**
     * Returns the password for the given username. If the username is not in the database, the empty string is returned.
     * @param username - username for which password should be returned
     * @return - String - password for the given username or the empty string if the username is not in the database
     */
    private String getPasswordForGivenUsernameInDatabase(String username){
        /*String tableName = "usernamesandpasswords";
		String SQLQuery = "SELECT password FROM " + tableName + "\n";
		SQLQuery += "WHERE username=" + "'" + username + "'" + ";";*/
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT password FROM usernamesandpasswords");
		stringBuffer.append("\n");
		stringBuffer.append("WHERE username='");
		stringBuffer.append(username);
		stringBuffer.append("';");
		
		String SQLQuery = stringBuffer.toString();
		
		ArrayList list = this.runSQLQueryAndGetReturnList(SQLQuery);
		
		if (list == null){
			return "";
		}
		if (list.size() == 0){
			return "";
		}
		
		for (Object o : list){
			if (o != null){
				return o.toString();
			}
		}
		// should never get to this point.
		return "";
	}
    
    /**
	 * Deletes a given user from the database.
	 * @param Username - username of the user to be deleted
	 */
    public void deleteGivenUserFromDataBase(String Username){
		/*String tableName = "usernamesandpasswords";
		
		String SQLQuery = "DELETE FROM " + tableName  + "\n";
		SQLQuery += "WHERE username='" + Username + "';";*/
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("DELETE FROM usernamesandpasswords");
		stringBuffer.append("\n");
		stringBuffer.append("WHERE username='");
		stringBuffer.append(Username);
		stringBuffer.append("';");
		
		String SQLQuery = stringBuffer.toString();
		
		this.runSQLQueryWithNoReturnValue(SQLQuery);
		
	}
	
	
	
	/**
     * Sets the session factory for this dao to use. 
     * 
     * @param factory the session factory for this dao.
     */
    @Resource
    public void setSesionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }
    
   
	@Override
	public UserBean getUserWithUsername(String Username) {
		String password = this.getPasswordForGivenUsernameInDatabase(Username);
		if (password == null){
			return null;
		}
		if (password.isEmpty()){
			return null;
		}
		// else
		UserBean retval = new UserBean();
		retval.setPassword(password);
		retval.setUsername(Username);
		retval.setId(this.getIDForGivenUsernameInDatabase(Username));
		return retval;
	}
	
	
	@Override
	public void changePasswordOfUser(String Username, String newPassword) {
		/*String tableName = "usernamesandpasswords";
		
		String SQLQuery = "";
		
		SQLQuery = "UPDATE " + tableName + "\n";
		SQLQuery += "SET password=" + "'" + newPassword + "'" + "\n";
		SQLQuery += "WHERE username=" + "'" + Username + "'" + ";";*/
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("UPDATE usernamesandpasswords");
		stringBuffer.append("\n");
		
		stringBuffer.append("SET password='");
		stringBuffer.append(newPassword);
		stringBuffer.append("'");
		stringBuffer.append("\n");
		
		stringBuffer.append("WHERE username='");
		stringBuffer.append(Username);
		stringBuffer.append("';");
		
		String SQLQuery = stringBuffer.toString();
		
		this.runSQLQueryWithNoReturnValue(SQLQuery);
	}

	
	@Override
	public void addUserToDatabase(String Username, String Password) {
		/*String tableName = "usernamesandpasswords";
		
		String SQLQuery = "INSERT INTO " + tableName + " (username, password)" + "\n";
		SQLQuery += "VALUES ('" + Username + "', '" + Password + "');";*/
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("INSERT INTO usernamesandpasswords (username, password)");
		stringBuffer.append("\n");
		stringBuffer.append("VALUES ('");
		stringBuffer.append(Username);
		stringBuffer.append("', '");
		stringBuffer.append(Password);
		stringBuffer.append("');");
		
		String SQLQuery = stringBuffer.toString();
		
		this.runSQLQueryWithNoReturnValue(SQLQuery);
	}

	
	@Override
	public void deleteUserFromDatabase(String Username) {
		this.deleteGivenUserFromDataBase(Username);
	}
	
	
	@Override
	public boolean isUsernameInDatabase(String Username) {
		String toCheck = this.getPasswordForGivenUsernameInDatabase(Username);
		if (toCheck == null){
			return false;
		}
		if (toCheck.isEmpty()){
			return false;
		}
		// else
		return true;
	}

	
	@Override
	public boolean isPasswordCorrectForGivenUsername(String Username,
			String Password) {
		if (this.isUsernameInDatabase(Username)){
			UserBean u = this.getUserWithUsername(Username);
			if (Password.equals(u.getPassword())){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
