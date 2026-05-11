package minitennis.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Timestamp;


public class appHibernate {
    public static void main (String [] args) {
        SessionFactory sf = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Partides.class) // LA CLASSE CORRECTA
                .buildSessionFactory();
        
        Session s = sf.openSession();
        
        try {
            s.beginTransaction();
            // Creem una partida de prova
            Partides p = new Partides("Proba", 100, null);
            s.persist(p);
            s.getTransaction().commit();
            System.out.println("Guardat correctament!");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            sf.close();
        }
    }
}