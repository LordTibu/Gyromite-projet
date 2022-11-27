package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

import java.awt.Point;
import java.util.ArrayList;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class CColonne extends RealisateurDeDeplacement {
    private boolean triggeredDown = false; //Colonnes vont vers le bas quand activées
    protected ArrayList<Integer> colTaille = new ArrayList<>(); //Contient la taille de chaque colonne dans le realisateur
    protected ArrayList<Integer> tilesMoved = new ArrayList<>();
    private static CColonne col3d;
    
    public static CColonne getInstance() {
        if (col3d == null) {
            col3d = new CColonne();
        }
        return col3d;
    }
    
    @Override
    protected boolean realiserDeplacement() {
        boolean ret = false;
        Integer sommeTailles = 0;
        
        if(triggeredDown){
            for(Integer x = 0; x < colTaille.size(); x++){
                if(tilesMoved.get(x) > 0){
                    for(Integer y = sommeTailles; y < sommeTailles + colTaille.get(x); y++){
                        ret = lstEntitesDynamiques.get(y).avancerDirectionChoisie(Direction.bas);
                    }
                    sommeTailles += colTaille.get(x);
                    tilesMoved.set(x, tilesMoved.get(x) - 1);
                }
            }
        }
        else{
            for(Integer x = 0; x < colTaille.size(); x++){
                if(tilesMoved.get(x) < colTaille.get(x) - 1){
                    for(Integer y = sommeTailles + colTaille.get(x) - 1; y >= sommeTailles; y--){
                        ret = lstEntitesDynamiques.get(y).avancerDirectionChoisie(Direction.haut);
                    }
                    sommeTailles += colTaille.get(x);
                    tilesMoved.set(x, tilesMoved.get(x) + 1);
                }
            }
        }
        
        return ret;
    }
    
    public void addCol(int taille){
        colTaille.add(taille);
        tilesMoved.add(taille - 1);
    }
    
    public void triggerColonne(){
        triggeredDown = true;
    }
    
    public void resetColonne(){
        triggeredDown = false;
    }
}
