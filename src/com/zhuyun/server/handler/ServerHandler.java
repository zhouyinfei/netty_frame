package com.zhuyun.server.handler;


import java.util.Arrays;

import com.zhuyun.util.DataParseUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable			//表明一个ChannelHandler可以被多个Channel安全的共享
public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {
	private int counter;
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {// 每次收到消息时被调用
		System.out.println("Server receive: " + Arrays.toString(data) + "; counter=" + ++counter);
		ctx.writeAndFlush(data);
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
