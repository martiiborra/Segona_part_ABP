package minitennis.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.net.URL;
import java.awt.Image;
import javax.swing.ImageIcon;

import minitennis.main.Game;

/**
 * Classe Obstacle que utilitza exclusivament imatges per al renderitzat.
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Obstacle {
	
	//Declaració d'atributs privats
	//Coordenada del vector X
    private int x;
    //Coordenada del vector Y
    private int y;
    //Declaració i inicialització d'atribut privat, per la velocitat lateral
    private int dx = 2;
    
    //Declaració i inicialització de finals per la mida de l'amplada de la caixa de xoc
    private static final int WIDTH = 26;
    //Declaració i inicialització de finals per la mida de l'alçada de la caixa de xoc
    private static final int HEIGHT = 28;
    //Declaració i inicialització de finals per la mida de l'amplada de l'imatge
    private static final int IMG_WIDTH = 30;
    //Declaració i inicialització de finals per la mida de l'alçada de l'imatge
    private static final int IMG_HEIGHT = 30;
   
    //Instància de la classe game
    private Game game;
    //Declaració i inicialització de variable, s'encarrega de la vida
    private int vida = 1;
   
    //Objectes per emmagatzemar les imatges dels fantasmes
    //Imatge quan té la vida màxima
    private Image imgNormal;
    //Imatge quan ha rebut un cop
    private Image imgDanyat;
   
    /**
     * Constructor del osbtacle
     * @param x
     * @param y
     * @param game
     * @param nivel
     */
    public Obstacle(int x, int y, Game game, int nivel) {
        this.x = x;
        this.y = y;
        this.game = game;
        
        //Estructura de control d'errors TRY-CATCH
        try {
            // Intentem carregar les imatges des del mateix paquet on està la classe
            URL urlNormal = getClass().getResource("/Imatge/fantasmaAmarillo.png");
            URL urlDanyat = getClass().getResource("/Imatge/fantasmaRojo.png");
           
            //Estructura condicional on s'avalua si la imatge no es nul·la
            if (urlNormal != null) {
            		//Si no es nul·la, mostrem la imatge
                imgNormal = new ImageIcon(urlNormal).getImage();
            }
            
            //Estructura condicional on s'avalua si la imatge no es nul·la
            if (urlDanyat != null) {
            		//Si no es nul·la, mostrem la imatge
                imgDanyat = new ImageIcon(urlDanyat).getImage();
            }
        } catch (Exception e) {
            // No dibuixem res si falla
        }
        
        //Estructura condicional mirem si el nivell arriab al 10, quan arriab el 10 el fantasma te dues vides
        if (nivel >= 10) {
        		this.vida = 2;
        }
    }

    /**
     * Mètode move que gestiona el moviment dels obstacles
     */
    public void move() {
    		//Estructura condicional on valida que es trobi dins de la finestra
        if (game.getWidth() > 0) {
        		//Sumem la velocitat lateral a la coordenada x per al desplaçament del obstacle
            x += dx;
            //Estructura condicional on avaluem la col·lisió de l'obstacle amb la paret
            if (x <= 0 || x >= game.getWidth() - WIDTH) {
            		//Invertim el desplaçament
            		dx *= -1;
            }
        }
    }

    /**
     * Mètode que controla la vida del fantasma, aquest serveix per apartir del nivell 10
     */
    public void ferDany() { 
    		//Decrementació de la vida
    		vida--; 
    	}
    
    /**
     * Mètode GETTER que accedeix a la vida
     * @return el valor de vida
     */
    public int getVida() { 
    		return vida; 
    	}

    /**
     * Mètode que dibuixa la representació gràfica de l'obstacle (fantasma) a la
	 * pantalla. 
     * @param g
     */
    public void paint(Graphics2D g) {
    	
    		//Declaració d'objecte imatge, per mostrar les diferents imatges
    		Image imgActual;
    		
        // Triem la imatge segons la vida amb l'estructura condicional
    		if(vida == 2) {
    			//Mostrem el fantasma groc
    			imgActual = imgNormal;
    		} else {
    			//Mostrem el fantasma vermell
    			imgActual = imgDanyat;
    		}
    		
        //Declaració i inicialització de variable per calcular el centrat per a la imatge a l'eix X
        int centratFantasmaX = x - (IMG_WIDTH - WIDTH) / 2;
        //Declaració i inicialització de variable per calcular el centrat per a la imatge a l'eix Y
        int centratFantasmaY = y - (IMG_HEIGHT - HEIGHT) / 2;
       
        // Estructura condicional que avalua si la imatge existeix. Si no, no es veurà res.
        if (imgActual != null) {
        		//Es dibuixa amb el mètode drawImage
            g.drawImage(imgActual, centratFantasmaX, centratFantasmaY, IMG_WIDTH, IMG_HEIGHT, null);
        }
    }

    /**
     * Mètode que retorna l'àrea rectangular dels obstacles per a càlculs de
	 * col·lisió.
     * @return
     */
    public Rectangle getBounds() {
    		//Objecte Rectangle.
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}

