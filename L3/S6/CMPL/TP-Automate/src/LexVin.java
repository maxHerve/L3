
import java.awt.TextArea;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyseur lexical pour TP livraison vins
 * @author SATTLER Donatien, HERVE Maxime, BESCON Dorian;
 *
 */
public class LexVin extends Lex {

	/** codage des items lexicaux */
	protected final int 
			BEAUJOLAIS = 0, BOURGOGNE = 1, IDENT = 2, NBENTIER = 3, 
			VIRGULE = 4, PTVIRG = 5, BARRE = 6, AUTRES = 7;
	public static final String[] images = { "BEAUJ", "BOURG", "IDENT", "NBENT", "  ,  ",
			"  ;  ", "  /  ", "AUTRE" };
 
	/** nombre de mots reserves dans Lex.tabId */
    private final int NBRES = 2 ; 
    /** indice de remplissage de Lex.tabid */
    private int itab ; 

    /** attributs lexicaux */
    private int valNb, numId ;
    
    /** caractere courant */
    private char carlu ; 
    
    public char getCarlu(){
		return this.carlu;
    }
    
    /** constructeur classe LexVin */
    public LexVin(InputStream flot) {
	 	/** initialisation du flot par la classe abstraite */
    	super(flot);
    	/** prelecture du premier caractere de la donnee */
    	lireCarlu();
    	/** initialisation tabId par mots reserves */
    	Lex.tabId[0] = "BEAUJOLAIS" ; Lex.tabId[1] = "BOURGOGNE" ;
    	itab = 1;
    }

    /** procedure de lecture du caractere courant */
	private void lireCarlu() {
		carlu = Lecture.lireChar(flot);
		this.notifyObservers(carlu);
		if ((carlu == '\r') || (carlu == '\n') || (carlu == '\t'))
			carlu = ' ';
		if (Character.isWhitespace(carlu))
			carlu = ' ';
		else
			carlu = Character.toUpperCase(carlu);
	}
	
	private int lireEnt() {
		String s = "";
		do { 
			s = s + carlu; lireCarlu();
		} while ((carlu >= '0') && (carlu <= '9'));
		valNb = Integer.parseInt(s);
		return NBENTIER;
	}
	
	private int lireIdent() {
		String s = "";
		
		do { 
			s = s + carlu; lireCarlu();
		} while (isLetter(carlu));
		
		System.out.println(s);
		
		int res = IDENT;
		
		int index = 0;
		for( int i=0 ; i<itab+1 ; i++) {
			if(Lex.tabId[i].contains(s)) {
				index = i;
				i = itab+1;
				break;
			}
			index = i+1;
		}
		
		if(index < NBRES) {
			res = index;
			if(res == BEAUJOLAIS)
			{
				numId = BEAUJOLAIS;
			}
			else
			{
				numId = BOURGOGNE; 
			}
		}
		else if(index == itab+1) {
			
			itab++;
			Lex.tabId[itab] = s;
			numId = index;		
		}
		else {
			numId = index;
		}
		
		return res;
	}
	
	private static boolean isLetter(char carlu) {
		Pattern p = Pattern.compile("[a-zA-Z]");
		 
		String tmp = new String();
		tmp += carlu;
		Matcher m = p.matcher(tmp);
		return m.matches();
	}

	/** determination de l'item lexical
	 * definition de la methode abstraite lireSymb de Lex  
	 * 
	 * @return code entier de l'item lexical reconnu
	 * */
	public int lireSymb() {
		while (carlu == ' ') {
			
			lireCarlu();
		}
		
		/** détection d'un item lexical IDENT **/
		if(isLetter(carlu)) {
				return lireIdent();
		}
		
		/** detection item lexical NBENTIER */
		if ((carlu >= '0') && (carlu <= '9')) {
			
			return lireEnt();
		}
		/** detection autres items lexicaux */
		switch (carlu) {
		
			case ';':
				lireCarlu();
				return PTVIRG;		
			case ',':
				lireCarlu();
				return VIRGULE;
			case '/':
				return BARRE;
			default: 
				System.out.println("LexVin : caractère incorrect : " + carlu);
				lireCarlu();
				return AUTRES;
		}
		
	} /** liresymb */

	/** fonction donnant la chaine associee a un ident de tabId
	 * definition de la methode abstraite repId de Lex 
	 * 
	 * @param nId indice de l'ident dans tabId
	 * @return chaine associee a l'ident 
	 * */
	public String repId(int nId) {
		if(nId >= 0 && nId < itab) {
			return Lex.tabId[nId];
		}
		return null;
	} 

	/** methodes d'acces aux attributs lexicaux */
	public int getValNb() {
		return this.valNb;
	}

	public int getNumId() {
		return this.numId;
	}
	
	/** utilitaire de test de l'analyseur lexical seul (sans analyse syntaxique) */
	private void testeur_lexical() {
		/** Unite lexicale courante */
		int token;
		/** definition du caractere de fin de chaine 
		 * utile uniquement pour text autonome du lexical*/ 
		int finDeChaine = BARRE;
		do {
			token = lireSymb();
			if (token == NBENTIER)
				Lecture.attenteSurLecture("token : " + images[token] + " attribut valNB = " + valNb);
			else if (token == IDENT)
				Lecture.attenteSurLecture("token : " + images[token] + " attribut numId = " + numId);
			else
				Lecture.attenteSurLecture("token : " + images[token]);
		} while (token != finDeChaine);
	}

	
	/**
	 * Main pour tester l'analyseur lexical seul (sans analyse syntaxique)
	 */
	public static void main(String args[]) {
		
		String nomfich;
		nomfich = Lecture.lireString("nom du fichier d'entree : ");
		InputStream flot = Lecture.ouvrir(nomfich);
		if (flot == null) {
			System.exit(0);
		}

		LexVin testVin = new LexVin(flot);
		testVin.testeur_lexical();

		Lecture.fermer(flot);
		Lecture.attenteSurLecture("fin d'analyse");
		System.exit(0);

	}

} /** class Lexvin */
