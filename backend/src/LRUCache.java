import java.util.*;

class LRUCache {
    private node head, tail;
    private int capacity;
    private Map<Integer, node> mp;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        mp = new HashMap<>();

        // Sentinel nodes to avoid null checks
        head = new node(-1, -1);
        tail = new node(-1, -1);

        head.next = tail;
        tail.prev = head;
    }
    public int get(int key) {
        if (!mp.containsKey(key))
            return -1;

        node n = mp.get(key);
        delete(n); 
        insert(n); 
        return n.value;
    }
    public void put(int key, int value) {
        if (mp.containsKey(key)) {
            node n = mp.get(key);
            n.value = value;
            delete(n);
            insert(n);
        } else {
            if (mp.size() == capacity) {
                node lru =tail.prev;
                delete(lru);
                mp.remove(lru.key);
            }

            node n = new node(key, value);
            insert(n);
            mp.put(key, n);
        }
    }
    private void insert(node n) {
        n.next = head.next;
        n.prev = head;

        head.next.prev = n;
        head.next = n;
    }
    private void delete(node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"cache\":[");
        
        node current = head.next;
        boolean first = true;
        
        while (current != tail) {
            if (!first) sb.append(",");
            sb.append("{\"key\":").append(current.key)
              .append(",\"value\":").append(current.value).append("}");
            first = false;
            current = current.next;
        }
        
        sb.append("],\"capacity\":").append(capacity)
          .append(",\"size\":").append(mp.size()).append("}");
        
        return sb.toString();
    }
}
