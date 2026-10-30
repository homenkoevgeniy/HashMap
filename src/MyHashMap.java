public class MyHashMap <K, V>{

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets;
    private int size;
    private final int initialCapacity;
    private final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.initialCapacity = 16;
        this.loadFactor = 0.75f;
        this.buckets = (Node<K, V>[]) new Node[initialCapacity];
        this.size = 0;
    }

    public MyHashMap(int capacity, float loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive and finite");
        }

        // Округляем capacity до ближайшей степени двойки (упрощённо)
        int cap = 1;
        while (cap < capacity) {
            cap <<= 1;
        }
        this.initialCapacity = cap;
        this.loadFactor = loadFactor;
        this.buckets = (Node<K, V>[]) new Node[cap];
        this.size = 0;
    }

    private int indexFor(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        // Нормализация хеша и получение индекса
        return h & (buckets.length - 1);
    }

    // put: добавить/обновить значение по ключу
    public V put(K key, V value) {
        int idx = indexFor(key);
        Node<K, V> head = buckets[idx];

        // Ищем существующий ключ в цепочке
        for (Node<K, V> node = head; node != null; node = node.next) {
            if ((node.key == key) || (key != null && key.equals(node.key))) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }


        buckets[idx] = new Node<>(key, value, head);
        size++;


        if ((float) size / buckets.length > loadFactor) {
            resize();
        }

        return null;
    }


    public V get(K key) {
        int idx = indexFor(key);
        Node<K, V> node = buckets[idx];

        while (node != null) {
            if ((node.key == key) || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }


    public V remove(K key) {
        int idx = indexFor(key);
        Node<K, V> head = buckets[idx];
        Node<K, V> prev = null;
        Node<K, V> current = head;

        while (current != null) {
            if ((current.key == key) || (key != null && key.equals(current.key))) {

                if (prev == null) {

                    buckets[idx] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean containsKey(K key) {
        return get(key) != null || (/* edge case: value может быть null */ false);

    }

    public boolean containsKeyExact(K key) {
        int idx = indexFor(key);
        Node<K, V> node = buckets[idx];
        while (node != null) {
            if ((node.key == key) || (key != null && key.equals(node.key))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        int newCapacity = oldBuckets.length * 2;
        Node<K, V>[] newBuckets = (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < oldBuckets.length; i++) {
            Node<K, V> node = oldBuckets[i];
            while (node != null) {
                Node<K, V> next = node.next;


                int newIdx = node.key == null ? 0 : node.key.hashCode() & (newCapacity - 1);


                node.next = newBuckets[newIdx];
                newBuckets[newIdx] = node;

                node = next;
            }
        }

        buckets = newBuckets;
    }
}

