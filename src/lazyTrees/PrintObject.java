package lazyTrees;
//import cs1c.Traverser;

/**
 * class PrintObject traverses and prints out data
 * @param <T> generic type
 *
 * @author Foothill College, Veronika Cabalova Joseph
 */

public class PrintObject<T> implements Traverser<T>
{
    public void visit(T x)
    {
        System.out.print(x + "");
    }
}