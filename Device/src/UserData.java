import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class UserData 
{
	public static String GetUserName() throws ParserConfigurationException, SAXException, IOException
	{
		File Data = new File("UserData.xml");
		DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder Builder = Factory.newDocumentBuilder();
		Document Doc = Builder.parse(Data);
		String Name = Doc.getElementsByTagName("Name").item(0).getTextContent();
		return Name;
	}
}
