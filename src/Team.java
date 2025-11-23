import java.util.ArrayList;
import java.util.List;

public class Team {
    private String teamID;
    private List<Participant> members;

    public Team() {
        this.members = new ArrayList<>();
    }

    //constructors
    public Team(String teamID) {
        this.teamID = teamID;
        this.members = new ArrayList<>();
    }

    //getters and setters
    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public List<Participant> getMembers() {
        return members;
    }

    public void setMembers(List<Participant> members) {
        this.members = members;
    }

    // methods used to manage teams
    public void addMember(Participant participant) {
        if (participant != null) {
            members.add(participant);
        }
    }

    public void removeMember(Participant participant) {
        members.remove(participant);
    }

    public int getTeamSize() {
        return members.size();
    }

    public double getAverageSkill() {
        if (members.isEmpty()) {
            return 0;
        }

        int totalSkill = 0;
        for (Participant member : members) {
            totalSkill += member.getSkillLevel();
        }
        return (double) totalSkill / members.size();
    }

    @Override
    public String toString() {
        return "Team [teamID=" + teamID + ", size=" + getTeamSize() + ", avgSkill=" + getAverageSkill() + "]";
    }

    public String getTeamDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Team: ").append(teamID)
                .append(" | Size: ").append(getTeamSize())
                .append(" | AverageSkill: ").append(getAverageSkill())
                .append("\nMembers: ");

        for (Participant member : members) {
            details.append(" - ").append(member.getName())
                    .append(" (").append(member.getPreferredRole())
                    .append(", ").append(member.getPersonalityType())
                    .append(")\n");
        }
        return details.toString();
    }
}