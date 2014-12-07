import java.util.*;

/**
 * Created by amin on 11/15/14.
 */
public class Town {
    String name;
    List<Building> buildings;

    public Town(String name) {
        this.name = name;
        buildings = new ArrayList<Building>();
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }


    public List<People> getPeople() {
        List<People> people = new ArrayList<People>();
        for (Building building : buildings)
            people.addAll(building.people);
        return people;
    }

    /**
     * @param type the type of people to return; type can be People,Hero or Villain
     * @return a space separated list of people with the type specified sorted lexicographically
     */
    public String getNames(Class<?> type) {
        String ans = "";
        List<String> peopleNames = new ArrayList<String>();
        for (People p : getPeople())
            if (p.getClass() == type)
                peopleNames.add(p.getName());
        Collections.sort(peopleNames);
        for (String s : peopleNames) {
            if (!ans.isEmpty())
                ans += ' ';
            ans += s;
        }
        return ans;
    }

    public String printVillanNames() {
        return getNames(Villain.class);
    }

    public String printHeroNames() {
        return getNames(Hero.class);
    }

    /**
     * Changes town property of all fields of loserTown
     *
     * @param loserTown  the town to move it's building to this town
     * @param deadPerson the person to ignore
     * @return this town
     */
    public Town conquer(Town loserTown, People deadPerson) {
        for (Building b : loserTown.getBuildings()) {
            b.removePeople(deadPerson);
            b.setTown(this);
        }
        return this;
    }

    /**
     * Simulates an attack from Town town with Hero hero to this town
     *
     * @param town the attacker town
     * @param hero the hero from attacker town
     * @return
     */
    public Town defend(Town town, Hero hero) {
        Superpower attackerSuperpower = hero.getBestSuperpower();
        Superpower best = null;
        People defender = null;
        //Finding a hero with the same superpower
        for (People p : getPeople())
            if (p instanceof Hero) {
                Superpower superpower = ((Hero) p).findSuperpower(attackerSuperpower);
                if (superpower == null) continue;
                if (best == null || best.getStrength() < superpower.getStrength()) {
                    best = superpower;
                    defender = p;
                }
            }
        if (best != null)//Found a hero with the same superpower
        {
            if (best.getStrength() > attackerSuperpower.getStrength())
                return conquer(town, hero);
            else
                return town.conquer(this, defender);
        }
        //Finding a hero with the maximum strength*level
        for (People p : getPeople())
            if (p instanceof Hero) {
                for (Superpower superpower : ((Hero) p).getSuperpowers()) {
                    if (best == null || best.getScore() < superpower.getScore()) {
                        best = superpower;
                        defender = p;
                    }
                }
            }
        if (best != null) {
            if (best.getScore() > attackerSuperpower.getScore())
                return conquer(town, hero);
            else
                return town.conquer(this, defender);
        }
        return this;//Undefined behavior
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

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
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

    public int getScore() {
        return getLevel() * getStrength();
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

    /**
     * Copy constructor
     *
     * @param p the person to copy
     */
    public People(People p) {
        this(p.name, p.job, p.town, p.position);
    }

    public String getName() {
        return name;
    }

    public Building getPosition() {
        return position;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public String getInfo() {
        return name + ' ' + job;
    }

    public void changePosition(Building newPosition) {
        position.removePeople(this);
        newPosition.addPeople(this);
        this.position = newPosition;
        this.town = newPosition.getTown();
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

    /**
     * Finds the superpower with the same name
     *
     * @param superpower
     * @return the superpower with the same name or null if not found
     */
    public Superpower findSuperpower(Superpower superpower) {
        for (Superpower power : getSuperpowers())
            if (power.getName().equals(superpower.getName()))
                return power;
        return null;
    }

    /**
     * @return the superpower with highest level
     */
    public Superpower getBestSuperpower() {
        Superpower best = null;
        for (Superpower power : getSuperpowers())
            if (best == null || power.getLevel() > best.getLevel())
                best = power;
        return best;
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

    public Town getTown() {
        return town;
    }

    /**
     * Changes the town of building and all people inside it
     *
     * @param town the new town
     */
    public void setTown(Town town) {
        this.town = town;
        for (People p : getPeople())
            p.setTown(this.town);
    }

    public List<People> getPeople() {
        return people;
    }

    public String getInfo() {
        return name + ' ' + height + ' ' + town.name;
    }

    /**
     * @return list of people in the building sorted decreasing each in a new line
     */
    public String getPopulation() {
        String ans = "";
        List<String> peopleNames = new ArrayList<String>();
        for (People p : getPeople())
            peopleNames.add(p.getName());
        Collections.sort(peopleNames);
        Collections.reverse(peopleNames);
        for (String s : peopleNames) {
            if (!ans.isEmpty())
                ans += '\n';
            ans += s;
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