package org.openflow.nchc.wunyuan;

import java.util.*;

public class FlowData{
	
	
//	public static boolean noFlow = true;
//	public static boolean allFlowGet = false;
//	public static boolean startFlowCheck = false;
//	public static int noContinueFlowCounter = 0;
//	public static int flowEntryCounter = 0;
//	public static int flowNumber = 0;
	
	public static ArrayList<FlowData> flowEntries = new ArrayList<FlowData>();
	public static long fidCount = 0;
	
	
	private String srcMacAddress;
	private String srcClientIp;
	private String dstMacAddress;
	private String dstClientIp;
	private String ethernetType;
	private long srcID;
	private short srcPort;
	private long dstID;
	private short dstPort;
	private long fid;

	
	
	
	public FlowData(final long srcNodeID, final long dstNodeID, short srcNodePort, short dstNodePort,
			String srcMac, String srcClIp, String dstMac, String dstClIp, String type, long flowId)
	{
		srcID = srcNodeID;
		srcPort = srcNodePort;
		dstID = dstNodeID;
		dstPort = dstNodePort;
		srcMacAddress = srcMac.toLowerCase();
		srcClientIp = srcClIp;
		dstMacAddress = dstMac.toLowerCase();
		dstClientIp = dstClIp;
		ethernetType = type;
		fid = flowId;
	}



	public long getSrcId()
	{
		return srcID;
	}
	public void setSrcId(long srcID)
	{
		this.srcID = srcID;
	}
	public long getDstId()
	{
		return dstID;
	}
	public void setDstId(long dstID)
	{
		this.dstID = dstID;
	}
	
	public short getSrcPort()
	{
		return srcPort;
	}
	
	public void setSrcPort(short srcPort)
	{
		this.srcPort = srcPort;
	}
	
	public short getDstPort()
	{
		return dstPort;
	}
	public void setDstPort(short dstPort)
	{
		this.dstPort = dstPort;
	}

	public String getSrcMacAddress()
	{
		return srcMacAddress;
	}
	public String getSrcClientIp()
	{
		return srcClientIp;
	}
	public String getDstMacAddress()
	{
		return dstMacAddress;
	}
	public String getDstClientIp()
	{
		return dstClientIp;
	}
	public String getEthernetType()
	{
		return ethernetType;
	}
	public void setEthernetType(String ethernetType)
	{
		this.ethernetType = ethernetType;
	}
	
	public long getFid()
	{
		return fid;
	}

	
	public boolean isSameFlow()
	{
		return false;
		
	}
	
    public boolean repeatFlowEntries()
    {
    	boolean repeat = false;
    	for(FlowData fd : this.flowEntries)
    	{
    		if( fd.getSrcId() == this.getSrcId() /*&& fd.getSrcPort() == this.getSrcPort()*/)
    		{
    			if(fd.getDstId() == this.getDstId()/* && fd.getDstPort() == this.getDstPort()*/)
    			{
    				if(fd.getSrcMacAddress().equals(this.getSrcMacAddress()) && 
    						fd.getDstMacAddress().equals(this.getDstMacAddress()))
    				{
    					if(fd.getEthernetType().equals(this.getEthernetType()))
    					{
    						repeat = true;
    					}
    				}
    			}
    		}
    	}
    
    	return repeat;
    }


}
