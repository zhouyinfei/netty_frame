package com.zhuyun.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import java.net.InetSocketAddress;

import com.zhuyun.client.handler.ClientHandler;

/**
 * Netty �ͻ��˴���
 * 
 * @author zhouyinfei
 */
public class NettyClient implements Runnable{
	private final String host;
	private final int port;
	
	public NettyClient(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();	//����Bootstrap
			b.group(group)	//ָ��EventLoopGroup������ͻ����¼�����ҪEventLoopGroup��NIOʵ��
			.channel(NioSocketChannel.class)		//����NIO�����Channel����
			.remoteAddress(new InetSocketAddress(host, port))	//���÷�������InetSocketAddress
			.handler(new ChannelInitializer<SocketChannel>() {	//��һ��Channel����ʱ����һ��EchoClientHandler��������pipeline��
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ByteArrayDecoder());//�ֽ��������
					ch.pipeline().addLast(new ByteArrayEncoder());//�ֽ��������
					ch.pipeline().addLast(new ClientHandler());
				}
			})
			.option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.connect().sync();			//���ӵ�Զ�ˣ�һֱ�ȵ��������
			f.channel().closeFuture().sync();				//һֱ������Channel�ر�
		}finally{
			group.shutdownGracefully().sync();				//�ر��������ӳأ��ͷ�������Դ
		}
		
	}
	
	@Override
	public void run() {
		try {
			for (int i = 0; i < 1000; i++) {
				this.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception {
//		ExecutorService es = Executors.newFixedThreadPool(1);
//		for (int i = 0; i < 1; i++) {
//			es.execute(new NettyClient("192.168.10.200", 9999));
//		}
		
			new NettyClient("localhost", 9999).start();
		
	}

	



}