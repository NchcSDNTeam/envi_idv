package org.openflow.nchc.restful;

public class NodeMessage {
	
	private int nodeType;
	private long nodeID;
	private int ipLength;
	private String controllerIP;
	
	public NodeMessage( int nodeType, long nodeID, int ipLength, String controllerIP) {
		this.nodeType = nodeType;
		this.nodeID = nodeID;
		this.ipLength = ipLength;
		this.controllerIP = controllerIP;
	}

	public int getNodeType() {
		return nodeType;
	}

	public long getNodeID() {
		return nodeID;
	}

	public int getIpLength() {
		return ipLength;
	}

	public String getControllerIP() {
		return controllerIP;
	}

}
