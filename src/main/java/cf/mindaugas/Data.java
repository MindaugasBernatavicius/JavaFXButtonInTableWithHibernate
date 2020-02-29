package cf.mindaugas;

import javax.persistence.*;

@Entity
@Table(name="data")
public class Data {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String name;

    public Data(){}
    public Data(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String nme) {
        this.name = nme;
    }

    @Override
    public String toString() {
        return "id: " + id + " - " + "name: " + name;
    }
}