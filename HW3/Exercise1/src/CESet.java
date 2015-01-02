/**
 * Created by amin on 1/1/15.
 */
public class CESet<E> implements CECollection<E> {
    private Object[] objects = new Object[1];
    private int size = 0;

    /**
     * Add an element to the set if it doesn't exist
     * @param a the element to add
     */
    @Override
    public void add(E a) {
        if (contain(a))
            return;
        if (objects.length == size) {
            Object[] bigger_objects = new Object[objects.length * 2];
            for (int i = 0; i < objects.length; i++)
                bigger_objects[i] = objects[i];
            objects = bigger_objects;
        }
        objects[size] = a;
        size++;
    }

    @Override
    public void add(E... a) {
        for (E x : a)
            add(x);
    }

    @Override
    public void clear() {
        objects = new Object[1];
        size = 0;
    }

    @Override
    public boolean containRef(E a) {
        for (int i = 0; i < size; i++) {
            if (objects[i] == a)
                return true;
        }
        return false;
    }

    @Override
    public boolean contain(E a) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(a))
                return true;
        }
        return false;
    }

    @Override
    public E remove(E a) {
        E ans = null;
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(a)) {
                ans = (E) objects[i];
                objects[i] = objects[size - 1];
                objects[size - 1] = null;
                size--;
                break;
            }
        }
        return ans;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] ans = new Object[size];
        for (int i = 0; i < size; i++)
            ans[i] = objects[i];
        return ans;
    }
}
