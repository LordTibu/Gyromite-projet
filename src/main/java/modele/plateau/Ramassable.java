package modele.plateau;

/**
 *
 * @author jhomi
 */
public abstract class Ramassable extends EntiteStatique{
    public Ramassable(Jeu _jeu) { super(_jeu); }
    
    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
    
    public abstract void ramasser();
}
