/**
 * Represents a list of Nodes. 
 */
public class LinkedList {
	
	private Node first; // pointer to the first element of this list
	private Node last;  // pointer to the last element of this list
	private int size;   // number of elements in this list
	
	/**
	 * Constructs a new list.
	 */ 
	public LinkedList () {
		first = null;
		last = first;
		size = 0;
	}
	
	/**
	 * Gets the first node of the list
	 * @return The first node of the list.
	 */		
	public Node getFirst() {
		return this.first;
	}

	/**
	 * Gets the last node of the list
	 * @return The last node of the list.
	 */		
	public Node getLast() {
		return this.last;
	}
	
	/**
	 * Gets the current size of the list
	 * @return The size of the list.
	 */		
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the node located at the given index in this list. 
	 * 
	 * @param index
	 *        the index of the node to retrieve, between 0 and size
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 * @return the node at the given index
	 */		
	public Node getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size - 1");
		}
		Node current = first; // Start from the first node
		for(int i = 0; i < index; i++){
			current = current.next; // Move to the next node
		}
		return current; // Return the node at the specified index
	}
	
	/**
	 * Creates a new Node object that points to the given memory block, 
	 * and inserts the node at the given index in this list.
	 * <p>
	 * If the given index is 0, the new node becomes the first node in this list.
	 * <p>
	 * If the given index equals the list's size, the new node becomes the last 
	 * node in this list.
     * <p>
	 * The method implementation is optimized, as follows: if the given 
	 * index is either 0 or the list's size, the addition time is O(1). 
	 * 
	 * @param block
	 *        the memory block to be inserted into the list
	 * @param index
	 *        the index before which the memory block should be inserted
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 */
	public void add(int index, MemoryBlock block) {
		
		if (block == null) {
            throw new NullPointerException("MemoryBlock cannot be null");
        }
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index must be between 0 and size");
        }

		Node newNode = new Node(block);
		
		if (index == 0) {
			// Add to the beginning
			newNode.next = first;
			first = newNode;
			if (size == 0) {
				last = newNode; // Update last if the list was empty
				
			}
		}

		else if (index == size) {
			// Add to the end
			if (size == 0) {
				first = last = newNode; // Special case for an empty list
			}
			else{
				last.next = newNode;
				last = newNode;
			}
		}
		else{
			// Add to the middle
			Node prev = getNode(index - 1);
			newNode.next = prev.next;
			prev.next = newNode;
		}

		size++;
	}

	/**
	 * Creates a new node that points to the given memory block, and adds it
	 * to the end of this list (the node will become the list's last element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addLast(MemoryBlock block) {
		add(size, block);
	}
	
	/**
	 * Creates a new node that points to the given memory block, and adds it 
	 * to the beginning of this list (the node will become the list's first element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addFirst(MemoryBlock block) {
		add(0, block);
	}

	/**
	 * Gets the memory block located at the given index in this list.
	 * 
	 * @param index
	 *        the index of the retrieved memory block
	 * @return the memory block at the given index
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public MemoryBlock getBlock(int index) {
		return getNode(index).block;
	}	

	/**
	 * Gets the index of the node pointing to the given memory block.
	 * 
	 * @param block
	 *        the given memory block
	 * @return the index of the block, or -1 if the block is not in this list
	 */
	public int indexOf(MemoryBlock block) {
		if (block == null) {
            throw new NullPointerException("MemoryBlock cannot be null");
        }

		Node currentNode = first;
		for(int i = 0; i < size; i++){
			if (currentNode.block.equals(block)) {
				return i;
			}
			
			currentNode = currentNode.next;
		}

		return -1;
	}

	/**
	 * Removes the given node from this list.	
	 * 
	 * @param node
	 *        the node that will be removed from this list
	 */
	public void remove(Node node) {
		if (node == null || first == null) {
			throw new IllegalArgumentException("Node cannot be null, and the list cannot be empty");
		}

		// Special case: Removing the first node
		if (node == first) {
			first = first.next;
			if (first == null) {
				last = null; // The list is now empty
			}
			size--;
			return;
		}

		// Traverse the list to find the node before the one to remove
		Node currentNode = first;
		while (currentNode.next != null && currentNode != node) {
			currentNode = currentNode.next;			
		}

		if (currentNode.next == null || currentNode == null) {
			throw new IllegalArgumentException("Node not found in the list");
		}

		// Remove the node
		currentNode.next = currentNode.next.next;
		if (currentNode.next == null) { // If the removed node was the last node
			last = currentNode;
		}
		size--;
		return;
	}

	/**
	 * Removes from this list the node which is located at the given index.
	 * 
	 * @param index the location of the node that has to be removed.
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public void remove(int index) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}

		Node nodeToRemove = getNode(index);
		remove(nodeToRemove);
	}

	/**
	 * Removes from this list the node pointing to the given memory block.
	 * 
	 * @param block the memory block that should be removed from the list
	 * @throws IllegalArgumentException
	 *         if the given memory block is not in this list
	 */
	public void remove(MemoryBlock block) {
		if (block == null) {
			throw new IllegalArgumentException("Block cannot be null");
		}

		Node currentNode = first;
		while (currentNode != null) {
			if (currentNode.block.equals(block)) {
				remove(currentNode);
				return;
			}
			currentNode = currentNode.next;
		}

		throw new IllegalArgumentException("Block not found in the list");
	}	

	/**
	 * Returns an iterator over this list, starting with the first element.
	 */
	public ListIterator iterator(){
		return new ListIterator(first);
	}

	public void sortByBaseAddress(){
		if (size <= 1) {
			return;			
		}

		boolean swapped;
		do{
			swapped = false;
			Node currentNode = first;
			while (currentNode != null && currentNode.next != null) {
				if (currentNode.block.getBaseAddress() > currentNode.next.block.baseAddress) {
					MemoryBlock tempBlock = currentNode.block;
					currentNode.block = currentNode.next.block;
					currentNode.next.block = tempBlock;

					swapped = true;					
				}

				currentNode = currentNode.next;
			}
		}
		while(swapped); // Continue sorting if a swap happened
	}
	
	/**
	 * A textual representation of this list, for debugging.
	 * Each node appears in order, separated by arrows.
	 * For example: {(208,10)} -> {(218,5)} -> {(223,7)}
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node currentNode = first;

		while (currentNode != null) {
			sb.append(currentNode);
			if (currentNode.next != null) {
				sb.append(" -> ");				
			}
			currentNode = currentNode.next;
		}
		return sb.toString();
	}
}