package db.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("Cannot find entity");
    }
    public EntityNotFoundException(String message) {
        super("Cannot find entity: " + message);
    }
    public EntityNotFoundException(int id) {
        super("Cannot find entity with id=" + id);
    }
}