package entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 26/06/2017.
 */
@Entity
@Table(name = "Link")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", insertable = false)
    private Integer Id;

    @Column(name = "hashnumber")
    private long hashnumber;

    @Column(name = "original_link")
    private String original_link;

    @Column(name = "short_link")
    private String short_link;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "password")
    private String password;

    @Column(name = "multiple_password")
    private String multiple_password;

    @Column(name = "captcha")
    private boolean captcha;

    @Column(name = "mail")
    private String mail;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "set_max_use")
    private Integer set_max_use;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public long getHashnumber() {
        return hashnumber;
    }

    public void setHashnumber(long hashnumber) {
        this.hashnumber = hashnumber;
    }

    public String getShortLink() {
        return short_link;
    }

    public void setShortLink(String shortLink) {
        this.short_link = shortLink;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int userID) {
        this.user_id = userID;
    }

    public Date getCreateDate() {
        return create_date;
    }

    public void setCreateDate(Date createDate) {
        this.create_date = createDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMultiplePwd() {
        return multiple_password;
    }

    public void setMultiplePwd(String multiplePwd) {
        this.multiple_password = multiplePwd;
    }

    public boolean isCaptcha() {
        return captcha;
    }

    public void setCaptcha(boolean captcha) {
        this.captcha = captcha;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date startDate) {
        this.start_date = startDate;
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date endDate) {
        this.end_date = endDate;
    }

    public int getMaxUse() {
        return set_max_use;
    }

    public void setMaxUse(Integer maxUse) {
        this.set_max_use = maxUse;
    }

    public void setOriginaLink(String link) {
        this.original_link = link;
    }

    public String getOriginaLink() {
        return this.original_link;
    }
}
