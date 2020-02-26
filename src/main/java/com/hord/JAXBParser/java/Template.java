package com.hord.JAXBParser.java;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Template {

    @XmlElement(name="Name")
    private String name;

    @XmlElement(name="Subject")
    private String subject;

    @XmlElement(name="Body")
    private String body;

    @XmlElement(name="From")
    private String from;

    @XmlElement(name="Cc")
    private String cc;

    @XmlElement(name="Bcc")
    private String bcc;

    @XmlElement(name="NoEmail")
    private boolean noEmail;

    @XmlElement(name="IsApproval")
    private boolean isApproval;

    @XmlElement(name="Unzip")
    private boolean unzip;

    @XmlElement(name="AttachGeneratedDocument")
    private boolean attachGeneratedDocument;

    @XmlElement(name="Attachments")
    private String attachments;

    public Template() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public boolean isNoEmail() {
        return noEmail;
    }

    public void setNoEmail(boolean noEmail) {
        this.noEmail = noEmail;
    }

    public boolean isApproval() {
        return isApproval;
    }

    public void setApproval(boolean approval) {
        isApproval = approval;
    }

    public boolean isUnzip() {
        return unzip;
    }

    public void setUnzip(boolean unzip) {
        this.unzip = unzip;
    }

    public boolean isAttachGeneratedDocument() {
        return attachGeneratedDocument;
    }

    public void setAttachGeneratedDocument(boolean attachGeneratedDocument) {
        this.attachGeneratedDocument = attachGeneratedDocument;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
