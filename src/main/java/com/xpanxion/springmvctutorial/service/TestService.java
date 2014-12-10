package com.xpanxion.springmvctutorial.service;

import java.util.List;

import com.xpanxion.springmvctutorial.dto.beans.TestBean;

/**
 * A service dealing with Tests beans.
 * 
 * @author bsmith
 *
 */
public interface TestService {

    /**
     * Return all of the available test beans. 
     * 
     * @return all of the test beans
     */
    List<TestBean> getTestBeans();

}