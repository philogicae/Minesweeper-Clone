package console;

import java.util.Scanner;
import model.DemineurModel;

public class DemineurConsole {
	private DemineurModel demineurModel = new DemineurModel();

	public DemineurConsole() {
		Scanner scanner = new Scanner(System.in);
		boolean continuer = true;
		
		do {
			afficher();
			System.out.println("Une position ?");

			int ligne = scanner.nextInt();
			int colonne = scanner.nextInt();

			if (demineurModel.positionValide(ligne, colonne))
				demineurModel.clic(ligne, colonne);
			else
				continuer = false;
		} while (continuer);
		
		scanner.close();
	}

	public void afficher() {
		for (int ligne = 0; ligne < demineurModel.getNbLignes(); ligne++) {
			for (int colonne = 0; colonne < demineurModel.getNbColonnes(); colonne++)
				System.out.print(demineurModel.getMine(ligne, colonne) ? " * ": " . ");
			System.out.println();
		}
	}
}
