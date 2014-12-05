import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Town t=new Town("Tehran");
        People v=new Villain("adsf","gasdf",t,new Building("asg",235,t),new ArrayList<Superpower>());
        System.out.println(v instanceof SuperPeople);
    }
}
