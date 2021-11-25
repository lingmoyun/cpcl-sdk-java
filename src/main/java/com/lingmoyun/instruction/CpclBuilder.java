package com.lingmoyun.instruction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * CPCL指令构建器
 *
 * @author guoweifeng
 */
public class CpclBuilder {

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

    private ByteArrayOutputStream baos;

    private CpclBuilder() {
        baos = new ByteArrayOutputStream();
    }

    public CpclBuilder area(int offset, int height, int qty) {
        return append(CPCL.area(offset, height, qty));
    }

    public CpclBuilder area(int offset, int width, int height, int qty) {
        return append(CPCL.area(offset, width, height, qty));
    }

    public CpclBuilder pageWidth(int width) {
        return append(CPCL.pageWidth(width));
    }

    public CpclBuilder density(int density) {
        return append(CPCL.density(density));
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
        return append(CPCL.text(font, fontSize, x, y, data));
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
        return append(CPCL.text(degree, font, fontSize, x, y, data));
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
        return append(CPCL.line(fromPoint, toPoint, width));
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
        return append(CPCL.line(x0, y0, x1, y1, width));
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
        return append(CPCL.barCode(width, ratio, height, x, y, data));
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
        return append(CPCL.qrCode(x, y, data));
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
        return append(CPCL.qrCode(ecc, x, y, data));
    }

    /**
     * 图片指令CG
     *
     * @param filename 文件路径
     * @param x        坐标x
     * @param y        坐标y
     * @return CPCL
     */
    public CpclBuilder imageCG(int x, int y, String filename) {
        return append(CPCL.imageCG(x, y, filename));
    }

    /**
     * 图片指令CG
     *
     * @param image 图片
     * @param x     坐标x
     * @param y     坐标y
     * @return CPCL
     */
    public CpclBuilder imageCG(int x, int y, BufferedImage image) {
        return append(CPCL.imageCG(x, y, image));
    }

    /**
     * 图片指令EG
     *
     * @param filename 文件路径
     * @param x        坐标x
     * @param y        坐标y
     * @return CPCL
     */
    public CpclBuilder imageEG(int x, int y, String filename) {
        return append(CPCL.imageEG(x, y, filename));
    }

    /**
     * 图片指令EG
     *
     * @param image 图片
     * @param x     坐标x
     * @param y     坐标y
     * @return CpclBuilder
     */
    public CpclBuilder imageEG(int x, int y, BufferedImage image) {
        return append(CPCL.imageEG(x, y, image));
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
    public CpclBuilder imageGG(int x, int y, String filename) {
        return append(CPCL.imageGG(x, y, filename));
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
    public CpclBuilder imageGG(int x, int y, int maxSize, String filename) {
        return append(CPCL.imageGG(x, y, maxSize, filename));
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
    public CpclBuilder imageGG(int x, int y, BufferedImage image) {
        return append(CPCL.imageGG(x, y, image));
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
    public CpclBuilder imageGG(int x, int y, int maxSize, BufferedImage image) {
        return append(CPCL.imageGG(x, y, maxSize, image));
    }

    public CpclBuilder form() {
        return append(CPCL.form());
    }

    public CpclBuilder print() {
        return append(CPCL.print());
    }

    public CpclBuilder formPrint() {
        return append(CPCL.formPrint());
    }

    public byte[] build() {
        return baos.toByteArray();
    }

    private CpclBuilder append(byte[] bytes) {
        baos.write(bytes, 0, bytes.length);
        return this;
    }

}
