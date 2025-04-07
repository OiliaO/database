package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;

    public static void add(Entity entity) {
        entity.id = nextId++;
        entities.add(entity.copy());
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        Entity entity = get(id);
        entities.remove(entity);
    }

    public static void update(Entity updatedEntity) throws EntityNotFoundException {
        Entity oldEntity = get(updatedEntity.id);
        entities.set(entities.indexOf(oldEntity), updatedEntity);
    }
}