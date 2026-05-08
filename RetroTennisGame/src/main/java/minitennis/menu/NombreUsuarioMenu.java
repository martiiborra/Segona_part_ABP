package minitennis.menu;

import javax.swing.*;
import javax.swing.Timer;

import minitennis.language.ControlLanguage;
import minitennis.main.Game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * Definició de la classe NombreUsuarioMenu.
 * Aquesta classe actua com finestra al moment d'afegir un nom d'usuari.
 * La seva funció es rebre el nombre afegit amb opció de continuar per començar a jugar
 * i l'altre es per tornar al menu principal
 * 
 * @autor André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class NombreUsuarioMenu extends JPanel {

	// Declarem i inicialitzem els parametres
	private ControlLanguage controlLang;
    private int nivel;
    private StringBuilder nombre = new StringBuilder();
    private Image fondo;
    
    //VARIABLE PER EL MODE DE JOC
    private int modoJuego;
    
    //VARIABLES DE JUGADORS I NICKNAME
    private String jugador1;
    private String jugador2;
    private String nickname;
    
    // FASES PER DEMANAR NOMS I NICKNAME
    private int fase = 1; // 1 = jugador1, 2 = jugador2, 3 = nickname
    
    /**
     * Constructor que rep l'idioma i el nivell seleccionat
     * 
     * @param controlLang el idioma seleccionat
     * @param nivel el nivel seleccionat
     */
    
    //AFEGIM MODO JUEGO
    public NombreUsuarioMenu(ControlLanguage controlLang, int nivel, int modoJuego) {
    	// Els parametres seleccionats canvien els valors
    	 this.controlLang = controlLang;
    	 this.nivel = nivel;
    	 this.modoJuego = modoJuego;
        // Carreguem l'imatge gif 
        try {
            fondo = new ImageIcon(getClass().getResource("/Imatge/nombreUsuario.gif")).getImage();
        } catch (Exception e) {
        	// No retornem res, ja que la ruta es correcta
        }

        // En cas de que la imatge no sigui detectada, es posa l'imatge en negre
        setBackground(Color.BLACK);
        // Permet que el panell detecti el teclat
        setFocusable(true);
        // Afegim un escoltador de teclat
        addKeyListener(new KeyAdapter() {
        	
        	/**
        	 * Afegim un metode per cada vegada que polsem un teclat i es sobreescriu
        	 */
            @Override
            public void keyPressed(KeyEvent e) {
            	// Comprova si la tecla polsada es Enter
            	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            		//HE HECHO CAMBIOS
            	    if (nombre.length() > 0) {

            	    	
            	    	if (modoJuego == 0) {
            	    		jugador1 = nombre.toString();
            	    		jugador2 = "";
            	    		nickname = jugador1;
            	    		confirmarYEmpezar();
            	    	}
            	    	else if (fase == 1) {
            	            jugador1 = nombre.toString();
            	            nombre.setLength(0); // limpiar
            	            fase = 2;

            	        } else if (fase == 2) {
            	            jugador2 = nombre.toString();
            	            nombre.setLength(0);
            	            fase = 3;

            	        } else if (fase == 3) {
            	            nickname = nombre.toString();
            	            confirmarYEmpezar();
            	        }
            	    }
            	} 
                // Si polsem la tecla "Esc" crida al metode per tornar al menu
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    volverAlMenuPrincipal();
                }
                // Si polsem la tecla de esborrar
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                	// Esborrem la ultima lletra afegida
                    if (nombre.length() > 0) {
                        nombre.deleteCharAt(nombre.length() - 1);
                    }
                } 
                // Si el nom no arriba al limit de 12 caracter
                else if (nombre.length() < 12) {//DECLARAR FINAL
                	// Obtenim el caracter fisic de la tecla
                    char c = e.getKeyChar();
                    // Comprova si es una lletra, nombre o espai
                    if (Character.isLetterOrDigit(c) || c == ' ') { 
                        nombre.append(c);
                    }
                }
                // Actualitza la pantalla per mostrar el nom mentre s'escriu
                repaint(); 
            }
        });
    }
    
    /**
     * Mètode per tornar al menú principal
     */
    private void volverAlMenuPrincipal() {
    	// Obtenim la finestra principal
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Si la finestra es diferent a null
        if (frame != null) {
        	// Creem una nova instancia
            MenuRetro menuRetro = new MenuRetro(controlLang);
            // Netejem el contingut de la finestra
            frame.getContentPane().removeAll();
            // Afegim el menu principal
            frame.add(menuRetro);
            // Refresca la seva jerarquia
            frame.revalidate();
            // I dibuixem de nou
            frame.repaint();
            // Passa el polsat al teclar al nou menu
            menuRetro.requestFocusInWindow();
        }
    }
    
    /**
     * Mètode per confirmar el nom i llançar la finestra del joc
     */
    private void confirmarYEmpezar() {
    	// Obtenim la finestra principal
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Si la finestra es diferent a null
        if (frame != null) {
        	// Passem el fram, després el nom i el nivell
            lanzarJuego(frame, nombre.toString(), nivel);
        }
    }
    
    /**
     * Mètode per crear la finestra definitiva del joc i inicia el bucle
     * 
     * @param frame es la finestra actual on es troba
     * @param nom es el nom que li anirem a declarar
     * @param niv es el nivell que seleccionem
     */
    private void lanzarJuego(JFrame frame, String nom, int niv) {
    	// Tanquem el menu de selccio
        frame.dispose(); 
        // Obre la finestra de joc
        JFrame gameFrame = new JFrame("Retro Tenis - ");
        // Crea el component del joc amb els parametres - CORREGIT: Pasem el idioma actual
        //AFEGIM MODOJUEGO JUGADOR1, JUGADOR2 I NICKNAME
        Game game = new Game(jugador1, jugador2, nickname, niv, controlLang.getIdiomaActual(), modoJuego);
        // Afegim el joc dins de la finestra
        gameFrame.add(game);
        // Definim la seva mida
        gameFrame.setSize(300, 400);
        // Posicionem la finestra al mig de la pantalla
        gameFrame.setLocationRelativeTo(null);
        // Tanquem tot el procés al moment de soritr
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Fem visible la finestra
        gameFrame.setVisible(true);
        // Creem un temporitzador per executar el bucle de 100 vegades per segon
        // CORREGIT: Guardem referència al Timer per poder-lo detenir més tard
        Timer gameTimer = new Timer(10, e -> {
        	// Actualitza la posició de la pilot i raqueta
            game.move();
            // Dibuixa els nous moviments
            game.repaint();
            // Comença el temporitzador
        });
        // Passem el Timer al joc perquè el pugui detenir quan acabi
        game.setGameTimer(gameTimer);
        gameTimer.start();
        // Activa el teclat per poder jugar
        game.requestFocusInWindow();
    }
    
	 /**
	  * Mètode per dibuixar la interfície gràfica del menu
	  */	
    @Override
    protected void paintComponent(Graphics g) {
    	// Cridem al primer metode per netejar el panell
        super.paintComponent(g);
        // Les convertim a funcions avançades
        Graphics2D g2 = (Graphics2D) g;
        // Millorem per tenir les lletres nitides
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Si el fons es diferent a null
        if (fondo != null) {
        	// Dibuixem el seu fons
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            // Triem el color negre
            g2.setColor(new Color(0, 0, 0, 180));
            // Pintem un rectangle sobre el fons
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Al titol principal te el color groc
        g2.setColor(Color.YELLOW);
        // Afegim la seva font i el tamany
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        // I escrivim el text traduit
        //DEPENEN DE LA FASE MOSTRA UN TEXT O ALTRE
        String texto = "";

        if (modoJuego == 0) {

            texto = "Nombre jugador";

        } else {

            if (fase == 1) {
                texto = "Jugador 1";
            } else if (fase == 2) {
                texto = "Jugador 2";
            } else if (fase == 3) {
                texto = "Nickname";
            }
        }
        

        g2.drawString(texto, 25, 100);

        // Es el color de les opcions que no estan seleccionades
        g2.setColor(Color.WHITE);
        // Dibuixem el seu contorn 
        g2.drawRect(30, 150, 240, 40);
        
        // Modifiquem per dibuixar el nom i el cursor independentment
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        // Un efecte visual per indicar la posició de la lletra que apareix cada mig segon
        String cursor = (System.currentTimeMillis() % 1000 < 500) ? "_" : "";
        // Escriu els caracters afegits més el "_"
        g2.drawString(nombre.toString() + cursor, 45, 178);

        // Configura la font a les instruccions de la part inferior
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        // Li donem el color gris
        g2.setColor(Color.GRAY);
        
        // Dibuixem la introducció de la tecla Enter
        g2.drawString("ENTER: " + controlLang.get("boto_acceptar"), 30, 340);
        
        // I mostrem la tecla "Esc" per tornar al menu
        g2.setColor(new Color(255, 100, 100));
        g2.drawString("ESC: " + controlLang.get("sortir"), 30, 360);
    }
}