package minitennis.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import minitennis.main.Game;
import minitennis.utils.Utils;

/**
 * Definició de la classe Racquet. Aquesta entitat representa l'element
 * controlable per l'usuari. Implementa la lògica de moviment horitzontal i la
 * gestió d'entrada per esdeveniments.
 * 
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià
 *         Chenovart
 */
public class Racquet {
	// Defineixen l'estat immutable de les dimensions i la posició vertical de la
	// raqueta

	// Posició fixa en l'eix d'ordenades
	private static final int Y_POSITION = 330;

	// Amplitud horitzontal de l'objecte
	private static final int WIDTH = 60;

	// Altura vertical de l'objecte
	private static final int HEIGHT = 10;

	// Velocitat de la pala
	private static final int SPEED_RACQUET = 6;

	// Atributs d'instància

	// Coordenada d'abscisses
	private int x = 0;

	// Vector de velocitat lineal horitzontal
	private int xVelocitat = 0;

	// Referència de composició cap a l'objecte contenidor
	private Game game;

	/**
	 * Constructor de la classe Racquet. Realitza l'assignació de la instància de
	 * control del joc. * @param game Instància de l'objecte Game per accedir al
	 * context global.
	 */
	public Racquet(Game game) {

		// Injecció de dependència de l'objecte Game
		this.game = game;
	}

	/**
	 * Mètode getter que acedeix a x
	 * 
	 * @return el valor de x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Mètode setter que modifica el valor x
	 * 
	 * @param x, valor modificable
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Mètode getter que accedeix xVelocitat
	 * 
	 * @return el valor de xVelocitat
	 */
	public int getxVelocitat() {
		return xVelocitat;
	}

	/**
	 * Mètode setter que modifica el valor de xVelocitat
	 * 
	 * @param xVelocitat, valor modificable
	 */
	public void setxVelocitat(int xVelocitat) {
		this.xVelocitat = xVelocitat;
	}

	/**
	 * Mètode getter que accedeix a game
	 * 
	 * @return game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Mètode setter que modifica game
	 * 
	 * @param game, valor modificable
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Mètode getter que accedeix a y
	 * 
	 * @return el valor de y
	 */
	public static int getY() {
		return Y_POSITION;
	}

	/**
	 * Mètode getter que accedeix a Width
	 * 
	 * @return el valor de WIDTH
	 */
	public static int getWidth() {
		return WIDTH;
	}

	/**
	 * Mètode getter que accediex a Height
	 * 
	 * @return el valor de HEIGHT
	 */
	public static int getHeight() {
		return HEIGHT;
	}

	/**
	 * Mètode que actualitza l'estat de la posició de l'objecte. Implementa una
	 * validació de límits per evitar que l'objecte surti del marc del component
	 * gràfic.
	 */
	public void move() {

		// Declaració de variabl per al control del limit de la raqueta
		int limit;

		// EStructura condicional on avalua si el joc no es troba al limit
		if (game.getWidth() <= 0) {
			// El limit es WINDOW_WIDTH
			limit = Utils.WINDOW_WIDTH;
		} else {
			// Si no el que sigui
			limit = game.getWidth();
		}

		// Condicional lògic per validar si el següent pas es manté dins del rang permès
		if (x + xVelocitat > 0 && x + xVelocitat < limit - WIDTH) {
			// Actualització de la variable d'estat x
			x += xVelocitat;
		}
	}

	/**
	 * Mètode mutador per al control mitjançant dispositiu apuntador. Calcula la
	 * posició centrada de l'objecte respecte a la coordenada del cursor. * @param
	 * mouseX Coordenada horitzontal obtinguda de l'esdeveniment del ratolí.
	 */
	public void setMouse(int mouseX) {

		// Càlcul per centrar l'objecte: es resta la meitat de l'amplitud a la posició
		// del cursor
		int newX = mouseX - (WIDTH / 2);
		// Declaració de variabl per al control del limit de la raqueta
		int limit;

		// Estructura condicional on avalua si el joc no es troba al limit
		if (game.getWidth() <= 0) {
			// El limit es WINDOW_WIDTH
			limit = Utils.WINDOW_WIDTH;
		} else {
			// Si no el que sigui
			limit = game.getWidth();
		}
		// Validació de seguretat per mantenir l'objecte dins de l'espai renderitzable
		if (newX > 0 && newX < limit - WIDTH) {
			this.x = newX;
		}
	}

	/**
	 * Mètode que renderitza la representació gràfica de la raqueta.
	 * 
	 * @param g2d Context gràfic Graphics2D per a l'execució d'operacions de dibuix.
	 */
	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.MAGENTA);
		g2d.fillRect(x, Y_POSITION, WIDTH, HEIGHT);
	}

	/**
	 * Mètode de control d'esdeveniments per a l'alliberament de tecles. Reinicia el
	 * vector de velocitat per aturar el moviment inercial.
	 * 
	 * @param e Objecte KeyEvent que conté la informació de l'esdeveniment de
	 *          teclat.
	 */
	public void keyReleased(KeyEvent e) {
		// Aturada del moviment
		xVelocitat = 0;
	}

	/**
	 * Controlador d'esdeveniments per a la pulsació de tecles. Modifica el vector
	 * de velocitat segons l'entrada de l'usuari. * @param e Objecte KeyEvent que
	 * identifica la tecla premuda.
	 */
	public void keyPressed(KeyEvent e) {

		// Verificació del codi de tecla mitjançant constants de la classe KeyEvent
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {

			// Decrement vector cap a l'esquerra
			xVelocitat = -SPEED_RACQUET;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

			// Increment vector cap a la dreta
			xVelocitat = SPEED_RACQUET;
		}
		
		//if (e.getKeyCode() == KeyEvent.)

	}

	/**
	 * Retorna el volum d'ocupació de l'objecte per a càlculs de col·lisió externs.
	 * * @return Una nova instància de Rectangle que defineix el Bounding Box de la
	 * raqueta.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, Y_POSITION, WIDTH, HEIGHT);
	}

	/**
	 * Mètode d'accés per a la coordenada superior de l'objecte. * @return El valor
	 * de la constant Y.
	 */
	public int getTopY() {
		return Y_POSITION;
	}

}
