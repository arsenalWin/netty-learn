package person.stern.m.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.stern.m.handler.DataFrameEncoder;
import person.stern.m.handler.ServerHandler;
import person.stern.m.utils.ByteConverter;

/**
 * @Author: zhouwei
 * @Description:
 * @Date: Create in 11:30 2018-12-18
 * @Modified By:
 */
public class NettyServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

  private EventLoopGroup bossGroup = new NioEventLoopGroup();
  private EventLoopGroup workerGroup = new NioEventLoopGroup();

  private int port = 8090;

  public void run() throws InterruptedException {
    final ByteBuf delimiter = Unpooled.copiedBuffer(ByteConverter.hexString2Bytes("6a69"));    //1

    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
              @Override
              public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))  //2
                        .addLast(new DataFrameEncoder())        //3
                        .addLast(new ServerHandler());
              }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

    // 绑定8090端口
    ChannelFuture f = b.bind(port).sync();

    if(f.isSuccess()){
      LOGGER.info("启动 Netty 服务端成功！");
    }

    // 阻塞线程一直到channel关闭
    f.channel().closeFuture().sync().channel();
  }
}
