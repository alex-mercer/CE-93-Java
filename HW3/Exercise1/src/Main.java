class MyInteger extends Number implements CEComparable<MyInteger> {
    int value;

    public MyInteger(int value) {
        this.value = value;
    }

    @Override
    public int CompareTo(MyInteger a) {
        if(this.intValue() - a.intValue()<0)return -1;
        if(this.intValue() - a.intValue()>0)return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyInteger myInteger = (MyInteger) o;

        if (value != myInteger.value) return false;

        return true;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

public class Main {

    public static void main(String[] args) {
        CEArrayList<MyInteger> tst = new CEArrayList<MyInteger>();
        tst.add(new MyInteger(4));
        tst.add(new MyInteger(3));
        tst.add(new MyInteger(1));
        tst.add(new MyInteger(2));
        tst.add(new MyInteger(1));
        System.out.println(tst.get(0));
        System.out.println(tst.size());
        tst.remove(new MyInteger(2));
        tst.add(new MyInteger(6));
        tst.add(new MyInteger(6));
        tst.add(new MyInteger(8));
        for (Object number : tst.toArray())
            System.out.println(number);
    }
}
