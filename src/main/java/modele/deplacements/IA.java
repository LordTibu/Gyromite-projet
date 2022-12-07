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
        //On calcule les directions valides depuis la position courante du bot
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
                if(tmp != null && tmp.peutServirDeSupport()){
                    if(tmp.regarderDansLaDirection(d) != null && tmp.regarderDansLaDirection(d).peutServirDeSupport()) return true; //Chemin Valide
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
        return false;
        //On calcule si d est une direction valide à prendre depuis la position actuelle du bot
    }
    
    @Override
    protected boolean realiserDeplacement() {
        int rand_int = 0;
        ArrayList<Direction> tabDir = null;
        int r = 0;
        for(int i = 0; i < lstEntitesDynamiques.size(); i++){
            if(lstEntitesDynamiques.get(i).regarderDansLaDirection(directionCourante.get(i)) instanceof Heros){
                lstEntitesDynamiques.get(i).regarderDansLaDirection(directionCourante.get(i)).matar();
                //Si l'entite dans le chemin du bot est l'heros du coup on le tue
                //Je sais que c'est horrible à lire mais c'est encore plus horrible à ecrire
            }
            if(isValidDir(lstEntitesDynamiques.get(i), directionCourante.get(i))){
                return lstEntitesDynamiques.get(i).avancerDirectionChoisie(directionCourante.get(i));
                //Si la direction courante du bot est valide (pas d'obstacle etc..) il pursuit son chemin
            }
            else{
                tabDir = getAvailableDir(lstEntitesDynamiques.get(i));
                if(tabDir.isEmpty()) return false;
                rand_int = getRandInt(tabDir.size());
                directionCourante.set(i, tabDir.get(rand_int));
                return lstEntitesDynamiques.get(i).avancerDirectionChoisie(tabDir.get(rand_int));
                //Sinon on calcule les directions possibles depuis sa position courante et on choisi une aleatoirement
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
    
    @Override
    public void reset(){
        lstEntitesDynamiques.clear();
        directionCourante.clear();
    }
}
