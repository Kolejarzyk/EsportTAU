package pl.kolejarz.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import pl.kolejarz.domain.Match;

@Ignore
public class CreateObjectTest
{
    @Test
    public void testCreateObject()
    {
     Match match = new Match("Fnatic","VirtusPro",16,10,"CounterStrike");
     assertNotNull(match);
    }
}