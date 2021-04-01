package com.njxs.netty.server;

import com.njxs.netty.handler.HelloServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 1.创建了两个 NioEventLoopGroup 对象实例：bossGroup 和 workerGroup。
 *
 * bossGroup : 用于处理客户端的 TCP 连接请求。
 * workerGroup ： 负责每一条连接的具体读写数据的处理逻辑，真正负责 I/O 读写操作，交由对应的 Handler 处理。
 * 举个例子：我们把公司的老板当做 bossGroup，员工当做 workerGroup，bossGroup 在外面接完活之后，扔给 workerGroup 去处理。一般情况下我们会指定 bossGroup 的 线程数为 1（并发连接量不大的时候） ，workGroup 的线程数量为 CPU 核心数 *2 。另外，根据源码来看，使用 NioEventLoopGroup 类的无参构造函数设置线程数量的默认值就是 CPU 核心数 *2 。
 *
 * 2.创建一个服务端启动引导/辅助类： ServerBootstrap，这个类将引导我们进行服务端的启动工作。
 *
 * 3.通过 .group() 方法给引导类 ServerBootstrap 配置两大线程组，确定了线程模型。
 *
 *     EventLoopGroup bossGroup = new NioEventLoopGroup(1);
 *     EventLoopGroup workerGroup = new NioEventLoopGroup();
 * 4.通过channel()方法给引导类 ServerBootstrap指定了 IO 模型为NIO
 *
 * NioServerSocketChannel ：指定服务端的 IO 模型为 NIO，与 BIO 编程模型中的ServerSocket对应
 * NioSocketChannel : 指定客户端的 IO 模型为 NIO， 与 BIO 编程模型中的Socket对应
 * 5.通过 .childHandler()给引导类创建一个ChannelInitializer ，然后指定了服务端消息的业务处理逻辑也就是自定义的ChannelHandler 对象
 *
 * 6.调用 ServerBootstrap 类的 bind()方法绑定端口 。
 *
 * //bind()是异步的，但是，你可以通过 `sync()`方法将其变为同步。
 * ChannelFuture f = b.bind(port).sync();
 */
public class HelloServer {

    private int port;

    HelloServer(int port){
        this.port = port;
    }

    private void start(){
        // 1.bossGroup 用于接收连接，workerGroup 用于具体的处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        //2.创建服务端启动引导/辅助类：ServerBootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //3.给引导类配置两大线程组,确定了线程模型
        serverBootstrap.group(bossGroup,workGroup)
                // (非必备)打印日志
                .handler(new LoggingHandler(LogLevel.INFO))
                // 4.指定 IO 模型
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //5.可以自定义客户端消息的业务处理逻辑
                        pipeline.addLast(new HelloServerHandler());
                    }
                });

        try {
            // 6.绑定端口,调用 sync 方法阻塞，直到绑定完成
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            // 7.阻塞等待直到服务器Channel关闭(closeFuture()方法获取Channel 的CloseFuture对象,然后调用sync()方法)
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //8.优雅关闭相关线程组资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new HelloServer(9000).start();
    }
}
