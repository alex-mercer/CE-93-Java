import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by amin on 11/15/14.
 */
public class Restaurant {

    public static final char[] NATIONALITIES = new char[]{'c', 'i', 'f', 'h', 'p', 'e'};
    public static final int INITIAL_FUND = 10000;
    public static final int WAITERS_PER_NATIONALITY = 5;
    public static final int WAITERS_COUNT = WAITERS_PER_NATIONALITY * NATIONALITIES.length;
    public static final int DISCOUNT_AMOUNT = 1;
    public static final int COOKING_QUANTITY = 10;
    public static final int COOKING_COST_PER_FOOD = 10;
    public static final int[] FOOD_QUALITIES = new int[]{10, 7, 5, 8};
    public static int maxId = 0;//maxId + 1 is always a new Id
    private String name;
    private int capacity;
    private int fund;
    private Food[] foods;
    private List<Customer> customers;
    private List<Waiter> customersWaiters;
    private List<Customer> allCustomers;
    private Waiter[] waiters;
    private int[] waitersTurns;

    public Restaurant(String name) {
        this(name, 50);
    }

    public Restaurant(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.fund = Restaurant.INITIAL_FUND;
        this.customers = new ArrayList<Customer>();
        this.customersWaiters = new ArrayList<Waiter>();
        this.allCustomers = new ArrayList<Customer>();
        //Generating foods
        foods = new Food[100];
        for (int i = 0; i < 100; i++) {
            int foodQuality = FOOD_QUALITIES[(i % 20) / 5];
            foods[i] = new Food(i + 1, foodQuality, foodQuality, NATIONALITIES[i / 20], (i % 20) < 15 ? 'n' : 'v');
        }
        //Generating Waiters
        waiters = new Waiter[WAITERS_COUNT];
        for (int i = 0; i < WAITERS_COUNT; i++) {
            waiters[i] = new Waiter(i + 1, NATIONALITIES[i / WAITERS_PER_NATIONALITY]);
        }
        waitersTurns = new int[NATIONALITIES.length];
        for (int i = 0; i < NATIONALITIES.length; i++)
            waitersTurns[i] = 0;
        checkFoods();
    }

    public void cooking(char nationality) {
        for (Food food : foods) {
            if (food.getNationality() == nationality) {
                food.setNumber(COOKING_QUANTITY);
                fund -= COOKING_QUANTITY * COOKING_COST_PER_FOOD;
            }
        }
    }

    /**
     * check whether foods of a nationality is emptied out or not
     * if it is the cooking function for that nationaliy is invoked
     */
    private void checkFoods() {
        for (int i = 0; i < 5; i++) {
            boolean hasFood = false;
            for (int j = 0; j < 20; j++)
                if (foods[i * 20 + j].getNumber() > 0)
                    hasFood = true;
            if (!hasFood)
                cooking(NATIONALITIES[i]);
        }
    }

    public void CustomerEntry(int id, char nationality, char foodType, char menuType,
                              int money, int vote) {
        boolean takhfif = false;
        for (Customer c : allCustomers)
            if (c.getId() == id && c.getNationality() == nationality)
                takhfif = true;
        if (!takhfif)
            id = maxId + 1;
        money -= Waiter.WAITER_TIP;
        Customer customer = new Customer(id, nationality, foodType, menuType, money, vote);
        customer.setTakhfif(takhfif);
        Menu menu = new Menu(getAvailableFoods(), customer);
        if (customer.sad())
            return;
        maxId = Math.max(maxId, id);
        if (customers.size() == capacity) {
            for (int i = 0; i < capacity / 5; i++)
                settlement(customers.get(0));
        }
        Food orderedFood = getCustomerOrderedFood(customer);
        orderedFood.setNumber(orderedFood.getNumber() - 1);
        checkFoods();
        int waiterType = 5;
        for (int i = 0; i < 5; i++)
            if (customer.getNationality() == NATIONALITIES[i])
                waiterType = i;
        int waiterId = waiterType * 5 + waitersTurns[waiterType];
        waitersTurns[waiterType] = (waitersTurns[waiterType] + 1) % 5;
        waiters[waiterId].setSalary(Waiter.WAITER_TIP);
        customers.add(customer);
        customersWaiters.add(waiters[waiterId]);
        allCustomers.add(customer);
    }

    private Food getCustomerOrderedFood(Customer customer) {
        return customer.getMenu().getMenuFoods()[0];
    }

    public void settlement(Customer customer) {
        int cost = customer.leaving();
        fund += cost;
        int index = customers.indexOf(customer);
        customers.remove(index);
        Waiter customerWaiter = customersWaiters.get(index);
        customerWaiter.incrementCustomers();
        if (customerWaiter.getCustomers() % 10 == 0) {
            customerWaiter.setSalary(10);
            fund -= 10;
        }
        customersWaiters.remove(index);
    }

    public Waiter[] getWaiters() {
        return waiters;
    }

    public Customer[] getCustomers() {
        return customers.toArray(new Customer[customers.size()]);
    }

    public Food[] getFoods() {
        return foods;
    }

    public int getFund() {
        return fund;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity - customers.size();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private Food[] getAvailableFoods() {
        ArrayList<Food> availableFoods = new ArrayList<Food>();
        for (Food food : getFoods()) {
            if (food.getNumber() > 0)
                availableFoods.add(food);
        }
        return availableFoods.toArray(new Food[availableFoods.size()]);
    }
}

class Food extends IDOwner

{
    private int qualityCount;
    private int internationalQualityCount;
    private int quality;
    private int internationalQuality;
    private char nationality;
    private char foodType;
    private int number;

    public Food(int id, int quality, int internationalQuality, char nationality, char foodType) {
        super(id);
        this.quality = quality;
        this.qualityCount = 1;
        this.internationalQuality = internationalQuality;
        this.internationalQualityCount = 1;
        this.nationality = nationality;
        this.foodType = foodType;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = (qualityCount * this.quality + quality) / (qualityCount + 1);
        qualityCount++;
    }

    public int getInternationalQuality() {
        return internationalQuality;
    }

    public void setInternationalQuality(int internationalQuality) {
        this.internationalQuality = (internationalQualityCount * this.internationalQuality + internationalQuality) /
                (internationalQualityCount + 1);
        internationalQualityCount++;
    }

    public char getNationality() {
        return nationality;
    }

    public char getFoodType() {
        return foodType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFinished() {
        return (this.number == 0);
    }

    /**
     * Computes the local price based on the formula and the local quality
     *
     * @return the computed price
     */
    private int getLocalPrice() {
        return Restaurant.COOKING_COST_PER_FOOD + 5 * getQuality();
    }

    /**
     * Computes the local price based on the formula and the international quality
     *
     * @return the computed price
     */
    private int getInternatinoalPrice() {
        return Restaurant.COOKING_COST_PER_FOOD + 5 * getInternationalQuality();
    }

    public int getPrice(char nationality) {
        if (getNationality() == nationality)
            return getLocalPrice();
        return getInternatinoalPrice();
    }
}

class FoodLocalComparator implements Comparator<Food> {
    @Override
    public int compare(Food f1, Food f2) {
        if (f1.getQuality() != f2.getQuality())
            return f2.getQuality() - f1.getQuality();
        return f1.getId() - f2.getId();
    }
}

class FoodInternationalComparator implements Comparator<Food> {
    @Override
    public int compare(Food f1, Food f2) {
        if (f1.getInternationalQuality() != f2.getInternationalQuality())
            return f2.getInternationalQuality() - f1.getInternationalQuality();
        return f1.getId() - f2.getId();
    }
}

class Menu {
    private static FoodInternationalComparator foodInternationalComparator = new FoodInternationalComparator();
    private static FoodLocalComparator foodLocalComparator = new FoodLocalComparator();
    private ArrayList<Food> menuFoods;
    private Customer myCustomer;

    public Menu(Food[] retaurantFoods, Customer myCustomer) {
        this.myCustomer = myCustomer;
        menuFoods = new ArrayList<Food>();
        for (Food food : retaurantFoods) {
            if (food.getFoodType() == 'n' && myCustomer.getFoodType() == 'v')//handling vegetarian customers
                continue;
            if (food.getNationality() != myCustomer.getMenuType())
                continue;
            int price = getFoodPrice(food);
            if (price > myCustomer.getMoney()) continue;
            menuFoods.add(food);
        }
        if (myCustomer.getNationality() == myCustomer.getMenuType())
            Collections.sort(menuFoods, foodLocalComparator);
        else
            Collections.sort(menuFoods, foodInternationalComparator);
        myCustomer.setMenu(this);
    }

    public int getFoodPrice(Food food) {
        int price = food.getPrice(myCustomer.getNationality());
        if (myCustomer.takhfif()) price -= Restaurant.DISCOUNT_AMOUNT;
        return price;
    }

    public Food[] getMenuFoods() {
        return menuFoods.toArray(new Food[menuFoods.size()]);
    }

    public Customer getMyCustomer() {
        return myCustomer;
    }
}

class IDOwner {

    private int id;

    IDOwner(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

class Customer extends IDOwner {
    private char nationality;
    private char foodType;
    private char menuType;
    private int money;
    private int vote;
    private Menu menu;
    private boolean takhfif;

    public Customer(int id, char nationality, char foodType, char menuType, int money, int vote) {
        super(id);
        this.nationality = nationality;
        this.foodType = foodType;
        this.menuType = menuType;
        this.money = money;
        this.vote = vote;
    }

    public int leaving() {
        int cost = getMenu().getFoodPrice(getOrderedFood());

        if (getOrderedFood().getNationality() == getNationality())
            getOrderedFood().setQuality(getVote());
        else
            getOrderedFood().setInternationalQuality(getVote());
        money -= cost;
        return cost;
    }

    public boolean sad() {
        return menu.getMenuFoods().length == 0;
    }

    public char getMenuType() {
        return menuType;
    }

    public void setTakhfif(boolean takhfif) {
        this.takhfif = takhfif;
    }

    public boolean takhfif() {
        return takhfif;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public char getNationality() {
        return nationality;
    }

    public char getFoodType() {
        return foodType;
    }

    public int getMoney() {
        return money;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    /**
     * @return the first food on the menu which is the ordered food
     */
    private Food getOrderedFood() {
        return menu.getMenuFoods()[0];
    }
}

class Waiter extends IDOwner {
    public static final int WAITER_TIP = 3;
    private char nationality;
    private int customers = 0;
    private int salary = 0;

    Waiter(int id, char nationality) {
        super(id);
        this.nationality = nationality;
    }

    public int getCustomers() {
        return customers;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary)// adds to current salary
    {
        this.salary += salary;
    }

    public char getNationality() {
        return nationality;
    }

    public void incrementCustomers() {
        customers++;
    }

}