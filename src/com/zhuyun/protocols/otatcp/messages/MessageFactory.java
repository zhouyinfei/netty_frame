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
	   * ���ⲿ�Ѵ�����Framer����������
	   * @param framer Framer
	   */
	public MessageFactory(Framer framer) {
	    this.setFramer(framer);
	    framer.registerFrameListener(this);
	  }
	
	/**
	   * Default Constructor.
	   * Ԥ���幹����
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
