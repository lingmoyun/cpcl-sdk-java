package com.lingmoyun.example;

import com.lingmoyun.instruction.CPCL;
import com.lingmoyun.instruction.CpclBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Example
 *
 * @author guoweifeng
 */
public class CpclExample {

    public static void main(String[] args) throws IOException {
        /* =====构建完整指令================================================================ */
        //byte[] cpcl = CpclBuilder.createAreaSize(0, 1680, 2374, 1) // Deprecated
        // 灵活构建
        //byte[] cpcl = CpclBuilder.newBuilder()
        //        .area(0, 203, 2374, 1)
        //        .pageWidth(1680) // 可选
        // 快捷构建
        //byte[] cpcl = CpclBuilder.createArea(0, 300, 2480, 3508, 1) // 300DPI
        byte[] cpcl = CpclBuilder.createArea(0, 203, 1680, 2374, 1) // 203DPI
                .text(0, 0, 500, 100, "Hello World!")
                .text(0, 0, 520, 150, "Hello World!")
                .text(0, 0, 540, 200, "Hello World!")
                .text(0, 0, 560, 250, "Hello World!")
                .text(0, 0, 580, 300, "Hello World!")
                .line(100, 100, 300, 100, 1)
                .line(100, 100, 300, 300, 2)
                .line(new Point(100, 100), new Point(100, 300), 3)
                .barCode(1, 1, 100, 100, 400, "A43009200005")
                .text(0, 0, 160, 510, "A43009200005")
                .qrCode(100, 600, "https://github.com/lingmoyun/cpcl-sdk-java")
                .qrCode(CPCL.QR_CODE_ECC_Q, 500, 600, "https://github.com/lingmoyun/cpcl-sdk-java")
                .imageEG(100, 800, "D:\\test.jpg")
                .imageCG(400, 800, "D:\\test.jpg")
                .imageGG(700, 800, "D:\\test.jpg")
                .imageGG(820, 1100, 4096, ImageIO.read(Files.newInputStream(Paths.get("D:\\test.jpg"))))
                // 手动拼接指令，下面三种方式等同
                .appendln("BARCODE QR " + 500 + " " + 600 + " M 2 U 6" + "\n" + "Q" + "A," + "https://github.com/lingmoyun/cpcl-sdk-java" + "\n" + "ENDQR")
                //.append("BARCODE QR " + 500 + " " + 600 + " M 2 U 6" + "\n" + "Q" + "A," + "https://github.com/lingmoyun/cpcl-sdk-java" + "\n" + "ENDQR" + "\n")
                //.append(("BARCODE QR " + 500 + " " + 600 + " M 2 U 6" + "\n" + "Q" + "A," + "https://github.com/lingmoyun/cpcl-sdk-java" + "\n" + "ENDQR" + "\n").getBytes("GBK"))
                .form()
                .print()
                //.formPrint()
                .cut(0) // 切刀指令，切纸，无切刀机器不受影响
                .build();
        String cpclHex = bytes2hex(cpcl);
        System.out.println(cpclHex);
        /* =====构建完整指令================================================================ */


        /* =====构建图片部分指令================================================================ */
        //byte[] imageGG = CPCL.imageGG(0, 0, 4096, "D:\\test.jpg");
        byte[] imageGG = CPCL.imageGG(0, 0, "D:\\test.jpg");
        String imageGGHex = bytes2hex(imageGG);
        System.out.println(imageGGHex);
        /* =====构建图片部分指令================================================================ */
    }

    /**
     * byte array -> hex string
     *
     * @param bytes byte array
     * @return hex string
     */
    public static String bytes2hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes)
            builder.append(String.format("%02X", (b & 0xFF)));

        return builder.toString();
    }

}
