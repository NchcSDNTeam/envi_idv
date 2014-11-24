package org.openflow.nchc.wunyuan;

import javax.swing.JFrame;

public class CallRedirectedFrame {
	
	public void go()
	{
		RedirectedConsoleFrame rf = new RedirectedConsoleFrame(true, false, "test.txt", 0, 0, JFrame.EXIT_ON_CLOSE);
		rf.setSize(800,200);
		rf.setVisible(true);
	}

}
