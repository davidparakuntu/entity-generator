package com.dav;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

public class NamespaceFilter extends XMLFilterImpl{
    private final String fromURI;
    private final String toURI;

    public NamespaceFilter(String fromURI, String toURI){
        this.fromURI = fromURI;
        this.toURI = toURI;
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if(uri != null && uri.equals(fromURI)){
            super.startElement(toURI,localName,qName,atts);
        }else {
            super.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(uri != null && uri.equals(fromURI)){
            super.endElement(toURI,localName,qName);
        }else {
            super.endElement(uri, localName, qName);
        }
    }
}
