package com.lingmoyun.instruction;

import com.lingmoyun.minilzo.MiniLZO;
import sun.nio.cs.ext.GBK;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * CPCL指令
 *
 * @author guoweifeng
 */
public class CPCL {
    public static final String QR_CODE_ECC_H = "H";
    public static final String QR_CODE_ECC_Q = "Q";
    public static final String QR_CODE_ECC_M = "M";
    public static final String QR_CODE_ECC_L = "L";

    //"\r"->0D  "\n"->0A
    static final String LINE = "\n";
    //"\r"->0D  "\n"->0A
    static final byte[] LINE_BYTES = toBytes(LINE);

    /**
     * 切纸，立即生效
     *
     * @param h 走纸h点后再切
     * @return CPCL
     */
    public static byte[] cut(int h) {
        return cut(h, -1);
    }

    /**
     * 切纸，立即生效
     *
     * @param h    走纸h点后再切
     * @param time 切纸时间，单位：ms
     * @return CPCL
     */
    public static byte[] cut(int h, int time) {
        if (time >= 0) {
            return toBytes("CUT " + h + "," + time + "" + LINE);
        } else {
            return toBytes("CUT " + h + LINE);
        }
    }

    /**
     * 开始标签
     *
     * @param offset 偏移量
     * @param height 最大高度
     * @param qty    打印份数
     * @return CPCL
     */
    public static byte[] area(int offset, int height, int qty) {
        return toBytes("! " + offset + " 203 203 " + height + " " + qty + LINE);
    }

    /**
     * 开始标签
     *
     * @param offset 偏移量
     * @param dpi    分辨率
     * @param height 最大高度
     * @param qty    打印份数
     * @return CPCL
     */
    public static byte[] area(int offset, int dpi, int height, int qty) {
        return toBytes("! " + offset + " " + dpi + " " + dpi + " " + height + " " + qty + LINE);
    }

    public static byte[] pageWidth(int width) {
        return toBytes("PW " + width + LINE);
    }

    public static byte[] density(int density) {
        return toBytes("DENSITY " + density + LINE);
    }

    /**
     * 文本
     *
     * @param font     字体
     * @param fontSize 字体大小
     * @param x        打印位置，x坐标，单位：点
     * @param y        打印位置，y坐标，单位：点
     * @param data     打印内容
     * @return CPCL
     */
    public static byte[] text(int font, int fontSize, int x, int y, String data) {
        return text(0, font, fontSize, x, y, data);
    }

    /**
     * 文本
     *
     * @param degree   角度 0/90/180/270
     * @param font     字体
     * @param fontSize 字体大小
     * @param x        打印位置，x坐标，单位：点
     * @param y        打印位置，y坐标，单位：点
     * @param data     打印内容
     * @return CPCL
     */
    public static byte[] text(int degree, int font, int fontSize, int x, int y, String data) {
        String command;
        switch (degree) {
            case 90:
                command = "TEXT90";
                break;
            case 180:
                command = "TEXT180";
                break;
            case 270:
                command = "TEXT270";
                break;
            default:
                command = "TEXT";
                break;
        }
        return toBytes(command + " " + font + " " + fontSize + " " + x + " " + y + " " + data + LINE);
    }

    /**
     * 绘制任何长度、宽度和角度方向的线条
     *
     * @param fromPoint 左上角的坐标
     * @param toPoint   (水平轴的右上角 or 垂直轴的左下角)的坐标
     * @param width     线条的单位宽度
     * @return CPCL
     */
    public static byte[] line(Point fromPoint, Point toPoint, int width) {
        return line(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y, width);
    }

    /**
     * 绘制任何长度、宽度和角度方向的线条
     *
     * @param x0    左上角的X 坐标
     * @param y0    左上角的Y 坐标
     * @param x1    (水平轴的右上角 or 垂直轴的左下角) 的Y 坐标
     * @param y1    (水平轴的右上角 or 垂直轴的左下角) 的Y 坐标
     * @param width 线条的单位宽度
     * @return CPCL
     */
    public static byte[] line(int x0, int y0, int x1, int y1, int width) {
        return toBytes("LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width + LINE);
    }

    /**
     * 条形码
     *
     * @param width  窄条的单位宽度
     * @param ratio  宽条与窄条的比率。
     *               0 = 1.5:1
     *               1 = 2.0:1
     *               2 = 2.5:1
     *               3 = 3.0:1
     *               4 = 3.5:1
     * @param height 条码的单位高度
     * @param x      打印位置，x坐标，单位：点
     * @param y      打印位置，y坐标，单位：点
     * @param data   条码数据
     * @return CPCL
     */
    public static byte[] barCode(int width, int ratio, int height, int x, int y, String data) {
        return toBytes("BARCODE 128 " + width + " " + ratio + " " + height + " " + x + " " + y + " " + data + LINE);
    }

    /**
     * 二维码
     *
     * @param x    打印位置，x坐标，单位：点
     * @param y    打印位置，y坐标，单位：点
     * @param data 二维码数据
     * @return CPCL
     */
    public static byte[] qrCode(int x, int y, String data) {
        return qrCode(QR_CODE_ECC_M, x, y, data);
    }

    /**
     * 二维码
     *
     * @param ecc  纠错级别
     *             H - 极高可靠性级别（ H 级） ；
     *             Q - 高可靠性级别（ Q 级） ；
     *             M - 标准级别（ M 级） ；
     *             L - 高密度级别（ L 级）
     * @param x    打印位置，x坐标，单位：点
     * @param y    打印位置，y坐标，单位：点
     * @param data 二维码数据
     * @return CPCL
     */
    public static byte[] qrCode(String ecc, int x, int y, String data) {
        return toBytes("BARCODE QR " + x + " " + y + " M 2 U 6"
                + LINE
                + ecc + "A," + data
                + LINE
                + "ENDQR"
                + LINE);
    }

    /**
     * 图片指令CG
     *
     * @param x        坐标x
     * @param y        坐标y
     * @param filename 文件路径
     * @return CPCL
     */
    public static byte[] imageCG(int x, int y, String filename) {
        return imageCG(x, y, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令CG
     *
     * @param x     坐标x
     * @param y     坐标y
     * @param image 图片
     * @return CPCL
     */
    public static byte[] imageCG(int x, int y, BufferedImage image) {
        byte[] bitmap = ImageUtils.image2Bitmap(image);
        return imageCG(image.getWidth(), image.getHeight(), x, y, bitmap);
    }

    /**
     * 图片指令CG
     *
     * @param w      宽，单位：px
     * @param h      高，单位：px
     * @param x      坐标x
     * @param y      坐标y
     * @param bitmap 位图
     * @return CPCL
     */
    public static byte[] imageCG(int w, int h, int x, int y, byte[] bitmap) {
        return merge(toBytes("CG " + ImageUtils.byteWidth(w) + " " + h + " " + x + " " + y + " "), bitmap, LINE_BYTES);
    }

    /**
     * 图片指令EG
     *
     * @param x        坐标x
     * @param y        坐标y
     * @param filename 文件路径
     * @return CPCL
     */
    public static byte[] imageEG(int x, int y, String filename) {
        return imageEG(x, y, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令EG
     *
     * @param x     坐标x
     * @param y     坐标y
     * @param image 图片
     * @return CPCL
     */
    public static byte[] imageEG(int x, int y, BufferedImage image) {
        byte[] bitmap = ImageUtils.image2Bitmap(image);

        // byte数组转十六进制
        StringBuilder builder = new StringBuilder();
        for (byte b : bitmap)
            builder.append(String.format("%02X", (b & 0xFF)));

        byte[] bitmapHex = toBytes(builder.toString());
        return merge(toBytes("EG " + ImageUtils.byteWidth(image.getWidth()) + " " + image.getHeight() + " " + x + " " + y + " "), bitmapHex, LINE_BYTES);
    }

    /**
     * 图片指令GG
     * GG w h x y size lzo(CG data)
     *
     * @param x        坐标x
     * @param y        坐标y
     * @param filename 文件路径
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, String filename) {
        return imageGG(x, y, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令GG
     * GG w h x y size lzo(CG data)
     *
     * @param x        坐标x
     * @param y        坐标y
     * @param maxSize  压缩数据最大值
     * @param filename 文件路径
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, int maxSize, String filename) {
        return imageGG(x, y, maxSize, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令GG
     * GG x y w h size lzo(CG data)
     *
     * @param x     坐标x
     * @param y     坐标y
     * @param image 图片
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, BufferedImage image) {
        return imageGG(x, y, 4096, image);
    }

    /**
     * 图片指令GG优化
     * GG w h x y size lzo(CG data)
     *
     * @param x       坐标x
     * @param y       坐标y
     * @param maxSize 压缩数据最大值
     * @param image   图片
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, int maxSize, BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int byteWidth = ImageUtils.byteWidth(width);
        int maxHeight = maxSize / byteWidth; // maxSize / (width / 8)
        int imageCount = (int) Math.ceil(height * 1.0 / maxHeight);

        byte[] bitmap = ImageUtils.image2Bitmap(image);

        ByteArrayOutputStream instructions = new ByteArrayOutputStream();
        for (int n = 0; n < imageCount; n++) {
            int subY = y + (maxHeight * n);
            int subHeight = (n == imageCount - 1) ? height - maxHeight * n : maxHeight;
            byte[] subBitmap = new byte[byteWidth * subHeight];
            System.arraycopy(bitmap, maxHeight * n * byteWidth, subBitmap, 0, subBitmap.length);

            byte[] subBitmapCompressed = null;
            try {
                subBitmapCompressed = MiniLZO.compress(subBitmap);
            } catch (Exception ignored) {
            }

            byte[] instruction;
            if (subBitmapCompressed == null || subBitmapCompressed.length == 0 || subBitmapCompressed.length >= subBitmap.length) {
                // 压缩失败或者压缩后比源数据还大，则不使用GG指令
                instruction = imageCG(width, subHeight, x, subY, subBitmap);
            } else {
                // 压缩成功，使用GG指令
                instruction = imageGG(width, subHeight, x, subY, subBitmapCompressed);
            }

            instructions.write(instruction, 0, instruction.length);
        }

        return instructions.toByteArray();
    }

    /**
     * 图片指令GG
     * GG w h x y size lzo(CG data)
     *
     * @param w                宽，单位：px
     * @param h                高，单位：px
     * @param x                坐标x
     * @param y                坐标y
     * @param bitmapCompressed 位图
     * @return CPCL
     */
    public static byte[] imageGG(int w, int h, int x, int y, byte[] bitmapCompressed) {
        return merge(toBytes("GG " + ImageUtils.byteWidth(w) + " " + h + " " + x + " " + y + " " + bitmapCompressed.length + " "), bitmapCompressed, LINE_BYTES);
    }

    /**
     * 走纸
     *
     * @return CPCL
     */
    public static byte[] form() {
        return toBytes("FORM" + LINE);
    }

    /**
     * 打印
     *
     * @return CPCL
     */
    public static byte[] print() {
        return toBytes("PRINT" + LINE);
    }

    /**
     * 走纸并打印
     *
     * @return CPCL
     */
    public static byte[] formPrint() {
        return merge(form(), print());
    }

    static byte[] toBytes(String s) {
        return s.getBytes(new GBK());
    }

    private static byte[] merge(byte[]... bytes) {
        if (bytes == null) return null;
        if (bytes.length == 1) return bytes[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte[] aByte : bytes) if (aByte != null) baos.write(aByte, 0, aByte.length);
        return baos.toByteArray();
    }


    /**
     * 蓝牙打印指令(CPCL)
     *
     * @author guoweifeng
     */
    static class ImageUtils {

        public static int byteWidth(int width) {
            return (width + 7) / 8;
        }

        /**
         * image -> 十六进制图像数据(CG)
         *
         * @param image 图片
         * @return 十六进制图像数据
         */
        public static byte[] image2Bitmap(BufferedImage image) {
            ByteArrayOutputStream bitmapHex = new ByteArrayOutputStream();
            // BufferedImage图片转为矩阵
            int w = image.getWidth();
            int h = image.getHeight();
            int byteWidth = byteWidth(w);
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < byteWidth; j++) {
                    int bin = 0;
                    for (int k = 0; k < 8; k++) {
                        int x = j * 8 + k;

                        bin = bin << 1;
                        if (x < w) { // 未超出边界
                            int rgb = image.getRGB(x, i);  //RGB
                            bin += rgb2Bin(rgb);
                        }
                    }

                    //CG指令
                    bitmapHex.write(bin);
                }
            }
            try {
                bitmapHex.flush();
                bitmapHex.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmapHex.toByteArray();
        }

        /**
         * 依次检查R、G、B是否超过阈值
         * 超过视为白色0，否则黑色1
         *
         * @param rgb rgb
         * @return 0 or 1
         */
        private static int rgb2Bin(int rgb) {
            int r = (rgb & 0x00ff0000) >> 16;
            int g = (rgb & 0x0000ff00) >> 8;
            int b = (rgb & 0x000000ff);

            //rgb2Grey
            int grey = (r * 38 + g * 75 + b * 15) >> 7;
            return grey > 0xff / 2 ? 0 : 1;
        }

        /**
         * 加载图片
         *
         * @param filename 图片路径
         * @return image
         */
        public static BufferedImage readImage(String filename) {
            try {
                BufferedImage image = ImageIO.read(new File(filename));
                // 非RGB色彩，转换为RGB色彩，否则会有问题，比如灰度图取RGB色值进行计算就会产生偏差
                if (image.getType() != BufferedImage.TYPE_INT_RGB || image.getType() != BufferedImage.TYPE_INT_ARGB) {
                    BufferedImage bimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D bGr = bimage.createGraphics();
                    bGr.drawImage(image, 0, 0, null);
                    bGr.dispose();
                    return bimage;
                }
                return image;
            } catch (IOException e) {
                throw new IllegalArgumentException("read image error. " + filename);
            }
        }

    }

}
