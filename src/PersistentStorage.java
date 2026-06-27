import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PersistentStorage implements StorageEngine {

    private final ConcurrentHashMap<String, String> store;
    private final WriteAheadLog writeAheadLog;
    private final SnapshotService snapshotService;
    private final Lock lock;

    public PersistentStorage() {
        this.store = new ConcurrentHashMap<>();
        this.writeAheadLog = new WriteAheadLog();
        this.snapshotService = new SnapshotService();
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

    @Override
    public void snapshot() {
        Map<String, String> snapshot = new HashMap<>();
        try {
            lock.lock();
            snapshot = new HashMap<>(store);
            snapshotService.save(snapshot);
            writeAheadLog.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private void loadDataToStore() throws IOException {
        final Map<String, String> snapshotData = snapshotService.load();
        try {
            lock.lock();
            store.putAll(snapshotData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

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
