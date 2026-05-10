package Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase GameData.
 * Esta clase actúa como contenedor completo del estado del juego.
 * Se utiliza para guardar/cargar partida y sincronizar datos entre pantallas.
 * Implementa Serializable para permitir persistencia del estado.
 * @author Primera part Grup 4
 * @author Segona part Grup 1
 */


public class GameData implements Serializable {

    private static final long serialVersionUID = 1L;

    // Variables on es guarden el nivell y la puntuacio de l'usuari
    public int level;
    public long score;

    //Variables on es guarden els noms dels jugadors y el nickname
    public String jugador1;
    public String jugador2;
    public String nickname;

    //Variables on es guarden el mode de joc y el jugador que esta jugan en aquest moment
    public int modoJuego;
    public int jugadorActual;

    // Variables on es guarden les puntuacions dels jugadors
    public int puntuacionJugador1;
    public int puntuacionJugador2;


    // Variables on es guarden la posicio de la raqueta bola y obstacles
    public int racquetX;
    public ArrayList<BallState> balls;
    public ArrayList<Integer> obstacleX;
    public ArrayList<Integer> obstacleY;

    /**
     * Constructor que inicialitza les arraylists de bola y obstacles
     */
    public GameData() {
        balls = new ArrayList<>();
        obstacleX = new ArrayList<>();
        obstacleY = new ArrayList<>();
    }
    
    
    /**
     * Clase on es creen les variables de la posicio velocitat i direccio de la bola
     */
    public static class BallState implements Serializable {
        private static final long serialVersionUID = 1L;

        public double x;
        public double y;
        public double speed;
        public double directionX;
        public double directionY;
    }
}