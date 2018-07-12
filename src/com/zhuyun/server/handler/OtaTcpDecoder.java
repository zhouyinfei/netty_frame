package com.zhuyun.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.ByteBuffer;

import com.zhuyun.protocols.otatcp.messages.MessageFactory;
import com.zhuyun.protocols.otatcp.messages.OtaFrame;


public class OtaTcpDecoder extends SimpleChannelInboundHandler<byte[]>{
	private ChannelHandlerContext currentCtx;
	private final MessageFactory messageFactory = new MessageFactory() {
	    @Override
	    public void onOtaFrame(OtaFrame frame) {
	      try {
	        processFrame(frame);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	      super.onOtaFrame(frame);
	    }
	  };
	  
	private void processFrame(OtaFrame frame) throws Exception {
		ByteBuffer buffer = frame.getFrame();
		byte[] array = buffer.array();
	    currentCtx.fireChannelRead(array);
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {// ÿ���յ���Ϣʱ������
		currentCtx = ctx;
		messageFactory.getFramer().pushBytes(data);
	}
	
	@Override						//����֪ͨhandler��һ��ChannelRead()�Ǳ�������Ϣ�е����һ����Ϣ����
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//ˢ�¹�������ݵ�Զ�ˣ�Ȼ��ر�Channel
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
	}
	
	@Override						//�ڶ������쳣���׳�ʱ������
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();			//��ӡ�쳣��ջ������Ϣ
		ctx.close();						//�ر����Channel
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}
	
}
