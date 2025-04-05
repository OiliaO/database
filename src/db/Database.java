package db;
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
}