import java.io.*;
import java.util.*;

public class FileHandler {

    // Load participants from CSV. Expect header row.
    public List<Participant> loadParticipants(String filePath) {
        List<Participant> participants = new ArrayList<>();

        SystemLogger.info("Reading CSV: " + filePath);
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
            SystemLogger.info("Loaded " + participants.size() + " participants from CSV");
            System.out.println("Loaded " + participants.size() + " participants from CSV");

        } catch (IOException e) {
            SystemLogger.error("Error reading CSV: " + e.getMessage());
            System.out.println("Error reading file: " + e.getMessage());
        }

        return participants;
    }

    // Parse one CSV line into Participant
    private Participant parseLine(String line) {
        try {
            String[] data = line.split(",");

            // Check if we have enough columns
            if (data.length < 8) {
                SystemLogger.error("Skipping short CSV line: " + line);
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

            return new Participant(id, name, email, game, skill, role, personalityScore, personalityType);

        } catch (Exception e) {
            SystemLogger.error("Skipping invalid line: " + line + " | error: " + e.getMessage());
            System.out.println("Skipping invalid line: " + line);
            return null;
        }
    }

    // Show loaded participants (console helper)
    public void showParticipants(List<Participant> participants) {
        System.out.println("\n=== Loaded Participants ===");
        for (int i = 0; i < participants.size(); i++) {
            System.out.println(participants.get(i));
        }
    }

    // Save a single participant to CSV (correct column order)
    public void saveParticipantToCSV(Participant participant, String filePath) {
        SystemLogger.info("Saving participant: " + participant.getName());
        try {
            boolean fileExists = new File(filePath).exists();
            FileWriter writer = new FileWriter(filePath, true);

            if (!fileExists) {
                // Header matches the columns below
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
            }

            // Write values in the correct order
            writer.write(participant.getId() + "," +
                    participant.getName() + "," +
                    participant.getEmail() + "," +
                    participant.getPreferredGame() + "," +
                    participant.getSkillLevel() + "," +
                    participant.getPreferredRole() + "," +
                    participant.getPersonalityScore() + "," +
                    participant.getPersonalityType() + "\n");

            writer.close();
            System.out.println("Saved to CSV: " + participant.getName());

        } catch (Exception e) {
            SystemLogger.error("Error saving participant to CSV: " + e.getMessage());
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }

    // Older method name kept for compatibility. It now delegates to saveParticipantToCSV.
    public void saveParticipantsToCSV(Participant participant, String filePath) {
        saveParticipantToCSV(participant, filePath);
    }

    // Generate next participant ID based on existing CSV
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
            // fallback unique id
            return "P" + System.currentTimeMillis();
        }
    }

    // Save all teams to CSV with rows per participant
    public void saveTeamsToCSV(List<Team> teams, String filePath) {
        SystemLogger.info("Saving " + teams.size() + " teams to: " + filePath);
        try {
            FileWriter writer = new FileWriter(filePath);

            // Header with team info and individual participant details
            writer.write("TeamID,ParticipantID,ParticipantName,PreferredRole,PreferredGame,PersonalityType,SkillLevel,AverageSkill,TeamSize\n");

            for (Team team : teams) {
                List<Participant> members = team.getMembers();
                double averageSkill = team.getAverageSkill();
                int teamSize = team.getTeamSize();

                // Create one row for each team member
                for (Participant member : members) {
                    writer.write(team.getTeamID() + "," +
                            member.getId() + "," +
                            member.getName() + "," +
                            member.getPreferredRole() + "," +
                            member.getPreferredGame() + "," +
                            member.getPersonalityType() + "," +
                            member.getSkillLevel() + "," +
                            String.format("%.2f", averageSkill) + "," +
                            teamSize + "\n");
                }
            }

            writer.close();
            System.out.println("✅ Saved " + teams.size() + " teams to: " + filePath);
        } catch (Exception e) {
            SystemLogger.error("Error saving teams: " + e.getMessage());
            System.out.println("Error saving teams: " + e.getMessage());
        }
    }
}
