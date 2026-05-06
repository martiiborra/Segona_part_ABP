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

import Data.GameData;

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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


/**
 * * Classe Game que hereta de JPanel, funciona com a motor principal del joc.
 * @author Candela Cabello, André Medinas, Izan Perez, Daner Coria i Adrià Chenovart
 * */

public class Game extends JPanel {

	//Declaració i inicialització de final per el control de nivells on ha de sortir obstacles
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
	
	//VARIABLE DE MODE DE JOC
	private int modoJuego;
	
	//VARIABLE DE JUGADORACTUAL
	private int jugadorActual = 1;

	//VARIABLES DE PUNTUACIONS
	private int puntuacionJugador1 = 0;
	private int puntuacionJugador2 = 0;
	
	//VARIABLE PER REINICIAR DESDE EL NIVEL SELECCIONAT
	private int selectedLevel;
	
	
	private String jugador1;
	private String jugador2;
	private String nickname;
	
	//VARIABLE PER PAUSAR PARTIDA
	private boolean paused = false;

	/**
	 * Constructor del joc. Inicialitza components i escoltadors d'entrada.
	 * @param playerName,    nom del jugador
	 * @param selectedLevel, nivell inicial seleccionat
	 * */
	
	//AFEGIM MODE JOC I AFEGIM ELS 2 JUGADORS I NICKNAME
	public Game(String jugador1, String jugador2, String nickname, int selectedLevel, String language, int modoJuego) {

		// IMPORTANT: Reiniciem l'estat de control per si venim d'una partida anterior
		this.gameEnded = false; 
		
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
		this.nickname = nickname;
		
		 this.playerName = nickname;
		 this.language = language;
		 this.modoJuego = modoJuego;
		
		 this.selectedLevel = selectedLevel;
		 Game.level = selectedLevel;
		
		Ball primeraBola = new Ball(this);

		//Declaració i incialització de variable que calcula la velocitat dinàmica de la pilota
		double velocidadCalculada = Utils.VELOCIDAD_BASE * Math.pow(Utils.INCREMENTO_POR_NIVEL, selectedLevel - 1);

		// Declaració i inicialització de variable que serveix com a limitador), mai superará el MAX_BALL_SPEED
		double velocidadFinal = Math.min(velocidadCalculada, Utils.MAX_BALL_SPEED);

		//Modifiquem la velocitat de primeraBola
		primeraBola.setSpeed(velocidadFinal);

		//Afegim a la llista la bola
		balls.add(primeraBola);

		// Estructura de control d'errors TRY-CATCH, gestiona el fons del videojoc
		try {

			/*
			 * Localitza, carrega i extreu la imatge del fitxer de fons per a la seva
			 * posterior renderització en el panell del joc.
			 */
			fons = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego.jpg")).getImage();

			// Càrrega de l'array de fons
			fondos[0] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego.jpg")).getImage();
			fondos[1] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego2.jpg")).getImage();
			fondos[2] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego3.jpg")).getImage();
			fondos[3] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego4.jpg")).getImage();
			fondos[4] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego5.jpg")).getImage();
			fondos[5] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego6.jpg")).getImage();

		} catch (Exception e) {

			// Si es produiex alguna mena d'excepció la capturem i mostrem el missatge
			System.out.println("No se pudo cargar la imagen de fondo.");

		}

		// Actualitzem els obstacles segons el nivell
		actualitzarObstacles(Game.level);

		/*
		 * Mètode del JPanel que registra un "escoltador" per detectar quan l'usuari
		 * prem tecles. 
		 */
		addKeyListener(new KeyAdapter() {

			/**
			 * Mètode que s'executa automàticament en el moment que es prem una tecla.
			 * Delega l'acció a la raqueta (racquet.keyPressed(e)) per iniciar el moviment.
			 */
			public void keyPressed(KeyEvent e) {

				
				
				racquet.keyPressed(e);

			}

			/** * Mètode que s'executa quan l'usuari deixa anar la tecla. Indica a la raqueta
			 * (racquet.keyReleased(e)) que s'ha d'aturar.
			 */
			public void keyReleased(KeyEvent e) {

				racquet.keyReleased(e);

			}

		});

		
		// Permet rebre focus per al teclat
		setFocusable(true);
		

		public void addNotify() {
		    super.addNotify();
		    
		    
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
		        javax.swing.KeyStroke.getKeyStroke("P"),
		        "pauseGame"
		);

		getActionMap().put("pauseGame", new javax.swing.AbstractAction() {
		    @Override
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	System.out.println("P FUNCIONA");
		        togglePause();
		    }
		});
		
		}
		
		setRequestFocusEnabled(true);
		/*
		 * Registra un escoltador per detectar el moviment del cursor del ratolí dins de
		 * la finestra del joc.
		 */
		
		addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mousePressed(java.awt.event.MouseEvent e) {
		        requestFocusInWindow();
		    }
		});
		
		
		addMouseMotionListener(new MouseMotionAdapter() {
			
			// Mètode que s'activa cada vegada que el ratolí es desplaça.
			public void mouseMoved(MouseEvent e) {

				/*
				 * Obté la coordenada horitzontal (X) del cursor i l'envia a la raqueta per
				 * posicionar-la exactament en aquest punt.
				 */
				racquet.setMouse(e.getX());

			}

		});

		

		// Reproducció del so de fons
		sonido.playFondo();
		
		requestFocusInWindow();
		
		SwingUtilities.invokeLater(() -> requestFocusInWindow());

	}
	
	public Game(GameData data, String language) {

	    this.jugador1 = data.jugador1;
	    this.jugador2 = data.jugador2;
	    this.nickname = data.nickname;

	    this.modoJuego = data.modoJuego;
	    this.jugadorActual = data.jugadorActual;

	    this.puntuacionJugador1 = data.puntuacionJugador1;
	    this.puntuacionJugador2 = data.puntuacionJugador2;

	    this.score = data.score;
	    Game.level = data.level;

	    this.language = language;
	    
	    this.selectedLevel = data.level;
	    Game.level = data.level;

	    this.modoJuego = data.modoJuego;
	    this.jugadorActual = data.jugadorActual;

	    this.puntuacionJugador1 = data.puntuacionJugador1;
	    this.puntuacionJugador2 = data.puntuacionJugador2;

	    this.jugador1 = data.jugador1;
	    this.jugador2 = data.jugador2;
	    this.nickname = data.nickname;
	    this.playerName = data.nickname;

	    // INITCIALITZEM TOT
	    Ball b = new Ball(this);
	    balls.add(b);

	    actualitzarObstacles(level);
	    setFocusable(true);
	    sonido.playFondo();
	    
	    racquet = new Racquet(this);
	    sonido.playFondo();
	    
	    if (data != null) {
	        cargarEstado(data);
	    }
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
	    
		if (gameTimer != null) {
		    gameTimer.stop();
		}

	    // 2. Aturem sons i posem el de derrota
	    sonido.stopFondo();
	    sonido.playGameOver();
	    
	    // 3. Inicialitzem el sistema de traducció
	    ControlLanguage ctrl = new ControlLanguage();
	    
	    // Fem servir la variable 'language' que hem rebut al constructor de Game.java
	    // Si per algun motiu fos null, posem l'anglès ("EN") per defecte.
	    String langPartida = (this.language != null) ? this.language : "EN";
	    ctrl.setIdiomaActual(langPartida);

	    //SI EL MODE DE JOC ES ASINCRON
	        if (modoJuego == 1) { // ASINCRONO

	            if (jugadorActual == 1) {
	                puntuacionJugador1 = (int) score; // GUARDA EL SCORE
	                
	                JOptionPane.showMessageDialog(this,
	                        "Turno de " + jugador1 + " terminado.\nPuntuación: " + puntuacionJugador1 +
	                        "\nAhora juega: " + jugador2
	                    );
	                
	                jugadorActual = 2;

	                // REINICIA PARTIDA PER JUGADOR 2
	                resetGame();
	                gameEnded = false;
	                return;

	            } else {
	                puntuacionJugador2 = (int) score;

	                int total = puntuacionJugador1 + puntuacionJugador2;

	                JOptionPane.showMessageDialog(this,
	                        "Equipo: " + nickname +
	                        "\n" + jugador1 + ": " + puntuacionJugador1 +
	                        "\n" + jugador2 + ": " + puntuacionJugador2 +
	                        "\nTOTAL: " + total
	                    );

	            }

	        } else {
	            // MODO NORMAL
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

		gameEnded = false;  //POSEM GAME ENDED FALSE PER REINICIAR LA PARTIDA EN EL MODE ASINCRON
		 
	    balls.clear();
	    obstacles.clear();

	    racquet = new Racquet(this);

	    level = selectedLevel;
	    Ball b = new Ball(this);
	    b.setSpeed(Utils.VELOCIDAD_BASE);
	    balls.add(b);

	    score = 0;
	    startTime = System.currentTimeMillis();
	    lastPointUpdate = System.currentTimeMillis();
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
	
	//METODO PARA GUARDAR LA PARTIDA
	public void guardarPartida() {
	    try {
	        GameData data = new GameData();

	        data.level = level;
	        data.score = score;

	        data.jugador1 = jugador1;
	        data.jugador2 = jugador2;
	        data.nickname = nickname;

	        data.modoJuego = modoJuego;
	        data.jugadorActual = jugadorActual;

	        data.puntuacionJugador1 = puntuacionJugador1;
	        data.puntuacionJugador2 = puntuacionJugador2;

	        
	        // GUARDAR RAQUETA
	        data.racquetX = racquet.getX();

	        // GUARDAR BOLAS
	        data.balls = new ArrayList<>();
	        
	        for (Ball b : balls) {
	            BallState bs = new BallState();

	            bs.x = b.getX();
	            bs.y = b.getY();

	            bs.speed = b.getSpeed();

	            // si tienes dirección:
	            bs.directionX = b.getDx();
	            bs.directionY = b.getDy();

	            data.balls.add(bs);
	        }
	        
	        // GUARDAR OBSTÁCULOS
	        data.obstacleX = new ArrayList<>();
	        data.obstacleY = new ArrayList<>();

	        for (Obstacle o : obstacles) {
	            data.obstacleX.add(o.getX());
	            data.obstacleY.add(o.getY());
	        }
	        
	        ObjectOutputStream oos = new ObjectOutputStream(
	                new FileOutputStream("partida.dat")
	        );

	        oos.writeObject(data);
	        oos.close();

	        JOptionPane.showMessageDialog(this, "Partida guardada");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	// METODO PARA CARGAR LA PARTIDA
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
	
	private void cargarEstado(GameData data) {

	    level = data.level;
	    score = data.score;

	    jugador1 = data.jugador1;
	    jugador2 = data.jugador2;
	    nickname = data.nickname;

	    modoJuego = data.modoJuego;
	    jugadorActual = data.jugadorActual;

	    puntuacionJugador1 = data.puntuacionJugador1;
	    puntuacionJugador2 = data.puntuacionJugador2;

	    // RAQUETA
	    racquet = new Racquet(this);
	    racquet.setX(data.racquetX);

	    // BOLAS
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

	    // OBSTÁCULOS
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
	
	private void pauseGame() {

	    paused = true;

	    if (gameTimer != null) {
	        gameTimer.stop();
	    }

	    sonido.stopFondo();

	    int opcion = JOptionPane.showConfirmDialog(
	            this,
	            "joc en pausa ¿Vols guardar la partida?",
	            "PAUSA",
	            JOptionPane.YES_NO_CANCEL_OPTION,
	            JOptionPane.INFORMATION_MESSAGE,
	            null,
	            new String[]{"Guardar y sortir", "Seguir jugant"},
	            "Seguir jugant"
	    );

	    if (opcion == 0) {

	        guardarPartida();

	        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
	        frame.dispose();

	        return;
	    }

	    resumeGame();
	}
	
	private void resumeGame() {

	    paused = false;

	    if (gameTimer != null) {
	        gameTimer.start();
	    }

	    sonido.playFondo();
	}
	

}