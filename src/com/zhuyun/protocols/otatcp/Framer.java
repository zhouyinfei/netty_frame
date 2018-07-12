package com.zhuyun.protocols.otatcp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zhuyun.protocols.otatcp.messages.FiveFrame;
import com.zhuyun.protocols.otatcp.messages.MessageType;
import com.zhuyun.protocols.otatcp.messages.OtaFrame;
import com.zhuyun.protocols.otatcp.messages.ThreeFrame;



public class Framer {
	/**
	   * Mqtt frame listeners list.
	   */
	private final List<OtaFramelistener> listeners;
	/**
	   * Current processing frame.
	   */
	private OtaFrame currentFrame;
	
	private int currentFramePayloadLength;
	
	/**
	   * Default constructor.
	   */
	  public Framer() {
	    listeners = new ArrayList<>();

	  }
	  
	  /**
	   * Register Mqtt frame listener.
	   *
	   * @param listener MqttFramelistener
	   */
	  public void registerFrameListener(OtaFramelistener listener) {
	    listeners.add(listener);
	  }
	  
	  /**
	   * Process incoming bytes stream.
	   * Assumes that bytes is unprocessed bytes.
	   * In case of previous  pushBytes() eaten not all bytes on next iterations
	   * bytes array should starts from unprocessed bytes.
	   * 处理传入字节流
	   * 假设这是未经处理的字节
	   * 假设先前pushBytes()不能处理完所有字节数组，那么下一个字节数组应该开始于未处理的字节
	   * @param bytes byte[] to push
	   * @return number of bytes processed from this array.
	 * @throws Exception 
	   * @throws KaaTcpProtocolException throws in case of protocol errors.
	   */
	  public int pushBytes(byte[] bytes) throws Exception{
//	    System.out.println("Received bytes: " + Arrays.toString(bytes));
	    
	    int used = 0;

	    while (bytes.length > used) {
	      if (currentFrame == null) {
	        if ((bytes.length - used) >= 1) { // 1 bytes minimum header length
	          int intType = bytes[used] & 0xFF;
	          currentFrame = getFrameByType((byte)intType);
	          currentFramePayloadLength = getFramePayloadLengthByType((byte)intType);
	          ++used;
	        } else {
	          break;
	        }
	      }
	      used += currentFrame.push(bytes, used, currentFramePayloadLength);
	      if (currentFrame.decodeComplete()) {
	        callListeners(currentFrame.upgradeFrame());
	        currentFrame = null;
	      }
	    }
	    return used;
	  }
	  
	  /**
	   * Notify all listeners on new Frame.
	   */
	  private void callListeners(OtaFrame frame) {
	    for (OtaFramelistener listener : listeners) {
	      listener.onOtaFrame(frame);
	    }
	  }
	  /**
	   * Creates specific Kaatcp message by MessageType.
	   *
	   * @param type - MessageType of mqttFrame
	   * @return mqttFrame
	 * @throws Exception 
	   * @throws KaaTcpProtocolException if specified type is unsupported
	   */
	  private OtaFrame getFrameByType(byte type) throws Exception{
			 OtaFrame frame = null;
			    if (type == MessageType.THREE.getType()) {
			      frame = new ThreeFrame();
			    }else if (type == MessageType.FIVE.getType()) {
				      frame = new FiveFrame();
				}else {
				      throw new Exception("Got incorrect messageType format " + type);
			    }

			    return frame;
	  }
	  
	  private int getFramePayloadLengthByType(byte type) throws Exception{
			 int length = 0;
			    if (type == MessageType.THREE.getType()) {
			    	length = ThreeFrame.PAYLOAD_LENGTH;
			    }else if (type == MessageType.FIVE.getType()) {
			    	length = FiveFrame.PAYLOAD_LENGTH;
				}else {
				      throw new Exception("Got incorrect messageType format " + type);
			    }
			  return length;
	  }
	  /**
	   * Reset Framer state by dropping currentFrame.
	   */
	  public void flush() {
	    currentFrame = null;
	  }
	

}
