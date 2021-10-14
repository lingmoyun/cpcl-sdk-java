package com.lingmoyun.instruction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * CPCL指令构建器
 *
 * @author guoweifeng
 */
public class CpclBuilder {
    public static final String QR_CODE_ECC_H = "H";
    public static final String QR_CODE_ECC_Q = "Q";
    public static final String QR_CODE_ECC_M = "M";
    public static final String QR_CODE_ECC_L = "L";
    public static final String CMD_CG = "CG";
    public static final String CMD_EG = "EG";

    /**
     * A4标签开始指令
     *
     * @param offset 偏移量
     * @param qty    打印数量
     * @return CpclBuilder
     */
    public static CpclBuilder createA4Size(int offset, int qty) {
        //A4 = 210mm × 297mm   300dpi:2480px x 3508px   203dpi:1678px x 2374px
        return createAreaSize(offset, 1680, 2376, qty);
    }

    /**
     * 标签开始指令
     *
     * @param offset 偏移量
     * @param width  宽
     * @param height 高
     * @param qty    打印数量
     * @return CpclBuilder
     */
    public static CpclBuilder createAreaSize(int offset, int width, int height, int qty) {
        return new CpclBuilder().area(offset, width, height, qty);
    }


    //"\r"->0D  "\n"->0A
    private static final byte[] LINE = new byte[]{0x0A};

    private ByteArrayOutputStream baos;

    private CpclBuilder() {
        baos = new ByteArrayOutputStream();
    }

    public CpclBuilder area(int offset, int width, int height, int qty) {
        return append("! " + offset + " 203 203 " + height + " " + qty).line().pageWidth(width);
    }

    private CpclBuilder pageWidth(int width) {
        return append("PW " + width).line();
    }

    public CpclBuilder density(int density) {
        return append("DENSITY " + density).line();
    }

    /**
     * 文本
     *
     * @param font     字体
     * @param fontSize 字体大小
     * @param x        打印位置，x坐标，单位：点
     * @param y        打印位置，y坐标，单位：点
     * @param data     打印内容
     * @return CpclBuilder
     */
    public CpclBuilder text(int font, int fontSize, int x, int y, String data) {
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
     * @return CpclBuilder
     */
    public CpclBuilder text(int degree, int font, int fontSize, int x, int y, String data) {
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
        return append(command + " " + font + " " + fontSize + " " + x + " " + y + " " + data).line();
    }

    /**
     * 绘制任何长度、宽度和角度方向的线条
     *
     * @param fromPoint 左上角的坐标
     * @param toPoint   (水平轴的右上角 or 垂直轴的左下角)的坐标
     * @param width     线条的单位宽度
     * @return CpclBuilder
     */
    public CpclBuilder line(Point fromPoint, Point toPoint, int width) {
        return line(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y, width).line();
    }

    /**
     * 绘制任何长度、宽度和角度方向的线条
     *
     * @param x0    左上角的X 坐标
     * @param y0    左上角的Y 坐标
     * @param x1    (水平轴的右上角 or 垂直轴的左下角) 的Y 坐标
     * @param y1    (水平轴的右上角 or 垂直轴的左下角) 的Y 坐标
     * @param width 线条的单位宽度
     * @return CpclBuilder
     */
    public CpclBuilder line(int x0, int y0, int x1, int y1, int width) {
        return append("LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width).line();
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
     * @return CpclBuilder
     */
    public CpclBuilder barCode(int width, int ratio, int height, int x, int y, String data) {
        return append("BARCODE 128 " + width + " " + ratio + " " + height + " " + x + " " + y + " " + data).line();
    }

    /**
     * 二维码
     *
     * @param x    打印位置，x坐标，单位：点
     * @param y    打印位置，y坐标，单位：点
     * @param data 二维码数据
     * @return CpclBuilder
     */
    public CpclBuilder qrCode(int x, int y, String data) {
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
     * @return CpclBuilder
     */
    public CpclBuilder qrCode(String ecc, int x, int y, String data) {
        return append("BARCODE QR " + x + " " + y + " M 2 U 6")
                .line()
                .append(ecc + "A," + data)
                .line()
                .append("ENDQR")
                .line();
    }


    public CpclBuilder imageCG(BufferedImage image, int x, int y) {
        return image(CMD_CG, image, x, y);
    }

    public CpclBuilder imageEG(BufferedImage image, int x, int y) {
        return image(CMD_EG, image, x, y);
    }

    public CpclBuilder image(String cmd, String imagePath, int x, int y) {
        return image(cmd, new File(imagePath), x, y);
    }

    public CpclBuilder image(String cmd, File imageFile, int x, int y) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            return this;
        }
        return image(cmd, image, x, y);
    }

    public CpclBuilder image(String cmd, BufferedImage image, int x, int y) {
        image = ImageCmdUtils.formatImage(image);
        byte[] bitmap;
        switch (cmd) {
            case CMD_CG:
                bitmap = ImageCmdUtils.image2BitmapHex(image);
                break;
            case CMD_EG:
                bitmap = ImageCmdUtils.image2BitmapAscii(image);
                break;
            default:
                return this;
        }
        // 图片数据长度
        // System.out.println(bitmap.length);
        return append(cmd + " " + image.getWidth() / 8 + " " + image.getHeight() + " " + x + " " + y + " ")
                .append(bitmap)
                .line();
    }

    public CpclBuilder form() {
        return append("FORM").line();
    }

    public CpclBuilder print() {
        return append("PRINT").line();
    }

    public CpclBuilder formPrint() {
        return form().print();
    }

    public byte[] build() {
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private CpclBuilder line() {
        return append(LINE);
    }

    private CpclBuilder append(String s) {
        byte[] bytes;
        try {
            bytes = s.getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }
        return append(bytes);
    }

    private CpclBuilder append(byte[] bytes) {
        try {
            baos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 蓝牙打印指令(CPCL)
     *
     * @author guoweifeng
     */
    static class ImageCmdUtils {

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
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w / 8; j++) {
                    int bin = 0;
                    for (int k = 0; k < 8; k++) {
                        int rgb = image.getRGB(j * 8 + k, i);  //RGB
                        bin = (bin << 1) + rgb2Bin(rgb);
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
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w / 8; j++) {
                    int bin = 0;
                    for (int k = 0; k < 8; k++) {
                        int rgb = image.getRGB(j * 8 + k, i);  //RGB
                        bin = (bin << 1) + rgb2Bin(rgb);
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
         * fix width、height
         *
         * @param source 图片
         * @return fixed image
         */
        public static BufferedImage formatImage(BufferedImage source) {
            int w = source.getWidth();
            int h = source.getHeight();
            if (w % 8 == 0 && h % 8 == 0) return source;

            if (w % 8 != 0) {
                w = (w / 8 + 1) * 8;
            }

            if (h % 8 != 0) {
                h = (h / 8 + 1) * 8;
            }

            BufferedImage image = new BufferedImage(w, h, source.getType());
            Graphics2D graphics = image.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.fillRect(0, 0, w, h);
            graphics.drawImage(source, 0, 0, null);
            graphics.dispose();

            return image;
        }

    }

}
