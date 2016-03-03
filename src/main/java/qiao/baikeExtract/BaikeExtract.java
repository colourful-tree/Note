package qiao.baikeExtract;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.cyberneko.html.filters.ElementRemover;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;

public class BaikeExtract {

  public void extract(String html) throws  SAXException, IOException, TransformerException {

    DOMParser parser = new DOMParser();
    XMLInputSource source = new XMLInputSource(null, "xpath-wrapper", null, new StringReader(html),
        "UTF8");
    parser.setFeature("http://xml.org/sax/features/namespaces", false);
    parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF8");
    parser.parse(source);
    Document document = parser.getDocument();
    NodeList nodeList = XPathAPI.selectNodeList(document,"//DIV[@class='star-info-block relations']//LI//DIV");
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node t = nodeList.item(i);
      String attrValue = t.getTextContent();
      String attr = t.getFirstChild().getTextContent();
      int pos = attrValue.indexOf(attr);
      String value = attrValue.substring(pos + attr.length());
      System.out.println(attr+"\t"+value);
    }
  }

  private String fetcherPage() throws UnsupportedEncodingException, IOException {
    URL url = new URL("http://baike.baidu.com/subview/2271/4818353.htm");
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "utf-8");
    BufferedReader bufReader = new BufferedReader(input);
    String line = "";
    String html = "";
    while ((line = bufReader.readLine()) != null) {
      html += line;
    }
    return html;
  }

  public static void main(String[] args) throws Exception {
    BaikeExtract be = new BaikeExtract();
    String page = be.fetcherPage();
    be.extract(page);
  }
}
