package minitennis.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities; 
import minitennis.db.Connexio;
import minitennis.objects.Ball;
import minitennis.objects.Obstacle;
import minitennis.objects.Racquet;
import minitennis.sound.Sound;
import minitennis.utils.Utils;
import minitennis.language.ControlLanguage;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import Data.GameData;
import Data.GameData.BallState;
import javax.swing.KeyStroke;


/**
 * Classe Game que hereta de JPanel, funciona com a motor principal del joc.
 * @author Primera part Grup 4
 * @author Segona part Grup 1
 * */

public class Game extends JPanel {

	// Declaracio i inicialitzacio de variables per pausar el joc
	private static final String teclaPausa = "P";
	private static final String accioPausa = "pauseGame";
	//Declaracio i inicialitzacio de variables que contenen les imatges del joc
	private static final String IMG_FONDO_BASE = "/Imatge/fondovideojuego.jpg";
	private static final String IMG_FONDO_2 = "/Imatge/fondovideojuego2.jpg";
	private static final String IMG_FONDO_3 = "/Imatge/fondovideojuego3.jpg";
	private static final String IMG_FONDO_4 = "/Imatge/fondovideojuego4.jpg";
	private static final String IMG_FONDO_5 = "/Imatge/fondovideojuego5.jpg";
	private static final String IMG_FONDO_6 = "/Imatge/fondovideojuego6.jpg";
	
	// Declaració i inicialització de final per el control de nivells on ha de sortir obstacles
	private final int NIVELL_MINIM_OBSTACLES = 2;

	private static final long serialVersionUID = 1L;

	// Instància dels elements del joc.

	// Instància d'objecte de la classe Racquet: La pala del jugador
	private Racquet racquet = new Racquet(this);
	// Instància d'objecte de la classe Sound: Gestor de sons
	private Sound sonido = new Sound();
	
	// Llista de boles actives (permet múltiples)
	List<Ball> balls = new ArrayList<>();
	// Llista d'obstacles a destruir
	List<Obstacle> obstacles = new ArrayList<>();

	//Declaració i inicialització de variable estàtica, que representa el nivelldel joc
	public static int level;
	// Declaració i inicialització de variable, puntuació basada en el temps
	private long score = 0;

	//Declaració i inicialització de variable, que controla la marca de temps per pujar de nivell
	private long startTime = System.currentTimeMillis();
	// Declaració i inicialització de variable, que emmagatzeman punts en el temps
	private long lastPointUpdate = System.currentTimeMillis();
	// Declaració i inicialització de variable, que serveix pel nom del jugador
	private String playerName;
	private String language;

	// Declaració i inicialització de variable, per l'imatge de fons del joc
	private Image fons;
	//Declaració i inicialització de finals, per el numero de fons disponibles
	private final int FONS_DISPONIBLES = 6;
	private Image[] fondos = new Image[FONS_DISPONIBLES];

	// Variable de control per assegurar que el bucle del joc s'aturi realment
	private boolean gameEnded = false;
	
	// Variable per guardar la referència del Timer del joc perquè es pugui detenir
	private Timer gameTimer = null;
	
	//Declaracio de la variable per el mode de joc
	private int modoJuego;
	
	//Variable que indica el jugador que esta jugan en aquell moment
	private int jugadorActual = 1;

	//Variables que emmagatzemen les puntuacions dels 2 jugadors una vegada termina la partida
	private int puntuacionJugador1 = 0;
	private int puntuacionJugador2 = 0;
	
	//Variable per reiniciar la partida de jugador 2 des de el nivell seleccionat a menu
	private int selectedLevel;
	
	//Variables que emmagatzemen informacio dels jugadors
	private String jugador1;
	private String jugador2;
	private String nickname;
	
	//Variable que cambia a true cuan es pausa la partida
	private boolean paused = false;

	/**
	 * Constructor del joc. Inicialitza components i escoltadors d'entrada.
	 * @param jugador1,    nom de jugador1
	 * @param jugador2,    nom de jugador2
	 * @param nickname,    nom dels dos jugadors com a grup
	 * @param selectedLevel, nivell inicial seleccionat
	 * @param language,    llenguatge seleccionat (angles, catala o castella)
	 * @param modoJuego,   mode de joc seleccionat (sigle o asincron)
	 * */
	
	//Afegim al constructor les variables modoJuego, jugador1, jugador2 i nickname
	public Game(String jugador1, String jugador2, String nickname, int selectedLevel, String language, int modoJuego) {

		// IMPORTANT: Reiniciem l'estat de control per si venim d'una partida anterior
		this.gameEnded = false; 
		
		initGameBasics(jugador1, jugador2, nickname, language, modoJuego, selectedLevel);
		
		Ball primeraBola = new Ball(this);

		//Declaració i incialització de variable que calcula la velocitat dinàmica de la pilota
		double velocidadCalculada = Utils.VELOCIDAD_BASE * Math.pow(Utils.INCREMENTO_POR_NIVEL, selectedLevel - 1);

		// Declaració i inicialització de variable que serveix com a limitador), mai superará el MAX_BALL_SPEED
		double velocidadFinal = Math.min(velocidadCalculada, Utils.MAX_BALL_SPEED);

		//Modifiquem la velocitat de primeraBola
		primeraBola.setSpeed(velocidadFinal);

		//Afegim a la llista la bola
		balls.add(primeraBola);

		// Cridem al metode que carrega el fons
		cargarFondos();

		// Actualitzem els obstacles segons el nivell
		actualitzarObstacles(Game.level);
		

		// Cridem al metode que te els controls
		initControls();		
		
		// Reproducció del so de fons
		sonido.playFondo();
		SwingUtilities.invokeLater(() -> requestFocusInWindow());

	}
	
	
	/**
	 * Constructor del joc quan es carrega una partida guardada
	 * Si existeix una partida es restaurara la seva estada.
	 * Si no, es inicialitza una nova partida amb valors per defecte
	 * @param data
	 * @param language
	 */
	public Game(GameData data, String language) {

		//Guardem idioma seleccionat
		this.language = language;

		//Carreguem els fons
		cargarFondos();
		// Si hi ha dades guardades les carreguem
	    if (data != null) {
	        cargarEstado(data);
	    } else {
	    	// Si no hi ha partida inicialitzem valors per defecte
	        this.modoJuego = 0;
	        this.jugadorActual = 1;
	        // Generem els obstacles del nivell inicial
	        actualitzarObstacles(level);
	    }
	    
	    //Cridem al metode que te els controls
	    initControls();
	    //Iniciem la musica de fons
	    sonido.playFondo();
	    
	    
	}

	/**
	 * Mètode que actualitza el nivell i la puntuació cada cert temps.
	 */

	private void updateLevel() {

		/*
		 * Declaració i inicialització de variable que que controla la marca de temps
		 * per pujar de nivell
		 */
		long now = System.currentTimeMillis();

		// Suma de puntuació (temps en ms)
		score += (now - lastPointUpdate);

		// Actualització de temps
		lastPointUpdate = now;

		// Estructura condicional on avaluem si arribem als 20s
		if (now - startTime >= Utils.TIME_TO_LEVEL_UP) {

			// Incrementem el nivell
			level++;

			// S'actualitza el temps
			startTime = now;

			// Estructura iterativa que recorre la llista balls
			for (Ball b : balls) {

				// Incrementem la velocitat de la bola amb el mètode increaseSpeed()
				b.increaseSpeed();

			}

			// Actualitzem els obstacles segons el nivell
			actualitzarObstacles(level);

		}

		getSonido().canviarMusica(level);

	}

	/**
	 * Mètode que executa tota la lògica de moviment del frame actual
	 * */
	public void move() {

		 
		// Si el joc ha acabat o pausat, ignorem qualsevol càlcul de moviment residual
		if (gameEnded || paused) {
		        return;
		    }
		

		// Estructura condicional que avalua si està fora de la pantalla
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}

		// Mètode updateLevel()
		updateLevel();

		// Fem servir el mètode .move() per moure la raqueta
		racquet.move();

		// Estructura condicional on si arriab al nivell 16 surt una bola nova
		if (level >= Utils.LEVEL_FOR_SECOND_BALL && balls.size() < 2) {

			// Instància d'objecte (bola) de la classe Ball
			Ball b2 = new Ball(this);

			// Li posem velocitat a la bola
			b2.setSpeed(balls.get(0).getSpeed());

			// Afegim la nova bola a la llista
			balls.add(b2);

		}

		// Estructura iterativa que s'encarrega de moure les boles
		for (int i = 0; i < balls.size(); i++) {

			// Instància d'objecte que mira si hagut col·lisió amb un obstacle
			Obstacle chocado = balls.get(i).move(obstacles);

			// Si hagut col·lisió
			if (chocado != null) {
				// Eliminem l'obstacle
				obstacles.remove(chocado);
			}
		}

		// Estructura iterativa que recorre la llista d'obstacles
		for (Obstacle o : obstacles) {
			// fem servir el mètode move() per moure el obstacle
			o.move();
		}

	}

	/**
	 * * Mètode principal de renderització del component.
	 * @param g, Objecte Graphics que permet realitzar operacions de dibuix.
	 * */
	@Override
	protected void paintComponent(Graphics g) {

		// Neteja el panell i prepara el dibuix base de la superclasse (JPanel)
		super.paintComponent(g);

		// Casting a Graphics2D per accedir a funcionalitats gràfiques
		Graphics2D g2d = (Graphics2D) g;

		// Mostra del fons mitjançant mètode drawImage, posem el fons i els límits
		g2d.drawImage(getFondoActual(), 0, 0, getWidth(), getHeight(), this);

		/*
		 * Fem servir el mètode setRenderingHint(), per renderitzar el fons i objectes i
		 * que es mostri net 
		 */
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Estructura iterativa que recorre la llista balls
		for (Ball b : balls) {
			// Fem servir el mètode .paint() per dibuixar les boles
			b.paint(g2d);
		}

		// Amb el mètode paint() pintem la raqueta
		racquet.paint(g2d);

		// Estructura iterativa que recorre la llista obstacles
		for (Obstacle o : obstacles) {
			// Amb el mètode paint(), pintem els obstacles
			o.paint(g2d);
		}

		// Posem color blanc amb el mètode setColor, per la lletra
		g2d.setColor(Color.WHITE);

		// Amb el mètode setFont posem una font Retro, la lletra en negreta i la mida
		g2d.setFont(new Font("SansSerif", Font.BOLD, 12));

		/*
		 * Dibuixem amb el mètode drawString perquè es mostri el nivell que s'està
		 * jugant
		 */
		g2d.drawString("Level: " + level, 10, 20);

		/*
		 * Dibuixem amb el mètode drawString perquè es mostri els punts que s'està
		 * jugant
		 */
		g2d.drawString("Score: " + score + " ms", 10, 40);

	}

	
	/**
	 * Mètode que selecciona i retorna la imatge de fons corresponent al nivell
	 * actual del joc. El canvi de fons es produeix de forma escalonada cada 5
	 * nivells.
	 * @return L'objecte Image que s'ha de renderitzar com a fons de pantalla
	 */
	private Image getFondoActual() {

		/*Declaració i incialització de final, aquesta controla cada quants nivells ha
		de canviar el fons*/
		final int CADA_CINC_NIVELL = 5;

		//Declaració i incialització de variable, aquesta calcula l'index de l'Array
		int index = (level - 1) / CADA_CINC_NIVELL;

		//Estructura condicional que controla que si el nivell és superior a la quantitat de fons disponibles.
		if (index >= fondos.length) {
			index = fondos.length - 1;
		}

		// Estructura condicional que assegura que l'índex mai sigui negatiu.
		if (index < 0) {
			index = 0;
		}

		// Estructura condicional que comprova si existeix una imatge carregada en la posició 'index'
		if (fondos[index] != null) {

			//Si la imatge existeix, la funció l'envia i s'acaba aquí
			return fondos[index];

		} else {
			// Si la posició està buida (null), enviem la imatge de reserva
			return fons;
		}

	}

	/**
	 * Mètode que actualitza els obstacles segons el nivell que s'està jugant
	 * @param nivellActual, el nivell que s'està jugant
	 */
	private void actualitzarObstacles(int nivellActual) {

		// Amb el mètode .clear() netejem els obstacles
		obstacles.clear();

		// Estructura condicional avalua si hem arribat al nivell 2
		if (nivellActual < NIVELL_MINIM_OBSTACLES) {
			// No retornem res
			return;

		}

		/*
		 * Declaració i inicialització de variable que calcula el numObstacles, com a
		 * màxim 10. Ho controlem amb el mètode Math.min()
		 */
		int numObstacles = Math.min(nivellActual - 1, Utils.MAX_OBSTACLES);

		// Declaració i incialització de random
		Random rand = new Random();

		/*
		 * Estructura iterativa que afegeix una instància d'objecte obstacle a la llista
		 * obstacles
		 */
		for (int i = 0; i < numObstacles; i++) {

			// Afegim un objecte obstacle a la llista obstacle amb coordenades aleatories
			obstacles.add(new Obstacle(rand.nextInt(250) + 10, rand.nextInt(150) + 50, this, nivellActual));

		}

	}

	/**
	 * Mètode que gestiona el final de la partida.
	 * Es crida quan la bola toca el fons de la pantalla.
	 */
	public void gameOver() {
	    

		// 1. Bloqueig de seguretat: evitem que el diàleg surti 2 cops si la bola rebota al fons
	    if (gameEnded) {
	        return;
	    }
	    gameEnded = true;
	    
	    stopTimer();

	    // 2. Aturem sons i posem el de derrota
	    sonido.stopFondo();
	    sonido.playGameOver();
	    
	    // 3. Inicialitzem el sistema de traducció
	    ControlLanguage ctrl = new ControlLanguage();
	    
	    // Fem servir la variable 'language' que hem rebut al constructor de Game.java
	    // Si per algun motiu fos null, posem l'anglès ("EN") per defecte.
	    String langPartida = (this.language != null) ? this.language : "EN";
	    ctrl.setIdiomaActual(langPartida);

	    //Si el mode de joc es asincron
	        if (modoJuego == 1) { 

	            if (jugadorActual == 1) {
	                puntuacionJugador1 = (int) score; // Guarda la puntuacio
	                // Mostrem que el torn del jugador 1 ha acabat, i que toca al jugador 2
	                JOptionPane.showMessageDialog(this,
	                        "Turno de " + jugador1 + " terminado.\nPuntuación: " + puntuacionJugador1 +
	                        "\nAhora juega: " + jugador2
	                    );
	                
	                jugadorActual = 2;

	                // Reinicia la partida per al jugador 2
	                paused = false;
	                
	                resetGame();
	                gameEnded = false;
	                
	                
	                startTimer();
	                
	                return;
	                

	            } else {
	                puntuacionJugador2 = (int) score;
	                // Sumem les puntuacions
	                int total = puntuacionJugador1 + puntuacionJugador2;
	                //Mmostrem puntuacions per separat i la suma
	                JOptionPane.showMessageDialog(this,
	                        "Equipo: " + nickname +
	                        "\n" + jugador1 + ": " + puntuacionJugador1 +
	                        "\n" + jugador2 + ": " + puntuacionJugador2 +
	                        "\nTOTAL: " + total
	                    );

	            }

	        } else {
	            // Mode normal
	        	//quan termina la partida mostrem game over
	            JOptionPane.showMessageDialog(this, "Game Over");
	            System.exit(0);
	        }


	    // Guardem la puntuació i obtenim el text del Ranking
	    String rankingFinal = "";
	    try {
	        Connexio c = new Connexio();
	        // Li passem el nom, la puntuació (score) i l'idioma per a la DB
	        rankingFinal = c.guardarPartida(playerName, (int) score, langPartida);
	    } catch (Exception e) {
	        rankingFinal = "Error loading ranking...";
	    }

	    // 5. Muntem el missatge final combinant les dades de la partida amb les traduccions
	    String missatge = "--- " + ctrl.get("game_over_titol") + " ---\n\n" +
	                      ctrl.get("jugador") + ": " + playerName + "\n" +
	                      ctrl.get("puntuacio") + ": " + (int)score + " ms\n\n" +
	                      ctrl.get("ranking_titol") + "\n" +
	                      rankingFinal + "\n\n" +
	                      ctrl.get("tornar_jugar");

	    // 6. Mostrem el quadre de text (JOptionPane)
	    // Utilitzem 'this' per centrar-lo sobre el joc i el títol traduït
	    int resposta = JOptionPane.showConfirmDialog(
	        this, 
	        missatge, 
	        ctrl.get("game_over_titol"), 
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.INFORMATION_MESSAGE
	    );

	   
	    
	    // 7. Lògica de decisió de l'usuari
	    if (resposta == JOptionPane.YES_OPTION) {
	        // L'usuari vol jugar: executem el reinici net
	        reiniciarJoc();
	    } else {
	        // L'usuari vol sortir: tanquem el programa
	        System.exit(0);
	    }
	}
	
	private void resetGame() {

		resetGameState();
	    resetEntities();
	    resetScoreState();
	}

	/**
	 * Mètode que tanca la finestra actual i reinicia tot el flux des del principi.
	 * Això garanteix que el joc torni a ser jugable i no quedin restes de la partida anterior.
	 */
	private void reiniciarJoc() {
	    // A) Detenim el Timer del joc si existeix para evitar que segueixi executant-se
	    if (gameTimer != null && gameTimer.isRunning()) {
	        gameTimer.stop();
	    }
	    
	    // B) Obtenim el JFrame (la finestra) que conté aquest JPanel (el joc)
	    javax.swing.JFrame topFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
	    
	    if (topFrame != null) {
	        // C) Tanquem i destruïm la finestra actual per alliberar memòria i fils d'execució
	        topFrame.dispose(); 
	    }

	    // D) Tornem a llançar el menú inicial
	    // Fem servir 'invokeLater' per assegurar-nos que la nova finestra s'obre 
	    // quan la vella ja s'hagi tancat completament (evita conflictes de focus)
	    javax.swing.SwingUtilities.invokeLater(() -> {
	        InitialWindow menu = new InitialWindow();
	        // Això obrirà la finestra de selecció d'idioma/nom com al principi
	        menu.mostrarMenu(); 
	    });
	}

	/**
	 * Setter per assignar el Timer del joc.
	 * Això permet que el Timer sigui detingut quan el joc acaba.
	 * @param timer el Timer del joc
	 */
	public void setGameTimer(Timer timer) {
	    this.gameTimer = timer;
	}

	/**
	 * Mètode getter de getRacquet que accedeix a racquet
	 * @return el valor de racquet
	 */
	public Racquet getRacquet() {

		return racquet;

	}

	/**
	 * Mètode getter de getSonido que accedeix a sonido
	 * @return sonido
	 */
	public Sound getSonido() {

		return sonido;

	}
	
	/**
	 * Metode que guarda el estat de les variables en el moment en el que es crida
	 */
	public void guardarPartida() {
	    try {
	        GameData data = new GameData();

	        //Nivell i puntuacio
	        data.level = level;
	        data.score = score;
	        // Jugadors i nickname
	        data.jugador1 = jugador1;
	        data.jugador2 = jugador2;
	        data.nickname = nickname;
	        // Mode de joc i jugador actual (en cas de mode asincron)
	        data.modoJuego = modoJuego;
	        data.jugadorActual = jugadorActual;
	        // Puntuacions dels jugadors
	        data.puntuacionJugador1 = puntuacionJugador1;
	        data.puntuacionJugador2 = puntuacionJugador2;
	        // Guarda posicio de la raqueta
	        data.racquetX = racquet.getX();

	        // Guarda la bola
	        data.balls = new ArrayList<>();
	        
	        for (Ball b : balls) {
	            BallState bs = new BallState();
	            // Guarda posicio de la bola
	            bs.x = b.getX();
	            bs.y = b.getY();
	            // Guarda velocitat de la bola
	            bs.speed = b.getSpeed();

	            // Guarda la direccio de la bola
	            bs.directionX = b.getDx();
	            bs.directionY = b.getDy();

	            data.balls.add(bs);
	        }
	        
	        // Guarda la posicio dels obstacles
	        data.obstacleX = new ArrayList<>();
	        data.obstacleY = new ArrayList<>();
	        
	        for (Obstacle o : obstacles) {
	            data.obstacleX.add(o.getX());
	            data.obstacleY.add(o.getY());
	        }
	        // Guarda tot aixo a un arxiu anomenat partida.dat
	        ObjectOutputStream oos = new ObjectOutputStream(
	                new FileOutputStream("partida.dat")
	        );

	        oos.writeObject(data);
	        oos.close();
	        // Mostrem per pantalla que s'ha guardat la partida
	        JOptionPane.showMessageDialog(this, "Partida guardada");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * metode que carrega la partida guardada com partida.dat
	 * @return data en cas de no trobar la partida null
	 */
	public static GameData cargarPartida() {
	    try {
	        ObjectInputStream ois = new ObjectInputStream(
	                new FileInputStream("partida.dat")
	        );

	        GameData data = (GameData) ois.readObject();
	        ois.close();

	        return data;

	    } catch (Exception e) {
	        return null;
	    }
	}
	
	/**
	 * Metode que una vegada cargada la partida asignem tots els valors guardats
	 * de la partida a les variables del joc
	 * @param data
	 */
	private void cargarEstado(GameData data) {

		//Nivell i puntuacio
	    level = data.level;
	    score = data.score;

	    //Noms de jugadors
	    jugador1 = data.jugador1;
	    jugador2 = data.jugador2;
	    nickname = data.nickname;

	    //mode de joc i el jugador que estaba jugan en aquell moment
	    modoJuego = data.modoJuego;
	    jugadorActual = data.jugadorActual;

	    //Les puntuacions finals de cada jugador
	    puntuacionJugador1 = data.puntuacionJugador1;
	    puntuacionJugador2 = data.puntuacionJugador2;

	    // Raqueta
	    racquet = new Racquet(this);
	    racquet.setX(data.racquetX);

	    // Bola
	    balls.clear();

	    for (BallState bs : data.balls) {
	        Ball b = new Ball(this);

	        b.setX(bs.x);
	        b.setY(bs.y);

	        b.setSpeed(bs.speed);

	        b.setDx(bs.directionX);
	        b.setDy(bs.directionY);

	        balls.add(b);
	    }

	    // Obstacle
	    obstacles.clear();

	    for (int i = 0; i < data.obstacleX.size(); i++) {
	        obstacles.add(new Obstacle(
	                data.obstacleX.get(i),
	                data.obstacleY.get(i),
	                this,
	                level
	        ));
	    }
	}
	
	private void togglePause() {

	    if (!paused) {
	        pauseGame();
	    } else {
	        resumeGame();
	    }
	}
	/**
	 * Metode que pausa la partida amb la tecla P
	 */
	private void pauseGame() {

		//Pausem el joc
	    paused = true;

	    // Pausem el timer
	    stopTimer();

	    // Pausem el so
	    sonido.stopFondo();

	    /*
	     * Mostrem un missatge que diu que el joc esta en pausa y si es vol guardar
	     * la partida o tornar al joc
	     */
	    int opcion = JOptionPane.showOptionDialog(
	            this,
	            "joc en pausa ¿Vols guardar la partida?",
	            "PAUSA",
	            JOptionPane.YES_NO_CANCEL_OPTION,
	            JOptionPane.INFORMATION_MESSAGE,
	            null,
	            new String[]{"Guardar y sortir", "Seguir jugant"},
	            "Seguir jugant"
	    );

	    // Si escull guardar partida
	    if (opcion == 0) {

	    	// Cridem al metode que guarda la partida
	        guardarPartida();

	        //Tancem finestra del joc
	        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
	        frame.dispose();

	        return;
	    }

	    resumeGame();
	}
	
	/**
	 * Metode que reanuda el joc
	 */
	private void resumeGame() {

		// Quitem el pause, activem timer i musica de fons
	    paused = false;
	    startTimer();
	    sonido.playFondo();
	}
	
	/**
	 * Metode que carrega els fons del joc com la del menu o la de dins del joc
	 */
	private void cargarFondos() {

	    try {

	    	fons = new ImageIcon(getClass().getResource(IMG_FONDO_BASE)).getImage();

	    	fondos[0] = new ImageIcon(getClass().getResource(IMG_FONDO_BASE)).getImage();
	    	fondos[1] = new ImageIcon(getClass().getResource(IMG_FONDO_2)).getImage();
	    	fondos[2] = new ImageIcon(getClass().getResource(IMG_FONDO_3)).getImage();
	    	fondos[3] = new ImageIcon(getClass().getResource(IMG_FONDO_4)).getImage();
	    	fondos[4] = new ImageIcon(getClass().getResource(IMG_FONDO_5)).getImage();
	    	fondos[5] = new ImageIcon(getClass().getResource(IMG_FONDO_6)).getImage();

	    } catch (Exception e) {
	    	// Si no es pot carregar el fons imprimim
	        System.out.println("No se pudo cargar la imagen de fondo.");

	    }
	}
	
	/**
	 * Metode que conte els controls del joc, raqueta, focus, tecla pause, mouse
	 */
	
	private void initControls() {

	    // Teclat (raqueta)
	    addKeyListener(new KeyAdapter() {

	        @Override
	        public void keyPressed(KeyEvent e) {
	            racquet.keyPressed(e);
	        }

	        @Override
	        public void keyReleased(KeyEvent e) {
	            racquet.keyReleased(e);
	        }
	    });

	    // Focus
	    setFocusable(true);
	    setRequestFocusEnabled(true);

	    // Tecla de pausa
	    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
	            KeyStroke.getKeyStroke(teclaPausa),
	            accioPausa
	    );

	    getActionMap().put(accioPausa, new javax.swing.AbstractAction() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	            togglePause();
	        }
	    });

	    // Click ratoli
	    addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mousePressed(java.awt.event.MouseEvent e) {
	            requestFocusInWindow();
	        }
	    });

	    // Moviment ratoli
	    addMouseMotionListener(new MouseMotionAdapter() {
	        @Override
	        public void mouseMoved(MouseEvent e) {
	            racquet.setMouse(e.getX());
	        }
	    });
	}
	
	/**
	 * Metode que inicialitza les variables del joc com els jugadors llenguatge etc.
	 * @param jugador1
	 * @param jugador2
	 * @param nickname
	 * @param language
	 * @param modoJuego
	 * @param selectedLevel
	 */
	
	private void initGameBasics(String jugador1, String jugador2, String nickname, String language, int modoJuego, int selectedLevel) {
	    
		this.jugador1 = jugador1;
	    this.jugador2 = jugador2;
	    this.nickname = nickname;

	    this.language = language;
	    this.modoJuego = modoJuego;

	    this.selectedLevel = selectedLevel;
	    Game.level = selectedLevel;

	    this.playerName = nickname;
	}
	
	/**
	 * Metode per pausar timer
	 */
	private void stopTimer() {
	    if (gameTimer != null) {
	        gameTimer.stop();
	    }
	}

	/**
	 * Metode per començar timer
	 */
	private void startTimer() {
	    if (gameTimer != null) {
	        gameTimer.start();
	    }
	}
	
	/**
	 * Metode que fa reset al estat del joc
	 */
	private void resetGameState() {
	    gameEnded = false;
	    paused = false;
	}
	/**
	 * Metode que fa reset a la puntuacio
	 */
	private void resetScoreState() {
	    score = 0;
	    startTime = System.currentTimeMillis();
	    lastPointUpdate = System.currentTimeMillis();
	}
	/**
	 * Metode que fa reset de les entitats (bola, raqueta i obstacles)S
	 */
	private void resetEntities() {
	    balls.clear();
	    obstacles.clear();
	    racquet = new Racquet(this);

	    level = selectedLevel;

	    Ball b = new Ball(this);
	    b.setSpeed(Utils.VELOCIDAD_BASE);
	    balls.add(b);
	}
}