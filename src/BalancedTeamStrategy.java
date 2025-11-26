import java.util.*;

public class BalancedTeamStrategy implements TeamFormationStrategy {

    @Override
    public List<Team> formTeams(List<Participant> participants, int teamSize) {
        System.out.println("🔧 Forming balanced teams with " + participants.size() + " participants...");

        // Group participants by personality type
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

        // Create teams until we run out of key personalities
        while (canFormMoreTeams(leaders, thinkers, balanced, teamSize)) {
            Team team = new Team("Team-" + (++teamCount));

            // Add 1 Leader if available
            if (!leaders.isEmpty()) {
                team.addMember(leaders.remove(0));
            }

            // Add 1-2 Thinkers if available
            int thinkersToAdd = Math.min(2, thinkers.size());
            for (int i = 0; i < thinkersToAdd; i++) {
                team.addMember(thinkers.remove(0));
            }

            // Fill remaining spots with Balanced
            int spotsLeft = teamSize - team.getTeamSize();
            int balancedToAdd = Math.min(spotsLeft, balanced.size());
            for (int i = 0; i < balancedToAdd; i++) {
                team.addMember(balanced.remove(0));
            }

            // If team is not full, use any remaining participants
            spotsLeft = teamSize - team.getTeamSize();
            if (spotsLeft > 0) {
                // Use thinkers first, then leaders, then balanced
                addRemainingParticipants(team, thinkers, leaders, balanced, spotsLeft);
            }

            teams.add(team);
        }

        // Add any leftover participants to existing teams
        distributeLeftoverParticipants(teams, leaders, thinkers, balanced);

        System.out.println("✅ Formed " + teams.size() + " balanced teams");
        return teams;
    }

    private boolean canFormMoreTeams(List<Participant> leaders, List<Participant> thinkers,
                                     List<Participant> balanced, int teamSize) {
        // We can form a team if we have at least 1 key personality or enough balanced
        int minParticipants = Math.min(teamSize, 3); // Need at least 3 for basic balance
        int totalAvailable = leaders.size() + thinkers.size() + balanced.size();
        return totalAvailable >= minParticipants;
    }

    private void addRemainingParticipants(Team team, List<Participant> thinkers,
                                          List<Participant> leaders, List<Participant> balanced, int spots) {
        for (int i = 0; i < spots && !thinkers.isEmpty(); i++) {
            team.addMember(thinkers.remove(0));
        }
        for (int i = 0; i < spots && !leaders.isEmpty(); i++) {
            team.addMember(leaders.remove(0));
        }
        for (int i = 0; i < spots && !balanced.isEmpty(); i++) {
            team.addMember(balanced.remove(0));
        }
    }

    private void distributeLeftoverParticipants(List<Team> teams, List<Participant> leaders,
                                                List<Participant> thinkers, List<Participant> balanced) {
        // Combine all leftovers
        List<Participant> leftovers = new ArrayList<>();
        leftovers.addAll(leaders);
        leftovers.addAll(thinkers);
        leftovers.addAll(balanced);
        Collections.shuffle(leftovers);

        // Distribute evenly among teams
        int teamIndex = 0;
        for (Participant p : leftovers) {
            if (teamIndex >= teams.size()) teamIndex = 0;
            teams.get(teamIndex).addMember(p);
            teamIndex++;
        }
    }
}