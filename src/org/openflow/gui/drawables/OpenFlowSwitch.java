package org.openflow.gui.drawables;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;



//add by wunyuan
import java.util.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openflow.nchc.wunyuan.*;
import javax.imageio.ImageIO;
//end

import org.openflow.gui.net.protocol.NodeType;
import org.openflow.nchc.wunyuan.CtlType;
import org.openflow.protocol.SwitchDescriptionStats;
import org.openflow.util.string.DPIDUtil;
import org.pzgui.icon.ShapeIcon;

/**
 * Describes an OpenFlow switch.
 * 
 * @author David Underhill
 */
public class OpenFlowSwitch extends NodeWithPorts {
    public OpenFlowSwitch(long dpid) {
        this(dpid, NodeType.OPENFLOW_SWITCH);
    }
    
    public OpenFlowSwitch(long dpid, NodeType nt) {
        this("", 0, 0, dpid, nt);
    }
    
    public OpenFlowSwitch(String name, int x, int y, long dpid) {
        this(name, x, y, dpid, NodeType.OPENFLOW_SWITCH);
    }
    
    //add by wunyuan
    public OpenFlowSwitch(String name, int x, int y, long dpid, NodeType nt) {
        super(NodeType.OPENFLOW_SWITCH, name, x, y, newDefaultOpenFlowSwitchShape1(nt));                
        this.datapathID = dpid;
    }
    //end
    
    /*original
    public OpenFlowSwitch(String name, int x, int y, long dpid, NodeType nt) {
        super(NodeType.OPENFLOW_SWITCH, name, x, y, newDefaultOpenFlowSwitchShape1(nt));                
        this.datapathID = dpid;
    }
    */
    
    
    //add by wunyuan
    public OpenFlowSwitch(long dpid, NodeType nt, String ctrip) {
        this("", 0, 0, dpid, nt, ctrip);                
    }
    
    
    public OpenFlowSwitch(String name, int x, int y, long dpid, NodeType nt, String ctrip) {
        super(NodeType.OPENFLOW_SWITCH, name, x, y, newDefaultOpenFlowSwitchShape(nt, ctrip),ctrip);                
        this.datapathID = dpid;
        //this.controllerIP = ctrip;
    }
    //end
    
    
    
    /** creates a new OpenFlowSwitch icon */
    //add by wunyuan
    private static Stack<String> controllerIpList = new Stack<String>();
    private static Stack<String> colorList = new Stack<String>();
    private static int used=0;
    private static int colorIndex=0;
    private static String colorInteger;
    
    public static Paint chooseColor(int colorNumber)
    {
    	Paint fill = new Color(0, 0, 0);
    	switch(colorNumber)
    	{        
    	case 0:
    		fill=new Color(0, 0, 255);
    		colorInteger="0,0,255";
    		break;
    	case 1:
    		fill=new Color(0, 255, 0);
    		colorInteger="0,255,0";
    		break;
    	case 2:
    		fill=new Color(0, 255, 255);
    		colorInteger="0,255,255";
    		break;
    	case 3:
    		fill=new Color(255, 0, 0);
    		colorInteger="255,0,0";
    		break;
    	case 4:
    		fill=new Color(255, 0, 255);
    		colorInteger="255,0,255";
    		break;
    	case 5:
    		fill=new Color(255, 255, 0);
    		colorInteger="255,255,0";
    		break;
    	case 6:
    		fill=new Color(128, 128, 128);
    		colorInteger="128,128,128";
    		break;
    	case 7:
    		fill=new Color(192, 192, 192);
    		colorInteger="192,192,192";
    		break;
    	case 8:
    		fill=new Color(128, 128, 255);
    		colorInteger="128,128,255";
    		break;
    	case 9:
    		fill=new Color(128, 255, 128);
    		colorInteger="128,255,128";
    		break;
    	case 10:
    		fill=new Color(128, 255, 255);
    		colorInteger="128,255,255";
    		break;
    	case 11:
    		fill=new Color(255, 128, 128);
    		colorInteger="255,128,128";
    		break;
    	case 12:
    		fill=new Color(255, 128, 255);
    		colorInteger="255,128,255";
    		break;
    	case 13:
    		fill=new Color(255, 255, 128);
    		colorInteger="255,255,128";
    		break;
    	}
    	return fill;
    }
    
    public static boolean ipMatch(Stack ipList, String ctrip)
    {
    	boolean ipMatch = false;
    	for(int i=0; i < ipList.size(); i++)
    	{
    		if(ctrip.equals(ipList.get(i)))
    		{
    			ipMatch = true;
    			colorIndex=i;
    			break;
    		}
    	}
		return ipMatch;
    }
    
    public static boolean colorMatch(Stack colorlist, String colorstring)
    {
    	boolean colorMatch = false;
    	for(int i=0; i < colorlist.size(); i++)
    	{
    		if(colorstring.equals(colorlist.get(i)))
    		{
    			colorMatch = true;
    			break;
    		}
    	}
		return colorMatch;
    }
    
    public static final ShapeIcon newDefaultOpenFlowSwitchShape(NodeType nt, String ctrip) {
        Paint fill = new Color(10, 10, 10);
        Shape shape = DEFAULT_SHAPE;
        Random r= new Random();
        int color;
        boolean loopCheck=true;
        if(controllerIpList.empty())
        {
        	color = r.nextInt(14);
        	fill = chooseColor(color);
        	controllerIpList.push(ctrip);
        	colorList.push(colorInteger);
        	used++;
        }
        else
        {
        	if(ipMatch(controllerIpList,ctrip))
        	{
        		String colorTemp = colorList.get(colorIndex);
        		int firstComma = colorTemp.indexOf(",");
        		int lastComma = colorTemp.indexOf(",", firstComma+1);
        		String firstString=colorTemp.substring(0, firstComma);
        		String middleString=colorTemp.substring(firstComma+1,lastComma);
        		String lastString=colorTemp.substring(lastComma+1);
        		fill = new Color(Integer.parseInt(firstString),Integer.parseInt(middleString),Integer.parseInt(lastString));
        	}
        	else
        	{
        		loopCheck=true;
        		while(loopCheck)
        		{
        			color = r.nextInt(14);
            		fill = chooseColor(color);
            		if(used >= 14)
            		{
            			controllerIpList.push(ctrip);
            			colorList.push(colorInteger);
            			loopCheck=false;
            		}
            		else
            		{
            			if(colorMatch(colorList,colorInteger))
            			{
            				loopCheck=true;
            			}
            			else
            			{
                			controllerIpList.push(ctrip);
                			colorList.push(colorInteger);
                			used++;
                			loopCheck=false;
            			}
            		}
        		}
        	}
        }
        
        if(nt == NodeType.OPENFLOW_WIRELESS_ACCESS_POINT)
        {
            shape =  new Rectangle2D.Double(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);
        }
        
       // ShapeIcon shi = new ShapeIcon(shape, fill);
		return new ShapeIcon(shape, fill);
    }
    
    public static final ShapeIcon newDefaultOpenFlowSwitchShape1(NodeType nt) {
        Paint fill;
        
        if(nt == NodeType.OPENFLOW_WIRELESS_ACCESS_POINT)
            fill = DEFAULT_FILL_WIFI;
        else
            fill = DEFAULT_FILL;
        
        return new ShapeIcon(DEFAULT_SHAPE, fill);
    }
    //end
    
    /*original
    public static final ShapeIcon newDefaultOpenFlowSwitchShape(NodeType nt) {
        Paint fill;
        if(nt == NodeType.OPENFLOW_WIRELESS_ACCESS_POINT)
            fill = DEFAULT_FILL_WIFI;
        else
            fill = DEFAULT_FILL;
        
        return new ShapeIcon(DEFAULT_SHAPE, fill);
    }
    */
    
    
    // ------------------- Drawing ------------------ //
    
    /** default size of the DEFAULT_SHAPE */
    public static final int DEFAULT_SIZE = 40;
    
    /** default shape used to represent a switch */
    public static final Shape DEFAULT_SHAPE = new Ellipse2D.Double(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);
    
    /** default fill color for DEFAULT_SHAPE */
    public static final Paint DEFAULT_FILL = new Color(128, 128, 255);
    public static final Paint DEFAULT_FILL_WIFI = new Color(128, 255, 128);
    
    /** 
     * Uses super.drawObject() to do most of the work and then draws switch
     * description stats if the switch is being hovered over or is selected.
     */
    public void drawObject(Graphics2D gfx) {
        super.drawObject(gfx);
        
        
        
        int x = getX() - getIcon().getWidth() / 2;
        int y = getY() - getIcon().getHeight() / 2;
        
        
        switch(getControllerType())
        {
        case NOX:
    		try {
    			BufferedImage image = ImageIO.read(getClass().getResource("/org/images/nox.png"));
    			gfx.drawImage(image,x+7,y+8,28,28,null);
    			//gfx.drawImage(bi, null, x, y);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		break;
        case Floodlight:
    		try {
    			BufferedImage image = ImageIO.read(getClass().getResource("/org/images/floodlight.png"));
    			gfx.drawImage(image,x-25,y-10,90,70,null);
    			//gfx.drawImage(bi, null, x, y);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		break;
        }
        

        
        if(SHOW_NAMES)
            y += gfx.getFontMetrics().getHeight();
           
        // display switch description stats on mouse over
       // if(this.isHovered() || this.isSelected()) {
        	
            Font f = new Font("TimesRoman",Font.BOLD,12);
            gfx.setFont(f);
        	
            if(isStringSet(desc)) {

                gfx.drawString(desc, x, y);
                y += gfx.getFontMetrics().getHeight();
            }

            gfx.drawString(DPIDUtil.dpidToHex(getID()), x-10, y-20);
            y += gfx.getFontMetrics().getHeight();

            //add by wunyuan
            String organization = getControllerIp();
            String [] ipString = organization.split(":");
            if( ipString[0] != null)
            {
                if( ipString[0].equals("127.0.0.1"))
                	organization = "localhost";
            }
            gfx.drawString(organization, x-10, y-20);
            y += gfx.getFontMetrics().getHeight();
            //end
            
            if(isStringSet(manufacturer)) {
                gfx.drawString(manufacturer, x, y);
                y += gfx.getFontMetrics().getHeight();
            }
            
            if(isStringSet(hw_desc)) {
                gfx.drawString(hw_desc, x, y);
                y += gfx.getFontMetrics().getHeight();
            }
            
            if(isStringSet(sw_desc)) {
                gfx.drawString(sw_desc, x, y);
                y += gfx.getFontMetrics().getHeight();
            }
            
            if(isStringSet(serial_num))
                gfx.drawString(serial_num, x, y);
      //  }
            
        	   
    }
    
 
    // --------------------- ID --------------------- //
    
    /** datapath ID of this switch */
    private long datapathID;
    
    //add by wunyuan
    private String controllerIP;
  
    
   /* public String getControllerIp()
    {
    	return controllerIP;
    }*/
    //end
    
    /** when switch description stats were last updated */
    private long descUpdateTime = 0;
    
    /** returns a string version of the switch's datapath ID */
    public String getDebugName() {
        return DPIDUtil.dpidToHex(datapathID);
    }
    
    /** gets the datapath ID of this switch */
    public long getID() {
        return datapathID;
    }
    
    /** sets the datapath ID of this switch */
    public void setID(long dpid) {
        this.datapathID = dpid;
    }
    
    
    // ------------- Description Stats -------------- //
    
    
    /** switch description stats */
    private String manufacturer="?", hw_desc="?", sw_desc="?", serial_num="?", desc="?";
    
    /** Returns true if s is not null, non-zero length, and not "None" or "?" */
    private boolean isStringSet(String s) {
        return s!=null && s.length()>0 && !s.equals("None") && !s.equals("?");
    }
    
    /** gets the manufacturer of the switch */
    public String getManufacturer() {
        return manufacturer;
    }
    
    /** gets the hardware description of the switch */
    public String getHWDescription() {
        return hw_desc;
    }
    
    /** gets the software description of the switch */
    public String getSWDescription() {
        return sw_desc;
    }
    
    /** gets the serial n umber of the switch */
    public String getSerialNumber() {
        return serial_num;
    }
    
    /** Returns the time the switch's description was last updated */
    public long getDescriptionUpdateTime() {
        return descUpdateTime;
    }
    
    /** Update info about the switch's description */
    public void setSwitchDescription(SwitchDescriptionStats stats) {
        manufacturer = stats.manufacturer;
        hw_desc = stats.hw_desc;
        sw_desc = stats.sw_desc;
        serial_num = stats.serial_num;
        desc = stats.desc;
        descUpdateTime = System.currentTimeMillis();
    }
    
    
    // -------------------- Other ------------------- //
    
    public String toString() {
        return getName() + "; dpid=" + DPIDUtil.dpidToHex(getID());
    }
}
