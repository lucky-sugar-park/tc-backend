package com.mymes.equip.vplc.machine;

public interface ClientConnectionEventListener {
	
	public String getName();

	public void onClientConnectionChanged(ClientConnectionEvent cce);
}
