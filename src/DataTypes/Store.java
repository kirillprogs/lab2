package DataTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Store implements Iterable<Group> {

    private final List<Group> groups;

    public Store(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public Store() {
        this.groups = new ArrayList<>();
    }

    public void add(Group group) {
        this.groups.add(group);
    }

    public int numberOfGroups() {
        return this.groups.size();
    }

    public Group get(int number) throws IncorrectGroupException {
        if (number >= 0 && number < this.groups.size())
            return this.groups.get(number);
        else throw new IncorrectGroupException("Group position "+number
                +" is out of bounds for this store");
    }

    public void remove(int position) throws IncorrectGroupException {
        if (position >= 0 && position < this.groups.size()) {
            this.groups.get(position).setFile(null);
            this.groups.remove(position);
        }
        else throw new IncorrectGroupException("Group position is out of bounds for this store");
    }

    public void set(int position, Group newGroup) throws IncorrectGroupException {
        if (position >= 0 && position < this.groups.size()) {
            if (newGroup != null && newGroup.getName() != null && newGroup.getDescription() != null)
                this.groups.set(position, newGroup);
            else throw new IncorrectGroupException("Incorrect group! (not all fields initialized)");
        }
        else throw new IncorrectGroupException("Group position is out of bounds for this store");
    }

    public boolean fileExist(File file){
        return groups.stream().map(Group::getFile).anyMatch(f -> f.getName().equals(file.getName()));
    }

    public boolean ProductExist(String productName){
        return groups.stream()
                .flatMap(gr -> gr.getProducts().stream())
                .map(Product::getName)
                .anyMatch(name -> name
                        .equals(productName));
    }

    public boolean isEmpty() {
        return this.groups.isEmpty();
    }

    public void sortByName(boolean fromAtoZ) {
        this.groups.sort((fst, snd) -> compare(fst.getName(), snd.getName())*(fromAtoZ ? 1 : -1));
    }

    public void sortByNumberOfProducts(boolean fromReachToPoor) {
        this.groups.sort((fst, snd) -> {
            if (fst.getNumberOfProducts() > snd.getNumberOfProducts())
                return fromReachToPoor ? 1 : -1;
            else if (fst.getNumberOfProducts() < snd.getNumberOfProducts())
                return fromReachToPoor ? -1 : 1;
            else return 0;
        });
    }

    private static int compare(String fst, String snd) {
        for (int i = 0; i < Math.min(fst.length(), snd.length()); i++) {
            if (fst.charAt(i) < snd.charAt(i))
                return -1;
            else if (fst.charAt(i) > snd.charAt(i))
                return 1;
        }
        if (fst.length() < snd.length())
            return -1;
        if (snd.length() > snd.length())
            return 1;
        return 0;
    }

    @Override
    public Iterator<Group> iterator() {
        return new Iterator<Group>() {
            int position = 0;
            @Override
            public boolean hasNext() {
                return position < groups.size();
            }
            @Override
            public Group next() {
                return groups.get(position++);
            }
        };
    }
}