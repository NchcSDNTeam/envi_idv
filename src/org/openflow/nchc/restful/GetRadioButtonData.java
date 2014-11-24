package org.openflow.nchc.restful;

import java.awt.event.*;

import javax.swing.*;

public class GetRadioButtonData implements ActionListener {

	
	public static String defaultString = "Restful";
	public static String defaultRestful_IP = "127.0.0.1";
	public static String restful_IP = "127.0.0.1";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JRadioButton srcButton = (JRadioButton) e.getSource();
		
		if(srcButton.getText().equals("Read Byte Data")) {
			this.defaultString = "ByteData";
		}
		if(srcButton.getText().equals("Read Restful Data"))	{
			this.defaultString = "Restful";
		}

		
	}
	
	

}
