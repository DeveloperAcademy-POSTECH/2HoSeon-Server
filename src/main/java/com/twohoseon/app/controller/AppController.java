package com.twohoseon.app.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppController
 * @date : 11/30/23 1:37 PM
 * @modifyed : $
 **/
@Controller
public class AppController {
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://apps.apple.com/kr/app/wote/id6470252868");
    }
}
