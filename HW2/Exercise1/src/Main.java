import java.util.Random;

public class Main {


    public static void log(Restaurant r) {
        System.out.println("Food log:");
        for (Food f : r.getFoods()) {
            System.out.printf("Food id:%d nationality:%c type:%c localQ:%d " +
                    "internationalQ:%d number:%d\n", f.getId(),
                    f.getNationality()
                    , f.getFoodType(), f.getQuality(),
                    f.getInternationalQuality(), f.getNumber());
        }
        System.out.printf("Restaurant capacity:%d fund:%d\n",
                r.getCapacity(), r.getFund());
        System.out.println("Waiter log");
        for (Waiter w : r.getWaiters())
            System.out.printf("Waiter id:%d salary:%d customers:%d\n",
                    w.getId(), w.getSalary(), w.getCustomers());
    }

    public static void main(String[] args) {
        Random rand = new Random();
        rand.setSeed(1393);
        Restaurant r = new Restaurant("KFC");
        String nations = "cifhpe";
        String ftypes = "nv";
        System.out.println(ftypes.charAt(1));
        log(r);
        for (int i = 0; i < 100; i++) {
            r.CustomerEntry(rand.nextInt(), nations.charAt(rand.nextInt(6)),
                    ftypes.charAt(rand.nextInt(2)),
                    nations.charAt(rand.nextInt(6))
                    , rand.nextInt(70), (rand.nextInt(10)) + 1);
            log(r);
        }

    }
}
