package todo.service;

import db.Database;
import db.exception.*;
import todo.entity.Step;

public class StepService {
    public static void saveStep(int taskRef, String title) throws InvalidEntityException {
        Step step = new Step(title, taskRef);
        Database.add(step);
    }

    public static void setAsCompleted(int stepId) throws EntityNotFoundException, InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.setStatus(Step.Status.COMPLETED);
        Database.update(step);
    }

    public static void updateStepTitle(int stepId, String newTitle) throws EntityNotFoundException, InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.setTitle(newTitle);
        Database.update(step);
    }
}