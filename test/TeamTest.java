import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
//Test cases to test team size and average skill
public class TeamTest {

    @Test
    void testTeamSize() {
        Team t = new Team("T1");
        t.addMember(new Participant("P01","John","j@x.com","Football",5,"Attacker",80,"Leader"));
        t.addMember(new Participant("P02","Jane","j@x.com","Football",6,"Defender",70,"Thinker"));

        assertEquals(2, t.getTeamSize());
    }

    @Test
    void testAverageSkill() {
        Team t = new Team("T1");
        t.addMember(new Participant("P01","John","j@x.com","Football",5,"Attacker",80,"Balanced"));
        t.addMember(new Participant("P02","Jane","j@x.com","Football",7,"Defender",70,"Thinker"));

        assertEquals(6.0, t.getAverageSkill());
    }
}
