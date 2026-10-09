
import java.util.ArrayList;
import java.util.Arrays;

public class Backtracking {

    private static final int MAX_SOLUTIONS = 50;

    Nurse[] nurses;
    Shift[] shifts;
    int[][] slots;
    int[] assigned;

    ArrayList<int[]> bestSolutions = new ArrayList<>();

    long bestOvertime = Long.MAX_VALUE;
    long bestScore = Long.MAX_VALUE;

    public Backtracking(Nurse[] nurses, Shift[] shifts, int[][] slots) {
        this.nurses = nurses;
        this.shifts = shifts;
        this.slots = slots;

        assigned = new int[slots.length];
        Arrays.fill(assigned, -1);
    }

    public void solve(int slot) {
        if (slot == slots.length) {
            saveSolution();
            return;
        }

        long overtime = calculateOvertime();
        long score = calculateScore();

        if (overtime > bestOvertime
                || (overtime == bestOvertime && score > bestScore)) {
            return;
        }

        int day = slots[slot][0];
        int shiftIndex = slots[slot][1];
        Shift shift = shifts[shiftIndex];

        for (int i = 0; i < nurses.length; i++) {
            Nurse nurse = nurses[i];

            boolean correctSkill
                    = shift.skill.equalsIgnoreCase("Any")
                    || nurse.skill.equalsIgnoreCase(shift.skill);

            boolean withinHours
                    = nurse.overtimeAllowed
                    || nurse.assignedHours + shift.hours <= nurse.maxHours;

            if (correctSkill && withinHours && freeThatDay(i, day, slot)) {
                assigned[slot] = i;
                nurse.assignedHours += shift.hours;

                solve(slot + 1);

                nurse.assignedHours -= shift.hours;
                assigned[slot] = -1;
            }
        }
    }

    private boolean freeThatDay(int nurseIndex, int day, int slot) {
        for (int i = 0; i < slot; i++) {
            if (slots[i][0] == day && assigned[i] == nurseIndex) {
                return false;
            }
        }
        return true;
    }

    private long calculateOvertime() {
        long total = 0;

        for (Nurse nurse : nurses) {
            if (nurse.assignedHours > nurse.maxHours) {
                total += nurse.assignedHours - nurse.maxHours;
            }
        }

        return total;
    }

    private long calculateScore() {
        long score = 0;

        for (Nurse nurse : nurses) {
            score += (long) nurse.assignedHours * nurse.assignedHours;
        }

        return score;
    }

    private void saveSolution() {
        long overtime = calculateOvertime();
        long score = calculateScore();

        if (overtime < bestOvertime
                || (overtime == bestOvertime && score < bestScore)) {

            bestOvertime = overtime;
            bestScore = score;
            bestSolutions.clear();
            bestSolutions.add(assigned.clone());
        }
    }

    public ArrayList<int[]> getBestSolutions() {
        return bestSolutions;
    }

    public long getBestOvertime() {
        return bestOvertime;
    }

    public long getBestScore() {
        return bestScore;
    }
}
