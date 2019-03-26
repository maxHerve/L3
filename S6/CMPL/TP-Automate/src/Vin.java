import java.awt.* ;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JFrame;
/**
 * La classe Vin genere un analyseur syntaxique avec actions 
 * de traitement de fiches de livraison de vin
 * @author Masson, Grazon
 *
*/
public class Vin {

	/**
	 * utilitaire d'initialisation de l'entree a analyser
	 * @return flot a analyser
	 */
	public static InputStream debutAnalyse() {
		String nomfich;
		nomfich = Lecture.lireString("nom du fichier d'entree : ");
		InputStream f = Lecture.ouvrir(nomfich);
		if (f == null) {
			System.exit(0);
		}
		return f;
	} 

	/**
	 * utilitaire de fermeture du fichier de l'entree a analyser
	 * @param f flot de l'entree a analyser
	 */
	public static void finAnalyse(InputStream f) {
		Lecture.fermer(f);
		Lecture.attenteSurLecture("fin d'analyse");
		System.exit(0);
	} 
	
   /**
    * Main du TP Automate - L3 Informatique
    */
	public static void main(String[] args) {

		FenAffichage fenetre = new FenAffichage();

		ActVin analyseur;
		InputStream flot = debutAnalyse();

		analyseur = new ActVin(flot);
		analyseur.newObserver(fenetre, fenetre);
		analyseur.interpreteur();
		finAnalyse(flot);
	}
} 

