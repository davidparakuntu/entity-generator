package com.dav;

import com.dav.persistence.Persistence;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceUnitModifier {
    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(Persistence.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            File file = new File("src/main/resources/openjpa.xml");
            Persistence persistence = (Persistence) unmarshaller.unmarshal(getInputSource(file));

            List<Persistence.PersistenceUnit> pus = persistence.getPersistenceUnit();
            Persistence.PersistenceUnit pu = pus.get(0);

            String entitiesPath = args[0];
            File dir = new File(entitiesPath+ File.separator + args[1].replaceAll("\\.", "\\"+File.separator));
            List<String> classNames = getListOfClasses(dir, args[1]);
            pu.getClazz().addAll(classNames);

            File metaInf = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "META-INF");
            metaInf.mkdirs();
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
            marshaller.marshal(persistence, new File(metaInf.getAbsoluteFile() + File.separator + "persistence.xml"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static SAXSource getInputSource(File file) throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();

        NamespaceFilter inFilter = new NamespaceFilter("http://java.sun.com/xml/ns/persistence", "http://xmlns.jcp.org/xml/ns/persistence");
        inFilter.setParent(reader);

        InputSource is = new InputSource(new FileInputStream(file));
        SAXSource saxSource = new SAXSource(inFilter,is);
        return saxSource;
    }

    private static List<String> getListOfClasses(File dir, String packageName) throws FileNotFoundException {
        System.out.println("Reading classes from "+dir.getAbsolutePath());
        File[] files = dir.listFiles();
        List<String> classes = new ArrayList<>();
        for (File file : files) {
            classes.add(packageName + "." + file.getName().replace(".java",""));
        }
        return classes;
    }


}
