package cn.com.flaginfo.platform.sso.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ImageCodeUtils {

    private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K', 'L',
            'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y',
            'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };


    /**
     * 生成验证码图片
     * @param width 图片的宽度 160
     * @param height 图片的高度 40
     * @param codeCount 验证码字符个数 4
     * @param lineCount 验证码干扰线数 100
     */
    public static Map<String, Object> generate(int width, int height, int codeCount, int lineCount) {
        int x, fontHeight, codeY;
        int red, green, blue;

        x = width / (codeCount +2);//每个字符的宽度
        fontHeight = height - 2;//字体的高度
        codeY = height - 4;

        // 图像buffer
        BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体
        Font font = new Font("Times New Roman",Font.ITALIC, fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs+random.nextInt(width/8);
            int ye = ys+random.nextInt(height/8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // randomCode记录随机产生的验证码
        StringBuilder randomCode = new StringBuilder();
        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", randomCode.toString());
        map.put("image", buffImg);
        return map;
    }
}
