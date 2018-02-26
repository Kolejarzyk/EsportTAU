package pl.kolejarz.app;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreateObjectTest
{
    @Test
    public void testCreateObject()
    {
        Match match = new Match();

        assertNotNull(match);
        assertEquals(match, new Match());
    }
}