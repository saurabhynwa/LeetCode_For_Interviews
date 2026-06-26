package src.design;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKeyValueStore<K, V> {

    // Core storage engine utilizing lock-striping for high-velocity thread safety
    private final ConcurrentHashMap<K, V> databaseIndex = new ConcurrentHashMap<>();

    private void validateInputs(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Database invariants violated: Keys and Values cannot be null.");
        }
    }

    /**
     * Create / Insert Operation.
     * Inserts a key-value pair only if the key does not already exist.
     * @return true if inserted successfully, false if the key already exists.
     */
    public boolean put(K key, V value) {
        validateInputs(key, value);
        // putIfAbsent acts atomically: returns null if the key was empty and inserts successfully
        V existingValue = databaseIndex.putIfAbsent(key, value);
        return existingValue == null;
    }

    /**
     * Read Operation.
     * @return The value associated with the key, or null if the key is missing.
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return databaseIndex.get(key);
    }

    /**
     * Update Operation.
     * Updates an existing key with a new value.
     * @return true if updated successfully, false if the key was missing.
     */
    public boolean update(K key, V newValue) {
        validateInputs(key, newValue);
        // replace acts atomically: mutates the value only if the key is already present
        V oldProcessedValue = databaseIndex.replace(key, newValue);
        return oldProcessedValue != null;
    }

    /**
     * Delete Operation.
     * Removes the key-value pair from the store.
     * @return true if deleted successfully, false if the key was missing.
     */
    public boolean delete(K key) {
        if (key == null) {
            return false;
        }
        V removedValue = databaseIndex.remove(key);
        return removedValue != null;
    }

    /**
     * Existence Check.
     * @return true if the key is actively present in the index.
     */
    public boolean exists(K key) {
        if (key == null) {
            return false;
        }
        return databaseIndex.containsKey(key);
    }
}
