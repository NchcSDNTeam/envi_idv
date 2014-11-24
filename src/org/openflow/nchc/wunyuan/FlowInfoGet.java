package org.openflow.nchc.wunyuan;

import java.awt.Color;

import javax.swing.*;

import org.openflow.gui.drawables.NodeWithPorts;
import org.openflow.gui.net.protocol.NodeType;
import org.pzgui.layout.PZLayoutManager;
import org.openflow.gui.Topology;

public class FlowInfoGet {

	private NodeWithPorts src;
	private NodeWithPorts dst;
	
	public FlowInfoGet(NodeWithPorts src, NodeWithPorts dst) {
		// TODO Auto-generated constructor stub
		this.src = src;
		this.dst = dst;
	}

	public void addFlowInfo()
	{		
		PZLayoutManager.flowInfoPanel.removeAll();
		PZLayoutManager.flowInfoPanel.setLayout(new BoxLayout(PZLayoutManager.flowInfoPanel, BoxLayout.Y_AXIS));
		
			int count=0;
			String flowEntry = null;
			for( FlowData fd : FlowData.flowEntries)
			{
				if(fd.getSrcId() == src.getID() && fd.getDstId() == dst.getID())
				{
					
					
					
				
					count++;
					flowEntry = Integer.toString(count) +"\n"+"Enthernet Type:\t"+fd.getEthernetType()+"\n"+
					                    "Client MAC:\t"+fd.getSrcMacAddress()+"  ----->  "+fd.getDstMacAddress()+"\n"+
					                    "Client IP:\t"+fd.getSrcClientIp()+"  ----->  "+fd.getDstClientIp()+"\n"+
					                    "Src ID:\t"+ Long.toHexString(fd.getSrcId())+"\t("+getControllerIP(true)+")"+"\n"+
					                    "Src port:\t"+Short.toString(fd.getSrcPort())+"\n"+
					                    "Dst ID:\t"+ Long.toHexString(fd.getDstId())+"\t("+getControllerIP(false)+")"+"\n"+
					                    "Dst port:\t"+Short.toString(fd.getDstPort())+"\n";
				
					MonitorFlowInfoPanel flowInfo = new MonitorFlowInfoPanel();
					flowInfo.setBorder (BorderFactory.createBevelBorder(1));
					flowInfo.append(flowEntry);
					PZLayoutManager.flowInfoPanel.add(flowInfo);
				                   				 
				}	
			}
			PZLayoutManager.scroller.validate();
	}

	
	public String getControllerIP(boolean srcNode)
	{
		if(srcNode)
		{
			if(src.getControllerIp() == null && src.getType() == NodeType.HOST)
			{
				return dst.getControllerIp();
			}
			return src.getControllerIp();
		}
		else
		{
			if(dst.getControllerIp() == null && dst.getType() == NodeType.HOST)
			{
				return src.getControllerIp();
			}
			return dst.getControllerIp();
		}
	}	
}
