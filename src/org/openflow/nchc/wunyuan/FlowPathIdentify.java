package org.openflow.nchc.wunyuan;

import java.awt.*;
import java.util.*;

import org.openflow.gui.drawables.Flow;
import org.openflow.util.*;

public class FlowPathIdentify {
	
	
	/*
	*ethernetType : it stores the ethernet type which obtained from FlowData.
	*clientMac :  it stores the client MAC which obtained from FlowData. 
	*             Index 0 is src client MAC, 1 is dst client MAC.
	*clientIP : it stores the client IP which obtained from FlowData. 
	*           Index 0 is src client IP, 1 is dst client IP.
	*OfIdit : it stores the of switch id which obtained from FlowData. 
	*         Index 0 is src switch id, 1 is dst switch id.
	*OfPort : it stores the of switch port which obtained from FlowData. 
	*         Index 0 is src switch outport, 1 is dst switch inport.
	*flowPath : it stores end host and port pair of flow path . 
	*           The first is of switch src and dst id pair.
	*           Thesecond is of switch src outport and dst inport pair.
	*changed : flow color will be replace with this color. 
	**/
	private String ethernetType;
	private String [] clientMac;
	private String [] clientIP;
	private long [] OfID = new long[2];
	private short [] OfPort = new short[2];
	private  ArrayList<Pair<Pair,Pair>> flowPath = new ArrayList<Pair<Pair,Pair>>();
	private Paint changed = Color.RED;
	
	
	/*
	 * flowInfoSegment: It is a flow information string.
	 * 
	 * This is a constructor. It can split the flowInfoSegment and store all information.
	 * Also, it call other member functions to identify flow path.
	 *  
	 */
	public FlowPathIdentify(String flowInfoSegment)
	{
		String [] cutEnter = flowInfoSegment.split("\n");
		for(int i=0; i < cutEnter.length; i++)
		{
			switch(i)
			{		
			case 1:
				storeEthernetType(cutEnter[i]);
				break;
			case 2:
				storeMACAndIPInfo(cutEnter[i],false);
				break;
			case 3:
				storeMACAndIPInfo(cutEnter[i],true);
				break;
			case 4:
				storeIdInfo(cutEnter[i],true);
				break;
			case 5:
				storePortInfo(cutEnter[i],true);
				break;
			case 6:
				storeIdInfo(cutEnter[i],false);
				break;
			case 7:
				storePortInfo(cutEnter[i],false);
				break;
			}
		}
		doFlowEntriesQuery(true,OfID[0]);
		doFlowEntriesQuery(false,OfID[1]);
		identifyFlowPath();
		
	}
	
	
	/*
	 * This is a identifly flow path function. It can identify a flow path color.
	 */
	
	private void identifyFlowPath() {
		// TODO Auto-generated method stub
		flowPath.add( new Pair<Pair,Pair>( new Pair<Long,Long>(OfID[0],OfID[1]),
				new Pair<Short,Short>(OfPort[0],OfPort[1])));
		while( !(flowPath.isEmpty()) )
		{
			Pair<Long,Long> srcDstIdPair = flowPath.get(0).a;
			Pair<Short,Short> srcDstPortPair = flowPath.get(0).b;			
        	Flow[] flows = FlowMapsData.flowsMap.get(0);
        	int count = 0;
        	if(flows != null)
        	{
        		for(Flow f: flows)
            	{
            		FlowHop[] flowhop = f.getPath();
            		long flowSrcId = flowhop[0].node.getID();
            		short flowSrcPort = flowhop[0].outport;
            		long flowDstId = flowhop[1].node.getID();
            		short flowDstPort = flowhop[1].inport;
            		if( flowSrcId == srcDstIdPair.a.longValue() && 
            				flowDstId == srcDstIdPair.b.longValue() )
            		{
            			if( flowSrcPort == srcDstPortPair.a.shortValue() &&
            					flowDstPort == srcDstPortPair.b.shortValue())
            			{
            				f.setPaint(changed);
            			}
            		}
            		count++;
            	}
        	}
        	flowPath.remove(0);
		}		
	}

	
	/*
	 * ethTypeString: is ethernet type string.
	 * 
	 * It can split ethernet type string and store ethernet type into ethernetType.
	 */
	
	private void storeEthernetType(String ethTypeString) {
		// TODO Auto-generated method stub
		String [] spiltTemp = ethTypeString.split("\t");
		ethernetType = spiltTemp[1];
	}

	
	/*
	 * fd : It is a flow data object.
	 * It can check selected flow message whether matches FlowData or not.
	 */
	private boolean flowMatchCheck(FlowData fd)
	{
		if(fd.getSrcMacAddress().equals(clientMac[0]) && 
				fd.getDstMacAddress().equals(clientMac[1]))
		{
			if(fd.getSrcClientIp().equals(clientIP[0]) &&
					fd.getDstClientIp().equals(clientIP[1]))
			{
				Pair<Long,Long> srcDstIdPair = new Pair<Long,Long>(fd.getSrcId(),fd.getDstId());
				Pair<Short,Short> SrcDstPortPair = new Pair<Short,Short>(fd.getSrcPort(),fd.getDstPort());
				Pair<Pair,Pair> srcDstPair = new Pair<Pair,Pair>(srcDstIdPair,SrcDstPortPair);
				
				for( Pair<Pair,Pair> currentSDP : flowPath )
				{
					if(currentSDP.equals(srcDstPair))
						return false;
				}			
				flowPath.add(srcDstPair);
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * searchWithSrc:when we selected src Id to find its flow path out, it is true.
	 * beQueryedId: it will be used for finding flow path.
	 * According to selected flow message, it can find its flow path out. 
	 */
	private void doFlowEntriesQuery(boolean searchWithSrc,long beQueryedId) {
		// TODO Auto-generated method stub
		//if( ethernetType.equals("ARP"))
		//	ethernetType = "IP";
		for(FlowData fd : FlowData.flowEntries)
		{
			if(searchWithSrc)
			{
				if( fd.getDstId() == beQueryedId /*&& fd.getEthernetType().equals(ethernetType)*/)
				{
					if(flowMatchCheck(fd))
					{
						doFlowEntriesQuery(true,fd.getSrcId());
					}					
				}
			}
			else
			{
				if( fd.getSrcId() == beQueryedId /*&& fd.getEthernetType().equals(ethernetType)*/)
				{
					if(flowMatchCheck(fd))
					{
						doFlowEntriesQuery(false,fd.getDstId());
					}	
				}
			}
		}
	}

	/*
	 * portInfoString: it is port message string.
	 * SrcNode: If it is true, this function can split port string and store src output port.
	 * 
	 * It can split port string and store port into OfPort.
	 */
	private void storePortInfo(String portInfoString, boolean SrcNode) {
		// TODO Auto-generated method stub
		String [] spiltTemp = portInfoString.split("\t");
		if(SrcNode)
			OfPort[0] = Short.parseShort(spiltTemp[1],10);
		else
			OfPort[1] = Short.parseShort(spiltTemp[1],10);
	}

	/*
	 * IdString: it is a id message string.
	 * SrcNode: If it is true, this function can split id string and store src id.
	 * 
	 * It can split id string and store id into OfID.
	 */
	private void storeIdInfo(String IdString, boolean srcNode) {
		// TODO Auto-generated method stub
		String [] spiltTemp = IdString.split("\t");
		if(srcNode)
			OfID[0] = Long.parseLong(spiltTemp[1],16);
		else
			OfID[1] = Long.parseLong(spiltTemp[1],16);
		
	}

	/*
	 * InfoString: it is a MAC or IP string.
	 * IPInfoString: If it is true, this function can split string and store IP.
	 * 
	 * It can split id string and store MAC or IP.
	 */
	private void storeMACAndIPInfo(String InfoString, boolean IPInfoString) {
		// TODO Auto-generated method stub
		
		if(IPInfoString)
		{
			String [] spiltTemp = InfoString.split("\t");
			clientIP =  spiltTemp[1].split("  ----->  ");
		}
		else
		{
			String [] spiltTemp = InfoString.split("\t");
			clientMac = spiltTemp[1].split("  ----->  ");	
		}
	}
}
