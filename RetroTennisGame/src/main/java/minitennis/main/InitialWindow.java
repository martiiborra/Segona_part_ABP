package minitennis.main;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import minitennis.language.ControlLanguage;
import minitennis.language.LanguageSelectionMenu;
import minitennis.utils.Utils;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import Data.GameData;


/**
 * Classe InitialWindow. Aquesta classe gestiona la configuració prèvia a
 * l'execució del joc (Input de l'usuari). Actua com a pont entre la lògica de
 * configuració de llenguatge i la instanciació del joc.
 * 
 * @author Primera part Grup 4
 * @author Segona part Grup 1
 */
public class InitialWindow {
	// Declaració i inicialització d'atribut de la classe controlLang
	private ControlLanguage controlLang;

	/**
	 * Constructor de la classe. Instancia l'objecte ControlLanguage per gestionar
	 * els recursos de text.
	 */
	public InitialWindow() {
		this.controlLang = new ControlLanguage();
	}

	/**
	 * Mètode per mostrar la interfície de configuració. Gestiona el flux de
	 * selecció d'idioma, dades de l'usuari i nivell inicial.
	 */
	public void mostrarMenu() {
		// Creem el frame principal del joc
		JFrame frame = new JFrame("Retro Tennis");

		// Instanciem el nostre nou panell de selecció d'idioma retro
		LanguageSelectionMenu selectionMenu = new LanguageSelectionMenu();

		// Configurem el frame
		frame.add(selectionMenu);
		frame.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // Centrat a la pantalla
		frame.setVisible(true);

		//Demanem el focus perquè el teclat funcioni al moment
		selectionMenu.requestFocusInWindow();
		// Al iniciar el programa pregntem a l'usuari que vol fer
		int opcion = JOptionPane.showOptionDialog(
		        frame,
		        controlLang.get("pregunta_partida"),
		        "Retro Tennis",
		        JOptionPane.DEFAULT_OPTION,
		        JOptionPane.INFORMATION_MESSAGE,
		        null,
		        new String[]{
		            controlLang.get("nueva_partida"),
		            controlLang.get("cargar_partida")
		        },
		        controlLang.get("nueva_partida")
		);
		// Si l'usuari vol carregar una partida
	    if (opcion == 1) {
	    	//Intenta carregar les dades de una partida guardada
	        GameData data = Game.cargarPartida();
	        //Si la partida s'ha guardat correctament
	        if (data != null) {
	        	//Creem una nou game utilitzant les dades guardades
	        	Game game = new Game(data, "ES");
	        	// Crea una nova finestra per mostrar el joc carregat
	        	JFrame gameFrame = new JFrame("Retro Tennis - LOAD");
	        	// Inicialitzem el joc
	        	iniciarJuego(game, gameFrame);
	        	// tanquem la finestra del menu principal
	            frame.dispose();
	        }
	    }
	}
	

	/**
	 * Mètode privat per a la inicialització del contenidor principal i el bucle del
	 * joc.
	 * 
	 * @param nom, Identificador de l'usuari.
	 * @param nivell, Valor sencer que determina la dificultat inicial.
	 */
	private void llançarJoc(String nom, int nivell, String language) {
		// Instancia de la classe principal del motor del joc
		Game game = new Game(nom, "", nom, nivell, language, 0);
		// Instancia del contenidor de finestra
		JFrame frame = new JFrame("Retro Tenis - " + nom);
		// Addició de l'objecte 'game' (panell del joc)
		frame.add(game);
		// Definició de les dimensions del frame
		frame.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
		// Activació de la visibilitat de la finestra
		frame.setVisible(true);
		// Centrat de la finestra a la pantalla
		frame.setLocationRelativeTo(null);
		// Tancar l'aplicació en tancar la finestra
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// El component del joc rep el focus per teclat
		SwingUtilities.invokeLater(game::requestFocusInWindow);

		// Bucle principal del joc
		Timer timer = new Timer(10, e -> {
		    game.move(); // Actualitza la logica del joc
		    game.repaint(); // Redibuixa la estada actual del joc en pantalla
		});
		timer.start(); // Inicia el bucle del joc
		// Desa la referencia del timer dins del joc
		game.setGameTimer(timer);
		
	}	
	/**
	 * Metode que inicialitza i arrenca la finestra del joc juntament amb el seu bucle principal
	 * d'encarrega de configurar Jframe, afegir el panel del joc i iniciar el timer
	 * @param game
	 * @param frame
	 */
	private void iniciarJuego(Game game, JFrame frame) {
		// Afegim panell del joc a la finestra
	    frame.add(game);
	    // Definim la mida de la finestra utilitzant constants del joc
	    frame.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
	    // Fem visible la finestra
	    frame.setVisible(true);
	    // Centrem la finestra a la pantalla
	    frame.setLocationRelativeTo(null);
	    // Definim que en tencar la finestra s'aturi el joc
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Creem el timer del joc
	    Timer timer = new Timer(10, e -> {
	        game.move();
	        game.repaint();
	    });
	    // Iniciem bucle del joc
	    timer.start();
	    // Guarden la referencia del timer dins del joc
	    game.setGameTimer(timer);
	}
}