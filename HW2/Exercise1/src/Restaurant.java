import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by amin on 11/15/14.
 */
public class Restaurant {

    public static final char[] NATIONALITIES = new char[]{'c', 'i', 'f', 'h', 'p', 'e'};
    static int maxId = 0;//maxId + 1 is always a new Id
    String name;
    int capacity;
    int fund;
    Food[] foods;
    List<Customer> customers;
    List<Customer> allCustomers;
    Waiter[] waiters;
    int[] waitersTurns;

    public Restaurant(String name) {
        this(name, 50);
    }

    public Restaurant(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.fund = 10000;//TODO
        this.customers = new ArrayList<Customer>();
        this.allCustomers = new ArrayList<Customer>();
        //Generating foods
        foods = new Food[100];
        int[] food_qualities = {10, 7, 5, 8};//TODO move this to a better place
        for (int i = 0; i < 100; i++) {
            foods[i] = new Food(i + 1, food_qualities[(i % 20) / 5], 0, NATIONALITIES[i / 20], (i % 20) < 15 ? 'v' : 'n');
        }
        //Generating Waiters
        waiters = new Waiter[30];//TODO:constant export
        for (int i = 0; i < 30; i++) {
            waiters[i] = new Waiter(i + 1, NATIONALITIES[i / 5]);
        }
        waitersTurns = new int[6];
        for (int i = 0; i < 6; i++)
            waitersTurns[i] = 0;
        checkFoods();
    }

    public void cooking(char nationality) {
        for (Food food : foods) {
            if (food.getNationality() == nationality) {
                food.setNumber(10);
                fund -= 10 * 10;//TODO: first 10 is number of foods second is each food cost
            }
        }
    }

    void checkFoods() {
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
        if (customers.size() == capacity)
            customers = customers.subList(capacity / 5, capacity);
        boolean takhfif = false;
        maxId = Math.max(maxId, id);
        for (Customer c : allCustomers) {
            if (c.getId() == id) {
                if (c.getNationality() == nationality)
                    takhfif = true;
                else {
                    maxId++;
                    id = maxId;
                }
                break;
            }
        }
        Customer customer = new Customer(id, nationality, foodType, menuType, money, vote);
        customer.setTakhfif(takhfif);
        Menu menu = new Menu(getAvailableFoods(), customer);
        if (customer.sad())
            return;
        Food orderedFood = customer.getOrderedFood();
        orderedFood.setNumber(orderedFood.getNumber() - 1);
        checkFoods();
        int waiterType = 5;
        for (int i = 0; i < 5; i++)
            if (customer.getNationality() == NATIONALITIES[i])
                waiterType = i;
        int waiterId = waiterType * 5 + waitersTurns[waiterType];
        waitersTurns[waiterType] = (waitersTurns[waiterType] + 1) % 5;
        waiters[waiterId].setSalary(3);//TODO:constant export
        customer.money -= 3;
        waiters[waiterId].incrementCustomers();
        if (waiters[waiterId].getCustomers() % 10 == 0)
            waiters[waiterId].setSalary(10);
        customers.add(customer);
        allCustomers.add(customer);
    }

    public void settlement(Customer customer) {
        int cost = customer.leaving();
        fund += cost;
        customer.money -= cost;
        customers.remove(customer);
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

    Food[] getAvailableFoods() {
        ArrayList<Food> availableFoods = new ArrayList<Food>();
        for (Food food : getFoods()) {
            if (food.getNumber() > 0)
                availableFoods.add(food);
        }
        return availableFoods.toArray(new Food[availableFoods.size()]);
    }
}

class Food

{
    int id;
    int qualityCount;
    int internationalQualityCount;
    int qualitySum;
    int internationalQualitySum;
    char nationality;
    char foodType;
    int number;

    public Food(int id, int quality, int internationalQuality, char nationality, char foodType) {
        this.id = id;
        this.qualitySum = quality;
        this.qualityCount = 1;
        this.internationalQualitySum = internationalQuality;
        this.internationalQualityCount = 1;
        this.nationality = nationality;
        this.foodType = foodType;
    }

    public int getId() {
        return id;
    }

    public int getQuality() {
        return qualitySum / qualityCount;
    }

    public void setQuality(int quality) {
        qualityCount++;
        qualitySum += quality;
    }

    public int getInternationalQuality() {
        return internationalQualitySum / internationalQualityCount;
    }

    public void setInternationalQuality(int internationalQuality) {
        internationalQualityCount++;
        internationalQualitySum += internationalQuality;
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

    public int getLocalPrice() {
        return 10 + 5 * getQuality();//TODO:export 10 as constant
    }

    public int getInternatinoalPrice() {
        return 10 + 5 * getInternationalQuality();
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
        return f2.getId() - f1.getId();
    }
}

class FoodInternationalComparator implements Comparator<Food> {
    @Override
    public int compare(Food f1, Food f2) {
        if (f1.getInternationalQuality() != f2.getInternationalQuality())
            return f2.getInternationalQuality() - f1.getInternationalQuality();
        return f2.getId() - f1.getId();
    }
}

class Menu {
    static FoodInternationalComparator foodInternationalComparator = new FoodInternationalComparator();
    static FoodLocalComparator foodLocalComparator = new FoodLocalComparator();
    ArrayList<Food> menuFoods;
    Customer myCustomer;

    public Menu(Food[] retaurantFoods, Customer myCustomer) {
        this.myCustomer = myCustomer;
        for (Food food : retaurantFoods) {
            if (food.getFoodType() == 'n' && myCustomer.getFoodType() == 'v')//handling vegetarian customers
                continue;
            if (food.getNationality() != myCustomer.getMenuType())
                continue;
            int price = getFoodPrice(food) + 3;//TODO:extract constant
            if (price > myCustomer.getMoney()) continue;
            menuFoods.add(food);
        }
        if (myCustomer.getNationality() == myCustomer.getFoodType())
            Collections.sort(menuFoods, foodLocalComparator);
        else
            Collections.sort(menuFoods, foodInternationalComparator);
        if (menuFoods.isEmpty())
            myCustomer.isSad = true;
        myCustomer.menu = this;
    }

    int getFoodPrice(Food food) {
        int price = food.getPrice(myCustomer.getNationality());
        if (myCustomer.takhfif()) price -= 1;//TODO:extract constant
        return price;
    }

    public Food[] getMenuFoods() {
        return menuFoods.toArray(new Food[menuFoods.size()]);
    }

    public Customer getMyCustomer() {
        return myCustomer;
    }
}

class Customer {
    int id;
    char nationality;
    char foodType;
    char menuType;
    int money;
    int vote;
    Menu menu;
    boolean takhfif;
    boolean isSad;

    Customer(int id, char nationality, char foodType, char menuType, int money, int vote) {
        this.id = id;
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
        return cost;
    }

    public boolean sad() {
        return isSad;
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

    public int getId() {
        return id;
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

    Food getOrderedFood() {
        return menu.getMenuFoods()[0];
    }
}

class Waiter {
    int id;
    char nationality;
    int customers = 0;
    int salary = 0;

    Waiter(int id, char nationality) {
        this.id = id;
        this.nationality = nationality;
    }

    public int getCustomers() {
        return customers;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) { // adds to current salary
        this.salary += salary;
    }

    public char getNationality() {
        return nationality;
    }

    public int getId() {
        return id;
    }

    public void incrementCustomers() {
        customers++;
    }

}

/* TODO:extract id and getId() method */