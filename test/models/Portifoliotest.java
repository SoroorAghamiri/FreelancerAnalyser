package models;

import model.Portifolio;
import model.Profile;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Portifoliotest {
    @Test
    public void testNoEmptyPortifolio(){
        Portifolio portifolio = new Portifolio("titlr","desc");
        assertEquals(false,portifolio==null);

    }

    /**
     * To test every getter and setter running
     */
    @Test
    public void testGetterSetter() {
        Portifolio portifolio = new Portifolio("title", "desc");
        assertEquals("title", portifolio.getTitle());
        assertEquals("desc", portifolio.getDesc());
    }
}
