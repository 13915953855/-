package com.njxs.netty.client;

import com.njxs.netty.handler.HelloClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 1.创建一个 NioEventLoopGroup 对象实例 （服务端创建了两个 NioEventLoopGroup 对象）
 *
 * 2.创建客户端启动的引导类是 Bootstrap
 *
 * 3.通过 .group() 方法给引导类 Bootstrap 配置一个线程组
 *
 * 4.通过channel()方法给引导类 Bootstrap指定了 IO 模型为NIO
 *
 * 5.通过 .childHandler()给引导类创建一个ChannelInitializer ，然后指定了客户端消息的业务处理逻辑也就是自定义的ChannelHandler 对象
 *
 * 6.调用 Bootstrap 类的 connect()方法连接服务端，这个方法需要指定两个参数：
 *
 * inetHost : ip 地址
 * inetPort : 端口号
 */
public class HelloClient {
    private String host;
    private int port;
    private String message;

    public HelloClient(String host, int port, String message){
        this.host = host;
        this.port = port;
        this.message=message;
    }

    private void start(){
        //1.创建一个 NioEventLoopGroup 对象实例
        EventLoopGroup group = new NioEventLoopGroup();
        //2.创建客户端启动引导/辅助类：Bootstrap
        Bootstrap b = new Bootstrap();
        b.group(group)//3.指定线程组
                .channel(NioSocketChannel.class)//4.指定 IO 模型
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 5.这里可以自定义消息的业务处理逻辑
                        pipeline.addLast(new HelloClientHandler(message));
                    }
                });

        try {
            // 6.尝试建立连接
            ChannelFuture sync = b.connect(host, port)
                    .addListener(future -> {
                        if(future.isSuccess()){
                            System.err.println("连接成功");
                        }else{
                            System.err.println("连接失败");
                        }
                    })
                    .sync();
            // 7.等待连接关闭（阻塞，直到Channel关闭）
            sync.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HelloClient("127.0.0.1",9000,"你好啊，帅哥！").start();
    }
}
