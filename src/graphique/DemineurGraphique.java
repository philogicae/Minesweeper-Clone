package graphique;

import model.DemineurModel;

public class DemineurGraphique {
	private DemineurModel demineurModel = new DemineurModel();
	private DemineurFrame demineurFrame = new DemineurFrame();
	
	public DemineurGraphique() {
		demineurFrame.setVisible(true);
		demineurFrame.setDemineurModel(demineurModel);
		demineurFrame.setTitle("Demineur");
	}

}
