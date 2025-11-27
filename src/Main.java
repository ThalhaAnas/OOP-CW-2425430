import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static ExecutorService teamExecutor = Executors.newFixedThreadPool(2);
    private static Scanner scanner = new Scanner(System.in);
    private static FileHandler fileHandler = new FileHandler();
    private static Survey survey = new Survey();
    private static List<Participant> allParticipants = new ArrayList<>();
    private static List<Team> formedTeams = new ArrayList<>();
    static int teamSize = 4; // default team size

    public static void main(String[] args) {
        System.out.println("=== Welcome to TeamMate System ===");

        while (true) {
            showMainMenu();
            int choice = getMenuChoice(1, 3);

            switch (choice) {
                case 1:
                    participantMenu();
                    break;
                case 2:
                    organizerMenu();
                    break;
                case 3:
                    System.out.println("Thank you for using TeamMate System!");
                    return;
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Participant Login");
        System.out.println("2. Organizer Login");
        System.out.println("3. Exit");
        System.out.print("Choose option (1-3): ");
    }

    // PARTICIPANT MENU
    private static void participantMenu() {
        System.out.println("\n=== PARTICIPANT MENU ===");
        System.out.println("1. Take Survey & Register");
        System.out.println("2. Back to Main Menu");
        System.out.print("Choose option (1-2): ");

        int choice = getMenuChoice(1, 2);
        if (choice == 1) {
            survey.conductSurvey();
        }
    }

    // ORGANIZER MENU
    private static void organizerMenu() {
        while (true) {
            System.out.println("\n=== ORGANIZER MENU ===");
            System.out.println("1. Load Participants from CSV");
            System.out.println("2. View All Participants");
            System.out.println("3. Set Team Size (Current: " + teamSize + ")");
            System.out.println("4. Run Team Formation");
            System.out.println("5. View Formed Teams");
            System.out.println("6. Save Teams to CSV");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose option (1-7): ");

            int choice = getMenuChoice(1, 7);

            switch (choice) {
                case 1:
                    loadParticipants();
                    break;
                case 2:
                    viewParticipants();
                    break;
                case 3:
                    setTeamSize();
                    break;
                case 4:
                    runTeamFormation();
                    break;
                case 5:
                    viewFormedTeams();
                    break;
                case 6:
                    saveTeamsToCSV();
                    break;
                case 7:
                    return;
            }
        }
    }

    // ORGANIZER FUNCTIONS
    private static void loadParticipants() {
        System.out.print("Enter CSV file path (or press enter for default): ");
        String path = scanner.nextLine();
        if (path.isEmpty()) {
            path = "data/participants_sample.csv";
        }

        allParticipants = fileHandler.loadParticipants(path);
        System.out.println("✅ Loaded " + allParticipants.size() + " participants");
    }

    private static void viewParticipants() {
        if (allParticipants.isEmpty()) {
            System.out.println("❌ No participants loaded. Please load CSV first.");
            return;
        }

        System.out.println("\n=== ALL PARTICIPANTS (" + allParticipants.size() + ") ===");
        for (Participant p : allParticipants) {
            System.out.println(p);
        }
    }

    private static void setTeamSize() {
        System.out.print("Enter team size: ");
        try {
            teamSize = Integer.parseInt(scanner.nextLine());
            if (teamSize < 2) {
                System.out.println("❌ Team size must be at least 2");
                teamSize = 4;
            } else {
                System.out.println("✅ Team size set to: " + teamSize);
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid number");
        }
    }

    private static void runTeamFormation() {

        if (allParticipants.isEmpty()) {
            System.out.println("❌ No participants loaded. Please load CSV first.");
            return;
        }

        if (allParticipants.size() < teamSize) {
            System.out.println("❌ Not enough participants for team formation");
            return;
        }

        System.out.println("🔧 Running parallel team formation using 2 threads...");

        try {
            // Split participants into 2 groups
            int mid = allParticipants.size() / 2;

            List<Participant> group1 = allParticipants.subList(0, mid);
            List<Participant> group2 = allParticipants.subList(mid, allParticipants.size());

            // Submit tasks
            Future<List<Team>> f1 = teamExecutor.submit(new TeamFormationTask(group1, teamSize));
            Future<List<Team>> f2 = teamExecutor.submit(new TeamFormationTask(group2, teamSize));

            // Get the results
            List<Team> t1 = f1.get();
            List<Team> t2 = f2.get();

            // Combine the results
            formedTeams = new ArrayList<>();
            formedTeams.addAll(t1);
            formedTeams.addAll(t2);

            // FIX: Rename teams sequentially so no ID duplicates
            for (int i = 0; i < formedTeams.size(); i++) {
                formedTeams.get(i).setTeamID("Team " + (i + 1));
            }

            System.out.println("✅ Teams formed successfully using concurrency!");
            showTeamStatistics();
            showFormationSummary();

        } catch (Exception e) {
            System.out.println("❌ Error during parallel team formation: " + e.getMessage());
        }
    }


    private static void showTeamStatistics() {
        System.out.println("\n📊 TEAM STATISTICS:");
        for (Team team : formedTeams) {
            // Count personality types
            int leaders = 0, thinkers = 0, balanced = 0;
            Set<String> roles = new HashSet<>();
            Set<String> games = new HashSet<>();

            for (Participant member : team.getMembers()) {
                switch (member.getPersonalityType()) {
                    case "Leader": leaders++; break;
                    case "Thinker": thinkers++; break;
                    case "Balanced": balanced++; break;
                }
                roles.add(member.getPreferredRole());
                games.add(member.getPreferredGame());
            }

            System.out.println(team.getTeamID() +
                    " | Size: " + team.getTeamSize() +
                    " | Avg Skill: " + String.format("%.1f", team.getAverageSkill()) +
                    " | Personalities: L:" + leaders + " T:" + thinkers + " B:" + balanced +
                    " | Roles: " + roles.size() + " unique" +
                    " | Games: " + games.size() + " unique");
        }
    }

    private static void showFormationSummary() {
        System.out.println("\n📈 FORMATION SUMMARY:");
        System.out.println("Total teams formed: " + formedTeams.size());
        System.out.println("Total participants used: " + formedTeams.stream().mapToInt(Team::getTeamSize).sum());
        System.out.println("Target team size: " + teamSize);

        // Count teams by size
        Map<Integer, Integer> sizeDistribution = new HashMap<>();
        for (Team team : formedTeams) {
            sizeDistribution.merge(team.getTeamSize(), 1, Integer::sum);
        }

        System.out.println("Team size distribution:");
        for (int size = teamSize - 2; size <= teamSize + 2; size++) {
            if (sizeDistribution.containsKey(size)) {
                System.out.println("  " + size + " members: " + sizeDistribution.get(size) + " teams");
            }
        }

        // Check if any teams are significantly smaller than target
        int smallTeams = formedTeams.stream()
                .filter(team -> team.getTeamSize() < teamSize - 1)
                .mapToInt(team -> 1)
                .sum();

        if (smallTeams > 0) {
            System.out.println("⚠️  Warning: " + smallTeams + " teams are significantly smaller than target size");
        } else {
            System.out.println("✅ All teams are reasonably sized");
        }
    }

    private static void viewFormedTeams() {
        if (formedTeams.isEmpty()) {
            System.out.println("❌ No teams formed yet. Run team formation first.");
            return;
        }

        System.out.println("\n=== FORMED TEAMS (" + formedTeams.size() + ") ===");
        for (int i = 0; i < formedTeams.size(); i++) {
            System.out.println("\n--- " + formedTeams.get(i).getTeamID() + " ---");
            System.out.println(formedTeams.get(i).getTeamDetails());
        }
    }

    private static void saveTeamsToCSV() {
        if (formedTeams.isEmpty()) {
            System.out.println("❌ No teams to save. Run team formation first.");
            return;
        }

        fileHandler.saveTeamsToCSV(formedTeams, "data/formed_teams.csv");
    }

    // HELPER METHOD
    private static int getMenuChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.print("Please enter " + min + "-" + max + ": ");
            } catch (Exception e) {
                System.out.print("Please enter a number: ");
            }
        }
    }
}