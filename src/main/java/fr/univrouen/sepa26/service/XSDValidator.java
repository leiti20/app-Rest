package fr.univrouen.sepa26.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

@Service
public class XSDValidator {

    public String validate(String xmlContent) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            Schema schema = factory.newSchema(new StreamSource(
                new ClassPathResource("xml/sepa26.tp1.xsd").getInputStream()
            ));
            
            Validator validator = schema.newValidator();
    
            validator.validate(new StreamSource(new StringReader(xmlContent)));
            return null; 
            
        } catch (SAXException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Erreur interne lors de la validation : " + e.getMessage();
        }
    }
}