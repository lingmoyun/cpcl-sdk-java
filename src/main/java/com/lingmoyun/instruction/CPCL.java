package com.lingmoyun.instruction;

import com.lingmoyun.minilzo.MiniLZO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
    private static final String LINE = "\n";
    //"\r"->0D  "\n"->0A
    private static final byte[] LINE_BYTES = toBytes(LINE);

    /**
     * 开始标签
     *
     * @param offset 偏移量
     * @param height 最大高度
     * @param qty    打印分数
     * @return CPCL
     */
    public static byte[] area(int offset, int height, int qty) {
        return toBytes("! " + offset + " 203 203 " + height + " " + qty + LINE);
    }

    /**
     * 开始标签
     *
     * @param offset 偏移量
     * @param width  宽度
     * @param height 最大高度
     * @param qty    打印分数
     * @return CPCL
     */
    public static byte[] area(int offset, int width, int height, int qty) {
        return merge(toBytes("! " + offset + " 203 203 " + height + " " + qty), LINE_BYTES, pageWidth(width));
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
     * @param filename 文件路径
     * @param x        坐标x
     * @param y        坐标y
     * @return CPCL
     */
    public static byte[] imageCG(int x, int y, String filename) {
        return imageCG(x, y, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令CG
     *
     * @param image 图片
     * @param x     坐标x
     * @param y     坐标y
     * @return CPCL
     */
    public static byte[] imageCG(int x, int y, BufferedImage image) {
        byte[] bitmap = ImageUtils.image2BitmapHex(image);
        return merge(toBytes("CG " + ImageUtils.byteWidth(image.getWidth()) + " " + image.getHeight() + " " + x + " " + y + " "), bitmap, LINE_BYTES);
    }

    /**
     * 图片指令EG
     *
     * @param filename 文件路径
     * @param x        坐标x
     * @param y        坐标y
     * @return CPCL
     */
    public static byte[] imageEG(int x, int y, String filename) {
        return imageEG(x, y, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令EG
     *
     * @param image 图片
     * @param x     坐标x
     * @param y     坐标y
     * @return CPCL
     */
    public static byte[] imageEG(int x, int y, BufferedImage image) {
        byte[] bitmap = ImageUtils.image2BitmapAscii(image);
        return merge(toBytes("EG " + ImageUtils.byteWidth(image.getWidth()) + " " + image.getHeight() + " " + x + " " + y + " "), bitmap, LINE_BYTES);
    }

    /**
     * 图片指令GG
     * GG x y w h size lzo(CG data)
     *
     * @param filename 文件路径
     * @param x        坐标x
     * @param y        坐标y
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, String filename) {
        return imageGG(x, y, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令GG
     * GG x y w h size lzo(CG data)
     *
     * @param filename 文件路径
     * @param x        坐标x
     * @param y        坐标y
     * @param maxSize  压缩数据最大值
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, int maxSize, String filename) {
        return imageGG(x, y, maxSize, ImageUtils.readImage(filename));
    }

    /**
     * 图片指令GG
     * GG x y w h size lzo(CG data)
     *
     * @param image 图片
     * @param x     坐标x
     * @param y     坐标y
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, BufferedImage image) {
        return imageGG(x, y, 4096, image);
    }

    /**
     * 图片指令GG
     * GG x y w h size lzo(CG data)
     *
     * @param image   图片
     * @param x       坐标x
     * @param y       坐标y
     * @param maxSize 压缩数据最大值
     * @return CPCL
     */
    public static byte[] imageGG(int x, int y, int maxSize, BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int byteWidth = ImageUtils.byteWidth(width);
        int maxHeight = maxSize / byteWidth; // maxSize / (width / 8)
        int imageCount = (int) Math.ceil(height * 1.0 / maxHeight);

        ByteArrayOutputStream instructions = new ByteArrayOutputStream();
        for (int n = 0; n < imageCount; n++) {
            BufferedImage subimage = image.getSubimage(0, maxHeight * n, width, (n == imageCount - 1) ? height - maxHeight * n : maxHeight);

            byte[] bitmap = ImageUtils.image2BitmapHex(subimage);
            byte[] bitmapCompressed = MiniLZO.compress(bitmap);

            byte[] instruction = merge(toBytes("GG " + byteWidth + " " + subimage.getHeight() + " " + x + " " + (y + (maxHeight * n)) + " " + bitmapCompressed.length + " "), bitmapCompressed, LINE_BYTES);
            instructions.write(instruction, 0, instruction.length);
        }
        return instructions.toByteArray();
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

    private static byte[] toBytes(String s) {
        byte[] bytes;
        try {
            bytes = s.getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }
        return bytes;
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
        public static byte[] image2BitmapHex(BufferedImage image) {
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
         * image -> Ascii图像数据(EG)
         *
         * @param image 图片
         * @return Ascii图像数据
         */
        public static byte[] image2BitmapAscii(BufferedImage image) {
            // BufferedImage图片转为矩阵
            int w = image.getWidth();
            int h = image.getHeight();
            int byteWidth = byteWidth(w);
            StringBuilder hex = new StringBuilder();
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

                    //EG指令
                    hex.append(String.format("%02X", bin));
                }
            }

            return hex.toString().toUpperCase().getBytes();
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
                return ImageIO.read(new File(filename));
            } catch (IOException e) {
                throw new IllegalArgumentException("read image error. " + filename);
            }
        }

    }

}
