public class Participant {

    private String id;
    private String name;
    private String email;
    private String preferredGame;
    private int skillLevel;
    private String preferredRole;
    private int personalityScore;
    private String personalityType;

    //constructors
    public Participant() {}

    public Participant(String id, String name, String email, String preferredGame, int skillLevel
            , String preferredRole, int personalityScore, String personalityType)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = personalityType;
    }

    //add getters and setters

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPreferredGame() {return preferredGame;}
    public void setPreferredGame(String preferredGame) {this.preferredGame = preferredGame;}

    public int getSkillLevel() {return skillLevel;}
    public void setSkillLevel(int skillLevel) {this.skillLevel = skillLevel;}

    public String getPreferredRole() {return preferredRole;}
    public void setPreferredRole(String preferredRole) {this.preferredRole = preferredRole;}

    public int getPersonalityScore() {return personalityScore;}
    public void setPersonalityScore(int personalityScore) {this.personalityScore = personalityScore;}

    public String getPersonalityType() {return personalityType;}
    public void setPersonalityType(String personalityType) {this.personalityType = personalityType;}

}
