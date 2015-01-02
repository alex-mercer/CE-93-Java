/**
 * Created by amin on 1/1/15.
 */
public class CEArrayList<E> implements CECollection<E> {
    private Object[] objects = new Object[1];
    private int size = 0;

    @Override
    public void add(E a) {
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
        int newSize = 0;//newSize that elements go to if some previous elements are removed
        E ans = null;
        for (int i = 0; i < size; i++) {
            objects[newSize] = objects[i];
            if (objects[newSize].equals(a))
                ans = (E) objects[newSize];
            else
                newSize++;
        }
        size = newSize;
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

    public E get(int index) throws CEArrayListIndexOutOfBoundException {
        if (index < 0 || index >= size)
            throw new CEArrayListIndexOutOfBoundException();
        return (E) objects[index];
    }

    public int indexOf(E a) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(a))
                return i;
        }
        return -1;
    }

    public void swap(int i, int j) throws CEArrayListIndexOutOfBoundException {
        if (i < 0 || i >= size || j < 0 || j >= size)
            throw new CEArrayListIndexOutOfBoundException();
        Object tmp = objects[i];
        objects[i] = objects[j];
        objects[j] = tmp;
    }

}

