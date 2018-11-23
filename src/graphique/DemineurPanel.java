package graphique;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.DemineurModel;

public class DemineurPanel extends JPanel {
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	private DemineurModel demineurModel;
	private long init = java.lang.System.currentTimeMillis()/1000;
	private final int DY = 50;
	private final int DX = 50;
	
	public DemineurPanel() {
		setBackground(Color.black);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int ligne = getLigne(e.getX(), e.getY());
				int colonne = getColonne(e.getX(), e.getY());
				if (e.getButton() == MouseEvent.BUTTON1)	
					demineurModel.clic(ligne, colonne);
				else if (e.getButton() == MouseEvent.BUTTON3) 
					demineurModel.clicDroit(ligne, colonne);
				if(demineurModel.getEtat() == 1) // Tant que ni perdu ni gagne
						repaint();
			}
		});
	}

	public void setDemineurModel(DemineurModel demineurModel) {
		this.demineurModel = demineurModel;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int ligne = 0; ligne < demineurModel.getNbLignes(); ligne++)
			for (int colonne = 0; colonne < demineurModel.getNbColonnes(); colonne++) {
				int x = getCelluleX(ligne, colonne);
				int y = getCelluleY(ligne, colonne);
				int Case = demineurModel.getPlateau(ligne, colonne);
				g.setColor(Color.lightGray);
				g.fill3DRect(x, y, DX, DY, true);
				
				if(Case == -1){
					if(demineurModel.getDrapeau(ligne, colonne)){
						g.setColor(Color.blue);
						g.fill3DRect(x+2, y+2, DX-4, DY-4, true);
						g.setColor(Color.white);
						g.fill3DRect(x+15, y+15, DX-27, DY-35, true);
						g.fill3DRect(x+15, y+DY-20, DX-48, DY-40, true);
					}
					else{
						g.setColor(Color.gray);
						g.fill3DRect(x+2, y+2, DX-4, DY-4, true);
					}
				}
				else if(Case == 0){}
				else if(Case == 9){
					g.setColor(Color.red);
					g.fill3DRect(x+2, y+2, DX-4, DY-4, true);
					try {
					    Image img = ImageIO.read(new File("image-mine.png"));
					    g.drawImage(img, x+5, y+5, DX-10, DY-10, this);
					} 
					catch(IOException e) {
						g.setColor(Color.black);
						g.fillOval(x+10, y+10, DX-20, DY-20);
					}
				}
				else{
					g.setFont(new Font("Arial", Font.BOLD, 20));
					switch(Case) {
						case 1: g.setColor(Color.blue); break;
						case 2: g.setColor(Color.green); break;
						case 3: g.setColor(Color.red); break;
						default: g.setColor(Color.black); break;
					}
					g.drawString(String.valueOf(Case), x-10+(DX/2), y+10+(DY/2));
				}
			}
		fin(g);
		texte(g);
		boutons(g);
		timer(g);
		if(demineurModel.getEtat() == 1){ // Actualisation toutes les 200ms tant que ni perdu ni gagne
			try {Thread.sleep(150);} catch (InterruptedException e){}
			repaint();
		}
	}
	
	public void texte(Graphics g){
		g.setFont(new Font("Georgia", Font.BOLD, 13));
		g.setColor(Color.yellow);
		g.drawString("Total: ", 5, 520);
		g.drawString("Restantes: ", 5, 540);
		if(demineurModel.getEtat() == 1){ // Si ni perdu ni gagne
			g.setFont(new Font("Georgia", Font.BOLD, 20));
			g.drawString("Risque: ", 180, 535);
			g.setFont(new Font("Georgia", Font.BOLD, 13));
			g.setColor(Color.yellow);
			g.drawString("Cases", 390, 520);
			g.drawString("à cliquer: ", 380, 540);
		}
		g.setColor(Color.white);
		g.drawString(""+demineurModel.getNbBombes(), 53, 520);
		g.drawString(""+(demineurModel.getNbBombes()-demineurModel.getNbDrapeaux()), 83, 540);
		double rate = (demineurModel.getNbBombes()-demineurModel.getNbDrapeaux()) * 1.0 /
				(demineurModel.getNbClicsRestants()+demineurModel.getNbBombes()-demineurModel.getNbDrapeaux());
		if(demineurModel.getEtat() == 1){ // Si ni perdu ni gagne
			g.drawString(""+demineurModel.getNbClicsRestants(), 450, 540);
			g.setFont(new Font("Georgia", Font.BOLD, 20));
			if((int)Math.round(rate*100) > 49) {g.setColor(Color.red);}
			g.drawString((int)Math.round(rate*100)+"%", 270, 535);
		}
	}
	
	public void fin(Graphics g){
		for (int ligne = 0; ligne < demineurModel.getNbLignes(); ligne++)
			for (int colonne = 0; colonne < demineurModel.getNbColonnes(); colonne++)
				if(demineurModel.getPlateau(ligne, colonne) == 9){
					g.setFont(new Font("Georgia", Font.BOLD, 35));
					g.setColor(Color.red);
					g.drawString("PERDU !", 180, 545);
					demineurModel.etatLose();
				}
		if(demineurModel.gagner()){
			g.setFont(new Font("Georgia", Font.BOLD, 35));
			g.setColor(Color.green);
			g.drawString("GAGNE !", 180, 545);
			demineurModel.etatWin();
			if(demineurModel.getEtat() == 2)
				score(g);
		}
	}
	
	public void boutons(Graphics g){
		g.setColor(Color.gray);
		g.fill3DRect(0, 550, DX*3, DY, true);
		g.fill3DRect(350, 550, DX*3, DY, true);
		g.setColor(Color.yellow);
		g.setFont(new Font("Georgia", Font.PLAIN, 20));
		g.drawString("Rejouer", 35, 580);
		g.drawString("Quitter", 395, 580);
	}
	
	public void timer(Graphics g){
		if(demineurModel.getNbClicsRestants() == demineurModel.getNbLignes()*demineurModel.getNbColonnes()-demineurModel.getNbBombes())
			init = java.lang.System.currentTimeMillis()/1000;
		long temps = java.lang.System.currentTimeMillis()/1000 - init;
		String duree = ""+(temps/60>9?"":'0')+temps/60+":"+(temps%60>9?"":'0')+temps%60;
		g.setFont(new Font("Georgia", Font.BOLD, 20));
		g.setColor(Color.yellow);
		g.drawString("Time :", 180, 580);
		if(demineurModel.getEtat() != 1) {g.setColor(Color.CYAN);}
		g.drawString(duree, 250, 580);
	}
	
	public void score(Graphics g){
		if(demineurModel.getEtat() == 2){ // Si gagne
			long score = bestScore(java.lang.System.currentTimeMillis()/1000 - init);
			g.setFont(new Font("Georgia", Font.BOLD, 13));
			g.setColor(Color.orange);
			g.drawString("Meilleur Score:", 380, 520);
			String duree = ""+(score/60>9?"":'0')+score/60+":"+(score%60>9?"":'0')+score%60;
			g.setColor(Color.white);
			g.drawString(duree, 410, 540);
			if(score == java.lang.System.currentTimeMillis()/1000 - init){
				g.setColor(Color.red);
				g.drawString("NEW!", 365, 540);
				g.drawString("NEW!", 460, 540);
			}			
		}
	}
	
	public static long bestScore(long score){
		File f = new File("scoreshistory.txt");
		long bestScore = 99999;
		try{
			PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			file.println(score);
			file.close();
		}catch(IOException e){e.printStackTrace();}
		try{
			BufferedReader file = new BufferedReader(new FileReader(f));
		    try{
		    	String line = file.readLine();
		    	do{
		    		if(Long.valueOf(line) < bestScore)
		    			bestScore = Long.valueOf(line);
		    		line = file.readLine();
		        }while (line != null);
		    }catch(IOException e){e.printStackTrace();}
		    file.close();
		}catch(IOException e){e.printStackTrace();}
		return bestScore;
	}
	
	public int getLigne(int x, int y) {
		return y / DY;
	}

	public int getColonne(int x, int y) {
		return x / DX;
	}

	public int getCelluleX(int ligne, int colonne) {
		return colonne * DX;
	}

	public int getCelluleY(int ligne, int colonne) {
		return ligne * DY;
	}

}
