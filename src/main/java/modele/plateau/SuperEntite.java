/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele.plateau;

/**
 *
 * @author jhomi
 */
public class SuperEntite extends EntiteDynamique{
    EntiteStatique staticEnt = null;
    EntiteDynamique dynaEnt = null;
    
    public SuperEntite(Jeu _jeu) { super(_jeu); }
    
    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
    
    public void couple(){
        
    }
    
    public void decouple(){
        
    }
}
