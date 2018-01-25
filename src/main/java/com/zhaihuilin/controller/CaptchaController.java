package com.zhaihuilin.controller;

import com.google.code.kaptcha.Producer;
import com.zhaihuilin.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;


/**
 * Created by SunHaiyang on 2017/9/28.
 */
@Controller
public class CaptchaController {


    @Autowired
    private Producer captchaProducer;

    @Autowired
    JedisUtils jedisUtils;


    @GetMapping(value = "/code")
    public ModelAndView getKaptchaImage(
            @RequestParam(value = "key")String key, @RequestParam(value = "time")String random,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        System.out.println("******************Key: " + key + "******************");
        System.out.println("******************验证码是: " + capText + "******************");

       jedisUtils.set(key,capText,60);


        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();


        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }
}
