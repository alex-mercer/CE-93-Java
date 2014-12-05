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
        log(r);
        for (int i = 0; i < 200; i++) {
            int id = rand.nextInt(1000);
            char nationality = nations.charAt(rand.nextInt(6));
            char foodType = ftypes.charAt(rand.nextInt(2));
            char menuType = nations.charAt(rand.nextInt(6));
            int money = rand.nextInt(70);
            int vote = (rand.nextInt(10)) + 1;
            System.out.printf("Customer Entry id:%d nation:%c foodType:%c menu:%c money:%d vote:%d\n", id, nationality,
                    foodType, menuType, money, vote);
            r.CustomerEntry(id, nationality, foodType, menuType, money, vote);
        }
        while (r.getCustomers().length > 0) {
            r.settlement(r.getCustomers()[rand.nextInt(r.getCustomers().length)]);
            log(r);
        }
    }
}
