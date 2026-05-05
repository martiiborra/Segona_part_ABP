package Data;

import java.io.Serializable;
import java.util.List;

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
}


class BallState implements Serializable {

    public double x;
    public double y;
    public double speed;
    public double directionX;
    public double directionY;
}
