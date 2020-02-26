package com.hord.JAXBParser.java;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class Demo {

    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        JAXBContext jc = JAXBContext.newInstance(SystemEmailTemplate.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        SystemEmailTemplate systemEmailTemplate = (SystemEmailTemplate) unmarshaller.unmarshal(new Demo().getFileFromResources("xml/New_User_Request.xml"));

        System.out.println(systemEmailTemplate);
    }

    private File getFileFromResources(String fileName) throws FileNotFoundException {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new FileNotFoundException();
        } else {
            return new File(resource.getFile());
        }

    }
}
