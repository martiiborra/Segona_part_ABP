package minitennis.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Definició de la classe Sound.
 * Aquesta classe actua com referenciador de la musica de fons.
 * La seva funció és per cada 5 nivells del joc canviar la musica, tindre efectes de so
 * a les colisions de lo bola o quan acaba la partida
 * @autor André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Sound {
	
	//Atributs per emmagatzemar els fitxers d'àudio en memòria
    private AudioClip fondoActual;
    private AudioClip golpe;
    private AudioClip gameover;
  
    //Variables per a les diferents pistes de música segons la progressió del joc
    //Les agrupem en un array per poder gestionar nivells infinits (més enllà del 30)
    private AudioClip[] musiques;
  
    /**
     * Constructor de la classe. S'encarrega d'inicialitzar i carregar 
     * tots els recursos d'àudio des de la carpeta de recursos.
     */
    public Sound() {
        try {
            // Càrrega de les músiques de fons assignades a diferents etapes
            musiques = new AudioClip[] {
                cargarSonido("musica1.wav"),
                cargarSonido("musica2.wav"),
                cargarSonido("musica3.wav"),
                cargarSonido("musica4.wav"),
                cargarSonido("musica5.wav"),
                cargarSonido("musica6.wav"),
                cargarSonido("Base_After_Base_(Full).wav")
            };

            // Càrrega d'efectes de game over y cop de bola
            golpe = cargarSonido("edr-8-bit-jump-001-171817.wav");
            gameover = cargarSonido("gameOver.wav");

            // Establim la pista inicial per defecte
            fondoActual = musiques[0];
            
        } catch (Exception e) {
            System.out.println("Error carregant sons: " + e.getMessage());
        }
    }

    /**
     * Mètode privat per carregar un fitxer d'àudio des del classpath.
     * @param archivo Nom del fitxer amb la seva extensió.
     * @return Un objecte AudioClip o null si no es troba el fitxer.
     */
    private AudioClip cargarSonido(String archivo) {
        try {
            // Busquem el fitxer dins de la carpeta "Sound" situada a resources
            URL url = getClass().getResource("/Sound/" + archivo); 
            if (url == null) {
                System.out.println("No se encuentra el audio: /Sound/" + archivo);
                return null;
            }
            // Retornem el clip d'àudio a punt per ser reproduït
            return Applet.newAudioClip(url);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Mètode que gestiona el canvi de música de fons de manera dinàmica segons el nivell.
     * Atura la música actual i inicia la nova només si hi ha un canvi de pista.
     * @param nivell El nivell actual del joc per determinar quina pista toca.
     */
    public void canviarMusica(int nivell) {
    	
    		//Declaració d'atribut per emmagtazemar la nova música
        AudioClip novaMusica;

        // Declaració i incialització de variable que serveix per la lògica de selecció de pista. Canvia aproximadament cada 5 nivells
        int index = (nivell - 1) / 5;

        // Si el nivell és molt alt, usem el mòdul (%) per tornar a començar el cicle de cançons
        index = index % musiques.length;
        
        novaMusica = musiques[index];

        // Si la música que toca és diferent a la que sona ara, fem el canvi
        if (novaMusica != fondoActual && novaMusica != null) {
            if (fondoActual != null) {
            		fondoActual.stop(); // Aturem l'actual
            }
            fondoActual = novaMusica; // Assignem la nova
            fondoActual.loop(); // La reproduïm en bucle
        }
    }

    /**
     * Mètode que inicia la reproducció en bucle de la música de fons actual.
     */
    public void playFondo() {
        if (fondoActual != null) {
            fondoActual.loop();
        }
    }

    /**
     * Mètode que atura completament la reproducció de la música de fons.
     */
    public void stopFondo() {
        if (fondoActual != null) {
            fondoActual.stop();
        }
    }

    /**
     * Mètode que reprodueix l'efecte de so de col·lisió (cop) una sola vegada.
     */
    public void playGolpe() {
        if (golpe != null) {
            golpe.play();
        }
    }

    /**
     * Mètode que reprodueix el so de final de partida (Game Over).
     */
    public void playGameOver() {
        if (gameover != null) {
            gameover.play();
        }
    }
}