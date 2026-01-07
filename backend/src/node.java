// Node class for Doubly Linked List
class node {
    int key, value;
    node prev, next;

    public node(int key, int value) {
        this.key = key;
        this.value = value;
        prev = next = null;
    }
}
