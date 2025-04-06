package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;

    public static void add(Entity entity) {
        entity.id = nextId++;
        entities.add(entity);
    }

    public static Entity get(int id) {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        Entity entity = get(id);
        entities.remove(entity);
    }

    public static void update(Entity updatedEntity) {
        Entity oldEntity = get(updatedEntity.id);
        entities.set(entities.indexOf(oldEntity), updatedEntity);
    }
}