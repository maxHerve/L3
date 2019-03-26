import java.io.*;

/**
 * automate de reconnaissance syntaxique des fiches de livraison de vin
 * 
 * @author ?? MERCI DE PRECISER LE NOM DU TRINOME ??
 *
 */
public class AutoVin extends Automate{

	/** table des transitions */
	private final int[][] transit =
	    {   
			/* Etat |       BJ    BG    IDENT   NBENT     ,     ;     /    AUTRES  */
	    	/* -----|--------------------------------------------------------------- 
			/* 0    |*/   { 7,    7,    1,      7,       7,     7,    7,   7   },
			/* 1    |*/   { 3,    3,    4,      2,       7,     7,    7,   7   },  
			/* 2    |*/   { 3,    3,    4,      7,       7,     7,    7,   7   },
			/* 3    |*/   { 7,    7,    4,      7,       7,     7,    7,   7   },
			/* 4    |*/   { 7,    7,    7,      5,       7,     7,    7,   7   },
			/* 5    |*/   { 7,    7,    4,      7,       2,     6,    7,   7   },
			/* 6    |*/   { 7,    7,    1,      7,       7,     7,    8,   7   },
			/* 7    |*/   { 7,    7,    7,      7,       7,     7,    7,   7   },
			
	    } ;
	
	/** utilitaire de suivi des modifications pour affichage */
	public void newObserver(ObserverAutomate oAuto, ObserverLexique oLex ){
		this.newObserver(oAuto);
		this.lex.newObserver(oLex);
		lex.notifyObservers(((LexVin)lex).getCarlu());
	}
 
	/** constructeur classe AutoVin */
	public AutoVin(InputStream flot) {
		/** on utilise ici un analyseur lexical de type LexVin */
		lex = new LexVin(flot);
		this.etatInitial = 0;
		this.etatFinal = transit.length;
		this.etatErreur = transit.length - 1;
	}

	/** definition de la methode abstraite getTransition de Automate */
	int getTransition(int etat, int unite) {
		return this.transit[etat][unite];
	}

	/** ici la methode abstraite faireAction de Automate n'est pas encore definie */
	void faireAction(int etat, int unite) {};
	
	/** ici la methode abstraite actionInit de Automate n'est pas encore definie */
	void initAction() {};
	
	/** ici la methode abstraite getAction de Automate n'est pas encore definie */
	int getAction(int etat, int unite) {return 0;};
		
}/** AutoVin */
