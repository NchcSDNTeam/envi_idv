package org.openflow.nchc.restful;

public class FlowMessage {
	
	private int LoadFlowNumber;
	private int FlowType;
	private int FlowID;
	private int srcNodeType;
	private long srcID;
	private int srcPort;
	private int dstNodeType;
	private long dstID;
	private int dstPort;
	private int srcHostMACLength;
	private String srcHostMAC;
	private int srcHostIPLength;
	private String srcHostIP;
	private int dstHostMACLength;
	private String dstHostMAC;
	private int dstHostIPLength;
	private String dstHostIP;
	private int ethernetTypeLength;
	private String ethernetType;
	private int pathLength;
	
	public FlowMessage( int LoadFlowNumber, int FlowType,
						int FlowID, int srcNodeType,
						long srcID, int srcPort,
						int dstNodeType, long dstID,
						int dstPort, int srcHostMACLength,
						String srcHostMAC, int srcHostIPLength,
						String srcHostIP,int dstHostMACLength,
						String dstHostMAC, int dstHostIPLength,
						String dstHostIP, int ethernetTypeLength,
						String ethernetType, int pathLength) {
		this.LoadFlowNumber = LoadFlowNumber;
		this.FlowType = FlowType;
		this.FlowID = FlowID;
		this.srcNodeType = srcNodeType;
		this.srcID = srcID;
		this.srcPort = srcPort;
		this.dstNodeType = dstNodeType;
		this.dstID = dstID;
		this.dstPort = dstPort;
		this.srcHostMACLength = srcHostMACLength;
		this.srcHostMAC = srcHostMAC;
		this.srcHostIPLength = srcHostIPLength;
		this.srcHostIP = srcHostIP;
		this.dstHostMACLength = dstHostMACLength;
		this.dstHostMAC = dstHostMAC;
		this.dstHostIPLength = dstHostIPLength;
		this.dstHostIP = dstHostIP;
		this.ethernetTypeLength = ethernetTypeLength;
		this.ethernetType = ethernetType;
		this.pathLength = pathLength;

	}

	public int getLoadFlowNumber() {
		return LoadFlowNumber;
	}

	public int getFlowType() {
		return FlowType;
	}

	public int getFlowID() {
		return FlowID;
	}

	public int getSrcNodeType() {
		return srcNodeType;
	}

	public long getSrcID() {
		return srcID;
	}

	public int getSrcPort() {
		return srcPort;
	}

	public int getDstNodeType() {
		return dstNodeType;
	}

	public long getDstID() {
		return dstID;
	}

	public int getDstPort() {
		return dstPort;
	}

	public int getSrcHostMACLength() {
		return srcHostMACLength;
	}

	public String getSrcHostMAC() {
		return srcHostMAC;
	}

	public int getSrcHostIPLength() {
		return srcHostIPLength;
	}

	public String getSrcHostIP() {
		return srcHostIP;
	}

	public int getDstHostMACLength() {
		return dstHostMACLength;
	}

	public String getDstHostMAC() {
		return dstHostMAC;
	}

	public int getDstHostIPLength() {
		return dstHostIPLength;
	}

	public String getDstHostIP() {
		return dstHostIP;
	}

	public int getEthernetTypeLength() {
		return ethernetTypeLength;
	}

	public String getEthernetType() {
		return ethernetType;
	}

	public int getPathLength() {
		return pathLength;
	}


}
