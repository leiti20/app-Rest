package fr.univrouen.sepa26.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class TestSepa26 {

    public String loadFileXML() {
        try {
            Resource resource = new DefaultResourceLoader().getResource("classpath:xml/testsepa.xml");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();

        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}