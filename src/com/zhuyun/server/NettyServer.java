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
 * Netty 服务端代码
 * 
 * @author zhouyinfei
 */
public class NettyServer {
	private final int port;
	
	public NettyServer(int port){
		this.port = port;
	}
	
	public void start() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();	//创建EventLoopGroup
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();	//创建ServerBootstrap
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)		//指定使用一个NIO传输Channel
			.localAddress(new InetSocketAddress(port))	//用指定的端口设置socket地址
			.childHandler(new ChannelInitializer<SocketChannel>() {	//在Channel的ChannelPipeline中加入EchoServerHandler
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ByteArrayDecoder());//字节数组解码
					ch.pipeline().addLast(new OtaTcpDecoder());
					
					ch.pipeline().addLast(new ByteArrayEncoder());//字节数组编码
					ch.pipeline().addLast(new ServerHandler());//EchoServerHandler是@Sharable的，所以我们可以一直用同一个实例
				}
			})
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_REUSEADDR,true)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind().sync();//异步的绑定服务器，sync()一直等到绑定完成
			System.out.println("服务器启动成功...");
			
			
			f.channel().closeFuture().sync();//获得这个Channel的CloseFuture，阻塞当前线程直到关闭操作完成
		}finally{
			workerGroup.shutdownGracefully().sync();//关闭EventLoopGroup，释放所有资源
			bossGroup.shutdownGracefully().sync();//关闭EventLoopGroup，释放所有资源
		}
	}

	public static void main(String args[]) throws Exception {
		new NettyServer(9999).start();
		
	}
}