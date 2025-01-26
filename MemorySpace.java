	/**
	 * Represents a managed memory space. The memory space manages a list of allocated 
	 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
	 * used, respectively, for creating new blocks and recycling existing blocks.
	 */
	public class MemorySpace {

		// A list of the memory blocks that are presently allocated
		private LinkedList allocatedList;

		// A list of memory blocks that are presently free
		private LinkedList freeList;

		private int maxSize;

		/**
		 * Constructs a new managed memory space of a given maximal size.
		 * 
		 * @param maxSize
		 *            the size of the memory space to be managed
		 */
		public MemorySpace(int maxSize) {
			// initiallizes an empty list of allocated blocks.
			allocatedList = new LinkedList();
			// Initializes a free list containing a single block which represents
			// the entire memory. The base address of this single initial block is
			// zero, and its length is the given memory size.
			freeList = new LinkedList();
			freeList.addLast(new MemoryBlock(0, maxSize));
			this.maxSize = maxSize;
		}

		/**
		 * Allocates a memory block of a requested length (in words). Returns the
		 * base address of the allocated block, or -1 if unable to allocate.
		 * 
		 * This implementation scans the freeList, looking for the first free memory block 
		 * whose length equals at least the given length. If such a block is found, the method 
		 * performs the following operations:
		 * 
		 * (1) A new memory block is constructed. The base address of the new block is set to
		 * the base address of the found free block. The length of the new block is set to the value 
		 * of the method's length parameter.
		 * 
		 * (2) The new memory block is appended to the end of the allocatedList.
		 * 
		 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
		 * For example, suppose that the requested block length is 17, and suppose that the base
		 * address and length of the the found free block are 250 and 20, respectively.
		 * In such a case, the base address and length of of the allocated block
		 * are set to 250 and 17, respectively, and the base address and length
		 * of the found free block are set to 267 and 3, respectively.
		 * 
		 * (4) The new memory block is returned.
		 * 
		 * If the length of the found block is exactly the same as the requested length, 
		 * then the found block is removed from the freeList and appended to the allocatedList.
		 * 
		 * @param length
		 *        the length (in words) of the memory block that has to be allocated
		 * @return the base address of the allocated block, or -1 if unable to allocate
		 */
		public int malloc(int length) {	
		if (length <= 0) {
			throw new IllegalArgumentException("Block size must be positive");
		}	

		ListIterator freeIterator = freeList.iterator();

		while (freeIterator.hasNext()) {
			MemoryBlock freeBlock = freeIterator.next();

			if (freeBlock.getLength() > length) {
				int baseAddress = freeBlock.getBaseAddress();
				MemoryBlock newBlock = new MemoryBlock(baseAddress, length);
				allocatedList.addLast(newBlock);

				freeBlock.setBaseAddress(baseAddress + length);
				freeBlock.setLength(freeBlock.getLength() - length);
								
				return newBlock.getBaseAddress();
			}

			else if (freeBlock.getLength() == length) {
				// Exact match
				freeList.remove(freeBlock);
				allocatedList.addLast(freeBlock);  // Add a new block to allocatedList

				return freeBlock.getBaseAddress();
			}
		}

		// No suitable block found
		return -1;
	}


		/**
		 * Frees the memory block whose base address equals the given address.
		 * This implementation deletes the block whose base address equals the given 
		 * address from the allocatedList, and adds it at the end of the free list. 
		 * 
		 * @param baseAddress
		 *            the starting address of the block to freeList
		 */
		public void free(int address) {
			if (allocatedList.getSize() == 0) {
				throw new IllegalArgumentException("index must be between 0 and size");
			}
			
			// Locate the block in the allocatedList
			ListIterator allocatedIterator = allocatedList.iterator();

			while (allocatedIterator.current != null) {
				MemoryBlock currentBlock = allocatedIterator.next();
				if (currentBlock.getBaseAddress() == address) {
					freeList.addLast(currentBlock);
					
					// Remove the block from the allocatedList
					allocatedList.remove(currentBlock);
					return;
				}
			}
		}
		
		/**
		 * A textual representation of the free list and the allocated list of this memory space, 
		 * for debugging purposes.
		 */
		public String toString() {
			return freeList.toString() + "\n" + allocatedList.toString() + "";
		}

		// Helper method to format a list as a space-separated string of MemoryBlocks
		private String formatList(LinkedList list) {
			StringBuilder formatted = new StringBuilder();
			ListIterator iterator = list.iterator();
			while (iterator.hasNext()) {
				formatted.append(iterator.next().toString()).append(" ");
			}
			// Trim the trailing space, if any
			if (formatted.length() > 0) {
				formatted.setLength(formatted.length() - 1);
			}
			return formatted.toString();
		}
		
		/**
		 * Performs defragmantation of this memory space.
		 * Normally, called by malloc, when it fails to find a memory block of the requested size.
		 * In this implementation Malloc does not call defrag.
		 */
		public void defrag() {
			if (freeList == null || freeList.iterator().hasNext() == false) {
				return; // Nothing to defragment if freeList is empty
			}

			// Sort the free list by the base address to ensure contiguous blocks are adjacent
			freeList.sortByBaseAddress();

			// Create a temporary list to store merged free blocks
			LinkedList mergedFreeList = new LinkedList();

			// Create an iterator for the sorted free list
			ListIterator freeIterator = freeList.iterator();
			MemoryBlock previousBlock = freeIterator.next(); // Get the first block in the sorted list
			
			while (freeIterator.hasNext()) {
				MemoryBlock currentBlock = freeIterator.next();

				// If the previous block is adjacent to the current block, merge them
				if (previousBlock.getBaseAddress() + previousBlock.getLength() == currentBlock.getBaseAddress()) {
					// Merge blocks
					previousBlock.setLength(previousBlock.getLength() + currentBlock.getLength());
				}
				else{
					mergedFreeList.addLast(previousBlock);
					previousBlock = currentBlock; // Move to the next block
				}
			}
			
			// Add the last block to the merged list
			mergedFreeList.addLast(previousBlock);

			// Replace the old freeList with the mergedFreeList
			freeList = mergedFreeList;
		}
	}
