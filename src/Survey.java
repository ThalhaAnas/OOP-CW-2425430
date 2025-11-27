import java.util.Scanner;

public class Survey {

    private Scanner scanner = new Scanner(System.in);
    private FileHandler fileHandler = new FileHandler();
    private final String CSV_PATH = "data/participants_sample.csv";

    public Participant conductSurvey() {
        System.out.println("\n=== TeamMate Registration Survey ===");

        // Get name
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Get email
        System.out.print("Enter your email username (without @university.edu): ");
        String emailUser = scanner.nextLine();
        String email = emailUser + "@university.edu";
        System.out.println("Your email: " + email);

        // Generate ID
        String id = fileHandler.generateNextParticipantId(CSV_PATH);
        System.out.println("Your ID: " + id);

        // Personality questions
        System.out.println("\n=== Personality Questions ===");
        int q1 = askQuestion("I enjoy taking the lead in groups");
        int q2 = askQuestion("I prefer analyzing situations");
        int q3 = askQuestion("I work well with others");
        int q4 = askQuestion("I am calm under pressure");
        int q5 = askQuestion("I like making quick decisions");

        int totalScore = (q1 + q2 + q3 + q4 + q5) * 4;

        System.out.println("\nYour personality score: " + totalScore);

        // ❌ Reject if below 50
        if (totalScore < 50) {
            System.out.println("\n❌ Your score is below 50.");
            System.out.println("Unfortunately, you are NOT eligible to participate in competitions.");
            System.out.println("Your registration has been cancelled.\n");
            return null; // End survey
        }

        // Temporary personality value (final set by thread)
        String tempType = "Processing...";

        // Gaming preferences (ONLY asked if eligible)
        System.out.println("\n=== Gaming Preferences ===");
        String game = askGame();
        int skill = askSkillLevel();
        String role = askRole();

        // Create participant object
        Participant participant = new Participant(
                id, name, email, game, skill, role, totalScore, tempType
        );

        // Thread to process and save data
        SurveyProcessor processor =
                new SurveyProcessor(participant, fileHandler, CSV_PATH);

        Thread thread = new Thread(processor);
        thread.start();

        System.out.println("\n🧵 Your responses are being processed in the background...");
        System.out.println("Thank you " + name + "!");

        return participant;
    }

    // ---------------- VALIDATION FUNCTIONS ----------------

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
}
