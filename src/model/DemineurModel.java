package model;

import java.util.Random;

public class DemineurModel {
	private int nbLignes;
	private int nbColonnes;
	private int nbBombes;
	private int nbDrapeaux;
	private int nbClicsRestants;
	private int etat;
	private boolean[][] mines;
	private boolean[][] drapeaux;
	private int[][] plateau;

	public DemineurModel() {
		initialiser(10, 10);
	}

	public boolean positionValide(int ligne, int colonne) {
		return ligne >= 0 && ligne < nbLignes && colonne >= 0 && colonne < nbColonnes;
	}

	public void clic(int ligne, int colonne) { // A savoir : -1 = case invisible
		if(!positionValide(ligne, colonne)){
			if(ligne==nbLignes+1 && 0<=colonne && colonne<=3)
				initialiser(10, 10);
			if(ligne==nbLignes+1 && 7<=colonne && colonne<=9)
				System.exit(0);
			return;
		}
		if(etat == 1){
			if(drapeaux[ligne][colonne])
				return;
			if(mines[ligne][colonne]){
				if(getNbClicsRestants() == getNbLignes()*getNbColonnes()-getNbBombes())
					for (int i = 0; i < nbLignes; i++)
						for (int j = 0; j < nbColonnes; j++)
							if (!mines[i][j]){
								mines[i][j] = true;
								mines[ligne][colonne] = false;
								compteurVoisin(ligne, colonne);
								return;
							}
				else
					nbClicsRestants = 0;
					plateau[ligne][colonne] = 9; // 9 = case bombe
			}
			else{
				if(plateau[ligne][colonne] == -1)
					nbClicsRestants--;
				compteurVoisin(ligne, colonne);
			}
		}
	}
	
	public void compteurVoisin(int ligne, int colonne){
		int compt = 0;
		for(int i = ligne-1; i <= ligne+1; i++)
			for(int j = colonne-1; j <= colonne+1; j++)
				if(positionValide(i, j) && !(i==ligne && j==colonne))
					if(mines[i][j])
						compt++;
		plateau[ligne][colonne] = compt; // si 1 a 8 = case indicatrice
		clicWave(ligne, colonne); // si 0, propagation
	}
	
	public void clicWave(int ligne, int colonne){
		if(plateau[ligne][colonne] == 0) // 0 = case blanche
			for(int i = -1; i < 2; i++)
				for(int j = -1; j < 2; j++)
					if(positionValide(ligne+i, colonne+j) && plateau[ligne+i][colonne+j] == -1)
							clic(ligne+i,colonne+j);
	}
	
	public void clicDroit(int ligne, int colonne) {
		if(!positionValide(ligne, colonne) || plateau[ligne][colonne] != -1)
			return;			
		else{
			if(!drapeaux[ligne][colonne]){
				if(nbDrapeaux < nbBombes){
					nbDrapeaux++;
					drapeaux[ligne][colonne] = true;
				}}
			else{
				nbDrapeaux--;
				drapeaux[ligne][colonne] = false;
			}
		}
	}
	
	public boolean gagner() {
		if((nbDrapeaux == nbBombes && nbClicsRestants == 0)	&& etat != 0)
			return true;
		return false;
	}
	
	public void initialiser(int nbLignes, int nbColonnes) {
		if (nbLignes < 0 || nbColonnes < 0)
			return;
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		nbBombes = 15;
		nbDrapeaux = 0;
		nbClicsRestants = nbLignes*nbColonnes - nbBombes;
		etat = 1;
		mines = new boolean[nbLignes][nbColonnes];
		drapeaux = new boolean[nbLignes][nbColonnes];
		plateau = new int[nbLignes][nbColonnes];
		for (int lignes = 0; lignes < nbLignes; lignes++)
			for (int colonnes = 0; colonnes < nbColonnes; colonnes++)
				plateau[lignes][colonnes] = -1;
		 initBombes();
	}
	
	public void initBombes() {
		for(int b = 0; b < nbBombes; b++) {
			int x = new Random().nextInt(nbLignes);
			int y = new Random().nextInt(nbColonnes);
			if(!mines[x][y])
				mines[x][y] = true;
			else
				b--;
		}
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public int getNbColonnes() {
		return nbColonnes;
	}
	
	public int getNbBombes() {
		return nbBombes;
	}
	
	public int getNbDrapeaux() {
		return nbDrapeaux;
	}
	
	public int getNbClicsRestants() {
		return nbClicsRestants;
	}
	
	public int getEtat() {
		return etat;
	}
	
	public void etatWin() {
		etat = 2;
	}
	
	public void etatLose() {
		etat = 0;
	}

	public boolean getMine(int ligne, int colonne) {
		if (!positionValide(ligne, colonne))
			return false;
		return mines[ligne][colonne];
	}
	
	public boolean getDrapeau(int ligne, int colonne) {
		if (!positionValide(ligne, colonne))
			return false;
		return drapeaux[ligne][colonne];
	}
	
	public int getPlateau(int ligne, int colonne) {
		if (!positionValide(ligne, colonne))
			return -2;
		return plateau[ligne][colonne];
	}
}