package fr.univrouen.sepa26.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController {

    @RequestMapping(value = {"/erreur", "/error"}, produces = MediaType.TEXT_HTML_VALUE)
    public String handleErrorHTML(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String path = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        if (statusCode == null) statusCode = 500;

        return "<!DOCTYPE html>" +
               "<html lang='fr'><head><meta charset='UTF-8'/>" +
               "<title>Erreur " + statusCode + " - SEPA26</title>" +
               "<style>" +
               "* { margin: 0; padding: 0; box-sizing: border-box; }" +
               "body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(135deg, #1a1a2e, #16213e, #0f3460); min-height: 100vh; display: flex; align-items: center; justify-content: center; color: white; }" +
               ".container { text-align: center; padding: 40px; }" +
               ".error-code { font-size: 120px; font-weight: bold; color: #e94560; text-shadow: 0 0 30px rgba(233,69,96,0.5); line-height: 1; }" +
               ".error-title { font-size: 28px; margin: 20px 0 10px; color: #a8dadc; }" +
               ".error-path { font-size: 14px; color: #888; margin-bottom: 40px; background: rgba(255,255,255,0.05); padding: 10px 20px; border-radius: 20px; display: inline-block; }" +
               ".buttons { display: flex; gap: 15px; justify-content: center; flex-wrap: wrap; }" +
               ".btn { padding: 12px 30px; border-radius: 25px; text-decoration: none; font-weight: bold; font-size: 15px; transition: all 0.3s; }" +
               ".btn-home { background: #e94560; color: white; }" +
               ".btn-home:hover { background: #c73652; transform: translateY(-2px); }" +
               ".btn-jenkins { background: transparent; color: #a8dadc; border: 2px solid #a8dadc; }" +
               ".btn-jenkins:hover { background: #a8dadc; color: #1a1a2e; transform: translateY(-2px); }" +
               ".sepa-logo { font-size: 18px; color: #a8dadc; margin-bottom: 30px; letter-spacing: 3px; opacity: 0.7; }" +
               "</style></head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='sepa-logo'>SEPA26 SERVICE REST</div>" +
               "<div class='error-code'>" + statusCode + "</div>" +
               "<div class='error-title'>Page introuvable</div>" +
               "<div class='error-path'> " + path + "</div>" +
               "<div class='buttons'>" +
               "<a href='/' class='btn btn-home'> Accueil</a>" +
               "<a href='http://10.130.162.184:8080' class='btn btn-jenkins'> Jenkins</a>" +
               "</div>" +
               "</div>" +
               "</body></html>";
    }

    @RequestMapping(value = {"/erreur", "/error"}, produces = MediaType.APPLICATION_XML_VALUE)
    public String handleErrorXML(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        String path = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        if (statusCode == null) statusCode = 500;

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<error><status>" + statusCode + "</status>" +
               "<message>" + (message != null ? message : "Erreur inconnue") + "</message>" +
               "<path>" + path + "</path></error>";
    }
}