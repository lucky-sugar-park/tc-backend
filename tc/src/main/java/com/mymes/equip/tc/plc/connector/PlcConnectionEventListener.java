package com.mymes.equip.tc.plc.connector;

public interface PlcConnectionEventListener {

	public abstract void onEvent(PlcConnectorEvent pevent);
}
