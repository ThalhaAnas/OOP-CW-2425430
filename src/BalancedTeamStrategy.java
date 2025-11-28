import java.util.*;

public class BalancedTeamStrategy implements TeamFormationStrategy {

    private static final int MAX_LEADERS_PER_TEAM = 2;
    private static final int MAX_THINKERS_PER_TEAM = 2;

    @Override
    public List<Team> formTeams(List<Participant> participants, int teamSize) {
        // log start
        SystemLogger.info("BalancedTeamStrategy started. participants=" + (participants == null ? 0 : participants.size()));

        System.out.println("🔧 Forming balanced teams with " + (participants == null ? 0 : participants.size()) + " participants...");

        if (participants == null || participants.isEmpty() || teamSize < 2) {
            SystemLogger.info("No participants or invalid teamSize. Returning empty list.");
            return new ArrayList<>();
        }

        // categorize by personality type
        List<Participant> leaders = new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        for (Participant p : participants) {
            String type = p.getPersonalityType();
            if ("Leader".equalsIgnoreCase(type)) leaders.add(p);
            else if ("Thinker".equalsIgnoreCase(type)) thinkers.add(p);
            else balanced.add(p); // Balanced or unknown go here
        }

        SystemLogger.info("Counts -> Leaders:" + leaders.size() + " Thinkers:" + thinkers.size() + " Balanced:" + balanced.size());

        // shuffle to add fairness
        Collections.shuffle(leaders);
        Collections.shuffle(thinkers);
        Collections.shuffle(balanced);

        // compute number of teams (ceil division)
        int totalParticipants = participants.size();
        int teamCount = Math.max(1, (totalParticipants + teamSize - 1) / teamSize);

        System.out.println("   Creating " + teamCount + " teams of size " + teamSize);

        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= teamCount; i++) {
            teams.add(new Team("Team-" + i));
        }

        // PHASE 1: give each team a leader if possible
        System.out.println("   1. Distributing leaders...");
        for (Team team : teams) {
            if (!leaders.isEmpty() && team.getTeamSize() < teamSize) {
                team.addMember(leaders.remove(0));
            }
        }

        // PHASE 2: give each team one thinker
        System.out.println("   2. Distributing thinkers...");
        for (Team team : teams) {
            if (!thinkers.isEmpty() && team.getTeamSize() < teamSize) {
                team.addMember(thinkers.remove(0));
            }
        }

        // PHASE 3: fill with balanced participants
        System.out.println("   3. Filling with balanced...");
        for (Team team : teams) {
            while (team.getTeamSize() < teamSize && !balanced.isEmpty()) {
                team.addMember(balanced.remove(0));
            }
        }

        // PHASE 4: try to add a second thinker if possible
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

        // PHASE 5: handle leftovers in round-robin
        List<Participant> leftovers = new ArrayList<>();
        leftovers.addAll(leaders);
        leftovers.addAll(thinkers);
        leftovers.addAll(balanced);

        System.out.println("   5. Handling " + leftovers.size() + " leftovers...");

        if (!leftovers.isEmpty()) {
            int index = 0;
            int safety = 0;
            while (!leftovers.isEmpty() && safety < 2000) {
                Team team = teams.get(index % teams.size());
                if (team.getTeamSize() < teamSize) {
                    Participant p = leftovers.remove(0);
                    if ("Leader".equalsIgnoreCase(p.getPersonalityType())) {
                        if (countType(team, "Leader") < MAX_LEADERS_PER_TEAM) {
                            team.addMember(p);
                        } else {
                            leftovers.add(p); // try later
                        }
                    } else {
                        team.addMember(p);
                    }
                }
                index++;
                safety++;
            }
        }

        // PHASE 6: create extra teams if needed for leftover participants
        int extraTeamCount = 0;
        while (!leftovers.isEmpty()) {
            Team extraTeam = new Team("Extra-Team-" + (++extraTeamCount));
            int toAdd = Math.min(teamSize, leftovers.size());
            for (int i = 0; i < toAdd; i++) {
                extraTeam.addMember(leftovers.remove(0));
            }
            teams.add(extraTeam);
        }

        // remove any empty teams just in case
        teams.removeIf(team -> team.getMembers().isEmpty());

        System.out.println("Final: " + teams.size() + " teams created");
        SystemLogger.info("BalancedTeamStrategy finished. createdTeams=" + teams.size());

        return teams;
    }

    // helper to count participants of a given personality in a team
    private int countType(Team team, String type) {
        int count = 0;
        for (Participant p : team.getMembers()) {
            if (p.getPersonalityType() != null && p.getPersonalityType().equalsIgnoreCase(type)) {
                count++;
            }
        }
        return count;
    }
}
