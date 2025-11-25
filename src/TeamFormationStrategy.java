import java.util.List;

public interface TeamFormationStrategy {
    List<Team> formTeams(List<Participant> participants, int teamSize);
}
