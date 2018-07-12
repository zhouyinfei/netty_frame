package com.zhuyun.protocols.otatcp.messages;

public enum MessageType {
	THREE((byte) 3),
	FIVE((byte) 5);
	
	private byte type;
	
	private MessageType(byte type) {
	    this.type = type;
	  }
	
	  public byte getType() {
	    return type;
	  }
}
