package person.stern.m.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.stern.m.utils.ByteConverter;

/**
 * @Author: zhouwei
 * @Description:
 * @Date: Create in 11:43 2018-12-18
 * @Modified By:
 */
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    byte[] receiveMsgBytes = new byte[msg.readableBytes()];
    msg.readBytes(receiveMsgBytes);
    String receiveHex = Hex.encodeHexString(receiveMsgBytes);
    LOGGER.info("服务端接收到数据包，内容为：{}", receiveHex);    //1

    String response = heartbeatGenerator();     //2
    ctx.writeAndFlush(response);                //3
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  //4
    cause.printStackTrace();
    ctx.close();
  }

  /**
   * 心跳回复数据帧生成器
   * @return
   */
  public static String heartbeatGenerator() {
    String response = "5a55" + "0006" + "1002" + "0f";
    response = response + ByteConverter.makeChecksum(response) + "6a69";
    return response;
  }
}
