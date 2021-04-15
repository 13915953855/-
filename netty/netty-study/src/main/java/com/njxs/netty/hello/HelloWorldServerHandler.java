package com.njxs.netty.hello;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter{
    
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(System.currentTimeMillis()+ctx.channel().remoteAddress().toString()+"[HelloWorldServerHandler-channelActive]");
		super.channelActive(ctx);
	}
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(System.currentTimeMillis()+"server channelRead..");
        System.out.println(ctx.channel().remoteAddress()+"->Server :"+ msg.toString());
        ctx.writeAndFlush("我是服务端");
        //ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
