package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.Mur;
import modele.plateau.Bot;
import modele.plateau.Heros;
import modele.plateau.Colonne;
import modele.plateau.Bot;
import modele.plateau.EntiteDynamique;
import java.util.ArrayList;
import java.util.Random;

public class IA extends RealisateurDeDeplacement {
    private Random r = new Random();
    protected ArrayList<Direction> directionCourante = new ArrayList<>();
    
    protected ArrayList<Direction> getAvailableDir(EntiteDynamique e){
        ArrayList<Direction> dir = new ArrayList<>();
        if(isValidDir(e, Direction.droite)) dir.add(Direction.droite);
        if(isValidDir(e, Direction.gauche)) dir.add(Direction.gauche);
        if(isValidDir(e, Direction.haut)) dir.add(Direction.haut);
        if(isValidDir(e, Direction.bas)) dir.add(Direction.bas);
        return dir;
    }
    
    protected boolean isValidDir(EntiteDynamique e, Direction d){
        Entite tmp;
        switch(d){
            case droite:
            case gauche:
                tmp = e.regarderDansLaDirection(d);
                if(tmp != null){
                    if(tmp instanceof Mur || tmp instanceof Bot) return false;
                } //Colission avec Mur
                tmp = e.regarderDansLaDirection(Direction.bas);
                if(tmp instanceof Mur){
                    if(tmp.regarderDansLaDirection(d) instanceof Mur) return true; //Chemin Valide
                }
                break;
            case haut:
            case bas:
                if(e.regarderDansLaDirection(d) != null){ 
                    if(e.regarderDansLaDirection(d).peutPermettreDeMonterDescendre())
                    return true;
                }
                break;
        }
        System.out.println("Unvalid Pathing");
        return false;
    }
    
    @Override
    protected boolean realiserDeplacement() {
        int rand_int = 0;
        ArrayList<Direction> tabDir = null;
        int r = 0;
        for(int i = 0; i < lstEntitesDynamiques.size(); i++){
            if(isValidDir(lstEntitesDynamiques.get(i), directionCourante.get(i))){
                return lstEntitesDynamiques.get(i).avancerDirectionChoisie(directionCourante.get(i));
            }
            else{
                tabDir = getAvailableDir(lstEntitesDynamiques.get(i));
                if(tabDir.isEmpty()) return false;
                rand_int = getRandInt(tabDir.size());
                directionCourante.set(i, tabDir.get(rand_int));
                return lstEntitesDynamiques.get(i).avancerDirectionChoisie(tabDir.get(rand_int));
            }
        }
        return false; 
    }
    
    int getRandInt(int range){
        return r.nextInt(range);
    }
    
    @Override
    public void addEntiteDynamique(EntiteDynamique ed){
        lstEntitesDynamiques.add(ed);
        directionCourante.add(Direction.gauche);
    }
}
