package minitennis.main;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import minitennis.language.ControlLanguage;
import minitennis.language.LanguageSelectionMenu;
import minitennis.utils.Utils;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


/**
 * Classe InitialWindow. Aquesta classe gestiona la configuració prèvia a
 * l'execució del joc (Input de l'usuari). Actua com a pont entre la lògica de
 * configuració de llenguatge i la instanciació del joc.
 * 
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià
 * Chenovart
 * 
 */
public class InitialWindow {
	// Declaració i inicialització d'atribut de la classe controlLang
	private ControlLanguage controlLang;
	
	private boolean running = true;

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
		
		//HE HECHO YO
		SwingUtilities.invokeLater(game::requestFocusInWindow);

		// GAME LOOP
		Timer timer = new Timer(10, e -> {
		    game.move();
		    game.repaint();
		});
		timer.start();
		game.setGameTimer(timer);
		
		

		
		new Thread(() -> {
			while (running) {
				// Actualització de la lògica de posicions
				game.move();

				//Invocació del renderitzat gràfic
				game.repaint();

				// Control del temps (Thread.sleep)
				try {
					// Pausa de 10 mil·lisegons
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					// Si el fil s'interromp, sortim del bucle
					e.printStackTrace();
					break;
				}
			}
		}).start(); // Arrencada del fil
	}
	
	public void stopGameLoop() {
	    running = false;
	}

	public void startGameLoop() {
	    running = true;
	}
}