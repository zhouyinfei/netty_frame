package com.zhuyun.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import java.net.InetSocketAddress;

import com.zhuyun.server.handler.ServerHandler;
import com.zhuyun.server.handler.OtaTcpDecoder;


/**
 * Netty ����˴���
 * 
 * @author zhouyinfei
 */
public class NettyServer {
	private final int port;
	
	public NettyServer(int port){
		this.port = port;
	}
	
	public void start() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();	//����EventLoopGroup
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();	//����ServerBootstrap
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)		//ָ��ʹ��һ��NIO����Channel
			.localAddress(new InetSocketAddress(port))	//��ָ���Ķ˿�����socket��ַ
			.childHandler(new ChannelInitializer<SocketChannel>() {	//��Channel��ChannelPipeline�м���EchoServerHandler
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ByteArrayDecoder());//�ֽ��������
					ch.pipeline().addLast(new OtaTcpDecoder());
					
					ch.pipeline().addLast(new ByteArrayEncoder());//�ֽ��������
					ch.pipeline().addLast(new ServerHandler());//EchoServerHandler��@Sharable�ģ��������ǿ���һֱ��ͬһ��ʵ��
				}
			})
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_REUSEADDR,true)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind().sync();//�첽�İ󶨷�������sync()һֱ�ȵ������
			System.out.println("�����������ɹ�...");
			
			
			f.channel().closeFuture().sync();//������Channel��CloseFuture��������ǰ�߳�ֱ���رղ������
		}finally{
			workerGroup.shutdownGracefully().sync();//�ر�EventLoopGroup���ͷ�������Դ
			bossGroup.shutdownGracefully().sync();//�ر�EventLoopGroup���ͷ�������Դ
		}
	}

	public static void main(String args[]) throws Exception {
		new NettyServer(9999).start();
		
	}
}