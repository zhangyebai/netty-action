package com.action.server;

import com.action.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ForkJoinPool;

public class Application {
	public static void main(String[] args) throws Exception {
		System.out.println("hello netty server.");
		startEcho();
		//ForkJoinPool
	}

	private static void startEcho() throws Exception{
		final EchoServerHandler echoServerHandler = new EchoServerHandler();
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		try {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(eventLoopGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(8080))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(echoServerHandler);
					}
				});

			ChannelFuture channelFuture = serverBootstrap.bind().sync();
			System.out.println(Application.class.getName() + "started at " + channelFuture.channel().localAddress());
			channelFuture.channel().closeFuture().sync();
		}finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
}
