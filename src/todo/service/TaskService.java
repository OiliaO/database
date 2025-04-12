package todo.service;

import db.Database;
import db.exception.*;
import todo.entity.Task;

public class TaskService {
    public static void setAsCompleted(int taskId) throws
            EntityNotFoundException, InvalidEntityException {

        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.COMPLETED);
        Database.update(task);
    }

    public static void updateTaskTitle(int taskId, String newTitle) throws
            EntityNotFoundException, InvalidEntityException {

        Task task = (Task) Database.get(taskId);
        task.setTitle(newTitle);
        Database.update(task);
    }
}