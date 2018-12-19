package person.stern.m.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.stern.m.handler.ClientHandler;
import person.stern.m.handler.DataFrameEncoder;
import person.stern.m.utils.ByteConverter;

/** @Author: zhouwei @Description: @Date: Create in 14:09 2018-12-19 @Modified By: */
public class NettyClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

  private EventLoopGroup workerGroup = new NioEventLoopGroup();

  public void run() throws Exception {
    final ByteBuf delimiter = Unpooled.copiedBuffer(ByteConverter.hexString2Bytes("6a69"));

    Bootstrap bootstrap = new Bootstrap();                 //1
    bootstrap.group(workerGroup);                          //2
    bootstrap.channel(NioSocketChannel.class);             //3
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);    //4
    bootstrap.handler(
        new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel
                .pipeline()
                .addLast(new IdleStateHandler(0, 5, 0)) // 5
                .addLast(new DelimiterBasedFrameDecoder(1024, delimiter)) // 6
                .addLast(new DataFrameEncoder())        // 7
                .addLast(new ClientHandler());          // 8
          }
        });

    // 启动客户端
    ChannelFuture future = bootstrap.connect("127.0.0.1", 8090).sync();    //9

    // Wait until the connection is closed.
    future.channel().closeFuture().sync();
  }

  public static void main(String[] args) throws Exception {
    NettyClient client = new NettyClient();
    client.run();
  }
}
