/**
 * Created by amin on 1/1/15.
 */
public interface CECollection<E> {
    public void add(E a);
    public void add(E... a);
    public void clear();
    public boolean containRef(E a);
    public boolean contain(E a);
    public E remove (E a);
    public boolean isEmpty();
    public int size();
    public E[] toArray();
}
