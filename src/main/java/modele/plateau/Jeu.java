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

    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);
    
    private Integer cmptBombes = 0;
    
    public IA ia;
    
    boolean running = true;
    
    public boolean isRunning() {return running;}
            
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
        addEntite(hector, 2, 2);
        
        Rabano r = new Rabano(this);
        //addEntite(r,3,5);

        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        ordonnanceur.add(g);

        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());
        
        cmptBombes = 3;
        Bot gustavo = new Bot(this);
        addEntite(gustavo,5,8);
        ia = new IA();
        ia.addEntiteDynamique(gustavo);
        
        Bot gustavo1 = new Bot(this);
        addEntite(gustavo1,20,8);
        ia.addEntiteDynamique(gustavo1);
        ordonnanceur.add(ia);
        
        
        for (int y =3; y > 0; y--) {
            Colonne e = new Colonne(this);
            addEntite(e, 10, y);
            CColonne.getInstance().addEntiteDynamique(e);
        }
        CColonne.getInstance().addCol(3);
        addEntite(new Mur(this), 10, 6);
        
        addEntite(new Bombe(this), 26, 2);
        addEntite(new Bombe(this), 2, 7);
        addEntite(new Bombe(this), 28, 7);
        for (int y =6; y > 2; y--) {
            Colonne e = new Colonne(this);
            addEntite(e, 26, y);
            CColonne.getInstance().addEntiteDynamique(e);
        }
        CColonne.getInstance().addCol(4);
        
        ordonnanceur.add( CColonne.getInstance());
        //CoRDE
        for (int y = 8; y > 3; y--) {
            addEntite(new Corde(this), 15, y );
        }
        addEntite(new Mur(this), 16, 8);
        //addEntite(new Corde(this),3,8);
       
        
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
        
        for (int xx =1 ;xx < 10;xx++){
            addEntite(new Mur(this), xx, 3);
        }
        
        for (int xx =12 ;xx < 15;xx++){
            addEntite(new Mur(this), xx, 5);
        }
        
        for (int xx =16 ;xx < 26;xx++){
            addEntite(new Mur(this), xx, 5);
        }
        for (int xx =27 ;xx < 30;xx++){
            addEntite(new Mur(this), xx, 5);
        }
        /*
        addEntite(new Mur(this), 2, 6);
        addEntite(new Bombe(this), 4, 7);
        addEntite(new Bombe(this), 1, 7);
        cmptBombes = 2;
        addEntite(new Mur(this), 3, 6);
        
        
        addEntite(new Mur(this), 4, SIZE_Y-2);*/
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
                if(objetALaPosition(pCible) instanceof Corde ||
                   objetALaPosition(pCible) instanceof Rabano){//Choca con la cuerda
                    if (cmptDeplV.get(e) == null) {
                            cmptDeplV.put(e, 1);
                            SuperEntite pp = new SuperEntite (this);
                            map.put(e,pCible);
                            pp.setStaticEnt(objetALaPosition(pCible));
                            pp.setDynaEnt(e);
                            e = pp;
                            retour = true;
                        }
                }
                else if(objetALaPosition(pCible) instanceof Rabano){
                    ((Bot)e).eatRabano();
                    retour = true;
                }
                else if(objetALaPosition(pCible) instanceof Bombe){ //Choca con bomba
                    if (cmptDeplV.get(e) == null) {
                            cmptDeplV.put(e, 1);
                            cmptBombes ++;
                            retour = true;
                            if(cmptBombes == 0) System.out.println("Victory!!!!!");
                        }
                }
                break;
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
                if(objetALaPosition(pCible) instanceof Corde ||
                   (objetALaPosition(pCible) instanceof Rabano &&
                        e instanceof Heros)){//Choca con la cuerda
                    if (cmptDeplH.get(e) == null) {
                            cmptDeplH.put(e, 1);
                            SuperEntite pp = new SuperEntite (this);
                            map.put(e,pCible);
                            pp.setStaticEnt(objetALaPosition(pCible));
                            pp.setDynaEnt(e);
                            e = pp;
                            retour = true;
                        }
                }
                else if(objetALaPosition(pCible) instanceof Rabano){
                    ((Bot)e).eatRabano();
                    retour = true;
                }
                else if(objetALaPosition(pCible) instanceof Bombe){ //Choca con bomba
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        cmptBombes ++;
                        retour = true;
                        if(cmptBombes == 0) System.out.println("Victory!!!!!");
                    }
                } 
                break;
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
        if(e instanceof Heros){
            //tratamiento para gameOver
            grilleEntites[p.x][p.y] = null;
            Controle4Directions.getInstance().removeEntiteDynamique(hector);
            System.out.println("last chance to look at me, Hector");
            running = false;
        }
        else{
            grilleEntites[p.x][p.y] = null;
            ia.removeEntiteDynamique((EntiteDynamique)e);
        }
    }
    
    public void reset() {
        running = true;
        ordonnanceur.reset();
        map.clear();
        cmptDeplH.clear();
        cmptDeplV.clear();
        grilleEntites = new Entite[SIZE_X][SIZE_Y];
        initialisationDesEntites();
    }
     
    public void tryRabano(){
        if(!hector.hasRabano()){
            Point p = map.get(hector);
            if(objetALaPosition(p) instanceof SuperEntite){
                SuperEntite spo = (SuperEntite)objetALaPosition(p);
                if(spo.staticEnt instanceof Rabano){
                    hector.getRabano();
                    map.remove(spo.staticEnt);
                    map.remove(spo);
                    grilleEntites[p.x][p.y] = hector;
                }
            }
        }
        else{
            Point p = map.get(hector);
            if(objetALaPosition(p) instanceof SuperEntite){
                System.out.println("invalid Rabano position");
            }
            else{
                Rabano rab = new Rabano(this);
                map.put(rab, p);
                SuperEntite sp = new SuperEntite(this);
                sp.staticEnt = rab;
                sp.dynaEnt = hector;
                map.put(sp, p);
                grilleEntites[p.x][p.y] = sp;
                hector.loseRabano();
            }
        }
    }
}
