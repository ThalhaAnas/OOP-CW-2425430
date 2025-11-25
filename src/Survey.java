import java.util.Scanner;

public class Survey {
    private Scanner scanner = new Scanner(System.in);
    private FileHandler fileHandler = new FileHandler();

    public Participant conductSurvey() {
        System.out.println("\n=== TeamMate Registration Survey ===");

        // Get name
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Get email (simple - just the first part)
        System.out.print("Enter your email username (without @university.edu): ");
        String emailUser = scanner.nextLine();
        String email = emailUser + "@university.edu";
        System.out.println("Your email: " + email);

        // Auto-generate ID
        String id = fileHandler.generateNextParticipantId("data/participants_sample.csv");
        System.out.println("Your ID: " + id);

        // Personality survey
        System.out.println("\n=== Personality Questions ===");
        int q1 = askQuestion("I enjoy taking the lead in groups");
        int q2 = askQuestion("I prefer analyzing situations");
        int q3 = askQuestion("I work well with others");
        int q4 = askQuestion("I am calm under pressure");
        int q5 = askQuestion("I like making quick decisions");

        int totalScore = (q1 + q2 + q3 + q4 + q5) * 4;
        String personalityType = classifyPersonality(totalScore);

        // Gaming preferences
        System.out.println("\n=== Gaming Preferences ===");
        String game = askGame();
        int skill = askSkillLevel();
        String role = askRole();

        // Create and save participant
        Participant newParticipant = new Participant(id, name, email, game, skill, role, totalScore, personalityType);
        fileHandler.saveParticipantToCSV(newParticipant, "data/participants_sample.csv");

        System.out.println("\n✅ Registration complete!");
        System.out.println("Welcome " + name + "! Personality: " + personalityType);

        return newParticipant;
    }

    private int askQuestion(String question) {
        while (true) {
            System.out.println(question);
            System.out.print("Rating (1-5): ");
            try {
                int rating = Integer.parseInt(scanner.nextLine());
                if (rating >= 1 && rating <= 5) return rating;
                System.out.println("Please enter 1-5");
            } catch (Exception e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private String askGame() {
        String[] games = {"Valorant", "DOTA 2", "CS:GO", "FIFA", "Basketball", "Chess"};
        for (int i = 0; i < games.length; i++) {
            System.out.println((i + 1) + ". " + games[i]);
        }

        while (true) {
            System.out.print("Choose game (1-6): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 6) return games[choice - 1];
                System.out.println("Please enter 1-6");
            } catch (Exception e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private int askSkillLevel() {
        while (true) {
            System.out.print("Skill level (1-10): ");
            try {
                int skill = Integer.parseInt(scanner.nextLine());
                if (skill >= 1 && skill <= 10) return skill;
                System.out.println("Please enter 1-10");
            } catch (Exception e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private String askRole() {
        String[] roles = {"Strategist", "Attacker", "Defender", "Supporter", "Coordinator"};
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i]);
        }

        while (true) {
            System.out.print("Choose role (1-5): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 5) return roles[choice - 1];
                System.out.println("Please enter 1-5");
            } catch (Exception e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private String classifyPersonality(int score) {
        if (score >= 90) return "Leader";
        else if (score >= 70) return "Balanced";
        else return "Thinker";
    }
}