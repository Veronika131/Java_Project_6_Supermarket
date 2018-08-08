project folder:
Veronika131-cs1c-project06/


Brief description of submitted files:


docs/
    -   JavaDocs folder based on source code



src/lazyTrees/Item.java
    -   One object of Item class represents one item in the inventory, with two class members.

src/lazyTrees/LazySearchTree.java
    -   Lazy Search Tree enables removal of nodes that have been marked for lazy deletion
        with functionality that cleans up the tree with hard deletions when call to our tree's
        collectGarbage() is made.

src/lazyTrees/PrintObject.java
    -   The functor class PrintObject traverses and prints out data from our tree.

src/lazyTrees/SuperMarket.java
    -   Simulates the market scenario of removing and adding items to a store's inventory.

src/lazyTrees/Traverser.java
    -   Traverser interface used for traversing binary search tree.



resources/inventory_empty.txt
    -   Testing the boundary condition when removing item from empty inventory.

resources/inventory_invalid_removal.txt
    -   Testing the boundary condition when removing an item that may not exist.

resources/inventory_log.txt
    -   Testing the LazySearchTree by adding and removing items from the inventory.

resources/inventory_no_such_item_in_inventory.txt
    -   Testing removing item that was never in inventory.

resources/inventory_short.txt
    -   Testing for removal of root node from LazySearchTree.

resources/RUN.txt
    -   Various test outputs of SuperMarket class that display different states of inventory
        while adding and removing items to and from inventory and cleaning up inventory with
        garbage collection function.



.gitignore
    -   .gitignore content tells git to ignore specified things.

README.txt
    -   Description of submitted files.