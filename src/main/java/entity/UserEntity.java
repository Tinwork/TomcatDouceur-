package entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 28/06/2017.
 */
@Entity
@Table(name = "User")
public class UserEntity {

    /**
     * Type
     * @Enum
     */
    public enum Type {
        Personnel,
        Association,
        Company
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", insertable = false)
    private int Id;

    @Column(name = "subscribe_date")
    private Date subscribe_date;

    @Column(name = "last_connection_date")
    private Date last_connection_date;

    @Column(name = "user")
    private String user;

    @Column(name = "hash")
    private String hash;

    @Column(name = "salt")
    private String salt;

    @Column(name = "mail")
    private String mail;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    public Type getType(){
        return this.type;
    }

    public int getId() {
        return Id;
    }

    public Date getSubscribe_date() {
        return subscribe_date;
    }

    public Date getLast_connection_date() {
        return last_connection_date;
    }

    public String getUser() {
        return user;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getMail() {
        return mail;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setSubscribe_date(Date subscribe_date) {
        this.subscribe_date = subscribe_date;
    }

    public void setLast_connection_date(Date last_connection_date) {
        this.last_connection_date = last_connection_date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

