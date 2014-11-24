package org.openflow.nchc.restful;

public class LinkMessage {
	
	private int host2Switch;
	private int interDomainLink;
	private int linkType;
	private int srcNodeType;
	private long srcID;
	private int srcPort;
	private int dstNodeType;
	private long dstID;
	private int dstPort;
	private long CapacityBps;
	
	public LinkMessage( int host2Switch, int interDomainLink,
						int linkType, int srcNodeType,
						long srcID, int srcPort,
						int dstNodeType, long dstID,
						int dstPort, long CapacityBps) {
		this.host2Switch = host2Switch;
		this.interDomainLink = interDomainLink;
		this.linkType = linkType;
		this.srcNodeType = srcNodeType;
		this.srcID = srcID;
		this.srcPort = srcPort;
		this.dstNodeType = dstNodeType;
		this.dstID = dstID;
		this.dstPort = dstPort;
		this.CapacityBps = CapacityBps;
	}

	public int getHost2Switch() {
		return host2Switch;
	}

	public int getInterDomainLink() {
		return interDomainLink;
	}

	public int getLinkType() {
		return linkType;
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

	public long getCapacityBps() {
		return CapacityBps;
	}

}
