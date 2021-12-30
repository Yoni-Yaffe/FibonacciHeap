import java.util.Arrays;
//Asaf Livne and Jonathan Yaffe
/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public HeapNode min_node, first_node;
    public int heap_size, marked_counter, trees_counter;
    public static int links_counter = 0, cuts_counter = 0;
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return min_node == null;
    }

    /**
     *
     * @return the number of trees in the heap
     */
    public int getNumOfTrees(){
        if(isEmpty()) //no trees in the heap
            return 0;
        int counter = 1;
        HeapNode node= first_node;
        node= node.next;
        while(node != first_node){
            counter++;
            node = node.next;
        }
        return counter;

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
        trees_counter++;
        if(heap_size == 0){ // first node in the heap
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
            min_node = node;  // update min
        first_node = node;
        heap_size++;
    	return node;
    }



    public void consolidation(){
        //insert to buckets, in fibonacci heaps we won't need more than 2log(n) buckets
        HeapNode[] B = new HeapNode[2*(int)(Math.ceil(Math.log(heap_size)/Math.log(2.0)))+1]; // no more than 2logn ranks
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
        HeapNode new_min = null;
        for(HeapNode node : B){
            if(node != null){
                if(x == null){ // first extraction
                    first_node = node;
                    x = node;
                    x.next = x;
                    x.prev = x;
                    new_min = x;
                }
                else{  // x is the current last node in the heap
                    x.next = node;
                    node.prev = x;
                    x = node;

                    if(node.key < new_min.key)
                        new_min = node;

                }
            }
        }
        x.next = first_node; // connect first to last
        first_node.prev = x;
        min_node = new_min;
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
        trees_counter += heap2.trees_counter;
        marked_counter += heap2.marked_counter;
    }

    /**
     * gets two trees with the same rank
     * @param node1 A root of a valid tree in a fibonacci heap
     * @param node2 A root of a valid tree in a fibonacci heap
     * @return link the two trees, the node with the smaler value will be on top and the second node will be
     * his child
     */
    public HeapNode connect(HeapNode node1, HeapNode node2){
        links_counter++;
        if(node1.getKey() > node2.key){ // flip the nodes to ensure node1 root is smaller or equal
            HeapNode tmp = node1;
            node1 = node2;
            node2 = tmp;
        }

        if(node1.rank == 0){
            node1.child = node2;
            node2.parent = node1;
            node2.prev = node2; // connect node2 to itself
            node2.next = node2;
        }
        else {
            HeapNode tmp_last = node1.child.prev; // keep indicator to node1 child prev(node1 rightmost child)
            node2.next = node1.child;
            node1.child.prev = node2;
            tmp_last.next = node2;
            node2.parent = node1;
            node2.prev = tmp_last;
            node1.child = node2;
        }
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
    	return heap_size;
    }

    /**
     *
     * @return The largest rank of a tree in the current heap
     */
    public int getLargestRank(){
        if(isEmpty()) // no trees
            return -1;
        int max = first_node.rank;
        HeapNode node = first_node;
        node = node.next;
        while(node != first_node){ // runs until we come back to the first node
            if(node.rank > max)
                max = node.rank;
            node = node.next;
        }
        return max;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
        if(isEmpty()) // empty heap returns empty array
            return new int[0];
    	int[] arr = new int[getLargestRank() + 1];
        HeapNode node = first_node;
        arr[node.rank]++;
        node = node.next;
        while(node != first_node){
            arr[node.rank]++;
            node = node.next;
        }
        return arr;
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
        if(x.parent != null) // if x is not a root we cut him first
    	    cascadingCut(x);

        if(heap_size == 1){ // if x is the only element in the heap we reset the heap to be empty
            min_node = null;
            first_node = null;
            heap_size = 0;
            trees_counter = 0;
            return;
        }
        if(x.rank == 0){
            x.prev.next = x.next;
            x.next.prev = x.prev;
            trees_counter--;
        }
        else {
            HeapNode node = x.child;
            node.parent = null;
            if(node.marked)
                marked_counter--;
            node.marked = false;
            node = node.next;
            while (node != x.child) {
                node.parent = null;
                if(node.marked)
                    marked_counter--;
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
        if(first_node == x && x.child != null)
            first_node = x.child;
        else if(first_node == x) // x is the first root and the heap and not the only one
            first_node = x.next;
        consolidation(); //
        trees_counter = getNumOfTrees(); // update the number of trees in the heap after the consolidation

    }

    /**
     * cut x from its parent and concat x to the start of the heap
     * @param x the node to be cut from its parent, x will become a root and will be in the start of the heap
     */
    public void cut(HeapNode x){
        cuts_counter++;
        HeapNode y = x.parent; // keep indicator to x's parent
        x.parent = null;
        if(x.marked)// x could be marked but not necessarily
            marked_counter--;
        x.marked = false;
        y.rank--; // y has one less child
        if(x == x.next) { // y doesn't have a child
            y.child = null;
        }
        else{
            if(x == y.child)
                y.child = x.next;
            x.prev.next = x.next;
            x.next.prev = x.prev;
        }
        // concat x to the start of the heap
        x.next = first_node;
        x.prev = first_node.prev;
        first_node.prev =x;
        x.prev.next = x;
        first_node = x;
        trees_counter++;
    }

    /**
     * perform a series of cuts from x to the root until we reach an unmarked node
     * @param x a node in the heap that the series of cuts will begin with
     */
    public void cascadingCut(HeapNode x){
        HeapNode y = x.parent;
        cut(x);
        HeapNode node;
        while(y.marked){
            node = y.parent;
            cut(y);
            y = node;
        }
        if(y.parent != null) { //y is not the root
            y.marked = true;
            marked_counter++;
        }
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
        if(x.parent != null && x.parent.key > x.key) // x is not a root and his new value is smaller than its parent's key
            cascadingCut(x);
        if(x.key < min_node.key) // update the min_node if necessary
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
    	return trees_counter+ 2 * marked_counter;
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
    	return links_counter;
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
    	return cuts_counter;
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
        int[] arr = new int[k];
        int index = 0;
        FibonacciHeap tmp_heap = new FibonacciHeap();
        tmp_heap.insert(H.min_node.key); //insert the key to the new heap
        tmp_heap.min_node.original_pointer = H.min_node; //keep a pointer to the original node from H
        while(index < k){ // runs k times find the min in the tmp heap delete it and add its sons while maintaining pointers
            HeapNode node = tmp_heap.findMin();
            HeapNode tmp_pointer = node.original_pointer;
            arr[index] = node.key;
            tmp_heap.deleteMin();
            index++;
            HeapNode node2 = tmp_pointer.child;
            if(node2 == null) // the min node has no childs
                continue;
            tmp_heap.insert(node2.key).original_pointer = node2;
            node2 = node2.next;
            while(node2 != node.original_pointer.child){
                tmp_heap.insert(node2.key).original_pointer = node2;
                node2 = node2.next;
            }

        }
        return arr;
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
        public HeapNode next, parent, child, prev, original_pointer; // original pointer is for kmin function only



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
}

