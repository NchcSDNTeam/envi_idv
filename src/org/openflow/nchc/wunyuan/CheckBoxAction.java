package org.openflow.nchc.wunyuan;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.pzgui.layout.PZLayoutManager;

public class CheckBoxAction implements ItemListener {

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

		if(PZLayoutManager.displayIsland.isSelected())
		{
			LinkData.showAllIslandHost = true;		
		}
		else
		{
			LinkData.showAllIslandHost = false;	
		}	
	}
}
