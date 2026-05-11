package minitennis.main;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "partides") 
public class Partides implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player")
    private Integer idPlayer;

    @Column(name = "name")
    private String name;

    @Column(name = "score")
    private Integer score;

    @Column(name = "language")
    private String language;

    // Aquest camp el posem com a "insertable = false" perquè SQL posi el DEFAULT
    @Column(name = "date", insertable = false, updatable = false)
    private Timestamp date;

    public Partides() {
    	
    }

    // Constructor simplificat: Només el que necessitem enviar
    public Partides(String name, Integer score, String language) {
        this.name = name;
        this.score = score;
        this.language = language;
    }

    // Getters i Setters (necessaris per Hibernate)
    public Integer getIdPlayer() { return idPlayer; }
    public void setIdPlayer(Integer idPlayer) { this.idPlayer = idPlayer; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
}