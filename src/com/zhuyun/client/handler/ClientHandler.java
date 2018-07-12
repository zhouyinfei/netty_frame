package com.zhuyun.client.handler;

import java.util.Arrays;

import com.zhuyun.util.DataParseUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable						//���������ʵ�����Ա����Channel����
public class ClientHandler extends SimpleChannelInboundHandler<byte[]>{
	private int counter;
	
	@Override							//�ͷ����������ӽ��������󱻵���
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		//���յ����ӳɹ���֪ͨ������һ����Ϣ
		  System.out.println("�Ѿ���Server�������ӡ�������");  
	      
	      byte[] b = {3,2,1};
	      for (int i = 0; i < 1000; i++) {
	    	  ctx.writeAndFlush(b);
		}
	      
	      
	}

	@Override							//�ӷ������յ�һ����Ϣʱ������
	protected void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {
		System.out.println("Client received: " + Arrays.toString(data) + ", counter=" + ++counter);//��ӡ�յ�����Ϣ����־
//		ctx.close();
	}
	
	@Override							//����������쳣����ʱ������
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();//�쳣����ʱ����¼������־���ر�Channel
		ctx.close();
	}
	
}
