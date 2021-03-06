package org.openflow.gui.net.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Structure to specify a link and its capacity.
 * 
 * @author David Underhill
 */
public class LinkSpec extends Link {
    public static final int SIZEOF = Link.SIZEOF + 10;
    //public static final int SIZEOF = Link.SIZEOF + 6;
    
    public final long capacity_bps;
    
    public LinkSpec(DataInput in) throws IOException {
        super(in);
        this.capacity_bps = in.readLong();
    }
    
    //add by wunyuan
    public LinkSpec(DataInput in, boolean interDomainLink) throws IOException {
        super(in,interDomainLink);
        this.capacity_bps = in.readLong();
    }
    
    
    
    
    public void write(DataOutput out) throws IOException {
        super.write(out);
        out.writeLong(capacity_bps);
    }
    
    public String toString() {
        return super.toString() + ":" + capacity_bps + "bps";
    }

    public int hashCode() {
        return super.hashCode() + 31*(int)capacity_bps;
    }
    
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof LinkSpec)) return false;
        LinkSpec l = (LinkSpec)o;
        return super.equals(l) && capacity_bps==l.capacity_bps;
    }
}
