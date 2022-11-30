package modele.plateau;

/**
 *
 * @author jhomi
 */
public class Bombe extends EntiteStatique{
    public Bombe(Jeu _jeu) { super(_jeu); }
    
    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
