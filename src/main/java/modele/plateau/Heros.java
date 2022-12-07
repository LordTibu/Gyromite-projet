/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

/**
 * Héros du jeu
 */
public class Heros extends EntiteDynamique {
    protected boolean rabano = false;
    
    public Heros(Jeu _jeu) {
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
    public boolean hasRabano() {return rabano;}
    public void getRabano(){rabano = true;}
    public void loseRabano(){rabano = false;}
}
