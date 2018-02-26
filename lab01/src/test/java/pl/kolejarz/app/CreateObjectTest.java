package pl.kolejarz.app;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreateObjectTest
{
    @Test
    public void testCreateObject()
    {
     Match match = new Match("Fnatic","VirtusPro",16,10,"CounterStrike");
     assertNotNull(match);
    }
}