package models;

import model.Profile;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProfileTest {
    /**
     * To Test the function if the profile is empty
     * @author Bariq
     */
    @Test
    public void TestEmptyProfile(){
        Profile profile = new Profile();
        assertFalse(profile==null);

    }

    /**
     * Check the parameterized constructor of the test
     */
    @Test
    public void testNoEmptyProfile(){
        Profile profile = new Profile("Name","city", "country","developer",true,"CAD $",1,0,10929229,new ArrayList<>());
        assertEquals(false,profile==null);

    }

    /**
     * To test every getter and setter running
     */
    @Test
    public void testGetterSetter(){
        Profile profile = new Profile("Name","city", "country","developer",true,"CAD $",1,0,10929229,new ArrayList<>());
        assertEquals("Name", profile.getName());
        assertEquals("city",profile.getCity());
        assertEquals("country",profile.getCountry());
        assertEquals("developer",profile.getRole());
        assertEquals(true,profile.isVerified());
    }
}
