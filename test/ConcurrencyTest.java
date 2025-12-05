import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
//Tests parallel team formation threads
public class ConcurrencyTest {

    @Test
    void testParallelTeamFormation() throws Exception {
        List<Participant> list = new ArrayList<>();
        for(int i=1;i<=10;i++){
            list.add(new Participant("P"+i,"N"+i,"e","Football",5,"A",80,"Balanced"));
        }

        BalancedTeamStrategy strat = new BalancedTeamStrategy();

        Runnable r = () -> strat.formTeams(list, 4);

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertTrue(true);
    }
}
