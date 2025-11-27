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
            System.out.println("⏳ Processing survey data for " + participant.getName() + "...");

           // Thread.sleep(1000); // optional demonstration delay

            int score = participant.getPersonalityScore();

            // Updated classification
            if (score >= 90) {
                participant.setPersonalityType("Leader");
            }
            else if (score >= 70) {
                participant.setPersonalityType("Balanced");
            }
            else if (score >= 50) {
                participant.setPersonalityType("Thinker");
            }
            else {
                participant.setPersonalityType("Not Eligible");
            }

            // Save to CSV
            fileHandler.saveParticipantToCSV(participant, filePath);

            System.out.println("✅ Survey processed for " + participant.getName() +
                    " | Final Personality: " + participant.getPersonalityType());

        } catch (Exception e) {
            System.out.println("❌ Error in survey thread: " + e.getMessage());
        }
    }
}
