package modele.plateau;

public class Corde extends EntiteStatique{
    public Corde(Jeu _jeu) { super(_jeu); }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return true; };
}
