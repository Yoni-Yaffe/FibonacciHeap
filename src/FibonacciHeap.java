/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public HeapNode min_node, first_node;
    public int heap_size;
    public boolean isRoot(HeapNode node){
        return node.parent == null;
    }
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return min_node == null; // should be replaced by student code
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {
        HeapNode node = new HeapNode(key);
        node.next = first_node;
        node.prev = first_node.prev;
        node.next.prev = node;
        node.prev.next = node;
        if(min_node.getKey() > node.getKey())
            min_node = node;
        first_node = node;
        heap_size++;
    	return node;
    }

    public void consolidation(){
        //insert to buckets
        HeapNode[] B = new HeapNode[2*(int)(Math.ceil(Math.log(heap_size)/Math.log(2.0)))];
        first_node.prev.next = null;
        HeapNode x = first_node;
        while(x != null){
            HeapNode y = x;
            x = x.next;
            while(B[y.rank] != null){
                y = connect(y, B[y.rank]);
                B[y.rank - 1] = null;
            }
            B[y.rank] = y;
        }
        //extract from buckets
        x = null;
        for(HeapNode node : B){
            if(node != null){
                if(x == null){ // first extraction
                    x = node;
                    x.next = x;
                    x.prev = x;
                }
                else{
                    HeapNode tmp = x.next;
                    x.next = node;
                    node.prev = x;
                    node.next = tmp;
                    tmp.prev = node;

                    if(node.key < x.key)
                        x = node;

                }
            }
        }
        first_node = x;
        min_node = x;
    }
   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
        if(heap_size == 1){
            min_node = null;
            first_node = null;
            heap_size = 0;
            return;
        }
        else if(min_node.rank == 0){
            min_node.prev.next = min_node .next;
            min_node.next.prev = min_node.prev;
        }
        else {
            HeapNode node = min_node.child;
            node.parent = null;
            node = node.next;
            while (node != min_node.child) {
                node.parent = null;
                node = node.next;
            }
            min_node.prev.next = min_node.child;
            HeapNode tmp = min_node.prev;
            min_node.child.prev = min_node.prev;
            tmp.next = min_node.next;
            min_node.next.prev = tmp;
        }
        heap_size--;
        if(first_node == min_node && min_node.next != null)
            first_node = min_node.next;
        else if(first_node == min_node)
            first_node = min_node.child;
        consolidation();
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()

    {
    	return min_node;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2) {
        HeapNode tmp_first = heap2.first_node;
        HeapNode tmp_last = heap2.first_node.prev;
        heap2.first_node.prev = first_node.prev;
        first_node.prev.next = heap2.first_node;
        first_node.prev = tmp_last; // second heap last tree
        tmp_last.next = first_node;
        heap_size += heap2.size();
        if(heap2.min_node.getKey() < min_node.key)
            min_node = heap2.min_node;
    }

    public HeapNode connect(HeapNode node1, HeapNode node2){
        if(node1.getKey() > node2.key){
            HeapNode tmp = node1;
            node1 = node2;
            node2 = tmp;
        }
        HeapNode tmp_last = node1.child.prev;
        node2.next = node1.child;
        node1.child.prev = node2;
        tmp_last.next = node2;
        node2.parent = node1;
        node2.prev = tmp_last;
        node1.child = node2;
        node1.rank++;
        return node1;
    }


   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return heap_size; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key, rank;
        public boolean marked;
        public HeapNode next, parent, child, prev;



    	public HeapNode(int key)
        {
    		this.key = key;
            marked = false;
            next = this;
            prev = this;
    	}

    	public int getKey()
        {
    		return this.key;
    	}

   }
}

