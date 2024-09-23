package com.mymes.equip.vplc;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.mymes.equip.vplc.transport.Payload;
import com.mymes.equip.vplc.utils.ByteUtil;
import com.mymes.equip.vplc.utils.ByteUtil.Endian;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyByteBufTest {

	public static void main(String[] args) {
//		ByteBuf bb=Unpooled.buffer(2048);
//		bb.writeBytes(new String("Hello").getBytes());
//		System.out.println("READER INDEX: " + bb.readerIndex());
//		
//		byte[] readBytes=new byte[bb.readableBytes()];
//		bb.getBytes(bb.readerIndex(), readBytes);
//		
//		System.out.println("READ STRING: " + new String(readBytes));
//		System.out.println("READER INDEX: " + bb.readerIndex());
//		
//		byte[] newReadBytes=new byte[bb.readableBytes()];
//		bb.readBytes(newReadBytes, bb.readerIndex(), bb.readableBytes());
//		
//		System.out.println("NEW READ STRING: " + new String(newReadBytes));
//		System.out.println("AFTER READ, READER INDEX: " + bb.readerIndex());
//		new NettyByteBufTest().doTest();
		new NettyByteBufTest().byteConvTest();
	}
	
	private void byteConvTest() {
		int v=15;
		byte[] arr=ByteUtil.intToBytes(v, 2, Endian.BIG);
//		StringBuffer sb=new StringBuffer();
//		for(byte b:arr) {
//			sb.append(Integer.toString((b&0xff) + 0x100, 16).substring(1));
//		}
//		System.out.println("str :" + sb.toString());
		System.out.println("str :" + ByteUtil.byteToHexString(arr).toUpperCase());
//		byte[] asciiArr=sb.toString().toUpperCase().getBytes();
//		System.out.println(asciiArr[0]+","+asciiArr[1]+","+asciiArr[2]+","+asciiArr[3]);
		
		byte b=(byte)0xD0;
		System.out.println(b);
		
		byte barr[]=new byte[] {};
		System.out.println(barr.length);
	}
	
	private ChannelFuture cf1=null;
	private ChannelFuture cf2=null;
	
	private void doTest () {
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		Bootstrap b=new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
//		b.option(ChannelOption.TCP_NODELAY, true);
		// keep alive 신호를 주기적으로 주고 받음
//		b.option(ChannelOption.SO_KEEPALIVE, true);
//		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
//		b.option(ChannelOption.SO_REUSEADDR, true);
		
		NettyMessageHandler msgHandler=new NettyMessageHandler();
		
		b.handler(new ChannelInitializer<SocketChannel>() {	 
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new MessageEncoder());
				ch.pipeline().addLast(msgHandler);
			}
		});
		
		Bootstrap b2=new Bootstrap();
		b2.group(workerGroup);
		b2.channel(NioSocketChannel.class);
//		b.option(ChannelOption.TCP_NODELAY, true);
		// keep alive 신호를 주기적으로 주고 받음
//		b.option(ChannelOption.SO_KEEPALIVE, true);
//		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
//		b.option(ChannelOption.SO_REUSEADDR, true);
		
		b2.handler(new ChannelInitializer<SocketChannel>() {	 
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new MessageEncoder());
				ch.pipeline().addLast(msgHandler);
			}
		});
		
		try {
			ChannelFuture cf1=b.connect(new InetSocketAddress("70.50.121.187", 9991)).sync();
			cf1.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture cf) throws Exception {
					if(!cf.isSuccess()) {
						System.out.println("########### Fail to connect, channel: " + cf.channel().id().asLongText() + ", cause: " + cf.cause());
					} else {
						System.out.println("########### Success to connect, channel: " + cf.channel().id());
					}
				}
			});
//			cf1.channel().closeFuture().sync();
			this.cf1=cf1;
			
			ChannelFuture cf2=b2.connect(new InetSocketAddress("70.50.121.187", 9992)).sync();
			cf2.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture cf) throws Exception {
					if(!cf.isSuccess()) {
						System.out.println("########### Fail to connect, channel: " + cf.channel().id().asLongText() + ", cause: " + cf.cause());
					} else {
						System.out.println("########### Success to connect, channel: " + cf.channel().id());
					}
				}
			});
			this.cf2=cf2;
//			cf2.channel().closeFuture().sync();
//			System.out.println("CH1 ID: " + cc.id().asLongText());
//			System.out.println("CH2 ID: " + cf.channel().id().asLongText());
//			Payload payload=new Payload();
//			payload.setRawMessage("HELLO!!!".getBytes());
//			cf.channel().writeAndFlush(payload);
//			cf.channel().writeAndFlush(Unpooled.copiedBuffer("HI!!!!!!!!!!".getBytes()));
//			msgHandler.send("Hi there!!!");
//			Thread.sleep(1000);
//			send2("HI !!!!!");
//			workerGroup.shutdownGracefully();
			Executors.newCachedThreadPool().execute(()->{
				int times=0;
				while(times++<10) {
					send3("Say hello : " + times);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				workerGroup.shutdownGracefully();
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("END OF FUNCTION !!!");
		}
	}
	
	private void send3(String message) {
		System.out.println("CH2 ID: " + cf1.channel().id().asLongText());
		Payload payload=new Payload();
		payload.setRawMessage(message.getBytes());
		cf1.channel().writeAndFlush(payload);
		
//		boolean rarraved=false;
//		long current=new Date().getTime(), timeout=3000;
//		while(response.isArrived()==false && new Date().getTime()-current<timeout) {
//			TimerUtil.waitWithSleep(50);
//		}
	}
	
	class MessageEncoder extends MessageToByteEncoder<Payload> {
		
		public MessageEncoder() {
		}

		@Override
		protected void encode(ChannelHandlerContext ctx, Payload msg, ByteBuf out) throws Exception {
			System.out.println("@@@@@ MessageEncoder.encode(ChannelHandlerContext ctx, Payload msg, ByteBuf out)...");
			System.out.println("@@@@@ ChannelId: " + ctx.channel().id());

			out.writeBytes(msg.getRawMessage());
		}
	}
	
	@Sharable
	class NettyMessageHandler extends ChannelInboundHandlerAdapter {
		
		private Channel channel=null;
		
		public void send(String message) {
			if(channel!=null) {
				channel.writeAndFlush(Unpooled.copiedBuffer(message, StandardCharsets.UTF_8));
			}
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			super.channelRead(ctx, msg);
		}
		
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			System.out.println("Channel active...");
			if(this.channel==null) this.channel=ctx.channel();
		}
		
		@Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//	        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	        super.channelReadComplete(ctx);
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	        super.exceptionCaught(ctx, cause);
	    }
	    
	    public void channelInactive(ChannelHandlerContext ctx) throws java.lang.Exception {
	    	super.channelInactive(ctx);
	    }	
	}
}
