public class SurveyProcessor implements Runnable {

    private Participant participant;
    private FileHandler fileHandler;
    private String filePath;

    public SurveyProcessor(Participant participant, FileHandler fileHandler, String filePath) {
        this.participant = participant;
        this.fileHandler = fileHandler;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            SystemLogger.info("Survey thread started for: " + participant.getName());
            System.out.println("Processing survey data for " + participant.getName() + "...");

            int score = participant.getPersonalityScore();

            // Assign personality type using your updated ranges
            if (score >= 90) {
                participant.setPersonalityType("Leader");
            } else if (score >= 70) {
                participant.setPersonalityType("Balanced");
            } else if (score >= 50) {
                participant.setPersonalityType("Thinker");
            } else {
                participant.setPersonalityType("Not Eligible");
            }

            SystemLogger.info("Final personality for " + participant.getName() + ": " + participant.getPersonalityType());

            // Save to CSV
            fileHandler.saveParticipantToCSV(participant, filePath);
            SystemLogger.info("Saved participant to CSV: " + participant.getName());

            System.out.println("Survey processed for " + participant.getName() +
                    " | Final Personality: " + participant.getPersonalityType());

        } catch (Exception e) {
            SystemLogger.error("Survey thread error for " + (participant == null ? "unknown" : participant.getName())
                    + ": " + e.getMessage());
            System.out.println("Error in survey thread: " + e.getMessage());
        }
    }
}
