package minitennis.language;

import minitennis.utils.Utils;

/**
 * Definició de la classe ControlLanguage.
 * Aquesta classe actua com donar una definició per cada opció escollida.
 * La seva funció és depenent de l'opció d'idioma escollida (àngles, català i castellà)
 * doncs retorna les seves definicions
 * @autor Primera part Grup 4
 * @author Segona part Grup 1
 */
public class ControlLanguage {
	// Defineixen els identificadors d'idioma suportats per l'aplicació
	private final String IDIOMA_PER_DEFECTE = Utils.LANG_EN;
	final static String CATALA = Utils.LANG_CAT;
	final static String CASTELLA = Utils.LANG_ES;
	final static String ANGLES = Utils.LANG_EN;
	
	//Emmagatzema el codi de l'idioma actiu en la instància
	private String idiomaActual = Utils.LANG_EN;
	
	/**
    * Mètode mutador per definir l'idioma de treball.
    * @param idioma Codi de l'idioma
    */
	public void setIdiomaActual(String idioma) {
		this.idiomaActual = idioma; // Assignació de la referència a l'atribut d'instància
	}
	
	/**
    * Mètode d'accés per obtenir l'idioma actual.
    * @return El codi de l'idioma actual
    */
	public String getIdiomaActual() {
		return this.idiomaActual;
	}
	
	/**
    * Mètode d'accés per obtenir la traducció d'una etiqueta.
    * Implementa una lògica de selecció múltiple per resoldre
    * la cadena de text corresponent a la clau i l'idioma actual.
    * @param clau Identificador únic del text.
    * @return El text traduït o un missatge d'error si l'idioma no existeix.
    */
	public String get(String clau) {
		// Estructura de control per a la selecció de l'idioma
		switch (idiomaActual) {

		case CATALA: // Cas en què l'idioma actiu és el Català
            switch (clau) {
            case "titol_menu": return "RETRO TENNIS"; // Corregit de titol:menu a titol_menu per coherència
            case "regles": return "Regles: Mou la pala per evitar que la bola caigui.";
            case "nivell": return "NIVELL: ";
            case "boto_acceptar": return "JUGAR";
            case "nom_usuari": return "Introdueix el teu nom:";
            case "error_nom": return "Has d'introduir un nom!";
            case "controles": return "CONTROLS";
            case "info_controles": return "Fletxes o Ratolí per moure la pala, 'P' per pausar la partida";
            case "cambiar_idioma": return "CANVIAR IDIOMA";
            case "sortir": return "SORTIR";
            case "guia_menu": return "Fletxes per navegar, ENTER per seleccionar";
            case "game_over_titol": return "FI DE LA PARTIDA"; // Claus afegides per al rànquing
            case "jugador": return "Jugador";
            case "puntuacio": return "Puntuació";
            case "ranking_titol": return "TOP 10 RÀNQUING ";
            case "tornar_jugar": return "Vols tornar a jugar?";
            case "cargar_partida": return "CARREGAR PARTIDA";
            case "nueva_partida": return "NOVA PARTIDA";
            case "pregunta_partida": return "Què vols fer?";
            case "joc_pausa": return "JOC EN PAUSA. Vols guardar la partida?";
            case "guardar_sortir": return "GUARDAR I SORTIR";
            case "seguir_jugant": return "SEGUIR JUGANT";
            case "titol_pausa": return "PAUSA";
            case "puntuacio_final": return "Puntuació final";
            case "torn_acabat": return "Torn de";
            case "ara_juga": return "Ara juga";
            case "equip": return "Equip";
            case "total": return "TOTAL";
            case "nom_jugador": return "Nom jugador";
            case "jugador1": return "Jugador 1";
            case "jugador2": return "Jugador 2";
            case "nickname": return "Nickname";
            default: return clau; 
            }
        case CASTELLA: // Cas en què l'idioma actiu és el Castellà
            switch (clau) {
            case "titol_menu": return "RETRO TENNIS";
            case "regles": return "Reglas: Mueve la pala para evitar que la bola caiga.";
            case "nivell": return "NIVEL: ";
            case "boto_acceptar": return "JUGAR";
            case "nom_usuari": return "Introduce tu nombre:";
            case "error_nom": return "¡Debes introducir un nombre!";
            case "controles": return "CONTROLES";
            case "info_controles": return "Flechas o Ratón para mover la pala, 'P' para pausar el juego";
            case "cambiar_idioma": return "CAMBIAR IDIOMA";
            case "sortir": return "SALIR";
            case "guia_menu": return "Flechas para navegar, ENTER para seleccionar";
            case "game_over_titol": return "JUEGO TERMINADO"; 
            case "jugador": return "Jugador";
            case "puntuacio": return "Puntuación";
            case "ranking_titol": return "TOP 10 RANKING";
            case "tornar_jugar": return "¿Quieres volver a jugar?";
            case "cargar_partida": return "CARGAR PARTIDA";
            case "nueva_partida": return "NUEVA PARTIDA";
            case "pregunta_partida": return "¿Qué quieres hacer?";
            case "joc_pausa": return "JUEGO EN PAUSA. ¿Quieres guardar la partida?";
            case "guardar_sortir": return "GUARDAR Y SALIR";
            case "seguir_jugant": return "SEGUIR JUGANDO";
            case "titol_pausa": return "PAUSA";	
            case "puntuacio_final": return "Puntuación final";
            case "torn_acabat": return "Turno de";
            case "ara_juga": return "Ahora juega";
            case "equip": return "Equipo";
            case "total": return "TOTAL";
            case "nom_jugador": return "Nombre jugador";
            case "jugador1": return "Jugador 1";
            case "jugador2": return "Jugador 2";
            case "nickname": return "Nickname";
            default: return clau;
            }
        default: // Idioma per defecte: English (s'executa si no és ni CAT ni ES)
            switch (clau) {
            case "titol_menu": return "RETRO TENNIS";
            case "regles": return "Rules: Move the paddle to prevent the ball from falling.";
            case "nivell": return "LEVEL: ";
            case "boto_acceptar": return "START";
            case "nom_usuari": return "Enter your name:";
            case "error_nom": return "You must enter a name!";
            case "controles": return "CONTROLS";
            case "info_controles": return "Arrows or Mouse to move the paddle, 'P' pause the game";
            case "cambiar_idioma": return "CHANGE LANGUAGE";
            case "sortir": return "EXIT";
            case "guia_menu": return "ARROWS to navigate, ENTER to select";
            case "game_over_titol": return "GAME OVER"; // Claus afegides per al rànquing
            case "jugador": return "Player";
            case "puntuacio": return "Score";
            case "ranking_titol": return "TOP 10 RANKING";
            case "tornar_jugar": return "Do you want to play again?";
            case "cargar_partida": return "LOAD GAME";
            case "nueva_partida": return "NEW GAME";
            case "pregunta_partida": return "What do you want to do?";
            case "joc_pausa": return "GAME PAUSED. Do you want to save the game?";
            case "guardar_sortir": return "SAVE AND EXIT";
            case "seguir_jugant": return "CONTINUE PLAYING";
            case "titol_pausa": return "PAUSE";
            case "puntuacio_final": return "Final score";
            case "torn_acabat": return "Turn of";
            case "ara_juga": return "Now playing";
            case "equip": return "Team";
            case "total": return "TOTAL";
            case "nom_jugador": return "Player name";
            case "jugador1": return "Player 1";
            case "jugador2": return "Player 2";
            case "nickname": return "Nickname";
            default: return clau;
            }
        }
	}
}