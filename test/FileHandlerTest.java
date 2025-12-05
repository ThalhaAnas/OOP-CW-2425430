import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;
//Test file with no participants and save new participant
public class FileHandlerTest {

    @Test
    void testLoadMissingFile() {
        FileHandler fh = new FileHandler();
        List<Participant> p = fh.loadParticipants("missing_file.csv");
        assertTrue(p.isEmpty());
    }


    @Test
    void testSaveParticipant() throws Exception {
        FileHandler fh = new FileHandler();

        Participant p = new Participant("P01","John","j@x.com","Football",5,"Attacker",80,"Leader");

        File file = File.createTempFile("save", ".csv");
        fh.saveParticipantToCSV(p, file.getAbsolutePath());

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
}
