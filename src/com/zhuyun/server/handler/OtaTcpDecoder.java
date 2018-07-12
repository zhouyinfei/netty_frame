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
	public void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {// 每次收到消息时被调用
		currentCtx = ctx;
		messageFactory.getFramer().pushBytes(data);
	}
	
	@Override						//用来通知handler上一个ChannelRead()是被这批消息中的最后一个消息调用
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//刷新挂起的数据到远端，然后关闭Channel
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
	}
	
	@Override						//在读操作异常被抛出时被调用
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();			//打印异常堆栈跟踪消息
		ctx.close();						//关闭这个Channel
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
