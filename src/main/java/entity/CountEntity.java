package entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 28/06/2017.
 */
@Entity
@Table(name = "count")
public class CountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "id_link")
    private int id_link;

    @Column(name = "date")
    private Date date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_link() {
        return id_link;
    }

    public void setId_link(int id_link) {
        this.id_link = id_link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
