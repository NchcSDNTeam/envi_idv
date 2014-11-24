package org.openflow.nchc.restful;

import java.util.ArrayList;

import org.json.*;

public class JsonParser {
	private int dataLength = 0;
	private int totalIpLength = 0;
	private int command = 0;
	private int Xid = 0;
	
	//globalData: dataLength, totalIpLength , command and Xid.
	private int globalDataNumber = 4;
	
	public JsonParser(JSONObject nodeJson) {
		
		try {	
			if(nodeJson.has("DataLength")) {
			
				this.dataLength = nodeJson.getInt("DataLength");
				if(nodeJson.has("TotalIpLength"))
					this.totalIpLength = nodeJson.getInt("TotalIpLength");
				else
					this.totalIpLength = nodeJson.getInt("isFlow");
				this.command = nodeJson.getInt("Command");
								
			}

		//	nodeJson.
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public ArrayList <NodeMessage> NodeJsonParser(JSONObject nodeJson) {
		
		String prefixName = "Node";
		ArrayList <NodeMessage> nodeList = new ArrayList <NodeMessage> () ;
		int nodeNumber = nodeJson.length() - this.globalDataNumber;
		for( int i = 0; i < nodeNumber; i++) {
			try {
				int nodeType = nodeJson.getJSONObject(prefixName+(i+1)).getInt("nodeType");
				long nodeID = nodeJson.getJSONObject(prefixName+(i+1)).getLong("NodeID");
				int ipLength = nodeJson.getJSONObject(prefixName+(i+1)).getInt("IpLength");
				String controllerIP = nodeJson.getJSONObject(prefixName+(i+1)).getString("ControllerIP");
				NodeMessage nodeMesg = new NodeMessage(nodeType, nodeID, ipLength, controllerIP);
				nodeList.add(nodeMesg);	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return nodeList;
	}
	
	
	public ArrayList <LinkMessage> LinkJsonParser(JSONObject LinkJson) {
		
		String prefixName = "Link";
		ArrayList <LinkMessage> linkList = new ArrayList <LinkMessage> () ;
		int nodeNumber = LinkJson.length() - this.globalDataNumber;
		for( int i = 0; i < nodeNumber; i++) {
			try {
				int host2Switch = LinkJson.getJSONObject(prefixName+(i+1)).getInt("Host2Switch");
				int interDomainLink = LinkJson.getJSONObject(prefixName+(i+1)).getInt("InterDomainLink");
				int linkType = LinkJson.getJSONObject(prefixName+(i+1)).getInt("LinkType");
				int srcNodeType = LinkJson.getJSONObject(prefixName+(i+1)).getInt("SrcNodeType");
				long srcId = LinkJson.getJSONObject(prefixName+(i+1)).getLong("SrcId");
				int srcPort = LinkJson.getJSONObject(prefixName+(i+1)).getInt("SrcPort");
				int dstNodeType = LinkJson.getJSONObject(prefixName+(i+1)).getInt("DstNodeType");
				long dstId = LinkJson.getJSONObject(prefixName+(i+1)).getLong("DstId");
				int dstPort = LinkJson.getJSONObject(prefixName+(i+1)).getInt("DstPort");
				long capacityBps = LinkJson.getJSONObject(prefixName+(i+1)).getLong("CapacityBps");
				LinkMessage linkMesg = new LinkMessage(host2Switch, interDomainLink, linkType,
													   srcNodeType, srcId, srcPort,
													   dstNodeType, dstId, dstPort, capacityBps);
				linkList.add(linkMesg);	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return linkList;
	}
	
	
	public ArrayList <FlowMessage> FlowJsonParser(JSONObject flowJson) {
		
		String prefixName = "Flow";
		ArrayList <FlowMessage> flowList = new ArrayList <FlowMessage> () ;
		int nodeNumber = flowJson.length() - this.globalDataNumber;
		for( int i = 0; i < nodeNumber; i++) {
			try {
				int loadFlowNumber = flowJson.getJSONObject(prefixName+(i+1)).getInt("RemainFlowNumber");
				int flowType = flowJson.getJSONObject(prefixName+(i+1)).getInt("FlowType");
				int flowID = flowJson.getJSONObject(prefixName+(i+1)).getInt("FlowID");
				int srcNodeType = flowJson.getJSONObject(prefixName+(i+1)).getInt("SrcNodeType");
				long srcId = flowJson.getJSONObject(prefixName+(i+1)).getLong("SrcNodeID");
				int srcPort = flowJson.getJSONObject(prefixName+(i+1)).getInt("SrcPort");
				int dstNodeType = flowJson.getJSONObject(prefixName+(i+1)).getInt("DstNodeType");
				long dstId = flowJson.getJSONObject(prefixName+(i+1)).getLong("DstNodeID");
				int dstPort = flowJson.getJSONObject(prefixName+(i+1)).getInt("DstPort");
				
				
				int SrcHostMACLength = flowJson.getJSONObject(prefixName+(i+1)).getInt("SrcHostMACLength");
				String SrcHostMAC = flowJson.getJSONObject(prefixName+(i+1)).getString("SrcHostMAC");
				int srcHostIPLength = flowJson.getJSONObject(prefixName+(i+1)).getInt("SrcHostIPLength");
				String srcHostIP = flowJson.getJSONObject(prefixName+(i+1)).getString("SrcHostIP");
				int dstHostMACLength = flowJson.getJSONObject(prefixName+(i+1)).getInt("DstHostMACLength");
				String dstHostMAC = flowJson.getJSONObject(prefixName+(i+1)).getString("DstHostMAC");
				int dstHostIPLength = flowJson.getJSONObject(prefixName+(i+1)).getInt("DstHostIPLength");
				String dstHostIP = flowJson.getJSONObject(prefixName+(i+1)).getString("DstHostIP");
				int ethernetTypeLength = flowJson.getJSONObject(prefixName+(i+1)).getInt("EthernetTypeLength");
				String ethernetType = flowJson.getJSONObject(prefixName+(i+1)).getString("EthernetType");
				
				int pathLength = flowJson.getJSONObject(prefixName+(i+1)).getInt("PathLength");
				

				
				FlowMessage flowMesg = new FlowMessage(loadFlowNumber, flowType, flowID,
													   srcNodeType, srcId, srcPort,
													   dstNodeType, dstId, dstPort,
													   SrcHostMACLength, SrcHostMAC, srcHostIPLength,
													   srcHostIP, dstHostMACLength, dstHostMAC,
													   dstHostIPLength, dstHostIP, ethernetTypeLength,
													   ethernetType, pathLength);
				flowList.add(flowMesg);	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return flowList;
	}
	

	public int getDataLength() {
		return dataLength;
	}

	public int getTotalIpLength() {
		return totalIpLength;
	}

	public int getCommand() {
		return command;
	}

	public int getXid() {
		return Xid;
	}
	
}
