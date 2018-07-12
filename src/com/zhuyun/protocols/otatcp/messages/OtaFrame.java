package com.zhuyun.protocols.otatcp.messages;

import java.nio.ByteBuffer;


public abstract class OtaFrame {
	public int PAYLOAD_LENGTH;
	protected ByteBuffer buffer;
	protected boolean frameDecodeComplete = false;
	protected int remainingLength = 0;				//完整的帧还剩多少字节需要解析
	protected FrameParsingState currentState = FrameParsingState.NONE;
	
	/*
	   * If adding any filed, don't forget to update MqttFrame(MqttFrame old)
	   * to clone all fileds.
	   * 如果你添加任何变量，请不要忘记更新MqttFrame到所有变量中
	   */
	  private MessageType messageType;
	  

	  protected OtaFrame() {
	  }


	  protected OtaFrame(OtaFrame old) {
	    this.messageType = old.getMessageType();
	    this.buffer = old.getBuffer();
	    this.frameDecodeComplete = old.frameDecodeComplete;
	    this.remainingLength = old.remainingLength;
	    this.currentState = old.currentState;
	  }
	  public MessageType getMessageType() {
		    return messageType;
		  }

	  protected void setMessageType(MessageType messageType) {
	    this.messageType = messageType;
	  }
	  

	  /**
	   * Return mqtt Frame.
	   * 返回mqtt帧
	   * @return ByteBuffer mqtt frame
	   */
	  public ByteBuffer getFrame() {
//	    if (buffer == null) {
//	      buffer.position(0);
//	    }
	    return buffer;
	  }

	  /**
	   * Return remaining length of mqtt frame, necessary for ByteBuffer size calculation.
	   * 返回mqtt帧的剩余长度，必须计算ByteBuffer的长度
	   * @return remaining length of mqtt frame
	   */
	  protected int getRemainingLegth() {
	    return remainingLength;
	  }


	  protected ByteBuffer getBuffer() {
	    return buffer;
	  }

	  private void onFrameDone(){
//	    System.out.println("Frame (" + getMessageType() + "): payload processed");
	    if (buffer != null) {
	      buffer.position(0);
	    }
	    frameDecodeComplete = true;
	  }

	  private void processByte(int payloadLength) {
	    if (currentState.equals(FrameParsingState.PROCESSING_PAYLOAD)) {
	    	remainingLength += payloadLength;
	        if (remainingLength != 0) {
	          buffer = ByteBuffer.allocate(remainingLength + 1);		//包含第一个字节 标志位
	        } else {
	          onFrameDone();
	        }
	    }
	  }

	  /**
	   * Push bytes of frame.
	   * 上传帧的字节
	   * @param bytes    the bytes array
	   * @param position the position in buffer
	   * @return int used bytes from buffer
	   */
	  public int push(byte[] bytes, int position, int payloadLength){
		  //	position初始位置，pos实际解析的位置
	    int pos = position;
	    if (currentState.equals(FrameParsingState.NONE)) {
	      remainingLength = 0;
	      currentState = FrameParsingState.PROCESSING_PAYLOAD;
	      processByte(payloadLength);
	      buffer.put(bytes, pos-1, 1);
	    }
	    while (pos < bytes.length && !frameDecodeComplete) {
	      if (currentState.equals(FrameParsingState.PROCESSING_PAYLOAD)) {
	        int bytesToCopy = (remainingLength > bytes.length - pos) ? bytes.length - pos :
	            remainingLength;
	        buffer.put(bytes, pos, bytesToCopy);
	        pos += bytesToCopy;
	        remainingLength -= bytesToCopy;
	        if (remainingLength == 0) {
	        	onFrameDone();
	        }
	      }
	    }
	    return pos - position;			//这一次解析了多少个字节
	  }

	  /**
	   * Test if Mqtt frame decode complete.
	   * 测试mqtt帧解码是否完成
	   * @return boolean 'true' if decode complete
	   */
	  public boolean decodeComplete() {
	    return frameDecodeComplete;
	  }

	  /**
	   * Used in case if Frame Class should be changed during frame decode,
	   * Used for migrate from KaaSync() general frame to specific classes like Sync, Bootstrap.
	   * Default implementation is to return this.
	   * 在帧解码期间帧类应该改变情况下使用，
	   * 
	   * @return new MqttFrame as specific class.
	   * @throws KaaTcpProtocolException the kaa tcp protocol exception
	   */
	  public OtaFrame upgradeFrame() {
	    return this;
	  }

	  /*
	   * (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */

	  protected enum FrameParsingState {
	    NONE,//没有
	    PROCESSING_PAYLOAD,//处理有效负荷
	  }

	
}
