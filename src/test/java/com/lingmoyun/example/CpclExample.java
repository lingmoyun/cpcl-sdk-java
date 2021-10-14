package com.lingmoyun.example;


import com.lingmoyun.instruction.CpclBuilder;

import java.awt.*;
import java.util.Base64;

/**
 * 示例
 *
 * @author guoweifeng
 */
public class CpclExample {

    public static void main(String[] args) throws Exception {
        /* =====构建指令================================================================ */
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
                .qrCode(CpclBuilder.QR_CODE_ECC_Q, 300, 580, "https://www.google.com")
                .image(CpclBuilder.CMD_CG, "D:\\test.jpg", 0, 0)
                .formPrint()
                .build();
        /* =====构建指令================================================================ */

        String base64 = new String(Base64.getEncoder().encode(cpcl));
        System.out.println(base64);
    }
}
