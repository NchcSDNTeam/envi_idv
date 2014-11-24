package org.openflow.nchc.wunyuan;

import java.awt.*;

import javax.swing.*;



public class WarningFrame extends JFrame {

	public JTextArea watingMessage = new JTextArea();
	
public WarningFrame(String str) {
		
		watingMessage.setText(str);
		Container c = getContentPane();		
		c.setLayout(new BorderLayout());
		c.add(BorderLayout.CENTER, watingMessage);
//		JOptionPane.showMessageDialog(null,"Cannot get Http respond. please click button to leave","Warning" ,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public WarningFrame() {
		
		watingMessage.setText("Wait http respond. If it does not get respond, this connection will be disconnected");
		watingMessage.append("\nAfter 30 sec, this connection will be disconnected and windows will be closed.");
		Container c = getContentPane();		
		c.setLayout(new BorderLayout());
		c.add(BorderLayout.CENTER, watingMessage);
//		JOptionPane.showMessageDialog(null,"Cannot get Http respond. please click button to leave","Warning" ,JOptionPane.INFORMATION_MESSAGE);
	}
	

	

}
