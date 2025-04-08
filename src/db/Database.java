package db;

import db.exception.*;
import java.util.*;;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator for this entity code already exists");
        }
        validators.put(entityCode, validator);
    }

    public static void add(Entity entity) throws InvalidEntityException {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            validator.validate(entity);
        }

        Entity copy = entity.copy();
        copy.id = nextId++;
        entities.add(copy);
    }
}