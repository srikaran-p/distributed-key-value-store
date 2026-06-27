public interface StorageEngine {

    void set(String key, String value);

    String get(String key);

    String delete(String key);

    boolean exists(String key);

    void snapshot();
}
