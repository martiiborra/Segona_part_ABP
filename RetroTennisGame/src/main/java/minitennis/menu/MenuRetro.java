package minitennis.menu;

import javax.swing.*;

import minitennis.language.ControlLanguage;
import minitennis.language.LanguageSelectionMenu;
import minitennis.main.Game;
import minitennis.utils.Utils;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import Data.GameData;


/**
 * Definició de la classe MenuRetro.
 * Aquesta classe actua com finestra de menu principal.
 * La seva funció es escollir una de les opcions donades com (jugar, escollir nivell,
 * canviar el idioma i sortir del programa)
 * 
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class MenuRetro extends JPanel {

	// Declarem i inicialitzem els atributs
    private ControlLanguage controlLang;
    private String[] opciones;
    private int seleccion = 0;
    private int nivel = 1;
    private String nombre = "";
    private Image fondo;
    
    // VARIABLE PER CAMBIAR MODE DE JOC
    private int modoJuego = 0; 
    // 0 = NORMAL
    // 1 = MULTIJUGADOR ASINCRON
    // 2 = MULTIJUGADOR SINCRONIC
    
    //Declaració i incialització de finals
    private static final int OPCIO_JUGAR = 0; 
    private static final int OPCIO_CONTROLS = 3;
    private static final int OPCIO_CANVIAR_IDIOMA = 4;
    private static final int OPCIO_SORTIR = 5;
    //NOVA OPCIO
    private static final int OPCIO_MODE = 1;

    /**
     * Constructor del menu que configura el fons, textos i controls
     * 
     * @param controlLang Referencia la classe fent el control del llenguatge
     */
    public MenuRetro(ControlLanguage controlLang) {
        this.controlLang = controlLang;
        // Utilitzem un "try" per carregar la matge de fons del menu
        try {
            fondo = new ImageIcon(getClass().getResource("/Imatge/fondoMenu.gif")).getImage();
        } catch (Exception e) { 
        	// En cas que no detecti l'imatge afegida surt l'excepció amb un missatge
        	System.out.println("La imagen afegida no s'ha detectat");
        }

        // Inicialitzem l'array d'opcions amb l'idioma actual
        actualizarTextos();
        // Com la imatge no s'ha pogut detectar, afegeix un fons en negre
        setBackground(Color.BLACK);
        // Detectem que el panell pugui detectar les tecles 
        setFocusable(true);

        // Utilitzem el KeyListenner per detecar el teclat
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                	// Va cap amunt si prem la fletxa cap amunt
                    case KeyEvent.VK_UP:
                        seleccion = (seleccion - 1 + opciones.length) % opciones.length;
                        break;
                    // Va cap avall si prem la fletxa cap avall
                    case KeyEvent.VK_DOWN:
                        seleccion = (seleccion + 1) % opciones.length;
                        break;
                    // Va cap a la esquerra si prem la fletxa cap a la esquerra
                    case KeyEvent.VK_LEFT:
                    	//EL CAMBIEM LA OPCIO DE MODE AMB LA FLETXA ESQUERRA
                        if (seleccion == OPCIO_MODE) {
                            modoJuego = (modoJuego - 1 + 3) % 3;
                        }  else if (seleccion == 2) { // NIVEL
                            nivel = Math.max(Utils.MIN_LEVELS, nivel - 1);
                        }
                        break;
                    // Va cap a la dreta si prem la fletxa cap a la dreta
                    case KeyEvent.VK_RIGHT:
                    	// EL CAMBIEM LA OPCIO DE MODE AMB LA FLETXA ESQUERRA
                        if (seleccion == OPCIO_MODE) {
                            modoJuego = (modoJuego + 1) % 3;
                        } else if (seleccion == 2) { // NIVEL
                            nivel++;
                        }
                        break;
                    // Executa la acció si prem la tecla Enter
                    case KeyEvent.VK_ENTER:
                        ejecutarOpcion();
                        break;
                }
                // Oblguem al programa a tornar a dibuixar el panell amb els canvis fets anteriorment
                repaint();
            }
        });
    }

    /**
     * Mètode que omple el array de Strings amb els textos traduïts, també 
     * cridem el metode paintComponent per si canviem l'idioma en temps real
     */
    private void actualizarTextos() {
    	// Totes les opcions oferides al array
    	opciones = new String[]{
    			// opcio jugar
    		    controlLang.get("boto_acceptar"),
    		    // OPCIO DE MODE DE JOC
    		    "MODE: " + getModeText(),
    		    // opcio per cambiar nivell
    		    controlLang.get("nivell") + nivel,
    		    //opcio de controls
    		    controlLang.get("controles"),
    		    // opcio per cambiar idioma
    		    controlLang.get("cambiar_idioma"),
    		    // opcio per sortir
    		    controlLang.get("sortir")
    		};
    }

    /**
     * Mètode que fa la lògica per a cada opció del menu quan polsem Enter
     */
    private void ejecutarOpcion() {
    	// Utilizem un switch depenent de l'opcio escollida
        switch (seleccion) {
        	// Opció de jugar depenent del nivell escollit
            case OPCIO_JUGAR: 
            	// Busca el JFrame a la finestra que conté el panell
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                // Si el fram es diferent a null fa el següent procés
                if (frame != null) {
                    // Creem el següent panell del flux que demana el nom, AFEGIM MODE DE JOC
                	NombreUsuarioMenu nombreMenu = new NombreUsuarioMenu(controlLang, nivel, modoJuego);
                    // Netegem la finestra actual
                    frame.getContentPane().removeAll();
                    // Afegim el nou panell
                    frame.add(nombreMenu);
                    // Refresquem el disseny
                    frame.revalidate();
                    // Pintem de nou
                    frame.repaint();
                    // Donem el focus del teclat a nou panell
                    nombreMenu.requestFocusInWindow();
                }
                break;
            // Opció per consultar quins son els controls
            case OPCIO_CONTROLS: 
            	// Mostra un finestra emergent amb la informaicó afegida
                JOptionPane.showMessageDialog(this, controlLang.get("info_controles"), 
                        controlLang.get("controles"), JOptionPane.INFORMATION_MESSAGE);
                break;
            // Opció de canviar el idioma dins del menu
            case OPCIO_CANVIAR_IDIOMA:
                volverAIdiomas();
                break;
            // Opció sortir del programa
            case OPCIO_SORTIR: 
            	// Tanquem l'aplicació completament amb el valor 0
                System.exit(0);
                break;
        }
    }

    /**
     * Mètode que retorna al menu de selecció d'idioma original
     */
    private void volverAIdiomas() {
    	// Busca el JFrame a la finestra que conté el panell
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Si el fram es diferent a null fa el següent procés
        if (frame != null) {
        	// Busca el JFrame a la finestra que conté el panell
            LanguageSelectionMenu langMenu = new LanguageSelectionMenu();
        	// Netegem la finestra actual
            frame.getContentPane().removeAll();
            // Afegim el nou panell
            frame.add(langMenu);
            // Refresquem el disseny
            frame.revalidate();
            // Pintem de nou
            frame.repaint();
        	// Donem el focus del teclat a nou panell	
            langMenu.requestFocusInWindow();
        }
    }

    /**
     * Metode alternatiu per començar el joc
     */
    private void iniciarFlujoJuego() {
    	// Mostra una finestra emergent que demana el nom de l'usuari i després s'utilitza per posarlo al titol de la finestra
        nombre = JOptionPane.showInputDialog(null, controlLang.get("nom_usuari"), "Mini Tennis", JOptionPane.QUESTION_MESSAGE);
        // Comprovem que l'usuari no hagi polsat l'opció cancelar o hagi deixat el camp buit
        if (nombre != null && !nombre.trim().isEmpty()) {
        	// Si accepta els parametres afegits, doncs et dona una finestra amb les regles del joc
            if (JOptionPane.showConfirmDialog(null, controlLang.get("regles"), "Rules", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                // Finalitza el mètode que tanca el menu i obre el joc
            	lanzarPantallaJuego();
            }
        }
    }

    /**
     * Mètode que tanca el menu i obre la finestra del joc
     */
    private void lanzarPantallaJuego() {
    	// Cerquem la finestra JFrame on esta el menu
        Window win = SwingUtilities.getWindowAncestor(this);
        // Si troba la finestra, la tanca directament, això fa que el menu desaparegui abans d'obrir el joc
        if (win != null) win.dispose();
        // Creem una nova finestra per al joc amb el titol que inclou el nom d'usuari
        JFrame gameFrame = new JFrame("Retro Tenis - " + nombre);
        // Creem una nova instancia //ANTES ESTABA GAME GAME AHORA HE CAMBIADO
        GameData data = new GameData();
        Game game = new Game(data, controlLang.getIdiomaActual());
        data.jugador1 = nombre;
        data.nickname = nombre;
        data.level = nivel;
        data.modoJuego = modoJuego;
        // Afegim el component del joc a la finestra creada
        gameFrame.add(game);
        // Definim la mida de la finestra del joc
        gameFrame.setSize(300, 400); //DEFINIR ATRIBUTOS
        // Centrem la finestra automaticament al mig de la nostra pantalla
        gameFrame.setLocationRelativeTo(null);
        // Fem que la finestra sigui visible per a l'usuari
        gameFrame.setVisible(true);
        
        // Creem un nou temporizador que s'executa cada 10 ms i l'inicia
        new Timer(10, e -> { game.move(); game.repaint(); }).start();
        game.requestFocusInWindow();
    }
    
    //METODE HELPER
    private String getModeText() {
        switch (modoJuego) {
            case 1: return "MULTI ASYNC";
            case 2: return "MULTI SYNC";
            default: return "SINGLE";
        }
    }

    /**
     * Mètode que treballa tota la part visual, i crida cada vegade que demana a la visual.
     * @param g, Objecte Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
    	// Netejem el panell
        super.paintComponent(g);
        // Actualizem el estat de Strings
        actualizarTextos();
        // Tenim mes opcions de dibuix
        Graphics2D g2 = (Graphics2D) g;
        // Si detecta un fons
        if (fondo != null) {
        	// Dibuixa la imatge ocupant tota la pantalla de la pestanya
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            // Millorem el contrast afegint una capa semitransparent
            g2.setColor(new Color(0, 0, 0, 180)); 
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        // Li donem el colo groc al titol
        g2.setColor(Color.YELLOW);
        // Afegim la seva font i tamany
        g2.setFont(new Font("Monospaced", Font.BOLD, 30));
        // I posem el titol a la posició especificada
        g2.drawString("RETRO TENNIS", 40, 70);

        // Ara dibuixem les opcions del menu amb el format i tamany
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        // Fem un bucle que repassa les opcions oferides
        for (int i = 0; i < opciones.length; i++) {
        	// Si la posició d'opcions es igual a la seccionada
            if (i == seleccion) {
            	// Li donem un color Cian
                g2.setColor(Color.CYAN);
                // Seguidament s'afegeix el sigmbol ">" amb les opcions que estem escollint
                g2.drawString("> " + opciones[i], 40, 140 + i * 45);
                
            } else {
            	// Si les opcions no estan seleccionades es pinten de blanc
                g2.setColor(Color.WHITE);
                // No afegim res a les opcions restants
                g2.drawString("  " + opciones[i], 40, 140 + i * 45);
            }
        }
        
        // Dibuixem guia de contrl al peu de pàgina amb el seu format i tamany
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        // Li donem el color en gris
        g2.setColor(Color.DARK_GRAY);
        // Afegim el contingut de controlLang i donem les posicions
        g2.drawString(controlLang.get("guia_menu"), 15, 370);
    }
}