package org.openflow.nchc.restful;


import java.io.*;
import java.net.*;
import java.util.*;

import org.json.*;

public class MessageHandler implements Runnable {
	
	private int defaultPort = 2503;
	private Socket clientSocket = null;
	public static boolean isRespond = true;
	public static boolean disconnect = false;
	
	private static String IPaddress = "127.0.0.1";
	
//	private ArrayList <NodeMessage> switchMesg, hostMesg;
//	private ArrayList <LinkMessage> swLinkMesg, hostLinkMesg;
//	private ArrayList <FlowMessage> swFlowMesg, hostFlowMesg;
	
	private HashMap<JsonParser, ArrayList <NodeMessage>> swMap, hostMap;
	private HashMap<JsonParser, ArrayList <LinkMessage>> swLinkMap, hostLinkMap;
	private HashMap<JsonParser, ArrayList <FlowMessage>> swFlowMap, hostFlowMap;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		startSocket();
		
	}
	
	public void startSocket() {
		
		try {
			ServerSocket serverSocket = new ServerSocket(defaultPort);
			while(true) {
				disconnect = false;
				clientSocket = serverSocket.accept();
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				handleMessage("Node");
				if(!disconnect)
				{
					handleMessage("Link");
					handleMessage("Flow");
					replyMessage(out);
				}
				else
				{
					MessageHandler.isRespond = true;
					System.out.println("close socket.");
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleMessage(String receiveType) {
		switch(receiveType)
		{
		case "Node":
			receiveNodeMessage();
			break;
			
		case "Link":
			receiveLinkMessage();
			break;

		case "Flow":
			receiveFlowMessage();
			break;
			
		}
		
	}
	
	private String replyMessage(DataOutputStream out) {
		

		
		if( this.swMap != null && this.hostMap != null ) {
			sendNodeMessageOut(out, this.swMap);
			sendNodeMessageOut(out, this.hostMap);
		}
		else
			return "Switch or Host data is null.";
		if( this.swLinkMap != null && this.hostLinkMap != null ) {
			sendLinkMessageOut(out, this.swLinkMap);
			sendLinkMessageOut(out, this.hostLinkMap);
		}
		else
			return "Switch link or Host link data is null.";
		if( this.swFlowMap != null && this.hostFlowMap!= null ) {
			sendFlowMessageOut(out, this.swFlowMap);
			sendFlowMessageOut(out, this.hostFlowMap);
		}
		else
			return "Switch flow or Host flow data is null.";
		
		return "All Data are gotten";
		
	}

	
	private void sendNodeMessageOut( DataOutputStream out,
			HashMap<JsonParser, ArrayList<NodeMessage>> nodeMap) {
		// TODO Auto-generated method stub		
		for( JsonParser globalData : nodeMap.keySet()) {
			
			if(nodeMap.get(globalData) != null ) {
				
				try {

					out.writeShort((short) globalData.getDataLength());
					out.writeShort(globalData.getTotalIpLength());
					out.writeByte(globalData.getCommand());
					out.writeInt(globalData.getXid());
					ArrayList<NodeMessage> nodeList = nodeMap.get(globalData);
					for( NodeMessage node : nodeList) {
						out.writeShort(node.getNodeType());
						out.writeLong(node.getNodeID());
						out.writeShort(node.getIpLength());
						out.write(node.getControllerIP().getBytes(), 0, node.getIpLength());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
	}

	private void sendLinkMessageOut( DataOutputStream out,
			HashMap<JsonParser, ArrayList<LinkMessage>> linkMap) {
		// TODO Auto-generated method stub
		
		for( JsonParser globalData : linkMap.keySet()) {
			
			if(linkMap.get(globalData) != null) {
				
				try {
					out.writeShort(globalData.getDataLength());
					out.writeShort(globalData.getTotalIpLength());
					out.writeByte(globalData.getCommand());
					out.writeInt(globalData.getXid());
					ArrayList<LinkMessage> linkList = linkMap.get(globalData);
					for( LinkMessage link : linkList) {
						out.writeShort(link.getHost2Switch());
						out.writeShort(link.getInterDomainLink());
						out.writeShort(link.getLinkType());
						out.writeShort(link.getSrcNodeType());
						out.writeLong(link.getSrcID());
						out.writeShort(link.getSrcPort());
						out.writeShort(link.getDstNodeType());
						out.writeLong(link.getDstID());
						out.writeShort(link.getDstPort());
						out.writeLong(link.getCapacityBps());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

	private void sendFlowMessageOut( DataOutputStream out,
			HashMap<JsonParser, ArrayList<FlowMessage>> flowMap) {
		// TODO Auto-generated method stub
		for( JsonParser globalData : flowMap.keySet()) {
			
			if(flowMap.get(globalData) != null) {
				
				try {

					ArrayList<FlowMessage> flowList = flowMap.get(globalData);
					for( FlowMessage flowData : flowList) {
						out.writeShort(globalData.getDataLength());
						out.writeShort(globalData.getTotalIpLength());
						out.writeByte(globalData.getCommand());
						out.writeInt(globalData.getXid());				
			
						out.writeInt(flowData.getLoadFlowNumber());
						out.writeShort(flowData.getFlowType());
						out.writeInt(flowData.getFlowID());
						
						out.writeShort(flowData.getSrcNodeType());
						out.writeLong(flowData.getSrcID());
						out.writeShort(flowData.getSrcPort());
						
						out.writeShort(flowData.getDstNodeType());
						out.writeLong(flowData.getDstID());
						out.writeShort(flowData.getDstPort());
						
						
						out.writeShort(flowData.getSrcHostMACLength());
						out.write(flowData.getSrcHostMAC().getBytes(), 0, flowData.getSrcHostMACLength());
						out.writeShort(flowData.getSrcHostIPLength());
						out.write(flowData.getSrcHostIP().getBytes(), 0, flowData.getSrcHostIPLength());
						
						out.writeShort(flowData.getDstHostMACLength());
						out.write(flowData.getDstHostMAC().getBytes(), 0, flowData.getDstHostMACLength());
						out.writeShort(flowData.getDstHostIPLength());
						out.write(flowData.getDstHostIP().getBytes(), 0, flowData.getDstHostIPLength());
						
						out.writeShort(flowData.getEthernetTypeLength());
						out.write(flowData.getEthernetType().getBytes(), 0, flowData.getEthernetTypeLength());
						
						out.writeShort(flowData.getPathLength());
									
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private void receiveNodeMessage() {
		
		HttpConnection sendSwitchReq = new HttpConnection("GET",IPaddress,"8080","/lavi/data/node/switch");
		HttpConnection sendHostReq = new HttpConnection("GET",IPaddress,"8080","/lavi/data/node/host");
		this.swMap = new HashMap<JsonParser, ArrayList <NodeMessage>>();
		this.hostMap = new HashMap<JsonParser, ArrayList <NodeMessage>>();
		//String respond = new String();
		        
		try {
			sendSwitchReq.DoConnection();
			sendHostReq.DoConnection();
			JSONObject switchJsonData = new JSONObject(sendSwitchReq.getRespond());
			JSONObject hostJsonData = new JSONObject(sendHostReq.getRespond());
			JsonParser swParser = new JsonParser(switchJsonData);
			JsonParser hostParser = new JsonParser(hostJsonData);
			ArrayList<NodeMessage> switchMesg = swParser.NodeJsonParser(switchJsonData);
			ArrayList<NodeMessage> hostMesg = hostParser.NodeJsonParser(hostJsonData);			
			this.swMap.put(swParser, switchMesg);
			this.hostMap.put(hostParser, hostMesg);
			} 
		catch (ConnectException e) {
		// TODO Auto-generated catch block
			disconnect = true;		
			System.out.println("disconnect.");
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			disconnect = true;
			
			}

	}
	
	
	
	private void receiveLinkMessage() {
		
		HttpConnection sendSwLinkReq = new HttpConnection("GET",IPaddress,"8080","/lavi/data/link/sw2sw");
		HttpConnection sendHostLinkReq = new HttpConnection("GET",IPaddress,"8080","/lavi/data/link/host2sw");
		//String respond = new String();
		        
		this.swLinkMap = new HashMap<JsonParser, ArrayList <LinkMessage>>();
		this.hostLinkMap = new HashMap<JsonParser, ArrayList <LinkMessage>>();
		
		try {
			sendSwLinkReq.DoConnection();
			sendHostLinkReq.DoConnection();
			JSONObject swLinkJsonData = new JSONObject(sendSwLinkReq.getRespond());
			JSONObject hostLinkJsonData = new JSONObject(sendHostLinkReq.getRespond());
			JsonParser swlinkParser = new JsonParser(swLinkJsonData);
			JsonParser hostlinkParser = new JsonParser(hostLinkJsonData);
			ArrayList<LinkMessage> swLinkMesg = swlinkParser.LinkJsonParser(swLinkJsonData);
			ArrayList<LinkMessage> hostLinkMesg = hostlinkParser.LinkJsonParser(hostLinkJsonData);			
			this.swLinkMap.put(swlinkParser, swLinkMesg);
			this.hostLinkMap.put(hostlinkParser, hostLinkMesg);
			}
		catch (ConnectException e) {
				// TODO Auto-generated catch block
				disconnect = true;		
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			disconnect = true;
			}		
	}
	
	
	private void receiveFlowMessage() {
		
		HttpConnection sendSwFlowReq = new HttpConnection("GET",IPaddress,"8080","/lavi/data/flow/swflows");
		HttpConnection sendHostFlowReq = new HttpConnection("GET",IPaddress,"8080","/lavi/data/flow/hostflows");
		//String respond = new String();
		
		this.swFlowMap = new HashMap<JsonParser, ArrayList <FlowMessage>>();
		this.hostFlowMap = new HashMap<JsonParser, ArrayList <FlowMessage>>();
		        
		try {
			sendSwFlowReq.DoConnection();
			sendHostFlowReq.DoConnection();
			JSONObject swFlowJsonData = new JSONObject(sendSwFlowReq.getRespond());
			JSONObject hostFlowJsonData = new JSONObject(sendHostFlowReq.getRespond());
			JsonParser swflowParser = new JsonParser(swFlowJsonData);
			JsonParser hostflowParser = new JsonParser(hostFlowJsonData);
			ArrayList<FlowMessage>  swFlowMesg = swflowParser.FlowJsonParser(swFlowJsonData);
			ArrayList<FlowMessage>  hostFlowMesg = hostflowParser.FlowJsonParser(hostFlowJsonData);	
			this.swFlowMap.put(swflowParser, swFlowMesg);
			this.hostFlowMap.put(hostflowParser, hostFlowMesg);
			} 
		catch (ConnectException e) {
			// TODO Auto-generated catch block
			disconnect = true;		
		}
	catch (Exception e) {
		// TODO Auto-generated catch block
		disconnect = true;
		}		
	}

	public static String getIPaddress() {
		return IPaddress;
	}

	public static void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
	}


	
}
