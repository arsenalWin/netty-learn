package person.stern.m;

import person.stern.m.server.NettyServer;

/**
 * @Author: zhouwei
 * @Description:
 * @Date: Create in 11:27 2018-12-18
 * @Modified By:
 */
public class App {
  public static void main(String[] args) throws InterruptedException {
    NettyServer server = new NettyServer();
    server.run();
  }
}
