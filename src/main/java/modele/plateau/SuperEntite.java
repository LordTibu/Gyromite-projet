package modele.plateau;
public class SuperEntite extends EntiteDynamique{
    protected Entite staticEnt = null;
    protected Entite dynaEnt = null;
    
    public SuperEntite(Jeu _jeu) { super(_jeu); }
    
    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
   
    
    public void couple(){
        
    }
    
    public void decouple(){
        
    }
}
