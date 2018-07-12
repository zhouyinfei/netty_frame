package com.zhuyun.server.handler;


import java.util.Arrays;

import com.zhuyun.util.DataParseUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable			//����һ��ChannelHandler���Ա����Channel��ȫ�Ĺ���
public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {
	private int counter;
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {// ÿ���յ���Ϣʱ������
		System.out.println("Server receive: " + Arrays.toString(data) + "; counter=" + ++counter);
		ctx.writeAndFlush(data);
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
