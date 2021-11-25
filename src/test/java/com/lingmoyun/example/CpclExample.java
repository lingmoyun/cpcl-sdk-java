package com.lingmoyun.example;

import com.lingmoyun.instruction.CPCL;
import com.lingmoyun.instruction.CpclBuilder;

import java.awt.*;
import java.util.Base64;

/**
 * 示例
 *
 * @author guoweifeng
 */
public class CpclExample {

    public static void main(String[] args) {
        /* =====构建完整指令================================================================ */
        byte[] cpcl = CpclBuilder.createAreaSize(0, 608, 1040, 1)
                .text(222, 0, 0, 2, "你好123abc")
                .text(222, 2, 0, 47, "你好123abc")
                .text(222, 3, 0, 87, "你好123abc")
                .text(222, 4, 0, 139, "你好123abc")
                .text(222, 5, 0, 215, "你好123abc")
                .text(222, 6, 0, 295, "你好123abc")
                .line(300, 100, 300, 300, 1)
                .line(300, 100, 500, 100, 2)
                .line(new Point(300, 100), new Point(500, 300), 3)
                .barCode(1, 1, 100, 100, 400, "A43009200005")
                .text(0, 0, 160, 510, "A43009200005")
                .qrCode(100, 580, "https://www.google.com")
                .qrCode(CPCL.QR_CODE_ECC_Q, 300, 580, "https://www.google.com")
                .imageCG(0, 0, "D:\\test.jpg")
                .formPrint()
                .build();
        String base64 = new String(Base64.getEncoder().encode(cpcl));
        System.out.println(base64);
        /* =====构建完整指令================================================================ */


        /* =====构建图片部分指令================================================================ */
        //byte[] imageGG = CPCL.imageGG(0, 0, 4096, "D:\\test.jpg");
        byte[] imageGG = CPCL.imageGG(0, 0, "D:\\test.jpg");
        String imageGGBase64 = new String(Base64.getEncoder().encode(imageGG));
        System.out.println(imageGGBase64);
        /* =====构建图片部分指令================================================================ */
    }
}
