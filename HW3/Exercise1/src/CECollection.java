/**
 * Created by amin on 1/1/15.
 */
public interface CECollection<E> {
    public void add(E a);
    public void add(E... a);

    /**
     * Empties the collection so the size returns 0
     */
    public void clear();

    /**
     * Checks if a is in array
     *
     * @param a the element to check
     * @return true if there is an elements which points to the same object as a otherwise it returns false
     */
    public boolean containRef(E a);
    public boolean contain(E a);

    /**
     * Removes all objects equal to a in the collection
     *
     * @param a the object to remove
     * @return null if no object was found or one of the objects otherwise
     */
    public E remove(E a);

    /**
     * Check whether the collection is empty
     * @return true if the collection is empty false otherwise
     */
    public boolean isEmpty();

    /**
     *
     * @return an integer which is the size of collection
     */
    public int size();
    public Object[] toArray();
}
