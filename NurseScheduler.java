
import java.util.ArrayList;
import java.util.Scanner;

public class NurseScheduler {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter number of days: ");
        int dayCount = readInt(true);
        String[] days = new String[dayCount];

        for (int i = 0; i < dayCount; i++) {
            System.out.print("Enter day " + (i + 1) + " name: ");
            days[i] = sc.nextLine().trim();

            if (days[i].isEmpty()) {
                System.out.println("Day name cannot be empty.");
                return;
            }
        }

        System.out.print("Enter number of nurses: ");
        int nurseCount = readInt(true);
        Nurse[] nurses = new Nurse[nurseCount];

        for (int i = 0; i < nurseCount; i++) {
            System.out.println("\nNurse " + (i + 1));

            System.out.print("Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Skill: ");
            String skill = sc.nextLine().trim();

            System.out.print("Maximum working hours: ");
            int maxHours = readInt(false);

            System.out.print("Willing to work overtime? (Y/N): ");
            String answer = sc.nextLine().trim();

            if (name.isEmpty() || skill.isEmpty()
                    || (!answer.equalsIgnoreCase("Y")
                    && !answer.equalsIgnoreCase("N"))) {
                System.out.println("Invalid nurse details.");
                return;
            }

            nurses[i] = new Nurse(name, skill, maxHours,
                    answer.equalsIgnoreCase("Y"));
        }

        System.out.print("\nEnter number of shift types: ");
        int shiftCount = readInt(true);
        Shift[] shifts = new Shift[shiftCount];

        int slotsPerDay = 0;

        for (int i = 0; i < shiftCount; i++) {
            System.out.println("\nShift " + (i + 1));

            System.out.print("Shift name: ");
            String name = sc.nextLine().trim();

            System.out.print("Required skill (or Any): ");
            String skill = sc.nextLine().trim();

            System.out.print("Nurses required per day: ");
            int required = readInt(true);

            System.out.print("Hours per shift: ");
            int hours = readInt(true);

            if (name.isEmpty() || skill.isEmpty()) {
                System.out.println("Invalid shift details.");
                return;
            }

            shifts[i] = new Shift(name, skill, required, hours);
            slotsPerDay += required;
        }

        if (slotsPerDay > nurseCount) {
            System.out.println("No valid schedule: there are more shift "
                    + "positions per day than nurses.");
            return;
        }

        int[][] slots = new int[dayCount * slotsPerDay][2];
        int index = 0;

        for (int day = 0; day < dayCount; day++) {
            for (int shift = 0; shift < shiftCount; shift++) {
                for (int count = 0; count < shifts[shift].required; count++) {
                    slots[index][0] = day;
                    slots[index][1] = shift;
                    index++;
                }
            }
        }

        Backtracking bt = new Backtracking(nurses, shifts, slots);

        System.out.println("\nFinding optimal schedules...");
        bt.solve(0);

        ArrayList<int[]> solutions = bt.getBestSolutions();

        if (solutions.isEmpty()) {
            System.out.println("No valid schedule found.");
            System.out.println("Check nurse skills and overtime preferences.");
            return;
        }

        System.out.println("\nBest Schedule");
        System.out.println("Minimum overtime: "
                + bt.getBestOvertime() + " hours");
        System.out.println("Workload score: " + bt.getBestScore());

        displaySchedule(days, nurses, shifts, slots, solutions.get(0));

        System.out.println("\nWorkload Summary:");
        displayWorkload(nurses, shifts, slots, solutions.get(0));
    }

    static int readInt(boolean positive) {
        try {
            int value = Integer.parseInt(sc.nextLine().trim());

            if (positive ? value <= 0 : value < 0) {
                throw new IllegalArgumentException();
            }

            return value;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Enter a valid " + (positive
                            ? "positive number." : "non-negative number."));
        }
    }

    static void displaySchedule(String[] days, Nurse[] nurses,
            Shift[] shifts, int[][] slots, int[] solution) {

        for (int day = 0; day < days.length; day++) {
            System.out.print(days[day] + ": ");

            for (int shift = 0; shift < shifts.length; shift++) {
                System.out.print(shifts[shift].name + " - ");

                boolean first = true;

                for (int slot = 0; slot < slots.length; slot++) {
                    if (slots[slot][0] == day
                            && slots[slot][1] == shift) {

                        if (!first) {
                            System.out.print(", ");
                        }

                        System.out.print(nurses[solution[slot]].name);
                        first = false;
                    }
                }

                if (shift < shifts.length - 1) {
                    System.out.print(" | ");
                }
            }

            System.out.println();
        }
    }

    static void displayWorkload(Nurse[] nurses, Shift[] shifts,
            int[][] slots, int[] solution) {

        int[] hours = new int[nurses.length];

        for (int slot = 0; slot < slots.length; slot++) {
            hours[solution[slot]] += shifts[slots[slot][1]].hours;
        }

        for (int i = 0; i < nurses.length; i++) {
            int overtime = Math.max(0, hours[i] - nurses[i].maxHours);

            System.out.println(nurses[i].name + ": " + hours[i]
                    + "/" + nurses[i].maxHours + " hours"
                    + (overtime > 0
                            ? " (Overtime: " + overtime + " hours)" : ""));
        }
    }
}
