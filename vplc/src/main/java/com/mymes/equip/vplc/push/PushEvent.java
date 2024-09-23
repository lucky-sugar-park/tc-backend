//package com.mymes.equip.vplc.push;
//
//public abstract class PushEvent {
//	
//	public enum PushEventKind {
//		MEMV_CHANGED("MemoryValueChanged"),
//		VPLC_STARTED("VirtualPlcStarted"),
//		VPLC_STOPPED("VirtualPlcStopped"),
//		VPLC_PAUSED("VirtualPlcPaused"),
//		VPLC_RESUMED("VirtualPlcResumed");
//		
//		String name;
//		
//		private PushEventKind(String name) {
//			this.name=name;
//		}
//		
//		public String getName() {
//			return this.name;
//		}
//	}
//
//	private PushEventKind eventKind;
//	
//	public void setEventKind(PushEventKind eventKind) {
//		this.eventKind=eventKind;
//	}
//	
//	public PushEventKind getEventKind () {
//		return this.eventKind;
//	}
//	
//	public abstract String toJson();
//}