package graphique;
import java.awt.Dimension;

import javax.swing.JFrame;

import model.DemineurModel;

public class DemineurFrame extends JFrame {
	/** Serial UID.  */
	private static final long serialVersionUID = 1L;
	
	private DemineurPanel demineurPanel = new DemineurPanel();

	public DemineurFrame() {
		setSize(new Dimension(502, 640));
		add(demineurPanel);
	}
	
	public void setDemineurModel(DemineurModel demineurModel) {
		demineurPanel.setDemineurModel(demineurModel);
	}
}
