package db;
import db.exception.*;
import java.util.ArrayList;

public class Database {
    private static Database instance;
    private ArrayList<Entity> entities;
    private int newId = 1;

    private Database() {
        entities = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void add(Entity entity) {
        entity.id = newId++;
        entities.add(entity);
    }

    public Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public void delete(int id) throws EntityNotFoundException {
        Entity entity = get(id);
        entities.remove(entity);
    }

    public void update(Entity updatedEntity) throws EntityNotFoundException {
        Entity entity = get(updatedEntity.id);
        entities.set(entities.indexOf(entity), updatedEntity);
    }
}