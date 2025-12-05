import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
//Tests personality assignment and threaded execution
public class SurveyProcessorTest {

    @Test
    void testPersonalityAssignment() {
        FileHandler fh = new FileHandler();
        File temp = new File("survey_test.csv");

        Participant p = new Participant("P01","John","e","Football",5,"A",95,"");

        SurveyProcessor sp = new SurveyProcessor(p, fh, temp.getAbsolutePath());
        sp.run(); // simulate thread

        assertEquals("Leader", p.getPersonalityType());
    }

    @Test
    void testSurveyThreadRuns() throws Exception {
        FileHandler fh = new FileHandler();
        File temp = File.createTempFile("survey", ".csv");

        Participant p = new Participant("P01","John","e","Football",5,"A",60,"");

        SurveyProcessor sp = new SurveyProcessor(p, fh, temp.getAbsolutePath());
        Thread t = new Thread(sp);
        t.start();
        t.join(); // wait for thread

        assertNotNull(p.getPersonalityType());
    }
}
