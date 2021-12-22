/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public HeapNode min_node, first_node;
    public int heap_size, links_counter, cuts_counter;
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
        if(heap_size == 0){
            HeapNode node = new HeapNode(key);
            first_node = node;
            min_node = node;
            heap_size++;
            return node;
        }
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
                    first_node = node;
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
        delete(min_node);
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
        links_counter++;
        if(node1.getKey() > node2.key){
            HeapNode tmp = node1;
            node1 = node2;
            node2 = tmp;
        }
        if(node1.rank == 0){
            node1.child = node2;
            node2.parent = node1;
            node1.rank++;
            node2.prev = node2;
            node2.next = node2;
            return node1;
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
        if(x.parent != null)
    	    cascadingCut(x);

        if(heap_size == 1){
            min_node = null;
            first_node = null;
            heap_size = 0;
            return;
        }
        else if(x.rank == 0){
            x.prev.next = x.next;
            x.next.prev = x.prev;
        }
        else {
            HeapNode node = x.child;
            node.parent = null;
            node = node.next;
            while (node != x.child) {
                node.parent = null;
                node.marked = false;
                node = node.next;
            }
            x.prev.next = x.child;
            HeapNode tmp = x.child.prev;
            x.child.prev = x.prev;
            tmp.next = x.next;
            x.next.prev = tmp;
        }
        heap_size--;
        if(first_node == x && x.next != x)
            first_node = x.next;
        else if(first_node == x)
            first_node = x.child;
        consolidation();
    }

    public void cut(HeapNode x){
        cuts_counter++;
        HeapNode y = x.parent;
        x.parent = null;
        x.marked = false;
        y.rank--;
        if(x == x.next) {
            y.child = null;
        }
        else{
            y.child = x.next;
            x.prev.next = x.next;
            y.child.prev = x.prev;
            x.next = x;
            x.prev = x;
        }
        x.next = first_node;
        x.prev = first_node.prev;
        first_node.prev =x;
        x.prev.next = x;
        first_node = x;
    }
    public void cascadingCut(HeapNode x){
        HeapNode y = x.parent;
        cut(x);
        HeapNode node = y;
        while(y.marked){
            node = y.parent;
            cut(y);
            y = node;
        }
        if(y.parent != null)
            y.marked = true;
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {
        x.key -= delta;
        if(x.parent != null){
            if(x.parent.key > x.key)
                cascadingCut(x);
        }
        if(x.key < min_node.key)
            min_node = x;
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



    	public HeapNode(int int_key)
        {
    		this.key = int_key;
            marked = false;
            next = this;
            prev = this;
    	}

    	public int getKey()
        {
    		return this.key;
    	}

   }

    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        for(int i =0;i<17;i++){
            heap.insert(i);
        }
        System.out.println(heap.size());
        heap.deleteMin();
        heap.delete(heap.min_node.child);
        System.out.println(heap.findMin());

    }
}

