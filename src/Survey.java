import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Survey {
    private Scanner scanner = new Scanner(System.in);
    private FileHandler fileHandler = new FileHandler();
    private final String CSV_PATH = "data/participants_sample.csv";

    // Thread pool for concurrent survey processing
    private static final ExecutorService surveyExecutor = Executors.newFixedThreadPool(3); // 3 concurrent survey processors

    // conduct survey and submit processing to executor
    public Participant conductSurvey() {
        SystemLogger.info("Survey started for new participant");
        System.out.println("\n=== TeamMate Registration Survey ===");

        // Get name
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        SystemLogger.info("Name entered: " + name);

        // Get email
        System.out.print("Enter your email username (without @university.edu): ");
        String emailUser = scanner.nextLine();
        String email = emailUser + "@university.edu";
        System.out.println("Your email: " + email);
        SystemLogger.info("Email generated: " + email);

        // Generate ID
        String id = fileHandler.generateNextParticipantId(CSV_PATH);
        System.out.println("Your ID: " + id);
        SystemLogger.info("Generated ID: " + id);

        // Personality questions
        System.out.println("\n=== Personality Questions ===");
        int q1 = askQuestion("I enjoy taking the lead in groups");
        int q2 = askQuestion("I prefer analyzing situations");
        int q3 = askQuestion("I work well with others");
        int q4 = askQuestion("I am calm under pressure");
        int q5 = askQuestion("I like making quick decisions");

        int totalScore = (q1 + q2 + q3 + q4 + q5) * 4;
        System.out.println("\nYour personality score: " + totalScore);
        SystemLogger.info("Calculated personality score: " + totalScore);

        // Reject if below 50
        if (totalScore < 50) {
            System.out.println("\nYour score is below 50.");
            System.out.println("Unfortunately, you are NOT eligible to participate in competitions.");
            System.out.println("Your registration has been cancelled.\n");
            SystemLogger.info("Participant rejected (score < 50): " + name);
            return null; // End survey early
        }

        SystemLogger.info("Participant eligible. Collecting gaming preferences.");

        // Gaming preferences (ONLY asked if eligible)
        System.out.println("\n=== Gaming Preferences ===");
        String game = askGame();
        int skill = askSkillLevel();
        String role = askRole();

        // Create participant object (personalityType set by processor)
        Participant participant = new Participant(
                id, name, email, game, skill, role, totalScore, "Processing..."
        );

        // Submit processing to executor (background)
        SystemLogger.info("Submitting survey for background processing: " + name);
        SurveyProcessor processor = new SurveyProcessor(participant, fileHandler, CSV_PATH);
        surveyExecutor.execute(processor);

        System.out.println("\nYour responses are being processed in the background...");
        System.out.println("Thank you " + name + "!");
        return participant;
    }

    // allow Main to shutdown the survey executor on exit
    public static void shutdownExecutor() {
        SystemLogger.info("Shutting down survey executor");
        surveyExecutor.shutdown();
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
