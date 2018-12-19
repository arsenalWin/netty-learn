package person.stern.m.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.stern.m.utils.ByteConverter;

/**
 * @Author: zhouwei
 * @Description: netty客户端的处理程序
 * @Date: Create in 15:14 2018-11-12
 * @Modified By:
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) { //1
    byte[] receiveMsgBytes = new byte[msg.readableBytes()];
    msg.readBytes(receiveMsgBytes);
    String receiveHex = Hex.encodeHexString(receiveMsgBytes);
    LOGGER.info("客户端接收到数据包，内容为：{}", receiveHex);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    //每隔30秒主动发送心跳包
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      if (event.state() == IdleState.WRITER_IDLE) {  //2
        String dataFrame = sendDataFrameGenerator("88888888");
        ctx.writeAndFlush(dataFrame);
        LOGGER.info("客户端发出消息：{}", dataFrame);
      }
    }

    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  //3
    cause.printStackTrace();
    ctx.close();
  }

  /**
   * 心跳发送数据帧的生成器
   */
  public static String sendDataFrameGenerator(String cabinetNo) {
    String response = "5a55" + "000e" + "1002" + "0f" + cabinetNo;
    response = response + ByteConverter.makeChecksum(response) + "6a69";
    return response;
  }
}
