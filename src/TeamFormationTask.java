import java.util.List;
import java.util.concurrent.Callable;

public class TeamFormationTask implements Callable<List<Team>> {

    private List<Participant> group;
    private int teamSize;

    public TeamFormationTask(List<Participant> group, int teamSize) {
        this.group = group;
        this.teamSize = teamSize;
    }

    @Override
    public List<Team> call() {
        TeamFormationStrategy strategy = new BalancedTeamStrategy();
        return strategy.formTeams(group, teamSize);
    }
}
