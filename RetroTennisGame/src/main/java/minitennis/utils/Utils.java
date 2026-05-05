package minitennis.utils;

/**
 * Definició de la classe Utils.
 * Aquesta classe actua com emmagatzemador de variables final
 * Hi han variables final que et repeteixen en les classes, doncs només refereciant
 * les final d'aquesta classe no caldria tornar a declarar les final
 * 
 * @autor André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Utils {
	
	//Declaració i incialotzació de finals per la mida de la finestra
	public static final int WINDOW_WIDTH = 300;
    public static final int WINDOW_HEIGHT = 500;
    
    //Declaració i incialotzació de finals per la lógica del joc
    public static final int TIME_TO_LEVEL_UP = 20000; // 20 segundos
    public static final int MAX_OBSTACLES = 10;
    public static final int MAX_BALLS = 6;
    public static final int LEVEL_FOR_SECOND_BALL = 16;
    public static final int MIN_LEVELS = 1;
    public static final double MAX_BALL_SPEED = 12.0; 
    public static final double VELOCIDAD_BASE = 1.0;
    public static final double INCREMENTO_POR_NIVEL = 1.1;
    
    //Declaració i inicialització de finals per els multiidioma
    public static final String LANG_CAT = "CAT";
    public static final String LANG_ES = "ES";
    public static final String LANG_EN = "EN";
}
