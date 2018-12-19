package person.stern.m.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.stern.m.utils.ByteConverter;


/**
 * @Author: zhouwei
 * @Description: 组包
 * @Date: Create in 17:38 2018-11-14
 * @Modified By:
 */
public class DataFrameEncoder extends MessageToByteEncoder<String> {
  private static Logger logger = LoggerFactory.getLogger(DataFrameEncoder.class);

  @Override
  protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
    byte[] bytes = ByteConverter.hexString2Bytes(msg);
    out.writeBytes(bytes) ;
  }
}
