import java.util.*;

public class BalancedTeamStrategy implements TeamFormationStrategy {

    private static final int MAX_LEADERS_PER_TEAM = 2;
    private static final int MAX_THINKERS_PER_TEAM = 2;

    @Override
    public List<Team> formTeams(List<Participant> participants, int teamSize) {
        System.out.println("🔧 Forming balanced teams with " + participants.size() + " participants...");

        if (participants == null || participants.isEmpty() || teamSize < 2) {
            return new ArrayList<>();
        }

        // Categorize participants
        List<Participant> leaders = new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        for (Participant p : participants) {
            String type = p.getPersonalityType();
            switch (type) {
                case "Leader": leaders.add(p); break;
                case "Thinker": thinkers.add(p); break;
                case "Balanced": balanced.add(p); break;
                default: balanced.add(p); break; // Handle any unknown types
            }
        }

        System.out.println("   Personalities: " + leaders.size() + " Leaders, " +
                thinkers.size() + " Thinkers, " + balanced.size() + " Balanced");

        // Shuffle for fairness
        Collections.shuffle(leaders);
        Collections.shuffle(thinkers);
        Collections.shuffle(balanced);

        // Calculate optimal team count
        int totalParticipants = participants.size();
        int teamCount = Math.max(1, (totalParticipants + teamSize - 1) / teamSize);

        System.out.println("   Creating " + teamCount + " teams of size " + teamSize);

        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= teamCount; i++) {
            teams.add(new Team("Team-" + i));
        }

        // PHASE 1: Distribute 1 Leader per team
        System.out.println("   1. Distributing leaders...");
        for (Team team : teams) {
            if (!leaders.isEmpty() && team.getTeamSize() < teamSize) {
                team.addMember(leaders.remove(0));
            }
        }

        // PHASE 2: Distribute 1 Thinker per team
        System.out.println("   2. Distributing thinkers...");
        for (Team team : teams) {
            if (!thinkers.isEmpty() && team.getTeamSize() < teamSize) {
                team.addMember(thinkers.remove(0));
            }
        }

        // PHASE 3: Fill with Balanced participants
        System.out.println("   3. Filling with balanced...");
        for (Team team : teams) {
            while (team.getTeamSize() < teamSize && !balanced.isEmpty()) {
                team.addMember(balanced.remove(0));
            }
        }

        // PHASE 4: Add second thinkers where possible
        System.out.println("   4. Adding second thinkers...");
        for (Team team : teams) {
            int currentThinkers = countType(team, "Thinker");
            while (team.getTeamSize() < teamSize &&
                    currentThinkers < MAX_THINKERS_PER_TEAM &&
                    !thinkers.isEmpty()) {
                team.addMember(thinkers.remove(0));
                currentThinkers++;
            }
        }

        // PHASE 5: Handle leftovers with round-robin
        List<Participant> leftovers = new ArrayList<>();
        leftovers.addAll(leaders);
        leftovers.addAll(thinkers);
        leftovers.addAll(balanced);

        System.out.println("   5. Handling " + leftovers.size() + " leftovers...");

        if (!leftovers.isEmpty()) {
            int index = 0;
            while (!leftovers.isEmpty()) {
                Team team = teams.get(index % teams.size());
                if (team.getTeamSize() < teamSize) {
                    Participant p = leftovers.remove(0);

                    // Check if adding this leader would exceed max leaders
                    if (p.getPersonalityType().equals("Leader")) {
                        if (countType(team, "Leader") < MAX_LEADERS_PER_TEAM) {
                            team.addMember(p);
                        } else {
                            // Skip this leader for now, try other teams
                            leftovers.add(p);
                        }
                    } else {
                        team.addMember(p);
                    }
                }
                index++;

                // Safety break
                if (index > 1000) break;
            }
        }

        // PHASE 6: Create extra teams if still leftovers
        int extraTeamCount = 0;
        while (!leftovers.isEmpty()) {
            Team extraTeam = new Team("Extra-Team-" + (++extraTeamCount));
            int toAdd = Math.min(teamSize, leftovers.size());
            for (int i = 0; i < toAdd; i++) {
                extraTeam.addMember(leftovers.remove(0));
            }
            teams.add(extraTeam);
        }

        // Remove any empty teams
        teams.removeIf(team -> team.getMembers().isEmpty());

        System.out.println("✅ Final: " + teams.size() + " teams created");
        return teams;
    }

    private int countType(Team team, String type) {
        int count = 0;
        for (Participant p : team.getMembers()) {
            if (p.getPersonalityType().equals(type)) {
                count++;
            }
        }
        return count;
    }
}