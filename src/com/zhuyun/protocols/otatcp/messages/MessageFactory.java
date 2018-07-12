package com.zhuyun.protocols.otatcp.messages;

import com.zhuyun.protocols.otatcp.Framer;
import com.zhuyun.protocols.otatcp.OtaFramelistener;

public class MessageFactory implements OtaFramelistener {
	private Framer framer;

	@Override
	public void onOtaFrame(OtaFrame frame) {

	}
	
	/**
	   * Constructor with externally created Framer.
	   * 用外部已创建的Framer创建构造器
	   * @param framer Framer
	   */
	public MessageFactory(Framer framer) {
	    this.setFramer(framer);
	    framer.registerFrameListener(this);
	  }
	
	/**
	   * Default Constructor.
	   * 预定义构造器
	   */
	public MessageFactory() {
	    this.setFramer(new Framer());
	    framer.registerFrameListener(this);
	  }

	 /**
	   * Framer getter.
	   * 
	   * @return Framer
	   */
	  public Framer getFramer() {
	    return framer;
	  }
	  /**
	   * Framer setter.
	   *
	   * @param framer Framer
	   */
	  public void setFramer(Framer framer) {
	    this.framer = framer;
	  }
}
