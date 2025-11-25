import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("\n--- Test 2: CSV File Loading ---");
        testCSVLoading();
    }

    private static void testCSVLoading() {
        FileHandler fileHandler = new FileHandler();

        // Try to load from CSV - adjust path if needed
        String csvPath = "data/participants_sample.csv"; // or "../data/participants_sample.csv"

        List<Participant> participants = fileHandler.loadParticipants(csvPath);

        if (!participants.isEmpty()) {
            fileHandler.showParticipants(participants);

            // Create a team from loaded data
            Team csvTeam = new Team("CSV-Team");
            for (int i = 0; i < Math.min(3, participants.size()); i++) {
                csvTeam.addMember(participants.get(i));
            }

            System.out.println("\nTeam from CSV data:");
            System.out.println(csvTeam.getTeamDetails());

        } else {
            System.out.println("No participants loaded. Check file path.");
            System.out.println("Current directory: " + System.getProperty("user.dir"));
        }
    }
}
