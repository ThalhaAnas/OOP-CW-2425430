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

    public void saveParticipantsToCSV(Participant participant, String filePath) {
        try{
            boolean fileExists = new File(filePath).exists();

            FileWriter writer = new FileWriter(filePath, true);

            if (!fileExists) {
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
            }

            writer.write(participant.getId() + "," +
                    participant.getName() + "," +
                    participant.getEmail() + "," +
                    participant.getPreferredGame() + "," +
                    participant.getPersonalityScore() + "," +
                    participant.getPersonalityType() + "\n");

            writer.close();
            System.out.println("Participant saved to CSV: " + participant.getName());

        }catch(IOException e){
            System.out.println("Error saving participant to CSV: " + e.getMessage());
        }
    }

    public String generateNextParticipantId(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean firstLine = true;
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length > 0) {
                    String id = data[0].trim();
                    if (id.startsWith("P")) {
                        try {
                            int currentId = Integer.parseInt(id.substring(1));
                            if (currentId > maxId) maxId = currentId;
                        } catch (Exception e) {
                            // Skip if not a number
                        }
                    }
                }
            }
            reader.close();

            return "P" + String.format("%03d", maxId + 1);

        } catch (Exception e) {
            return "P" + System.currentTimeMillis();
        }
    }

    public void saveParticipantToCSV(Participant participant, String filePath) {
        try {
            boolean fileExists = new File(filePath).exists();
            FileWriter writer = new FileWriter(filePath, true);

            if (!fileExists) {
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
            }

            writer.write(participant.getId() + "," +
                    participant.getName() + "," +
                    participant.getEmail() + "," +
                    participant.getPreferredGame() + "," +
                    participant.getSkillLevel() + "," +
                    participant.getPreferredRole() + "," +
                    participant.getPersonalityScore() + "," +
                    participant.getPersonalityType() + "\n");

            writer.close();
            System.out.println("✅ Saved to CSV: " + participant.getName());

        } catch (Exception e) {
            System.out.println("❌ Error saving to CSV: " + e.getMessage());
        }
    }

    public void saveTeamsToCSV(List<Team> teams, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write("TeamID,Members,AverageSkill,TeamSize\n");

            for (Team team : teams) {
                String members = "";
                for (Participant member : team.getMembers()) {
                    if (!members.isEmpty()) members += "; ";
                    members += member.getName() + "(" + member.getPreferredRole() + ")";
                }

                writer.write(team.getTeamID() + "," +
                        "\"" + members + "\"," +
                        String.format("%.2f", team.getAverageSkill()) + "," +
                        team.getTeamSize() + "\n");
            }

            writer.close();
            System.out.println("✅ Saved " + teams.size() + " teams to: " + filePath);

        } catch (Exception e) {
            System.out.println("❌ Error saving teams: " + e.getMessage());
        }
    }
}