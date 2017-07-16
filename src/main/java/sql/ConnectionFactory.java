package sql;

import helper.Loghandler;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 19/06/2017.
 */
public class ConnectionFactory {

    protected org.hibernate.SessionFactory factory;

    /**
     * Constructor
     */
    public ConnectionFactory() {
        this.makeCon();
    }

    /**
     * Make Con
     */
    protected void makeCon() {
        try {
            this.setUp();
        } catch (Exception e) {

        }
    }

    /**
     * Set Up
     * @throws Exception
     */
    protected void setUp() {
        final Configuration config = new Configuration();
        //config.configure("/sql/hibernate.cfg.xml");
        config.addAnnotatedClass(entity.LinkEntity.class);
        config.addAnnotatedClass(entity.UserEntity.class);
        config.addAnnotatedClass(entity.CountEntity.class);
        config.configure();

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                      .applySettings(config.getProperties())
                      .build();

        try {
             this.factory = config.buildSessionFactory(registry);
        } catch (Exception e ) {
            Loghandler.log(e.toString(), "info");
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * Get Factory
     * @return
     */
    public org.hibernate.SessionFactory getFactory() {
        return this.factory;
    }
}
