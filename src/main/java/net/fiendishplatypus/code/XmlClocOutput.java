package net.fiendishplatypus.code;

/*
<?xml version="1.0" encoding="UTF-8"?><results>
<header>
  <cloc_url>github.com/AlDanial/cloc</cloc_url>
  <cloc_version>2.00</cloc_version>
  <elapsed_seconds>0.241128921508789</elapsed_seconds>
  <n_files>16</n_files>
  <n_lines>2394</n_lines>
  <files_per_second>66.3545455264553</files_per_second>
  <lines_per_second>9928.29887439587</lines_per_second>
</header>
<source>
  <target>2004-02-01</target>
  <origin>https://github.com/apache/maven.git</origin>
  <branch>master</branch>
  <commit>f9404ad2f645c12d04a45d02214ac5e4badc66b4</commit>
</source>
<languages>
  <language name="Java" files_count="5" blank="283" comment="100" code="1100" />
  <language name="Bourne Shell" files_count="5" blank="139" comment="55" code="365" />
  <language name="Text" files_count="6" blank="102" comment="0" code="250" />
  <total sum_files="16" blank="524" comment="155" code="1715" />
</languages>
</results>
*/

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class XmlClocOutput {
  public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
    String example = """
      <?xml version="1.0" encoding="UTF-8"?>
      <results>
      <header>
        <cloc_url>github.com/AlDanial/cloc</cloc_url>
        <cloc_version>2.00</cloc_version>
        <elapsed_seconds>0.241128921508789</elapsed_seconds>
        <n_files>16</n_files>
        <n_lines>2394</n_lines>
        <files_per_second>66.3545455264553</files_per_second>
        <lines_per_second>9928.29887439587</lines_per_second>
      </header>
      <source>
        <target>2004-02-01</target>
        <origin>https://github.com/apache/maven.git</origin>
        <branch>master</branch>
        <commit>f9404ad2f645c12d04a45d02214ac5e4badc66b4</commit>
      </source>
      <languages>
        <language name="Java" files_count="5" blank="283" comment="100" code="1100" />
        <language name="Bourne Shell" files_count="5" blank="139" comment="55" code="365" />
        <language name="Text" files_count="6" blank="102" comment="0" code="250" />
        <total sum_files="16" blank="524" comment="155" code="1715" />
      </languages>
      </results>
            """;

    Collection<Loc> locs = new XmlClocOutput().parse(example);

    System.out.println(locs);

  }

  public Collection<Loc> parse(String xml) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    return parse(documentBuilder, xml);
  }

  private static Collection<Loc> parse(DocumentBuilder documentBuilder, String example)
    throws SAXException, IOException {
    Document doc = documentBuilder.parse(new ByteArrayInputStream(example.getBytes()));
    doc.getDocumentElement().normalize();

    Node sourceNode = doc.getElementsByTagName("source").item(0);
    NodeList sourceChildList = sourceNode.getChildNodes();

    Map<String, String> source = new HashMap<>();

    for (int i = 0; i < sourceChildList.getLength(); i++) {
      Node node = sourceChildList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        source.put(node.getNodeName(), node.getTextContent());
      }
    }


    Node first = doc.getElementsByTagName("languages").item(0);
    NodeList nodeList = first.getChildNodes();

    Collection<Loc> locs = new ArrayList<>();

    int n = nodeList.getLength();
    Node current;
    for (int i = 0; i < n; i++) {
      current = nodeList.item(i);
      if (current.getNodeType() == Node.ELEMENT_NODE) {
        NamedNodeMap attributes = current.getAttributes();
        if (current.getNodeName().equals("language")) {
          LocalDate branchAsTimeStamp = LocalDate.parse(source.get("target"), DateTimeFormatter.ISO_LOCAL_DATE);
          long nOfFiles = Long.parseLong(attributes.getNamedItem("files_count").getTextContent());
          long nOfLinesOfCode = Long.parseLong(attributes.getNamedItem("code").getTextContent());
          String lang = attributes.getNamedItem("name").getTextContent();
          locs.add(new Loc(branchAsTimeStamp, nOfFiles, nOfLinesOfCode, lang));
        }
      }
    }
    return locs;
  }
}
