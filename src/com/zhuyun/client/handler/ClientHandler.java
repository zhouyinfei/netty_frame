package com.zhuyun.client.handler;

import java.util.Arrays;

import com.zhuyun.util.DataParseUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable						//标记这个类的实例可以被多个Channel共享
public class ClientHandler extends SimpleChannelInboundHandler<byte[]>{
	private int counter;
	
	@Override							//和服务器的连接建立起来后被调用
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		//当收到连接成功的通知，发送一条消息
		  System.out.println("已经与Server建立连接。。。。");  
	      
	      byte[] b = {3,2,1};
	      for (int i = 0; i < 1000; i++) {
	    	  ctx.writeAndFlush(b);
		}
	      
	      
	}

	@Override							//从服务器收到一条消息时被调用
	protected void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {
		System.out.println("Client received: " + Arrays.toString(data) + ", counter=" + ++counter);//打印收到的消息到日志
//		ctx.close();
	}
	
	@Override							//处理过程中异常发生时被调用
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();//异常发生时，记录错误日志，关闭Channel
		ctx.close();
	}
	
}
