package VueControleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.Controle4Directions;
import modele.deplacements.CColonne;

import modele.deplacements.Direction;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private AlphaIcon icoHero;
    
    private AlphaIcon icoHeroM;
    private AlphaIcon icoBot;
    private AlphaIcon icoVide;
    private AlphaIcon icoMur;
    private AlphaIcon icoMur2;
    private AlphaIcon icoPlataform;
    private AlphaIcon icoColonne;
    private AlphaIcon icoCorde;
    private AlphaIcon icoHeroCorde;
    private AlphaIcon icoBotCorde;
    private AlphaIcon icoBombe;
    private ImageIcon icoTest;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    
    private Integer typeHero = 0;

    public VueControleurGyromite(Jeu _jeu) {
        sizeX = _jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }
    
    public void reset(){
        for(JLabel[] E : tabJLabel){
            for(JLabel e : E){
                e = null;
            }
        }
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); 
                                            typeHero = 1;
                                            break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite);
                                            typeHero = 1;
                                            break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_R : CColonne.getInstance().triggerColonne(); break;
                    case KeyEvent.VK_F : jeu.reset(); reset(); break;
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                typeHero = 0;
                switch(e.getKeyCode()){
                    case KeyEvent.VK_R : CColonne.getInstance().resetColonne(); break;
                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero = new AlphaIcon(chargerIcone("Images/player_ca.png", 0, 0, 35, 40),1.0f);
        icoHeroM = new AlphaIcon(chargerIcone("Images/player_ca.png", 67, 0, 30, 45),1.0f);
        icoBot = new AlphaIcon(chargerIcone("Images/smick_ca.png", 35, 3, 26, 28),1.0f);
        
        icoTest = chargerIcone("Images/Vide.png");
        icoVide = new AlphaIcon(chargerIcone("Images/Mur.png"),1.0f);
        icoColonne = new AlphaIcon(chargerIcone("Images/Colonne.png"),1.0f);
        icoMur = new AlphaIcon(chargerIcone("Images/tileset.png",32,1,16,16),1.0f);
        icoMur2 = new AlphaIcon(chargerIcone("Images/tileset.png",0,16,16,16),1.0f);
        icoCorde = new AlphaIcon(chargerIcone("Images/tileset.png",20,2,10,14),1.0f);
        icoHeroCorde = new AlphaIcon(chargerIcone("Images/player_ca.png", 0, 88, 35, 40),1.0f);
        icoBotCorde = new AlphaIcon(chargerIcone("Images/smick_ca.png", 96, 66, 31, 30),1.0f);
        icoBombe = new AlphaIcon(chargerIcone("Images/bomb_ca.png",83,0,26,48),1.0f);
        icoPlataform = new AlphaIcon(chargerIcone("Images/tileset.png",0,1,16,16),1.0f);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();

                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

  
    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue     
                   if(typeHero == 1){ //Movimiento 
                       icoHeroM.paintIcon(null,tabJLabel[x][y].getGraphics(),0,0);
                   }else {
                       icoHero.paintIcon(null,tabJLabel[x][y].getGraphics(),0,0);
                   }
                    
                } else if (jeu.getGrille()[x][y] instanceof Bot) {
                    icoBot.paintIcon(null,tabJLabel[x][y].getGraphics(),0,0);
                } else if (jeu.getGrille()[x][y] instanceof Mur) {
                    if(x > 0 && x < sizeX-1 && y > 0 && y < sizeY-1){
                        tabJLabel[x][y].setIcon(icoPlataform);
                    } else if (x == 0 || x ==sizeX-1) {
                        tabJLabel[x][y].setIcon(icoMur2);
                    } else{
                        tabJLabel[x][y].setIcon(icoMur);
                    }
                } else if (jeu.getGrille()[x][y] instanceof Colonne) {
                    tabJLabel[x][y].setIcon(icoColonne);
                } else if (jeu.getGrille()[x][y] instanceof Corde){
                    icoCorde.paintIcon(null,tabJLabel[x][y].getGraphics(),0,0);
                } else if (jeu.getGrille()[x][y] instanceof Bombe){
                    tabJLabel[x][y].setIcon(icoVide);
                    icoBombe.paintIcon(null, tabJLabel[x][y].getGraphics(), 0, 0);
                } else if (jeu.getGrille()[x][y] instanceof SuperEntite){
                    SuperEntite spo = (SuperEntite)jeu.getGrille()[x][y];
                    if(spo.getStaticEnt() instanceof Corde && spo.getDynaEnt() instanceof Heros){
                        icoHeroCorde.paintIcon(null, tabJLabel[x][y].getGraphics(), 0, 0);
                    } else if(spo.getStaticEnt() instanceof Corde && spo.getDynaEnt() instanceof Bot){
                        icoBotCorde.paintIcon(null,tabJLabel[x][y].getGraphics(),0,0);
                    }
                    
                } else {
                    tabJLabel[x][y].setIcon(icoVide);
                    icoVide.paintIcon(null,tabJLabel[x][y].getGraphics(),0,0);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }


    // chargement de l'image entière comme icone
    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


        return new ImageIcon(image);
    }

    // chargement d'une sous partie de l'image
    private ImageIcon chargerIcone(String urlIcone, int x, int y, int w, int h) {
        // charger une sous partie de l'image à partir de ses coordonnées dans urlIcone
        BufferedImage bi = getSubImage(urlIcone, x, y, w, h);
        // adapter la taille de l'image a la taille du composant (ici : 20x20)
        return new ImageIcon(bi.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    }
    
    private BufferedImage getSubImage(String urlIcone, int x, int y, int w, int h) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        BufferedImage bi = image.getSubimage(x, y, w, h);
        return bi;
    }

}
