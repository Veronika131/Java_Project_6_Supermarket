package lazyTrees;

import java.util.NoSuchElementException;


/**
 * Binary search tree (BST) that uses lazy deletion and also has functionality of
 * hard deletion using collectGarbage().
 *
 * @author Foothill College, Veronika Cabalova Joseph
 */

public class LazySearchTree<E extends Comparable< ? super E >>
        implements Cloneable
{

    //*************************** Inner Class LazySTNode START **************************
    /**
     * inner class LazySTNode describes single Node object
     */
    private class LazySTNode
    {
        // use public access so the tree or other classes can access members
        public LazySTNode lftChild, rtChild;
        public E data;
        public LazySTNode myRoot;  // needed to test for certain error
        public boolean deleted;

        public LazySTNode(E d, LazySTNode lft, LazySTNode rt )
        {
            lftChild = lft;
            rtChild = rt;
            data = d;
            deleted = false;
        }

        public LazySTNode()
        {
            this(null, null, null);
        }
    }
    //**************************** Inner Class LazySTNode END ****************************

    //reflects number of un-deleted nodes
    protected int mSize;

    //reflects number of hard nodes, deleted and un-deleted
    protected int mSizeHard;

    protected LazySTNode mRoot;

    /**
     * constructor for LazySearchTree
     */
    public LazySearchTree()
    {
        clear();
    }

    /**
     * this method checks if tree is empty
     * @return true if tree is empty
     */
    public boolean empty()
    {
        return (mSize == 0);
    }

    /**
     * accessor method for mSize
     * @return returns soft size (number of un-deleted nodes) of tree
     */
    public int size()
    {
        return mSize;
    }

    /**
     * accessor method for mSizeHard
     * @return returns hard size of tree (size of both deleted and un-deleted nodes)
     */
    public int sizeHard()
    {
        return mSizeHard;
    }

    /**
     * this method clears tree by re-setting default values to 0 & null
     */
    public void clear()
    {
        mSize = 0;
        mSizeHard = 0;
        mRoot = null;
    }

    /**
     * this method shows height of tree
     * @return int height of tree
     */
    public int showHeight()
    {
        return findHeight(mRoot, -1);
    }

    /**
     * this method finds minimum value in the tree
     * @return returns (generic type) minimum value of the tree
     */
    public E findMin()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMin(mRoot).data;
    }

    /**
     * this method finds maximum value in the tree
     * @return returns (generic type) maximum value of the tree
     */
    public E findMax()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMax(mRoot).data;
    }

    /**
     * this method calls private find() to search for x data in the tree
     * @param x general type
     * @return data of node that was found
     */
    public E find( E x )
    {
        LazySTNode resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }

    /**
     * this method checks if tree contains specified data value by calling
     * private find(), *soft
     * @param x generic type value
     * @return true if value was found, false if not
     */
    public boolean contains(E x)
    {
        return find(mRoot, x) != null;
    }

    /**
     * method inserts specified data into the tree
     * @param x generic type value
     * @return ture if insertion was successful
     */
    public boolean insert( E x )
    {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * method removes value from the tree by calling private remove()
     * @param x general type value
     * @return true if removal was successful
     */
    public boolean remove( E x )
    {
        int oldSize = mSize;
        remove(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * Traverser Hard, traverses the tree by calling protected
     * traverseHard and displays all the nodes (deleted and un-deleted)
     * @param func
     * @param <F>
     */
    public < F extends Traverser<? super E >>
    void traverseHard(F func)
    {
        traverseHard(func, mRoot);
    }

    /**
     * Traverser Soft, traverses the tree by calling protected traverseSoft
     * and displays only nodes that have not been lazily deleted
     * @param func
     * @param <F>
     */
    public <F extends Traverser<? super E >>
    void traverseSoft(F func)
    {
        traverseSoft(func, mRoot);
    }

    /**
     * Clone
     * @return new cloned object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException
    {
        LazySearchTree<E> newObject = (LazySearchTree<E>)super.clone();
        newObject.clear();  // can't point to other's data

        newObject.mRoot = cloneSubtree(mRoot);
        newObject.mSize = mSize;

        return newObject;
    }

    /**
     * method calls private collectGarbage method to remove nodes marked as deleted
     * @return true if removal was successful
     */
    public boolean collectGarbage()
    {
        int oldSize = mSize;
        mRoot = collectGarbage(mRoot);
        return (mSize != oldSize);
    }

    //=========================== private helper methods ===========================

    /**
     * method finds true minimum value, *hard
     * @param rootNode at which to start recursion down the tree
     * @return a reference to the node with the minimum value
     */
    protected LazySTNode findMinHard(LazySTNode rootNode)
    {
        if(rootNode == null)
        {
            return null;
        }

        LazySTNode minValueFound = findMinHard(rootNode.lftChild);

        //if minimum value of root node is null, return the root node
        if (minValueFound == null)
        {
            return rootNode;
        }
        //else return left child of root
        return minValueFound;
    }

    /**
     * method finds maximum value, *hard
     * @param rootNode at which to start recursion down the tree
     * @return a reference to the node with the maximum value
     */
    protected LazySTNode findMaxHard(LazySTNode rootNode)
    {
        if(rootNode == null)
        {
            return null;
        }

        LazySTNode maxValueFound = findMax(rootNode.rtChild);
        //if maximum value of root node is null, return the root node
        if (maxValueFound == null)
        {
            return rootNode;
        }
        //else return right child of root
        return findMaxHard(rootNode.rtChild);
    }

    /**
     * method removes *hard one node
     * @param root LazySTNode of root where we want to remove
     * @param x data of generic type to remove
     * @return object of type LazySTNode or the node that was just removed
     */
    protected LazySTNode removeHard(LazySTNode root, E x)
    {
        // avoid multiple calls to compareTo()
        int compareResult;

        if (root == null)
        {
            return null;
        }

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
        {
            //run remove hard on left child node
            root.lftChild = removeHard(root.lftChild, x);
        }
        else if ( compareResult > 0 )
        {
            //run remove hard on right child node
            root.rtChild = removeHard(root.rtChild, x);
        }

        // found the node
        else if (root.lftChild != null && root.rtChild != null)
        {
            root.data = findMinHard(root.rtChild).data;
            root.deleted = false;
            root.rtChild = removeHard(root.rtChild, root.data);
        }
        else
        {
            root =
                    (root.lftChild != null)? root.lftChild : root.rtChild;
            mSizeHard--;
        }
        return root;
    }


    /**
     * method allows the client to truly remove all deleted (stale) nodes from the tree
     * @param root LazySTNode of tree
     * @return LazySTNode root node
     */
    private LazySTNode collectGarbage(LazySTNode root)
    {
        //return null if root is null
        if (root == null)
        {
            return null;
        }

        //collect garbage on left and right root child
        root.lftChild = collectGarbage(root.lftChild);
        root.rtChild = collectGarbage(root.rtChild);

        //removes *hard all soflty deleted nodes from tree
        if(root.deleted)
        {
            root = removeHard(root, root.data);
        }
        return root;
    }

    /**
     * method finds minimum value, *soft
     * @param root LazySTNode of tree
     * @return smallest value of tree, LazySTNode type
     */
    protected LazySTNode findMin( LazySTNode root )
    {
        if (root == null)
        {
            return null;
        }
        LazySTNode minValueFound = findMin(root.lftChild);
        //if minimum value is not null, return min value of left child of root
        if (minValueFound != null)
        {
            return minValueFound;
        }
        //return root if its not marked as "deleted = ture"
        if(!root.deleted)
        {
            return root;
        }

        //else return right child of root
        return findMin(root.rtChild);
    }

    /**
     * method finds maximum value *soft
     * @param root LazySTNode of tree
     * @return biggest value of the tree, LazySTNode type
     */
    protected LazySTNode findMax( LazySTNode root )
    {
        if (root == null)
        {
            return null;
        }

        LazySTNode maxValueFound = findMax(root.rtChild);
        //if maximum value is not null, return max value of right child of root
        if (maxValueFound != null)
        {
            return maxValueFound;
        }
        //return root if its not marked as "deleted = ture"
        if(!root.deleted)
        {
            return root;
        }
        //else return left child of root
        return findMax(root.lftChild);
    }

    /**
     * method inserts x data of generic type into the tree
     * @param root LazySTNode of tree into which we want to insert
     * @param x data of generic type that we want to insert
     * @return LazySTNode
     */
    protected LazySTNode insert( LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        //if empty tree, create a root node if user passes in null
        if (root == null)
        {
            //updates both soft (mSize) and hard (mSizeHard)
            mSize++;
            mSizeHard++;
            return new LazySTNode(x, null, null);
        }

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
        {
            root.lftChild = insert(root.lftChild, x);
        }
        else if ( compareResult > 0 )
        {
            root.rtChild = insert(root.rtChild, x);
        }
        //checks if item was lazy deleted, if so, increment the size
        else if (root.deleted)
        {
            root.deleted = false;
            mSize++;
        }
        return root;
    }


    /**
     * this method implements implement lazy deletion and *softly removes
     * specified data from the tree by marking deleted as true (***Lazy Deletion)
     * @param root LazySTNode of root where we want to remove
     * @param x data of generic type to remove
     */
    protected void remove( LazySTNode root, E x  )
    {
        LazySTNode foundRoot = new LazySTNode();
        foundRoot = find(root, x);

        if(root == null)
        {
            return;
        }
        //if specified node is found, mark deleted as true
        if(foundRoot != null)
        {
            foundRoot.deleted = true;
            mSize--;
        }
    }


    /**
     * traverses the tree and displays all nodes (deleted and non-deleted nodes)
     * @param func specified function
     * @param treeNode node
     * @param <F> generic type
     */
    protected <F extends Traverser<? super E>>
    void traverseHard(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
        {
            return;
        }

        traverseHard(func, treeNode.lftChild);
        func.visit(treeNode.data);
        traverseHard(func, treeNode.rtChild);
    }

    /**
     * traverses the tree and displays only nodes that have not been marked as deleted
     * @param func specified function
     * @param treeNode node
     * @param <F> generic type
     */
    protected <F extends Traverser<? super E>>
    void traverseSoft(F func, LazySTNode treeNode)
    {
        if(treeNode == null)
        {
            return;
        }

        traverseSoft(func, treeNode.lftChild);
        //if node is NOT deleted display the data
        if(!treeNode.deleted)
        {
            func.visit(treeNode.data);
        }
        traverseSoft(func, treeNode.rtChild);
    }

    /**
     * protected find(), used by public find() to find x in specified root
     * @param root LazySTNode, where we want to look for x
     * @param x generic type of value we want to find
     * @return LazySTNode
     */
    protected LazySTNode find( LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
        {
            return null;
        }

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
        {
            return find(root.lftChild, x);
        }
        if (compareResult > 0)
        {
            return find(root.rtChild, x);
        }
        if(!root.deleted)
        {
            return root;// found
        }
        return null;
    }

    /**
     * clones subtree of specified root
     * @param root LazySTNode of subtree we want to clone
     * @return cloned subtree
     */
    protected LazySTNode cloneSubtree(LazySTNode root)
    {
        LazySTNode newNode;
        if (root == null)
            return null;

        // does not set myRoot which must be done by caller
        newNode = new LazySTNode
                (
                        root.data,
                        cloneSubtree(root.lftChild),
                        cloneSubtree(root.rtChild)
                );
        return newNode;
    }

    /**
     * method finds height of the tree
     * @param treeNode LazySTNode of tree we want to find height of
     * @param height integer
     * @return height
     */
    protected int findHeight( LazySTNode treeNode, int height )
    {
        int leftHeight, rightHeight;
        if (treeNode == null)
            return height;
        height++;
        leftHeight = findHeight(treeNode.lftChild, height);
        rightHeight = findHeight(treeNode.rtChild, height);
        return (leftHeight > rightHeight)? leftHeight : rightHeight;
    }
}