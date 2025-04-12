import db.*;
import db.exception.*;
import todo.entity.*;
import todo.service.*;
import todo.validator.*;
import java.util.*;
import java.text.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        initializeDatabase();

        while (true) {
            System.out.print("\nCommand (add/update/delete/get/exit): ");
            String command = scanner.nextLine().trim().toLowerCase();

            try {
                switch (command) {
                    case "add":
                        handleAddCommand();
                        break;
                    case "update":
                        handleUpdateCommand();
                        break;
                    case "delete":
                        handleDeleteCommand();
                        break;
                    case "get":
                        handleGetCommand();
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Invalid command!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void initializeDatabase() {
        Database.registerValidator(20, new TaskValidator());
        Database.registerValidator(21, new StepValidator());
    }

    private static void handleAddCommand() throws ParseException, InvalidEntityException {
        System.out.print("Add (task/step): ");
        String type = scanner.nextLine().trim().toLowerCase();

        if (type.equals("task")) {
            System.out.print("Title: ");
            String title = scanner.nextLine();
            System.out.print("Description: ");
            String description = scanner.nextLine();
            System.out.print("Due date (yyyy-MM-dd): ");
            Date dueDate = dateFormat.parse(scanner.nextLine());

            Task task = new Task(title, description, dueDate);
            Database.add(task);
            System.out.println("Task added. ID: " + task.getId());

        } else if (type.equals("step")) {
            System.out.print("Task ID: ");
            int taskId = Integer.parseInt(scanner.nextLine());
            System.out.print("Step Title: ");
            String title = scanner.nextLine();

            StepService.saveStep(taskId, title);
            System.out.println("Step added.");
        }
    }

    private static void handleUpdateCommand() throws EntityNotFoundException, InvalidEntityException, ParseException {
        System.out.print("Update (task/step): ");
        String type = scanner.nextLine().trim().toLowerCase();

        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (type.equals("task")) {
            System.out.print("Field (title/description/duedate/status): ");
            String field = scanner.nextLine().trim().toLowerCase();

            Task task = (Task) Database.get(id);
            switch (field) {
                case "title":
                    System.out.print("New Title: ");
                    task.setTitle(scanner.nextLine());
                    break;
                case "description":
                    System.out.print("New Description: ");
                    task.setDescription(scanner.nextLine());
                    break;
                case "duedate":
                    System.out.print("New Due Date (yyyy-MM-dd): ");
                    task.setDueDate(dateFormat.parse(scanner.nextLine()));
                    break;
                case "status":
                    TaskService.setAsCompleted(id);
                    break;
            }
            Database.update(task);
            System.out.println("Task updated.");

        } else if (type.equals("step")) {
            System.out.print("Field (title/status): ");
            String field = scanner.nextLine().trim().toLowerCase();

            Step step = (Step) Database.get(id);
            if (field.equals("title")) {
                System.out.print("New Title: ");
                step.setTitle(scanner.nextLine());
            } else if (field.equals("status")) {
                StepService.setAsCompleted(id);
            }
            Database.update(step);
            System.out.println("Step updated.");
        }
    }

    private static void handleDeleteCommand() throws EntityNotFoundException {
        System.out.print("Delete (task/step): ");
        String type = scanner.nextLine().trim().toLowerCase();
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Database.delete(id);
        System.out.println("Deleted successfully.");
    }

    private static void handleGetCommand() throws EntityNotFoundException {
        System.out.print("Get (task/step/all-tasks): ");
        String type = scanner.nextLine().trim().toLowerCase();

        if (type.equals("task")) {
            System.out.print("Task ID: ");
            Task task = (Task) Database.get(Integer.parseInt(scanner.nextLine()));
            printTaskDetails(task);

        } else if (type.equals("step")) {
            System.out.print("Step ID: ");
            Step step = (Step) Database.get(Integer.parseInt(scanner.nextLine()));
            printStepDetails(step);

        } else if (type.equals("all-tasks")) {
            for (Entity entity : Database.getAll(20)) {
                printTaskDetails((Task) entity);
            }
        }
    }

    private static void printTaskDetails(Task task) {
        System.out.println("\nID: " + task.getId());
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
        System.out.println("Status: " + task.getStatus());
        System.out.println("Created: " + task.getCreationDate());
        System.out.println("Last Modified: " + task.getLastModificationDate());
    }

    private static void printStepDetails(Step step) {
        System.out.println("\nID: " + step.getId());
        System.out.println("Title: " + step.getTitle());
        System.out.println("Status: " + step.getStatus());
        System.out.println("Task Ref: " + step.getTaskRef());
    }
}