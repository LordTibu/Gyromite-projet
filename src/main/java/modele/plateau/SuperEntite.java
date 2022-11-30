package modele.plateau;
public class SuperEntite extends EntiteDynamique{
    protected Entite staticEnt = null;
    protected Entite dynaEnt = null;
    
    public SuperEntite(Jeu _jeu) { super(_jeu); }
    
    public boolean peutEtreEcrase() { 
        return staticEnt.peutEtreEcrase()&&dynaEnt.peutEtreEcrase() ; 
    }
    public boolean peutServirDeSupport() { 
        return staticEnt.peutServirDeSupport()||dynaEnt.peutServirDeSupport() ;
    }
    public boolean peutPermettreDeMonterDescendre() { 
        return staticEnt.peutPermettreDeMonterDescendre()||dynaEnt.peutPermettreDeMonterDescendre() ; 
    };
   
    
    public void couple(){
        
    }
    
    public void decouple(){
        
    }
}
