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
        return "MyInteger{" +
                "value=" + value +
                '}';
    }
}

public class Main {

    public static void main(String[] args) {
        CESortedArrayList<MyInteger> tst = new CESortedArrayList<MyInteger>();
        tst.add(new MyInteger(43));
        tst.add(new MyInteger(43));
        tst.add(new MyInteger(42));
        tst.add(new MyInteger(45));
        System.out.println(tst.get(0));
        System.out.println(tst.size());
        tst.remove(new MyInteger(43));
        tst.add(new MyInteger(49));
        tst.add(new MyInteger(51));
        tst.add(new MyInteger(46));
        for (int i = 0; i < tst.size(); i++) {
            System.out.println(tst.get(i));
        }
//        System.out.println(tst.indexOf(new MyInteger(42)));
//        System.out.println(tst.indexOf(new MyInteger(46)));
//        System.out.println(tst.size());
    }
}
