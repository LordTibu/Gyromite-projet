package modele.plateau;

import modele.deplacements.Direction;

public abstract class Entite {
    protected Jeu jeu;
    
    public Entite(Jeu _jeu) {
        jeu = _jeu;
    }
    
    public Entite regarderDansLaDirection(Direction d) {return jeu.regarderDansLaDirection(this, d);}
    
    public abstract boolean peutEtreEcrase(); // l'entité peut être écrasée (par exemple par une colonne ...)
    public abstract boolean peutServirDeSupport(); // permet de stopper la gravité, prendre appui pour sauter
    public abstract boolean peutPermettreDeMonterDescendre(); // si utilisation de corde (attention, l'environnement ne peut pour l'instant sotker qu'une entité par case (si corde : 2 nécessaires), améliorations à prévoir)
    
    public void matar(){ jeu.matar(this); }
}
