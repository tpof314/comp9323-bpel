
package controller.tools;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A tool class for reading compile/executing results from the BPEL 
 * Runtime Server.
 * @author Haojie Huang
 */
public class ResultReader {
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	
	private final static String COMPILE_RESULT = "compileResult";
	private final static String EXECUTE_RESULT = "executeResult";
	private final static String URI = "uri";

	public ResultReader() throws ParserConfigurationException {
		this.factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
	}
	
	/**
	 * Get the content inside the <compileResult> tag in a given xml string.
	 * @param strXml the xml string given.
	 * @return the compile result.
	 * @throws SAXException
	 * @throws IOException 
	 */
	public String readCompileResult(String strXml) throws SAXException, IOException {
		String result = "";
		InputSource is = new InputSource(new StringReader(strXml));
		
		Document doc = builder.parse(is);
		
		NodeList nodes = doc.getElementsByTagName(COMPILE_RESULT);
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			result += node.getTextContent();
		}
		
		return result;
	}
	
	private String trimResult(String result) {
		int start = result.indexOf("<?xml");
		int end   = result.length() - 1;
		
		return result.substring(start, end);
	}
	
	
	/**
	 * Get the content inside the <executeResult> tag in a given xml string.
	 * @param strXml the xml string given.
	 * @return the execute result.
	 * @throws SAXException
	 * @throws IOException 
	 */
	public String readExecuteResult(String strXml) throws SAXException, IOException {
		String result = "";
		InputSource is = new InputSource(new StringReader(strXml));
		
		Document doc = builder.parse(is);
		
		NodeList nodes = doc.getElementsByTagName(EXECUTE_RESULT);
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			result += node.getTextContent();
		}
		
		// Trim Result.
		if (result.equals("")) {
			return result;
		}
		else {
			if (result.charAt(0) != '<') {
				result = trimResult(result);
			}
			return result;
		}
	}
	
	/**
	 * Fetch the &lt;uri&gt; tag from a given xml.
	 * @param strXml the xml structure in string format.
	 * @return the contain of the &lt;uri&gt; tag.
	 * @throws SAXException
	 * @throws IOException 
	 */
	public String readUri(String strXml) throws SAXException, IOException {
		String result = "";
		InputSource is = new InputSource(new StringReader(strXml));
		
		Document doc = builder.parse(is);
		
		NodeList nodes = doc.getElementsByTagName(URI);
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			result += node.getTextContent();
		}
		
		return result;
	}

	/*
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
		String strXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><runtime><compileResult>2013-10-15 16:32:44 INFO  org.apache.ode.tools.bpelc.cline.BpelC     - Compilation completed in 884ms\n" +
"</compileResult><executeResult>2013-10-15 17:03:25 WARN  org.apache.commons.httpclient.HttpMethodBase     - Going to buffer response body of large or unknown size. Using getResponseBodyAsStream instead is recommended.\n" +
"&lt;?xml version='1.0' encoding='UTF-8'?&gt;&lt;soapenv:Envelope xmlns:soapenv=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot;&gt;&lt;soapenv:Body&gt;&lt;odens:helloResponse xmlns:odens=&quot;http://ode/bpel/unit-test.wsdl&quot;&gt;&lt;TestPart xmlns:SOAP-ENV=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot; xmlns:ns1=&quot;http://ode/bpel/unit-test.wsdl&quot;&gt;Hello World&lt;/TestPart&gt;&lt;/odens:helloResponse&gt;&lt;/soapenv:Body&gt;&lt;/soapenv:Envelope&gt;\n" +
"\n" +
"</executeResult><id>1000</id><project>HelloWorld</project><token>0B942fHMEpI9sMzB1cG9DWlJHVUE</token><uri>http://localhost:8080/comp9323-runtime/bpel/runtimes/1000</uri><user>Peter</user></runtime>\n" +
"";
		ResultReader rreader = new ResultReader();
		String result = rreader.readExecuteResult(strXml);
		System.out.println(result);
	}
	*/
	
}
