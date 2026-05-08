package Data;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class GameData implements Serializable {

    private static final long serialVersionUID = 1L;

    public int level;
    public long score;

    public String jugador1;
    public String jugador2;
    public String nickname;

    public int modoJuego;
    public int jugadorActual;

    public int puntuacionJugador1;
    public int puntuacionJugador2;


    public int racquetX;
    public ArrayList<BallState> balls;
    public ArrayList<Integer> obstacleX;
    public ArrayList<Integer> obstacleY;

    public GameData() {
        balls = new ArrayList<>();
        obstacleX = new ArrayList<>();
        obstacleY = new ArrayList<>();
    }
    
    
    public static class BallState implements Serializable {
        private static final long serialVersionUID = 1L;

        public double x;
        public double y;
        public double speed;
        public double directionX;
        public double directionY;
    }
}