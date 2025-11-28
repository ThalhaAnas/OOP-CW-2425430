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
        // Log thread start and group size
        SystemLogger.info(Thread.currentThread().getName() + " forming teams for group size: " + group.size());
        TeamFormationStrategy strategy = new BalancedTeamStrategy();
        List<Team> teams = strategy.formTeams(group, teamSize);
        SystemLogger.info(Thread.currentThread().getName() + " created " + teams.size() + " teams");
        return teams;
    }
}
