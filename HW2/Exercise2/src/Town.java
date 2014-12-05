import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by amin on 11/15/14.
 */
public class Town {
    String name;
    List<Building> buildings;

    public Town(String name) {
        this.name = name;

    }

    public void addBuilding(Building building) {

    }

    public String getNames(Class<?> type) {
        String ans = "";
        for (Building building : buildings)
            for (People p : building.people)
                if (p.getClass() == type) {
                    if (!ans.isEmpty())
                        ans += ' ';
                    ans += p.name;
                }
        return ans;
    }

    public String printVillanNames() {
        return getNames(Villain.class);
    }

    public String printHeroNames() {
        return getNames(Hero.class);
    }
    public Town defend(Town town,Hero hero)
    {
        return this;
    }
}

class Superpower {
    String name;
    int strength;
    int level;

    public Superpower(String name, int strength, int level) {
        this.name = name;
        this.strength = strength;
        this.level = level;
    }

    public Superpower(Superpower superpower) {
        this(superpower.name, superpower.strength, superpower.level);
    }

    public String returnName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int newStrength) {
        this.strength = newStrength;
    }


}

class People {
    String name;
    String job;
    Town town;
    Building position;

    public People(String name, String job, Town town, Building position) {
        this.name = name;
        this.job = job;
        this.town = town;
        this.position = position;
        position.addPeople(this);
    }

    public People(People p) {
        this(p.name, p.job, p.town, p.position);
    }

    public String getInfo() {
        return name + ' ' + job;
    }

    public void changePosition(Building newPosition) {
        position.removePeople(this);
        newPosition.addPeople(this);
    }
}

abstract class SuperPeople extends People {
    List<Superpower> superpowers;

    protected SuperPeople(String name, String job, Town town, Building position, List<Superpower> superpowers) {
        super(name, job, town, position);
        this.superpowers = superpowers;
    }

    public void addSuperpower(Superpower superpower) {
        superpowers.add(superpower);
    }

    public List<Superpower> getSuperpowers() {
        return superpowers;
    }

}

class Villain extends SuperPeople {
    Villain(String name, String job, Town town, Building position, List<Superpower> superpowers) {
        super(name, job, town, position, superpowers);
    }
}

class Hero extends SuperPeople {
    Hero(String name, String job, Town town, Building position, List<Superpower> superpowers) {
        super(name, job, town, position, superpowers);
    }

    @Override
    public String getInfo() {
        return name + ' ' + job + ' ' + superpowers.size();
    }
}

class Building {
    String name;
    int height;
    Town town;
    List<People> people;

    public Building(String name, int height, Town town) {
        this.name = name;
        this.height = height;
        this.town = town;
        people = new ArrayList<People>();
        town.addBuilding(this);
    }

    public String getInfo() {
        return name + ' ' + height + ' ' + town.name;
    }

    public String getPopulation() {
        String ans = "";
        for (People p : people) {
            if (ans.length() > 0)
                ans += '\n';
            ans += p.name;
        }
        return ans;
    }

    void addPeople(People p) {
        people.add(p);
    }

    void removePeople(People p) {
        people.remove(p);
    }

}

class NaturalDisaster {
    Building position;
    int strength;//strength of disaster not Superpower
    Superpower superpower;

    NaturalDisaster(Building position, int strength, Superpower superpower) {
        this.position = position;
        this.strength = strength;
        this.superpower = superpower;
    }

    public void attack() {
        List<People> people = position.people;
        for (int i = 0; i < people.size(); i++) {
            People p = people.get(i);
            if (superpower == null) {
                if (p instanceof SuperPeople) {
                    SuperPeople sp = (SuperPeople) p;
                    //not using foreach because we need to remove elements
                    for (Iterator<Superpower> it = sp.getSuperpowers().iterator(); it.hasNext(); ) {
                        Superpower power = it.next();
                        power.strength -= strength;
                        if (power.strength <= 0)
                            it.remove();
                    }
                    if (sp.superpowers.isEmpty())//Converting to normal person
                        people.set(i, new People(p));
                }
                else {
                    people.remove(i);
                    i--;
                }
            }
            else {
                if (p instanceof SuperPeople) {
                    SuperPeople sp = (SuperPeople) p;
                    boolean foundPower = false;
                    for (Superpower power : sp.getSuperpowers()) {
                        if (power.name.equals(superpower.name)) {
                            power.strength += superpower.strength;
                            foundPower = true;
                            break;
                        }
                    }
                    if (!foundPower)
                        sp.getSuperpowers().add(new Superpower(superpower));//cloning superpower
                }
                else {
                    LinkedList<Superpower> powers = new LinkedList<Superpower>();
                    powers.add(new Superpower(superpower));//cloning superpower
                    people.set(i, new Hero(p.name, p.job, p.town, p.position, powers));
                }
            }
        }
    }
}

class LightningHit extends NaturalDisaster {
    int minHeight;

    LightningHit(Building position, int strength, Superpower superpower, int minHeight) {
        super(position, strength, superpower);
        this.minHeight = minHeight;
    }

    @Override
    public void attack() {
        if (position.height > minHeight)
            super.attack();
    }
}