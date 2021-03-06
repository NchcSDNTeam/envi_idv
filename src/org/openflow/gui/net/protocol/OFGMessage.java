package org.openflow.gui.net.protocol;

import java.io.*;

import org.openflow.gui.net.Message;

/**
 * Header for OpenFlow GUI protocol messages.
 * 
 * @author David Underhill
 */
public class OFGMessage implements Message<OFGMessageType> {
    /** size in bytes of a OFGMessage (when serialized) */
    //public static final int SwitchSIZEOF = 9;
    public static final int SIZEOF = 7;
    
    //add by wunyuan
    public static int datalength;

    public int ipDataLen() {
    	return datalength;
    }
    
    public int switchSize()
    {
    	return SIZEOF + 2;
    }
    
    //end
    
    /** separator between two toString() operators */
    protected static final String TSSEP = " // "; 
    
    /** total length of this message in bytes */
    public int length() {
        return SIZEOF;
    }
    
    /** type of this message */
    public OFGMessageType type;
    
    /** transaction id of this message, if any */
    public int xid;
    
    /** the time this message was created in this application */
    public long timeCreated = System.currentTimeMillis();
    
    /** used to construct a message */
    public OFGMessage(final OFGMessageType t, final int xid) {
        this.type = t;
        this.xid = xid;
    }
    
    /** gets the type associated with this class */
    public OFGMessageType getType() {
        return type;
    }
    
    /** returns whether this message starts a stateful exchange (default=false) */
    public boolean isStatefulRequest() {
        return false;
    }

    /** returns whether this message is a reply to a stateful exchange (default=false) */
    public boolean isStatefulReply() {
        return false;
    }
    
    /** sends the message over the specified output stream */
    public void write(DataOutput out) throws IOException {
        out.writeShort(length());
        out.writeByte(type.getTypeID());
        out.writeInt(xid);
    }

    /** Gets the time this message was created in this application */
    public long timeCreated() {
        return timeCreated;
    }
    
    /** 
     * String representation of the message including the date and type.  Also
     * includes the transaction ID if it is non-zero.
     */
    public String toString() {
        return new java.util.Date(timeCreated).toString()     + " " +
               (type == null ? "null-type" : type.toString()) + " " +
               (xid == 0 ? "" : xid);
    }
}
