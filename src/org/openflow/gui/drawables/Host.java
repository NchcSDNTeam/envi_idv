package org.openflow.gui.drawables;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.openflow.gui.net.protocol.NodeType;
import org.openflow.nchc.wunyuan.LinkData;
import org.openflow.util.string.DPIDUtil;
import org.openflow.util.string.IPUtil;
import org.pzgui.icon.ShapeIcon;

/**
 * Describes a host.
 * 
 * @author David Underhill
 */
public class Host extends NodeWithPorts {
    public Host(long ip) {
        this("", 0, 0, ip);
    }
    
    public Host(String name, long ip) {
        this(name, 0, 0, ip);
    }
    
    public Host(String name, int x, int y, long ip) {
        super(NodeType.HOST, name, x, y, newDefaultHostShape());
        this.ip = ip;
    }
    
    /** creates a new Host icon */
    public static final ShapeIcon newDefaultHostShape() {
        return new ShapeIcon(DEFAULT_SHAPE, DEFAULT_FILL);
    }
    
    
    // ------------------- Drawing ------------------ //
    
    //add by wunyuan
    public boolean showInformation = false;
    
    
    /** default size of the DEFAULT_SHAPE */
    public static final int DEFAULT_SIZE = 30;
    
    /** default shape used to represent a switch */
    public static final Shape DEFAULT_SHAPE = new Rectangle2D.Double(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);
    
    /** default fill color for DEFAULT_SHAPE */
    public static final Paint DEFAULT_FILL = new Color(240, 240, 240);
    
    
    
    public void drawObject(Graphics2D gfx) {
    	
        super.drawObject(gfx);    
        showNode = false;
        if(showInformation || LinkData.showAllIslandHost)
        {
        int x = getX() - getIcon().getWidth() / 2;
        int y = getY() - getIcon().getHeight() / 2;

            y += gfx.getFontMetrics().getHeight();
           
        // display switch description stats on mouse over
       // if(this.isHovered() || this.isSelected()) {
        	
            Font f = new Font("TimesRoman",Font.BOLD,12);
            gfx.setFont(f);
            
            gfx.drawString(DPIDUtil.dpidToHex(getID()), x-15, y-5);
            y += gfx.getFontMetrics().getHeight();
            gfx.drawString(getName(), x-15, y-6);
            y += gfx.getFontMetrics().getHeight();
            

            //add by wunyuan
   //         gfx.drawString(getControllerIp(), x, y);
 //           y += gfx.getFontMetrics().getHeight();
            //end
            
      //  }
        }
    }
    
    
    
    
 
    // --------------------- ID --------------------- //
    
    /** IP address of this host */
    private long ip;
    
    /** returns a string version of the switch's datapath ID */
    public String getDebugName() {
    	
        return IPUtil.toString((int)getID());
    }
    
    /** gets the IP address of this host */
    public long getID() {
        return ip;
    }
    
    /** sets the IP address of this host */
    public void setID(long ip) {
        this.ip = ip;
    }
    
    
    // -------------------- Other ------------------- //
    
    public String toString() {
        return getName() + "; ip=" + IPUtil.toString((int)getID());
    }
}
