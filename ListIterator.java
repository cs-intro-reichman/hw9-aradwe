/** Represents an iterator of a linked list. */
public class ListIterator {

    // current position in the list (cursor)
    public Node current;
    // reference to the previous node, used for removal
    private Node previous;

    /** Constructs a list iterator, starting at the given node */
    public ListIterator(Node node) {
        current = node;
        previous = null;
    }

    /** Checks if this iterator has more nodes to process */
    public boolean hasNext() {
        return (current != null);
    }

    /** Returns the current element in the list, and advances the cursor */
    public MemoryBlock next() {
        Node currentNode = current;
        current = current.next;
        previous = currentNode;
        return currentNode.block;
    }

     /** Removes the current node from the list */
     public void remove(){
        if (previous == null) {
            throw new IllegalStateException("Cannot remove element before calling next()");            
        }
        
        // Remove the current node by updating the previous node's next pointer
        previous.next = current;  // Skip the current node, linking previous to current.next
        current = previous.next;  // Move current pointer to the next node
     
    }
}