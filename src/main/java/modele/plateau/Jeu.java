/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.deplacements.Gravite;
import modele.deplacements.Ordonnanceur;
import modele.deplacements.CColonne;
import modele.deplacements.IA;

import java.awt.Point;
import java.util.HashMap;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHector() {
        return hector;
    }
    
    private void initialisationDesEntites() {
        hector = new Heros(this);
        addEntite(hector, 2, 1);

        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        ordonnanceur.add(g);

        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());
        
        
        Bot gustavo = new Bot(this);
        addEntite(gustavo,5,8);
        IA ia = new IA();
        ia.addEntiteDynamique(gustavo);
        ordonnanceur.add(ia);
        
        
        for (int y = 6; y > 3; y--) {
            Colonne e = new Colonne(this);
            addEntite(e, 10, y);
            CColonne.getInstance().addEntiteDynamique(e);
        }
        CColonne.getInstance().addCol(3);
        ordonnanceur.add( CColonne.getInstance());
        
        //CoRDE
        for (int y = 8; y > 5; y--) {
            addEntite(new Corde(this), 13, y );
        }

       
        /*for (int x = 5; x < 12; x++) {
            addEntite(new Mur(this), x, 7);
        }*/
        
        // murs extérieurs horizontaux
        for (int x = 0; x < SIZE_X; x++) {
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, SIZE_Y-1);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < SIZE_Y-1; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), SIZE_X-1, y);
        }

        addEntite(new Mur(this), 2, 6);
        addEntite(new Mur(this), 3, 6);
        addEntite(new Mur(this), 14, 8);
        addEntite(new Mur(this), 14, 7);
        addEntite(new Mur(this), 4, SIZE_Y-2);
    }

    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        Point pCourant = map.get(e);
       
        Point pCible = calculerPointCible(pCourant, d);
         
        
        switch(d){
            case bas:
            case haut:
                //S'il y a pas d'objet dans la direction de l'entite e, e peut avancer 
                if (contenuDansGrille(pCible) && objetALaPosition(pCible) == null ) {
                    if (cmptDeplV.get(e) == null) {
                                cmptDeplV.put(e, 1);

                                retour = true;
                            }
                            break;
                }else if(objetALaPosition(pCible) != null){ //Choca con algo
                    if(objetALaPosition(pCible) instanceof Corde){//Choca con la cuerda
                        System.out.println("obstaculo cuerda");
                        if (cmptDeplV.get(e) == null) {
                                cmptDeplV.put(e, 1);
                                SuperEntite pp = new SuperEntite (this);
                                map.put(e,pCible);
                                pp.setStaticEnt(objetALaPosition(pCible));
                                pp.setDynaEnt(e);
                                e = pp;
                                retour = true;
                            }
                            break;
                    }
                }
            case gauche:
            case droite:
                //S'il y a pas d'objet dans la direction de l'entite e, e peut avancer 
                if (contenuDansGrille(pCible) && objetALaPosition(pCible) == null ) {
                    if (cmptDeplH.get(e) == null) {
                            cmptDeplH.put(e, 1);

                            retour = true;

                        }
                        break;
                }else if(objetALaPosition(pCible) != null){ //Choca con algo
                    if(objetALaPosition(pCible) instanceof Corde){//Choca con la cuerda
                        System.out.println("obstaculo cuerda");
                        if (cmptDeplH.get(e) == null) {
                                cmptDeplH.put(e, 1);
                                SuperEntite pp = new SuperEntite (this);
                                map.put(e,pCible);
                                pp.setStaticEnt(objetALaPosition(pCible));
                                pp.setDynaEnt(e);
                                e = pp;
                                retour = true;

                            }
                            break;
                    }
                }
        
    }
        
        if (retour) {
            deplacerEntite(pCourant, pCible, e);
        }

        return retour;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        if(objetALaPosition(pCourant) instanceof SuperEntite){
            SuperEntite spo = (SuperEntite)objetALaPosition(pCourant);
            grilleEntites[pCourant.x][pCourant.y] = spo.getStaticEnt();
            spo = null;
            
            System.out.println("se esta detectando");
        }else {
            grilleEntites[pCourant.x][pCourant.y] = null;
        }
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
    
    public void matar(Entite e){
        Point p = map.get(e);
        //System.out.println("El punto es: " + p.x + " " + p.y );
        if(e instanceof Heros){
            //tratamiento para gameOver
            grilleEntites[p.x][p.y] = null;
        }
        else{
            grilleEntites[p.x][p.y] = null;
        }
    }
}
