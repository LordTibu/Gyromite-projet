package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

import java.awt.Point;
import java.util.ArrayList;

public class CCorde extends RealisateurDeDeplacement{
    protected ArrayList<Integer> tailleC = new ArrayList<>();
    private static CCorde corde;
    private boolean TouchUp = false;
    
    public static CCorde getInstance() {
        if (corde == null) {
            corde = new CCorde();
        }
        return corde;
    }
    /*
    @Override
    protected boolean realiserDeplacement() {
        boolean ret = false;
        
        if(TouchUp){
            System.out.println("Dr going up");
            for (EntiteDynamique e : lstEntitesDynamiques) {
                Entite eBas = e.regarderDansLaDirection(Direction.haut);
                if (eBas == null || (eBas != null && !eBas.peutServirDeSupport())) {
                    if (e.avancerDirectionChoisie(Direction.haut))
                        ret = true;
                    }
            }
        }
        
        return ret;
    }
    */
    public void CordeUp(){
        TouchUp = true;
    }
    
    public void NoCordeUp(){
        TouchUp = false;
    }
}
