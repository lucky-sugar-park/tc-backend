package com.mymes.equip.vplc.machine;

import com.mymes.equip.vplc.Types.FrameFormat;
import com.mymes.equip.vplc.Types.MemoryType;

import lombok.Data;

@Data
public class MemoryInfo {
	
	private String vplcId;
	
	private MemoryType memoryType;
	
	private FrameFormat frameFormat;
	
	private int startAddress;
	
	private short[] words;
	
	private int capacity;
	
	private WordInfo[] wordInfos;
	
	public MemoryInfo() {
		
	}
	
	public void setWords(short[] words) {
		this.words=words;
		this.wordInfos=new WordInfo[words.length];
		for(int i=0;i<words.length;i++) {
			WordInfo wInfo=new WordInfo(words[i]);
			this.wordInfos[i]=wInfo;
		}
	}
	
	public void setWords(byte[] bytes) {
		short wordValues[]=new short[bytes.length>>1];
		int idx=0;
		for(int i=0;i<bytes.length;i+=2) {
			wordValues[idx++]=(short)(bytes[i+1]<<8|bytes[i]);
		}
		this.setWords(wordValues);
	}
	
	public void setWordInfos(WordInfo[] words) {
		this.wordInfos=words;
	}
}
