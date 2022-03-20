package models;

import junit.framework.TestCase;
import Helpers.FreelanceAPI;

/**
 * Test class for FreelanceAPI class
 * @author Kazi Asif Tanim
 */
public class FreelanceAPITest extends TestCase {

    /**
     * Test case for getUrl method
     * @author Kazi Asif Tanim
     */
    public void testGetUrl() {
        assertEquals("/api/projects/0.1/projects/", FreelanceAPI.PROJECT_BY_ID.getUrl());
    }
}