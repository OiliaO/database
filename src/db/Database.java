package db;

import db.exception.*;
import java.util.*;

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

        if (copy instanceof Trackable) {
            Trackable trackable = (Trackable) copy;
            Date now = new Date();
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }

        entities.add(copy);
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities) {
            if (entity.id == id) {
                if (entity instanceof Trackable) {
                    Trackable trackable = (Trackable) entity;
                    return entity.copy();
                }
                return entity.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity entity) throws InvalidEntityException, EntityNotFoundException {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            validator.validate(entity);
        }

        int index = -1;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == entity.id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new EntityNotFoundException(entity.id);
        }

        Entity copy = entity.copy();
        if (copy instanceof Trackable) {
            ((Trackable) copy).setLastModificationDate(new Date());
        }
        entities.set(index, copy);
    }

    public static void delete(int id) throws EntityNotFoundException {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == id) {
                entities.remove(i);
                return;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static List<Entity> getAll() {
        List<Entity> copies = new ArrayList<>();
        for (Entity entity : entities) {
            copies.add(entity.copy());
        }
        return copies;
    }
}