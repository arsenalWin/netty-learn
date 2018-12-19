package person.stern.m.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ByteConverter {

    /**
     * 字节转10进制
     */
    public static int byte2Int(byte b) {
        int r = (int)b;
        return r;
    }

    /**
     * 10进制转字节
     */
    public static byte int2Byte(int i) {
        byte r = (byte)i;
        return r;
    }

    /**
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }
        return r;
    }

    /**
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    /**
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {
        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte)(charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    /**
     * 16进制字符串倒置
     */
    public static String hexStringInvert(String hex) {
        if (hex.isEmpty() || hex.length() % 2 != 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hex.length() / 2; i++) {
            sb.append(hex.substring(hex.length() - 2 * (i + 1), hex.length() - 2 * i));
        }
        return sb.toString();
    }

    /**
     * 16进制字符串按一个字节一组并用"-"连接
     */
    public static String hexStringConcat(String hex, String concat) {
        if (hex.isEmpty() || hex.length() % 2 != 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hex.length() / 2; i++) {
            sb.append(hex.substring(2 * i, 2 * (i + 1)));
            if (i != hex.length() / 2 - 1) {
                sb.append(concat);
            }
        }
        return sb.toString();
    }

    /**
     * 字节数组转字符串
     */
    public static String bytes2String(byte[] b) throws Exception {
        String r = new String(b, "UTF-8");
        return r;
    }

    /**
     * 字符串转字节数组
     */
    public static byte[] string2Bytes(String s) {
        byte[] r = s.getBytes();
        return r;
    }

    /**
     * 16进制字符串转字符串
     */
    public static String hex2String(String hex) throws Exception {
        String r = bytes2String(hexString2Bytes(hex));
        return r;
    }

    /**
     * 字符串转16进制字符串
     */
    public static String string2HexString(String s) throws Exception {
        String r = bytes2HexString(string2Bytes(s));
        return r;
    }

    /**
     * 16进制字符串计算校验和
     */
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF 
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字符串转为16进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 16进制字符串直接转换成为字符串(无需Unicode解码)
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte)(n & 0xff);
        }
        return new String(bytes);
    }

  /**
   * 16进制转成acsii
   * @param hex
   * @return
   */
  public static String hex2Ascii(String hex) {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < hex.length(); i += 2) {
      String str = hex.substring(i, i + 2);
      output.append((char)Integer.parseInt(str, 16));
    }
    return output.toString();
  }

  /**
   * ascii转成16进制
   * @param ascii
   * @return
   */
  public static String ascii2Hex(String ascii) {
    char[] chars = ascii.toCharArray();

    StringBuffer hex = new StringBuffer();
    for(int i = 0; i < chars.length; i++){
      hex.append(Integer.toHexString((int)chars[i]));
    }

    return hex.toString();
  }

  /**
   * 4 字节转 uint32
   * @param b
   * @return
   */
  public static int byteArrayToInt(byte[] b) {
    return   b[3] & 0xFF |
            (b[2] & 0xFF) << 8 |
            (b[1] & 0xFF) << 16 |
            (b[0] & 0xFF) << 24;
  }

  /**
   * int32 转byte[]
   * @param a
   * @return
   */
  public static byte[] intToByteArray(int a) {
    return new byte[] {
            (byte) ((a >> 24) & 0xFF),
            (byte) ((a >> 16) & 0xFF),
            (byte) ((a >> 8) & 0xFF),
            (byte) (a & 0xFF)
    };
  }

  /**
   * 16 str--> byte[4]---> int32
   * @param str
   * @return
   */
  public static int hexString2Bytes2Int(String str) {
    return byteArrayToInt(hexString2Bytes(str));
  }

  /**
   * int32 -> bytes[4] -> hexStr
   * @param time
   * @return
   */
  public static String int2Bytes2HexStr(int time) {
    return bytes2HexString(intToByteArray(time));
  }

    /** 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt * 1000L);
        res = simpleDateFormat.format(date);
        return res;
    }

    /** 
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 日期转换成字符串
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sf.format(date);
        return str;
    }
}
