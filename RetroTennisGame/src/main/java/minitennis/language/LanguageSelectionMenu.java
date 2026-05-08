package minitennis.language;

import javax.swing.*;

import minitennis.menu.MenuRetro;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Definició de la classe LanguageSelectionMenu.
 * Aquesta classe actua com fineestra per seleccionar el idioma
 * La seva funció es mostrar a la finestra les 3 opcions d'idioma oferides
 * 
 * @autor Primera part Grup 4
 * @author Segona part Grup 1
 */
public class LanguageSelectionMenu extends JPanel {
	
	// Declarem els String en final
	private final String IDIOMA_ES = "ESPAÑOL";
	private final String IDIOMA_EN = "ENGLISH";
	private final String IDIOMA_CAT = "CATALÀ";
	
	// Declarem els int en final per la selecció d'idiomes
	private final int ANGLES = 0;
	private final int CASTELLA = 1;
	private final int CATALA = 2;
	
	// Llista d'opcions disponibles
    private String[] idiomas = {IDIOMA_EN, IDIOMA_ES, IDIOMA_CAT};
    // Index per saber quina opció està ressaltada
    private int seleccion = 0;
    // Variable per guardar la imatge de fons del menu
    private Image fondo;
    
    /**
     * Constructor que configura el panell inicial
     */
    public LanguageSelectionMenu() {
        try {
        	// Intenta carregar el gif pel fons 
            fondo = new ImageIcon(getClass().getResource("/Imatge/fondoMenu.gif")).getImage();
        } catch (Exception e) { 
        	// Si no detecta cap imatge, continua sense res
        }
        // En cas que no hagi imatge posa el color de fons negre
        setBackground(Color.BLACK);
        // Permet que aquest panell capti el teclat
        setFocusable(true);
        // Afegeix un escoltador per cada vegada que polsem el teclat
        addKeyListener(new KeyAdapter() {
        	
        	/**
        	 * Metode actuar per cada tecla polsada
        	 */
            @Override
            public void keyPressed(KeyEvent e) {
            	// Si es prem la fletxa amunt
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    seleccion = (seleccion - 1 + idiomas.length) % idiomas.length;
                 // Si es prem la fletxa avall
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    seleccion = (seleccion + 1) % idiomas.length;
                 // Si es prem la tecla Enter
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmarIdioma();
                }
             // Actualitza visualment el panell per mostrar el canvi d'opcio
                repaint(); 
            }
        });
    }
    
    /**
     * Mètode per aplicar l'idioma i canviar al menú principal
     */
    private void confirmarIdioma() {
    	// Instància de l'objecte que controla l'idioma
        ControlLanguage cl = new ControlLanguage();
        // Assigna l'idioma seleccionat per cada idioma
        if (seleccion == ANGLES) {
        		cl.setIdiomaActual(ControlLanguage.ANGLES);
        }
        if (seleccion == CASTELLA) {
        		cl.setIdiomaActual(ControlLanguage.CASTELLA);
        }
        if (seleccion == CATALA) {
        		cl.setIdiomaActual(ControlLanguage.CATALA);
        }
        // Localitzem la finestra on es troba aquest panell
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Si la finestra no es nula
        if (frame != null) {
        	// Crea el menú principal amb l'idioma escollit
            MenuRetro menuRetro = new MenuRetro(cl); 
            // Elimina el contingut actual de la finestra
            frame.getContentPane().removeAll();
            // Afegeix el nou menú a la finestra
            frame.add(menuRetro);
            // Refà el disseny dels components
            frame.revalidate();
            // Torna a dibuixar la finestra
            frame.repaint();
            // Deaman el focus per teclat
            menuRetro.requestFocusInWindow();
        }
    }
    
    /**
     * Mètode que s'encarrega de dibuixar tots els elements gràfics
     */
    @Override
    protected void paintComponent(Graphics g) {
    	// Crida al mètode original de la superclasse
        super.paintComponent(g); 
        // Converteix a funcions avançades
        Graphics2D g2 = (Graphics2D) g; 
        // Activa el suavitzat de vores per a una millor qualitat visual
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Si la imatge de fons s'ha carregat, la dibuixa i aplica un fons fosc
        if (fondo != null) {
        	// Dibuixa el fons
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            // Defineix un color negre semi-transparent
            g2.setColor(new Color(0, 0, 0, 180)); 
            // Dibuixa un rectangle fosc a sobre
            g2.fillRect(0, 0, getWidth(), getHeight()); 
        }

        // Lògica per mostrar el títol de "Seleccionar" depenent de l'idioma escollit
        String titulo;
        // Depenent de l'idioma que anirem a escollir canvis el titol
        if (seleccion == ANGLES) {
        		titulo = "SELECT";
        }
        else if (seleccion == CASTELLA) {
        		titulo = "SELECCIONAR"; 
        }
        else {
        		titulo = "SELECCIONAR "; 
        }

        // Afegim el color de titol a groc
        g2.setColor(Color.YELLOW);
        // Li asignem al seva font i tamany
        g2.setFont(new Font("Monospaced", Font.BOLD, 26));	
        // Escriu el titol a la part superior 
        g2.drawString(titulo, 25, 80);

        // Fem un bucle per escriure les tres opcions d'idioma
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        for (int i = 0; i < idiomas.length; i++) {
        	// si la posicio d'idioma es igual a la seleccio d'idioma
            if (i == seleccion) {
            	// Canvia el color a Cian
                g2.setColor(Color.CYAN);
                // Indica la posició actual amb un ">"
                g2.drawString("> " + idiomas[i], 60, 180 + i * 50);
            // En cas que no estigui seleccionat per teclat
            } else {
            	// Es posen en color blanc
                g2.setColor(Color.WHITE);
                // No afegim res a les posicions
                g2.drawString("  " + idiomas[i], 60, 180 + i * 50);
            }
        }
        
        // Li donem una font i tamny
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        // Asignem el color gris
        g2.setColor(Color.GRAY);
        // El text de guia canvia depenent de l'idioma
        String guia;
        if(seleccion == ANGLES) {
        		guia = "UP/DOWN to move, ENTER to select";
        } else if(seleccion == CASTELLA) {
        		guia = "Flechas para mover, ENTER para elegir";
        } else {
        		guia = "Fletxes per moure, ENTER per triar";
        }
        // S'aplica a la part inferior
        g2.drawString(guia, 20, 360);
    }
}