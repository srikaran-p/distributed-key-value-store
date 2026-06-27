import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PersistentStorage implements StorageEngine {

    private final ConcurrentHashMap<String, String> store;
    private final WriteAheadLog writeAheadLog;
    private final Lock lock;

    public PersistentStorage() {
        this.store = new ConcurrentHashMap<>();
        this.writeAheadLog = new WriteAheadLog();
        lock = new ReentrantLock();
        try {
            loadDataToStore();
        } catch (Exception e) {
            throw new RuntimeException("Could not load logs into store");
        }
    }

    @Override
    public void set(String key, String value) {
        try {
            lock.lock();
            writeAheadLog.appendSet(key, value);
            store.put(key, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String get(String key) {
        return store.get(key);
    }

    @Override
    public String delete(String key) {
        String value;
        try {
            lock.lock();
            writeAheadLog.appendDelete(key);
            value = store.remove(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        return value;
    }

    @Override
    public boolean exists(String key) {
        return store.containsKey(key);
    }

    private void loadDataToStore() throws IOException {
        final List<LogEntry> logEntries = writeAheadLog.loadEntries();
        if (logEntries.isEmpty()) {
            return;
        }

        for (LogEntry entry : logEntries) {
            if (entry == null) {
                continue;
            }

            final Command command = entry.getCommand();
            if (command == null) {
                continue;
            }

            switch (command) {
                case Command.SET:
                    store.put(entry.getKey(), entry.getValue());
                    break;
                case Command.DELETE:
                    store.remove(entry.getKey());
                    break;
                default:
                    break;
            }
        }
    }
}
