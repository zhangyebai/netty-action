package com.action.client;

import com.action.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Application {
	private static Channel channel = null;
	public static void main(String[] args) throws Exception {
		System.out.println("hello netty client.");
		/// start();
		new Thread(()->{
			try {
				Application.start();
			}catch (Exception e){

			}
		}).start();
		for(;;){
			if(null != channel){
				Scanner scanner = new Scanner(System.in);
				String data = scanner.nextLine();
				ByteBuf bf = Unpooled.buffer();
				bf.writeBytes(data.getBytes(CharsetUtil.UTF_8));
				channel.writeAndFlush(bf);
			}else{
				System.out.println("channel == null");
			}
		}
	}


	public static void start() throws Exception{
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try{
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress("localhost", 8080))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							ch.pipeline().addLast(new EchoClientHandler());
							channel  = ch;
						}
					});
			ChannelFuture channelFuture = bootstrap.connect().sync();
			channelFuture.channel().closeFuture().sync();
		}finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
}
