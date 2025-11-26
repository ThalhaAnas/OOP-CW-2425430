import java.util.*;

public class BalancedTeamStrategy implements TeamFormationStrategy {

    @Override
    public List<Team> formTeams(List<Participant> participants, int teamSize) {
        System.out.println("🔧 Forming balanced teams with " + participants.size() + " participants...");

        // Create copies to avoid modifying original list
        List<Participant> leaders = new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        for (Participant p : participants) {
            switch (p.getPersonalityType()) {
                case "Leader": leaders.add(p); break;
                case "Thinker": thinkers.add(p); break;
                case "Balanced": balanced.add(p); break;
            }
        }

        // Shuffle for randomness
        Collections.shuffle(leaders);
        Collections.shuffle(thinkers);
        Collections.shuffle(balanced);

        List<Team> teams = new ArrayList<>();
        int teamCount = 0;

        // Create teams while we have enough participants
        while (hasEnoughParticipants(leaders, thinkers, balanced, teamSize)) {
            Team team = new Team("Team-" + (++teamCount));

            // Add 1 Leader
            if (!leaders.isEmpty()) {
                team.addMember(leaders.remove(0));
            }

            // Add 1-2 Thinkers
            int thinkersNeeded = Math.min(2, teamSize - team.getTeamSize());
            for (int i = 0; i < thinkersNeeded && !thinkers.isEmpty(); i++) {
                team.addMember(thinkers.remove(0));
            }

            // Fill rest with Balanced
            int balancedNeeded = teamSize - team.getTeamSize();
            for (int i = 0; i < balancedNeeded && !balanced.isEmpty(); i++) {
                team.addMember(balanced.remove(0));
            }

            teams.add(team);
        }

        // Add leftover participants to existing teams
        addLeftoversToTeams(teams, leaders, thinkers, balanced);

        System.out.println("✅ Formed " + teams.size() + " balanced teams");
        return teams;
    }

    private boolean hasEnoughParticipants(List<Participant> leaders, List<Participant> thinkers,
                                          List<Participant> balanced, int teamSize) {
        int total = leaders.size() + thinkers.size() + balanced.size();
        return total >= teamSize;
    }

    private void addLeftoversToTeams(List<Team> teams, List<Participant> leaders,
                                     List<Participant> thinkers, List<Participant> balanced) {
        List<Participant> allLeftovers = new ArrayList<>();
        allLeftovers.addAll(leaders);
        allLeftovers.addAll(thinkers);
        allLeftovers.addAll(balanced);

        // Add leftovers to smallest teams first
        for (Participant p : allLeftovers) {
            Team smallestTeam = findSmallestTeam(teams);
            if (smallestTeam != null) {
                smallestTeam.addMember(p);
            }
        }
    }

    private Team findSmallestTeam(List<Team> teams) {
        Team smallest = null;
        for (Team team : teams) {
            if (smallest == null || team.getTeamSize() < smallest.getTeamSize()) {
                smallest = team;
            }
        }
        return smallest;
    }
}