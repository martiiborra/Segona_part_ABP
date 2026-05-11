package minitennis.db;

import java.sql.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import minitennis.main.Partides;
/**
 * Definició de la classe Connexio.
 * Aquesta classe actua com connector del joc amb la base de dades.
 * La seva funció és connectar la Base de Dades MySQL al Eclipse, emmagatzemant les puntuacions	
 * 
 * @autor Primera part Grup 4
 * @author Segona part Grup 1
 */
public class Connexio {
	// Objecte que manté la connexió activa amb la BD
    private Connection cn = null;
    private static SessionFactory sessionFactory;

    //Constructor: s'executa en crear l'objecte i estableix el primer intent de connexió
    public Connexio() {
        connectar();
    }

    /**
     * Mètode privat per carregar el controlador (driver) i obrir la connexió.
     * Gestiona la reobertura si la connexió s'ha perdut o tancat.
     */
    private void connectar() {
        try {
        	// Comprova si la connexió és nul·la o s'ha tancat per seguretat
            if (cn == null || cn.isClosed()) {
            	// Carrega dinàmicament el Driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
             // Defineix la ruta de la BD, l'usuari i la contrasenya (contrasenya de MySQL candela)
                cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/retro_tennis", "root", "7022");
            }
        } catch (Exception ex) {
        	// Captura qualsevol error (Driver no trobat o error de xarxa)
            System.out.println("Error al connectar: " + ex.getMessage());
        }
    }
    /**
     * Guarda una partida a la base de dades i retorna el rànquing actualitzat.
     * @param nombre Nom del jugador
     * @param puntuacion Punts aconseguits
     * @param idioma Codi d'idioma utilitzat
     * @return Cadena de text amb el rànquing dels 10 millors
     */
    public String guardarPartida(String nombre, int puntuacion, String idioma) {
    		// Assegurem que estem connectats abans d'operar
    		connectar(); 
    		// Per construir la llista de millors puntuacions
        StringBuilder sb = new StringBuilder();
        // Crida al Procediment Emmagatzemat de MySQL
        String sql = "{CALL ranking10millors(?, ?, ?)}";
        // Utilitzem CallableStatement per executar el procediment de la base de dades
        try (CallableStatement cs = cn.prepareCall(sql)) {
        		// Assignem el primer paràmetre
            cs.setString(1, nombre);
            // Assignem el segon paràmetre
            cs.setInt(2, puntuacion);
            // Assignem el tercer paràmetre
            cs.setString(3, idioma);
            // Declaració i inicialització de variabel que executa la crida
            boolean resultados = cs.execute();
            // Si el procediment retorna dades (el rànquing), les processem
            if (resultados) {
                try (ResultSet rs = cs.getResultSet()) {
                    int pos = 1;
                    while (rs.next()) {
                    	// Afegim cada fila del rànquing al StringBuilder
                        sb.append(pos).append(". ")
                          .append(rs.getString("name")).append(" - ")
                          .append(rs.getInt("score")).append(" pts\n");
                        pos++;
                    }
                }
            } else {
                return "Partida guardada (sense rànquing disponible).";
            }// Retornem el rànquing complet com a text
            return sb.toString();// Retornem el rànquing complet com a text
        } catch (SQLException e) {
            return "Error al desar la partida: " + e.getMessage();
        }
    }
    
    /**
     * Consulta el top 10 de partides directament mitjançant una sentència SELECT.
     * Els resultats es mostren per la consola del sistema.
     */
    public void consultarRanking() {
        connectar();// Verifiquem la connexió
        String sql = "SELECT name, score, date, language FROM PARTIDES ORDER BY "
        		+ "score DESC LIMIT 10";

     // El try-with-resources garanteix que Statement i ResultSet es tanquin automàticament
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- RANKING ACTUAL ---");
         // Iterem per cada fila retornada per la consulta
            while (rs.next()) {
                System.out.println("Nom: " + rs.getString("name") +
                        " | Punts: " + rs.getInt("score") +
                        " | Data: " + rs.getTimestamp("date") +
                        " | Idioma: " + rs.getString("language"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la BD: " + e.getMessage());
        }
    }
    /**
     * Mètode principal per fer proves ràpides de la connexió.
     */
    
    public static void main (String[] args) {
    	// Creem la instància de connexió
    	Connexio c = new Connexio();


    	// Prova: Guardar una partida de test
    	c.guardarPartida("JugadorTest", 5000, "ES");


    	// Prova: Mostrar el rànquing per consola
    	c.consultarRanking();

    	}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                
                // Configuració manual (Substitueix usuari i pass si cal)
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/retro_tennis");
                configuration.setProperty("hibernate.connection.username", "root"); // El teu usuari
                configuration.setProperty("hibernate.connection.password", "");     // La teva pass
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
                
                // Afegim la classe
                configuration.addAnnotatedClass(minitennis.main.Partides.class);
                
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    	}
