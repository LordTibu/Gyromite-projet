package VueControleur;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class AlphaIcon implements Icon{
   private Icon icon;
   private float alpha;
   
   
   public AlphaIcon(Icon icon, float alpha){
       this.icon = icon;
       this.alpha = alpha;
   }
   
   public float getAlpha(){
       return alpha;
   }
   
   public Icon getIcon(){
       return icon;
   }
   
   @Override
   public void paintIcon(Component c, Graphics g, int x, int y){
       Graphics2D g2 = (Graphics2D) g.create();
       g2.setComposite(AlphaComposite.SrcAtop.derive(alpha));
       icon.paintIcon(c, g2, x, y);
       g2.dispose();
   }
   
   @Override 
    public int getIconWidth() {
    return icon.getIconWidth();
    }
    
    @Override
    public int getIconHeight() {
    return icon.getIconHeight();
    }
}
