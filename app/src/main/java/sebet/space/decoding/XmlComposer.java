package sebet.space.decoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import sebet.space.utils.CRC16;

/**
 * Created by eti22 on 21.08.2017.
 */

public class XmlComposer {

    private static final String PROPOSAL_LIST = "http://www.bbraun.com/HC/AIS/Space/AutoProgramming";

    public static Document composeXml(FormatDecoder decoder){
        if(!(decoder.hasDrug() && decoder.hasPump()))
            return null;
        try {
            Map<String, String> data = decoder.getData();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            Element root = doc.createElement("ProposalList");
            root.setAttribute("xmlns", PROPOSAL_LIST);
            doc.appendChild(root);

            Element drug = doc.createElement("Drug");

            for (Map.Entry<String, String> entry : data.entrySet()) {
                if(!entry.getKey().equals(FormatDecoder.IP)) {
                    Element e = doc.createElement(entry.getKey().substring(1));
                    e.setTextContent(entry.getValue());
                    drug.appendChild(e);
                }
            }
            root.appendChild(drug);

            Element totalChecksum = doc.createElement("TotalChecksum");
            totalChecksum.setTextContent(String.valueOf(CRC16.calculate(decoder.getData().get(FormatDecoder.CHECKSUM))));
            root.appendChild(totalChecksum);
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String XmlAsString(FormatDecoder decoder){
        try {
            DOMSource domSource = new DOMSource(composeXml(decoder));
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch(TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
