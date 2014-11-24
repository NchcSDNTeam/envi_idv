package org.openflow.nchc.wunyuan;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

import org.openflow.gui.drawables.Flow;

public class MonitorFlowInfoPanel extends JTextArea implements MouseListener {

	public MonitorFlowInfoPanel() {
		// TODO Auto-generated constructor stub
		//super(i,j);
		addMouseListener(this);
			
		}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		FlowPathIdentify fpi = new FlowPathIdentify(this.getText());
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		Flow[] flows = FlowMapsData.flowsMap.get(0);
		if(flows != null)
		{
			for(Flow f : flows)
				f.setPaint(Color.BLUE);
		}
			
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
