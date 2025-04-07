package example;

import db.*;
import db.exception.*;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Human)) {
            throw new IllegalArgumentException("Entity must be of type Human");
        }

        Human human = (Human) entity;
        if (human.name == null || human.name.isEmpty()) {
            throw new InvalidEntityException("Name cannot be null or empty");
        }
        if (human.age < 0) {
            throw new InvalidEntityException("Age must be a positive integer");
        }
    }
}