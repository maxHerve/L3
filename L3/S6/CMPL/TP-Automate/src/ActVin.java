import java.io.InputStream;

/**
 * gestion des actions associees a la reconnaissance syntaxique des fiches de
 * livraison de vin
 * 
 * @author HERVE Maxime, SATTLER Donatien, BESCON Dorian
 */
public class ActVin extends AutoVin {

	/** table des actions */
	private final int[][] action = {
		/* Etat |       BJ    BG    IDENT   NBENT     ,     ;     /    AUTRES  */
    	/* -----|--------------------------------------------------------------- 
		/* 0    |*/   { 9,    9,    1,      9,       9,     9,    9,   9   },
		/* 1    |*/   { 5,    5,    7,      2,       9,     9,    9,   9   },  
		/* 2    |*/   { 5,    5,    7,      9,       9,     9,    9,   9   },
		/* 3    |*/   { 9,    9,    7,      9,       9,     9,    9,   9   },
		/* 4    |*/   { 9,    9,    9,      3,       9,     9,    9,   9   },
		/* 5    |*/   { 9,    9,    7,      9,       4,     6,    9,   9   },
		/* 6    |*/   { 9,    9,    1,      9,       9,     9,    8,   9   },
		/* 7    |*/   { 9,    9,    9,      9,       9,     9,    9,   9   },
		/* 8    |*/   {-1,   -1,   -1,     -1,      -1,    -1,   -1,  -1   },
    } ;	       

	/** constructeur classe ActVin */
	public ActVin(InputStream flot) {
		super(flot);
	}

	/** types d'erreurs detectees */
	private static final int FATALE = 0, NONFATALE = 1;

	/** taille d'une colonne pour affichage ecran */
	private static final int MAXLGID = 20;
	
	/** nombre maximum de chauffeurs */
	private static final int MAXCHAUF = 10;
	
	/** capacité de la citerne actuel */
	private static final int ID_BEAUJOLAIS = 0;
	private static final int ID_BOURGOGNE = 1;
	
	/** tableau des chauffeurs et resume des livraison de chacun */
	private Chauffeur[] tabChauf = new Chauffeur[MAXCHAUF];

	/** indice courant du nombre de chauffeurs dans le tableau tabChauf */
	private int ichauf;

	/** capacité de la citerne actuel */
	private int capacite;
	
	/** somme des hectolitres de vin déposées  */
	private int c;
	
	/** capacité de la citerne actuel */
	private int indexChauf;
	
	/** capacité de la citerne actuel */
	private int idChauf;
	
	/** numId du type de vin contenu par la citerne */
	private int idVin;
	
	/** capacité de la citerne actuel */
	private int qteOrdin;
	
	/** capacité de la citerne actuel */
	private int qteBj;
	
	/** capacité de la citerne actuel */
	private int qteBg;
	
	/** capacité de la citerne actuel */
	private int nbFiches;
	
	/** capacité de la citerne actuel */
	private int nbFichesValides;

	/** capacité de la citerne actuel */
	private SmallSet listeMagasins;

	/**
	 * utilitaire d'affichage a l'ecran
	 * 
	 * @param ch est une chaine de longueur quelconque
	 * @return chaine ch cadree a gauche sur MAXLGID caracteres
	 */
	private String chaineCadrageGauche(String ch) {

		int lgch = Math.min(MAXLGID, ch.length());
		String chres = ch.substring(0, lgch);
		for (int k = lgch; k < MAXLGID; k++)
			chres = chres + " ";
		return chres;
	}

	/** affichage de tout le tableau de chauffeurs a l'ecran */
	private void afficherchauf() {
		String idchaufcour, ch;
		Ecriture.ecrireStringln("");
		ch = "CHAUFFEUR                   BJ        BG       ORD     NBMAG\n"
				+ "---------                   --        --       ---     -----";
		Ecriture.ecrireStringln(ch);
		for (int i = 0; i <= ichauf; i++) {
//			System.out.println(" numchauf courant "+tabChauf[i].numchauf);
			idchaufcour = ((LexVin) lex).repId(tabChauf[i].numchauf);
			Ecriture.ecrireString(chaineCadrageGauche(idchaufcour));
			Ecriture.ecrireInt(tabChauf[i].bj, 10);
			Ecriture.ecrireInt(tabChauf[i].bg, 10);
			Ecriture.ecrireInt(tabChauf[i].ordin, 10);
			Ecriture.ecrireInt(tabChauf[i].magdif.size(), 10);
			Ecriture.ecrireStringln("");
		}
	}

	/**
	 * gestion des erreurs
	 * 
	 * @param te      type de l'erreur
	 * @param messErr message associe a l'erreur
	 */
	private void erreur(int te, String messErr) {
		Lecture.attenteSurLecture(messErr);
		switch (te) {
		case FATALE:
			errFatale = true;
			break;
		case NONFATALE:
			etatCourant = etatErreur;
			break;
		default:
			Lecture.attenteSurLecture("parametre incorrect pour erreur");
		}
	}

	/**
	 * initialisations a effectuer avant les actions
	 */
	private void initialisations() {
		ichauf = -1;
		idVin = -1;
		capacite = 100;
	}

	/**
	 * acces a un attribut lexical cast pour preciser que lex est de type LexVin
	 * 
	 * @return valNb associe a l'unite lexicale nbentier
	 */
	private int valNb() {
		return ((LexVin) lex).getValNb();
	}

	/**
	 * acces a un attribut lexical cast pour preciser que lex est de type LexVin
	 * 
	 * @return numId associe a l'unite lexicale ident
	 */
	private int numId() {
		return ((LexVin) lex).getNumId();
	}
	

	/**
	 * execution d'une action
	 * 
	 * @param numact numero de l'action a executer
	 */
	public void executer(int numact) {

		switch (numact) {

		case -1:
			
			break;

		case 1:
			
			nbFiches++;
			idChauf = numId();
			indexChauf = 0;
			qteOrdin = 0;
			qteBj = 0;
			qteBg = 0;
			c = 0;
			listeMagasins = new SmallSet();
			break;

		case 2:
			
			capacite = valNb();
			break;

		case 3:
			
			if(valNb() <= 0) {
				
				erreur(NONFATALE,"Val nb négatif !\n");
			}

			c += valNb();

			if (c > capacite) {
				
				System.out.println("Capacité maximum dépassé : " + capacite + " < " + c);
			}
			
			if (idVin == ID_BEAUJOLAIS) {

				qteBj += valNb();

			} else if (idVin == ID_BOURGOGNE) {
				
				qteBg += valNb();
				
			} else {
				
				qteOrdin += valNb();
			}

			break;

		case 4:
			
			c = 0;
			idVin = numId();
			break;

		case 5:
			
			idVin = numId();
			System.out.println(idVin);
			break;

		case 6:
			
			capacite = 100;
			int i = 0;
			boolean isPresent = false;

			while (i < ichauf && !isPresent) {
				
				if (tabChauf[i].numchauf == idChauf) {
					
					isPresent = true;
					indexChauf = i;
				}
				i++;
			}

			if (!isPresent) {

				ichauf++;
				if (ichauf < MAXCHAUF) {
					
					tabChauf[ichauf] = new Chauffeur(idChauf, 0, 0, 0, listeMagasins);
					tabChauf[ichauf].bj = qteBj;
					tabChauf[ichauf].bg = qteBg;
					tabChauf[ichauf].ordin = qteOrdin;
					
				} else {
					
					erreur(FATALE, "Le maximum de chauffeur est déjà atteint");
					break;
					
				}
				
			} else {

				tabChauf[indexChauf].bj += qteBj;
				tabChauf[indexChauf].bg += qteBg;
				tabChauf[indexChauf].ordin += qteOrdin;
				tabChauf[indexChauf].magdif.union(listeMagasins);
				
			}
			
			capacite = 100;
			idVin = -1;
			nbFichesValides++;
			
			afficherchauf();

		case 7:
			
			if(numId() >= 256) {
				
				erreur(FATALE, "Nombre maximal de magasins atteint.");
				
			}
			
			listeMagasins.add(numId());
			break;
		
		case 8:
			
			String nameChauf = "NOBODY";
			int maxMagasins = 0;
			for(int j = 0; j <= ichauf; j++ ) {
				
				if(tabChauf[j].magdif.size() > maxMagasins) {
					
					maxMagasins = tabChauf[j].magdif.size();
					nameChauf = ((LexVin) lex).repId(tabChauf[j].numchauf);
				}
			}
			
			System.out.println("\n\nLe chauffeur ayant livré le plus de magasins différents est " + nameChauf + "");
			System.out.println("Fiches correctes : " + nbFichesValides + " - Nombre total de fiches : " + nbFiches + "");
			
		case 9:
			
			erreur(NONFATALE, "Erreur de syntaxe.");
			break;
			
		default:
			
			erreur(FATALE, "Action " + numact + " non prévue.");
		}
	}

	/**
	 * definition methode abstraite faireAction de Automate
	 */
	public void faireAction(int etat, int unite) {
		executer(action[etat][unite]);
	};

	/**
	 * definition methode abstraite initAction de Automate
	 */
	public void initAction() {
		// action 0 a effectuer a l'init
		initialisations();
	};

	/**
	 * definition methode abstraite getAction de Automate
	 */
	public int getAction(int etat, int unite) {
		return action[etat][unite];
	};

} // class Actvin
