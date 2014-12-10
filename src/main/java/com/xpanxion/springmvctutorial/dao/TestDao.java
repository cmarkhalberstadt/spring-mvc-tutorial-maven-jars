package com.xpanxion.springmvctutorial.dao;

import java.util.List;

import com.xpanxion.springmvctutorial.dto.entity.TestEntity;

/**
 * Interface for the Test Dao
 * 
 * @author bsmith
 *
 */
public interface TestDao {

    /**
     * Returns all of the test entities.
     * 
     * @return all of the test entities. 
     */
    List<TestEntity> getAllItems();

}