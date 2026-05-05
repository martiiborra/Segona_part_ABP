package minitennis.main;

/**
* Definició de la classe MainApp.
* Aquesta classe actua com el punt d'entrada principal del programa.
* La seva funció és arrencar l'aplicació mitjançant la instanciació i visualització
* de la finestra inicial, delegant la càrrega de la interfície.
* @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
*/
public class MainApp {
	public static void main(String[] args) {
		
		// Creem una instància de la finestra inicial del joc
		InitialWindow menu = new InitialWindow();
		// Cridem al mètode per visualitzar el menú principal
		menu.mostrarMenu();
	}
}
