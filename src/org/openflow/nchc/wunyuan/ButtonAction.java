package org.openflow.nchc.wunyuan;

import java.awt.event.*;

import javax.swing.JTextArea;

import org.openflow.gui.net.*;
import org.pzgui.layout.PZLayoutManager;

public class ButtonAction implements ActionListener {
	

	
	public void actionPerformed(ActionEvent e)
	{
		
		//MonitorFlowInfoPanel jts = (MonitorFlowInfoPanel) PZLayoutManager.flowInfoPanel.getComponent(0);
				//PZLayoutManager.scroller.validate();
		//jts.setText("");
		//System.out.println(jts.getText());
		
		PZLayoutManager.flowInfoPanel.removeAll();
		JTextArea emptyText = new JTextArea();
		emptyText.setText("Please click flow to show flow information.");
		PZLayoutManager.flowInfoPanel.add(emptyText);
		PZLayoutManager.scroller.validate();
		BackendConnection.setReconnect(true);

	}

}
