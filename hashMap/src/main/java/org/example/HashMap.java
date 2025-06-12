package org.example;

import java.util.*;

public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    int threshold = 16;

    transient int size;
    transient Node<K, V>[] table;

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return key + "-" + value;
        }

        @Override
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    public V get(Object key) {
        int n, keyHash = hash(key);
        Node<K, V> node;
        Node<K, V>[] tab;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (node = tab[(n - 1) & keyHash]) != null) {
            if (node.hash == keyHash) {
                return node.value;
            } else {
                do {
                    if (node.hash == keyHash) return node.value;
                } while ((node = node.next) != null);
            }
        }
        return null;
    }

    public V put(K key, V value) {
        Node<K, V>[] tab;
        int n, i, keyHash = hash(key);
        if (key == null) throw new NullPointerException("key is null");
        if ((tab = table) == null || (n = table.length) == 0) n = (tab = resize()).length;
        if (tab[i = (keyHash & (n - 1))] == null)
            tab[i] = new Node<>(keyHash, key, value, null);
        else {
            Node<K, V> node = tab[i];
            for (; ; ) {
                if (node.next == null) {
                    node.next = new Node<>(keyHash, key, value, null);
                    break;
                } else node = node.next;
            }
        }
        if (++size > threshold)
            resize();
        return value;
    }

    public V remove(Object key) {
        int n, i, keyHash = hash(key);
        Node<K, V> node, had, p;
        Node<K, V>[] tab;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = node = tab[(i = ((n - 1) & keyHash))]) != null) {
            if (node.hash == keyHash) {
                if (node.next == null) {
                    table[i] = null;
                } else {
                    table[i] = node.next;
                }
                size--;
                return node.value;
            } else if (node.next != null) {
                had = node;
                do {
                    if (node.next.hash == keyHash) {
                        node.next = node.next.next;
                        table[had.hash & (n - 1)] = had;
                        size--;
                        return p.value;
                    }
                } while ((p = node = node.next) != null);
            }
        }
        return null;
    }

    final Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int newCap, oldCap = oldTable != null ? oldTable.length : 0;
        if (oldCap == 0)
            newCap = DEFAULT_INITIAL_CAPACITY;
        else if (oldCap >= DEFAULT_INITIAL_CAPACITY)
            threshold = newCap = oldCap * 2;
        else newCap = oldCap;
        if (newCap > MAXIMUM_CAPACITY)
            newCap = MAXIMUM_CAPACITY;

        @SuppressWarnings({"unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        if (oldTable != null) {
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> oldNode;
                if ((oldNode = oldTable[i]) != null) {
                    oldTable[i] = null;
                    if (oldNode.next == null) {
                        newTab[oldNode.hash & (newCap - 1)] = oldNode;
                    } else {
                        Node<K, V> nextNode, newNode;
                        do {
                            nextNode = oldNode.next;
                            if (newTab[oldNode.hash & (newCap - 1)] == null)
                                newTab[oldNode.hash & (newCap - 1)] = new Node<>(oldNode.hash, oldNode.key, oldNode.value, null);
                            else {
                                newNode = newTab[oldNode.hash & (newCap - 1)];
                                for (; ; ) {
                                    if (newNode.next == null) {
                                        newNode.next = new Node<>(oldNode.hash, oldNode.key, oldNode.value, null);
                                        break;
                                    } else newNode = newNode.next;
                                }
                            }
                        } while ((oldNode = nextNode) != null);
                    }
                }
            }
        }
        return newTab;
    }

    final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Node<K, V> node;
        Set<Entry<K, V>> set = new HashSet<>();
        for (Node<K, V> entry : table) {
            node = entry;
            if (entry != null)
                if (entry.next == null)
                    set.add(entry);
                else do {
                    set.add(node);
                } while ((node = node.next) != null);
        }
        return set;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        Set<Entry<K, V>> set = entrySet();
        for (Entry<K, V> entry : set) {
            if (value.equals(entry.getValue())) return true;
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
