import java.io.*;
import java.util.*;

public class FileHandler {

    public List<Participant> loadParticipants(String filePath) {
        List<Participant> participants = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Parse each line
                Participant participant = parseLine(line);
                if (participant != null) {
                    participants.add(participant);
                }
            }

            reader.close();
            System.out.println("Loaded " + participants.size() + " participants from CSV");

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return participants;
    }

    private Participant parseLine(String line) {
        try {
            String[] data = line.split(",");

            // Check if we have enough columns
            if (data.length < 8) {
                return null;
            }

            String id = data[0].trim();
            String name = data[1].trim();
            String email = data[2].trim();
            String game = data[3].trim();
            int skill = Integer.parseInt(data[4].trim());
            String role = data[5].trim();
            int personalityScore = Integer.parseInt(data[6].trim());
            String personalityType = data[7].trim();

            // Create participant object
            return new Participant(id, name, email, game, skill, role, personalityScore, personalityType);

        } catch (Exception e) {
            System.out.println("Skipping invalid line: " + line);
            return null;
        }
    }

    // Show loaded participants from CSV
    public void showParticipants(List<Participant> participants) {
        System.out.println("\n=== Loaded Participants ===");
        for (int i = 0; i < participants.size(); i++) {  //or i < Math.min(5, participants.size()) middle
            System.out.println(participants.get(i));
        }
        //if (participants.size() > 5) {
            //System.out.println("... and " + (participants.size() - 5) + " more");
        //}
    }


}