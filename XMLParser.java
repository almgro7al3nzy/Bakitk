package com.wassemsy.bekity;

import android.util.Log;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {

    public Document getDomElement(String str) {
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(str));
            return newDocumentBuilder.parse(inputSource);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Log.e("Bekity1: ", e.getMessage());
            return null;
        }
    }

    public final String getElementValue(Node node) {
        if (node != null && node.hasChildNodes()) {
            for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 3) {
                    return firstChild.getNodeValue();
                }
            }
        }
        return "";
    }

    public String getValue(Element element, String str) {
        return getElementValue(element.getElementsByTagName(str).item(0));
    }

    public String getXmlFromUrl(String str) {
        try {
            return EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost(str)).getEntity());
        } catch (IOException e) {
            Log.e("Bekity2: ", e.getMessage());
            return null;
        }
    }
}
