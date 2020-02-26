package com.hord.JAXBParser.java;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SystemEmailTemplate")
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemEmailTemplate {

    @XmlElement(name="Template")
    Template template;

    @XmlElement(name="LocalizedValues")
    LocalizedValues localizedValues;

    public SystemEmailTemplate() {
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public LocalizedValues getLocalizedValues() {
        return localizedValues;
    }

    public void setLocalizedValues(LocalizedValues localizedValues) {
        this.localizedValues = localizedValues;
    }
}
