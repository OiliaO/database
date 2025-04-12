package db;

import db.exception.*;
import java.util.*;
import java.util.stream.Collectors;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already exists for entity code: " + entityCode);
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
        Date now = new Date();

        if (copy instanceof Trackable) {
            Trackable trackable = (Trackable) copy;
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);

            if (entity instanceof Trackable) {
                Trackable original = (Trackable) entity;
                original.setCreationDate(now);
                original.setLastModificationDate(now);
            }
        }

        entities.add(copy);
        entity.id = copy.id;
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity entity) throws EntityNotFoundException, InvalidEntityException {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            validator.validate(entity);
        }

        boolean found = false;
        Date now = new Date();

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == entity.id) {
                Entity copy = entity.copy();

                if (copy instanceof Trackable) {
                    ((Trackable) copy).setLastModificationDate(now);

                    if (entity instanceof Trackable) {
                        ((Trackable) entity).setLastModificationDate(now);
                    }
                }

                entities.set(i, copy);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new EntityNotFoundException(entity.id);
        }
    }

    public static void delete(int id) throws EntityNotFoundException {
        if (!entities.removeIf(e -> e.id == id)) {
            throw new EntityNotFoundException(id);
        }
    }

    public static List<Entity> getAll(int entityCode) {
        return entities.stream()
                .filter(e -> e.getEntityCode() == entityCode)
                .map(Entity::copy)
                .collect(Collectors.toList());
    }

    public static List<Entity> getAll() {
        return new ArrayList<>(entities);
    }
}