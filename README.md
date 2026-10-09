# Nurse Scheduler

Nurse Scheduler is a console-based Java program that assigns nurses to shifts
across a set of days. It uses backtracking to search for valid assignments and
selects the best schedule according to these priorities:

1. Minimize total overtime hours.
2. If multiple schedules have the same overtime, minimize the workload score.

The workload score is the sum of each nurse's assigned hours squared. This
helps distribute work more evenly instead of assigning most of the hours to
only one or two nurses.

## Requirements

- Java Development Kit (JDK) 8 or newer

No external libraries are required.

## Files

- `NurseScheduler.java` - Reads input and displays the schedule.
- `Backtracking.java` - Searches for valid assignments and evaluates schedules.
- `Nurse.java` - Represents a nurse's details.
- `Shift.java` - Represents a shift type's requirements.

## How to run

Open a terminal in the project directory and compile the source files:

```text
javac *.java
```

Then start the scheduler:

```text
java NurseScheduler
```

The program reads one value per prompt from standard input. Enter numbers as
whole numbers. A positive number is required for counts and shift hours;
maximum working hours may be zero.

## Input

### 1. Days

The program asks for:

1. The number of days.
2. The name of each day.

Day names must not be empty.

### 2. Nurses

For each nurse, enter:

1. **Name** - The nurse's display name.
2. **Skill** - The nurse's skill, such as `Registered Nurse` or `Pediatric`.
3. **Maximum working hours** - The number of hours the nurse may work before
   overtime begins.
4. **Willing to work overtime?** - Enter `Y` or `N`.

A nurse may be assigned to at most one shift on each day. A nurse who answers
`N` is not assigned beyond their maximum hours. A nurse who answers `Y` may be
assigned additional hours, but those hours count as overtime.

### 3. Shift types

For each shift type, enter:

1. **Shift name** - For example, `Morning` or `Night`.
2. **Required skill** - The nurse must have this skill. Enter `Any` to allow
   nurses with any skill.
3. **Nurses required per day** - The number of nurses needed for this shift on
   every day.
4. **Hours per shift** - The number of hours assigned for one occurrence of
   this shift.

The total number of nurse positions required per day must not exceed the number
of nurses.

## Output

When a valid schedule is found, the program prints:

- The minimum total overtime in hours.
- The workload score for the selected schedule.
- Each day with the nurses assigned to each shift.
- A workload summary showing each nurse's assigned hours and maximum hours.
- Overtime for a nurse when their assigned hours exceed their maximum.

Example output format:

```text
Best Schedule
Minimum overtime: 0 hours
Workload score: 576
Monday: Morning - Alice | Night - Bob
Tuesday: Morning - Bob | Night - Alice

Workload Summary:
Alice: 16/24 hours
Bob: 16/24 hours
```

The exact schedule and score depend on the input. If more than one schedule
has the same best metrics, the program displays one of the best schedules.

## Example input session

The following values illustrate a two-day schedule with two nurses and two
shift types:

```text
Enter number of days: 2
Enter day 1 name: Monday
Enter day 2 name: Tuesday
Enter number of nurses: 2

Nurse 1
Name: Alice
Skill: General
Maximum working hours: 24
Willing to work overtime? (Y/N): N

Nurse 2
Name: Bob
Skill: General
Maximum working hours: 24
Willing to work overtime? (Y/N): N

Enter number of shift types: 2

Shift 1
Shift name: Morning
Required skill (or Any): General
Nurses required per day: 1
Hours per shift: 8

Shift 2
Shift name: Night
Required skill (or Any): General
Nurses required per day: 1
Hours per shift: 8
```

## When no schedule is found

The program reports that no valid schedule exists when, for example:

- A nurse's skill does not match any required shift skill.
- Nurses who cannot work overtime do not have enough available hours.
- The daily number of required shift positions is greater than the number of
  nurses.
- The input contains invalid nurse or shift details.

If a numeric response is not a valid integer, the program raises an error
describing the expected positive or non-negative value.
