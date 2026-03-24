package fr.univrouen.sepa26.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import fr.univrouen.sepa26.model.TestSepa26;

@RestController
public class PostController {

    @RequestMapping(value = "/testpost", method = RequestMethod.POST,consumes = "application/xml")
    public String postTest(@RequestBody String flux) {
        return "<result><response>Message reçu : </response>" 
            + flux + "</result>";
    }
    
    @PostMapping(value = "/testload", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String testLoad() {
        TestSepa26 sepa = new TestSepa26();
        return sepa.loadFileXML();
    }
}
