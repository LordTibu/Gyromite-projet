package modele.plateau;
public class SuperEntite extends EntiteDynamique{
    protected Entite staticEnt = null;
    protected Entite dynaEnt = null;
    
    public SuperEntite(Jeu _jeu) { super(_jeu); }
    
    public boolean peutEtreEcrase() { 
        return getStaticEnt().peutEtreEcrase()&&getDynaEnt().peutEtreEcrase() ; 
    }
    public boolean peutServirDeSupport() { 
        return getStaticEnt().peutServirDeSupport()||getDynaEnt().peutServirDeSupport() ;
    }
    public boolean peutPermettreDeMonterDescendre() { 
        return getStaticEnt().peutPermettreDeMonterDescendre()||getDynaEnt().peutPermettreDeMonterDescendre() ; 
    };
   
    
    public void couple(){
        
    }
    
    public void decouple(){
        
    }

    /**
     * @return the staticEnt
     */
    public Entite getStaticEnt() {
        return staticEnt;
    }

    /**
     * @param staticEnt the staticEnt to set
     */
    public void setStaticEnt(Entite staticEnt) {
        this.staticEnt = staticEnt;
    }

    /**
     * @return the dynaEnt
     */
    public Entite getDynaEnt() {
        return dynaEnt;
    }

    /**
     * @param dynaEnt the dynaEnt to set
     */
    public void setDynaEnt(Entite dynaEnt) {
        this.dynaEnt = dynaEnt;
    }
}
