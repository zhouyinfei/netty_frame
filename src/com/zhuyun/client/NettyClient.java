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
 * Netty 客户端代码
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
			Bootstrap b = new Bootstrap();	//创建Bootstrap
			b.group(group)	//指定EventLoopGroup来处理客户端事件；需要EventLoopGroup的NIO实现
			.channel(NioSocketChannel.class)		//用于NIO传输的Channel类型
			.remoteAddress(new InetSocketAddress(host, port))	//设置服务器的InetSocketAddress
			.handler(new ChannelInitializer<SocketChannel>() {	//当一个Channel创建时，把一个EchoClientHandler加入它的pipeline中
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ByteArrayDecoder());//字节数组解码
					ch.pipeline().addLast(new ByteArrayEncoder());//字节数组编码
					ch.pipeline().addLast(new ClientHandler());
				}
			})
			.option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.connect().sync();			//连接到远端，一直等到连接完成
			f.channel().closeFuture().sync();				//一直阻塞到Channel关闭
		}finally{
			group.shutdownGracefully().sync();				//关闭所有连接池，释放所有资源
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