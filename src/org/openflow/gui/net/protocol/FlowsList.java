package org.openflow.gui.net.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.openflow.gui.net.BackendConnection;
import org.openflow.gui.net.SocketConnection;
import org.openflow.nchc.restful.GetRadioButtonData;
import org.openflow.nchc.wunyuan.*;


/**
 * A list of flows.
 * 
 * @author David Underhill
 */
public abstract class FlowsList extends OFGMessage {
    public Flow[] flows;
    
    public FlowsList(OFGMessageType t, final Flow[] flows) {
        this(t, 0, flows);
    }
    
    public FlowsList(OFGMessageType t, int xid, final Flow[] flows) {
        super(t, xid);
        this.flows = flows;
    }
    
    
 
    
    public FlowsList(final int len, final OFGMessageType t, final int xid, final DataInput in) throws IOException {
        super(t, xid);
        int left = len - super.length();
        if(left < 4)
            throw new IOException("Body of flows has a bad length (not enough bytes for # of flows field): " + left + "B left, need >=4B");
        
        // read in the flows
     //   flows = new Flow[in.readInt()];
        flows = new Flow[1];
     //   int remainFlow = 0;
        int remainFlow = in.readInt();
        for(int flowOn=0; flowOn<flows.length; flowOn++) { 
            if(left < 32)
                throw new IOException("Body of flows has a bad length (not enough for a flow length): " + left + "B left, need >=32B");  
     /*       if(flowOn != 0)
            {
            	@SuppressWarnings("unused")
				short type1 = in.readShort();
            	short type2 = in.readShort();
            	short type3 = in.readShort();
            	short type4 = in.readShort();
            	short type5 = in.readShort();
            	short type6 = in.readShort();
            	short type7 = in.readShort();
            	remainFlow = in.readInt();  
            }*/
            boolean interDomainHostFlow = false;
            short type = in.readShort();
            int id = in.readInt();
            Node srcNode = new Node(in, false);
            short srcPort = in.readShort();
            Node dstNode = new Node(in, false);
            short dstPort = in.readShort();
//add by wunyuan
            long flowId = FlowData.fidCount;
            ++FlowData.fidCount;
            FlowData clientData = new FlowData(srcNode.id, dstNode.id, srcPort, dstPort,
            		SocketConnection.readString(in, in.readShort()),
            		SocketConnection.readString(in, in.readShort()),
            		SocketConnection.readString(in, in.readShort()),
            		SocketConnection.readString(in, in.readShort()),
            		SocketConnection.readString(in, in.readShort()), flowId);
            
            
            short pathLen = in.readShort();
            
            if(left < pathLen * (2 + Node.SIZEOF + 2))
                throw new IOException("Body of flows has a bad length (not enough for a flow)");
            
       
            FlowHop[] path = new FlowHop[pathLen];
            for(int i=0; i<pathLen; i++)
                path[i] = new FlowHop(in.readShort(), new Node(in, false), in.readShort());
     
            
            for(LinkSpec ls : LinkData.interDomainLinkList)
            {
            	if(ls.srcNode.equals(srcNode) && ls.srcPort == srcPort)
            	{
            		if(ls.dstNode.equals(dstNode) && ls.dstPort == dstPort)
            			interDomainHostFlow = true;
            	}
            	if(ls.dstNode.equals(srcNode) && ls.dstPort == srcPort)
            	{
            		if(ls.srcNode.equals(dstNode) && ls.srcPort == dstPort)
            			interDomainHostFlow = true;           					
            	}
            }
            
            
            if(!interDomainHostFlow)
            {
            Flow f = new Flow(FlowType.typeValToMessageType(type), id, srcNode, srcPort, dstNode, dstPort, clientData, path);
            flows[flowOn] = f;
            left -= f.length();
            if(FlowData.flowEntries.isEmpty())
                FlowData.flowEntries.add(clientData);
            else
            {
            	
            	if(!clientData.repeatFlowEntries())
            	{
            		FlowData.flowEntries.add(clientData);
            	}
            }
            }
   //         if(GetRadioButtonData.defaultString.equals("Restful") && remainFlow > 0 )
   //         	remainFlow = in.readInt();

        }
        
  //      int len1 = in.readShort();
  //      int len2 = in.readByte();
  //      int len3 = in.readByte();
  //      int len4 = in.readByte();
        
        BackendConnection.test = 0;
        
    }
    //end
   
   /*original 
    public FlowsList(final int len, final OFGMessageType t, final int xid, final DataInput in) throws IOException {
        super(t, xid);
        int left = len - super.length();
        if(left < 4)
            throw new IOException("Body of flows has a bad length (not enough bytes for # of flows field): " + left + "B left, need >=4B");
        
        // read in the flows
        flows = new Flow[in.readInt()];
        for(int flowOn=0; flowOn<flows.length; flowOn++) { 
            if(left < 32)
                throw new IOException("Body of flows has a bad length (not enough for a flow length): " + left + "B left, need >=32B");
            
            short type = in.readShort();
            int id = in.readInt();
            Node srcNode = new Node(in);
            short srcPort = in.readShort();
            Node dstNode = new Node(in);
            short dstPort = in.readShort();
            short pathLen = in.readShort();
            if(left < pathLen * (2 + Node.SIZEOF + 2))
                throw new IOException("Body of flows has a bad length (not enough for a flow)");
            
            FlowHop[] path = new FlowHop[pathLen];
            for(int i=0; i<pathLen; i++)
                path[i] = new FlowHop(in.readShort(), new Node(in), in.readShort());
            
            Flow f = new Flow(FlowType.typeValToMessageType(type), id, srcNode, srcPort, dstNode, dstPort, path);
            flows[flowOn] = f;
            left -= f.length();
        }
    }
 
    */
    
    
    public int length() {
        int len = super.length();
        for(Flow f : flows)
            len += f.length();
        return len;
    }
    
    public void write(DataOutput out) throws IOException {
        super.write(out); 
        for(Flow f : flows)
            f.write(out);
    }
    
    public String toString() {
        String strFlows;
        if(flows.length > 0)
            strFlows = flows[0].toString();
        else
            strFlows = "";
        
        for(int i=1; i<flows.length; i++)
            strFlows += ", " + flows[i].toString();
        
        return super.toString() + TSSEP + strFlows;
    }
}
