package org.openflow.nchc.wunyuan;

import java.util.concurrent.ConcurrentHashMap;

import org.openflow.gui.drawables.Flow;

public class FlowMapsData {

	public static ConcurrentHashMap<Integer, Flow[]> flowsMap = new ConcurrentHashMap<Integer, Flow[]>();
}
