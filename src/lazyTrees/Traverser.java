package lazyTrees;

/**
 * traverser interface used for all tree traversal algorithms
 * @param <E> generic type
 *
 * @author Foothill College, Veronika Cabalova Joseph
 */
public interface Traverser<E>
{
    public void visit(E x);
}