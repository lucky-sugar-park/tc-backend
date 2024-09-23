package com.mymes.equip.tc.plc.service.impl;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.mymes.equip.tc.plc.service.PlcConnectorInfo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlcConnectionTester {

	public static void main(String[] args) {
		PlcConnectorInfo pInfo=new PlcConnectorInfo();
		pInfo.setHostIp("70.50.121.187");
		pInfo.addPort(9991);
		pInfo.addVip("70.50.121.187");

		new PlcConnectionTester().testConnection(pInfo);
	}
	
	private Map<String, String> testConnection(PlcConnectorInfo pInfo) {
		log.debug("PlcManagementServiceImpl.testConnection(PlcInfo pInfo) is called.");

		EventLoopGroup workerGroup=new NioEventLoopGroup();
		Bootstrap b=new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.TCP_NODELAY, true);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
		b.option(ChannelOption.SO_REUSEADDR, true);
		b.handler(new ChannelInitializer<SocketChannel>() {	 
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new MessageToByteEncoder<byte[]>() {
					@Override
					protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
						out.writeBytes(msg);
					}
				});
				ch.pipeline().addLast(new ByteToMessageDecoder() {
					@Override
					protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
						byte frames[]=new byte[in.readableBytes()];
						out.add(in.readBytes(frames));
					}
				});
				ch.pipeline().addLast(new ChannelInboundHandlerAdapter());
			}
		});
		int ASSIGN_DYNAMIC_PORT_RANGE=0;
		try {
			ChannelFuture cf=b.connect(
					new InetSocketAddress(pInfo.getHostIp(), pInfo.getPorts().get(0)), 
					new InetSocketAddress(pInfo.getConnectionVips().get(0), ASSIGN_DYNAMIC_PORT_RANGE)).sync();
			cf.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture cf) throws Exception {
						if(!cf.isSuccess()) {
							System.out.println("########### Message send Fail for channel: " + cf.channel().id().asLongText() + ", cause:" + cf.cause());
						} else {
							System.out.println("########### Message send Success for channel: " + cf.channel().id());
						}
						cf.channel().disconnect();
						workerGroup.shutdownGracefully();
					}
				});
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.warn("Fail to connect to host: {}, port: {}", "", 0, e);
		}
		
		return null;
	}
}
