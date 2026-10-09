public class Nurse {
    String name;
    String skill;
    int maxHours;
    boolean overtimeAllowed;
    int assignedHours;

    public Nurse(String name, String skill, int maxHours,
                 boolean overtimeAllowed) {
        this.name = name;
        this.skill = skill;
        this.maxHours = maxHours;
        this.overtimeAllowed = overtimeAllowed;
        this.assignedHours = 0;
    }
}