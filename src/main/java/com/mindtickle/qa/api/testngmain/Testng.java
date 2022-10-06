package com.mindtickle.qa.api.testngmain;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.xml.sax.SAXException;

import com.mindtickle.qa.apibase.APITestBase;


public class Testng {

	public static void main(String[] args) {
		
		TestNG testng = new TestNG();
		args[0] = "UserTest.xml";
		args[1] = "";
		args[2] = "";
		args[3] = "";
		InputStream is = Testng.class.getClassLoader().getResourceAsStream("SuiteXmls/"+args[0]);
		
		if(args.length == 3){
			APITestBase.emailForReporting = args[1];
			APITestBase.groups = args[2];
		}else if (args.length == 2){
			if( args[1].contains("@") ){
				APITestBase.emailForReporting = args[1];
			}else{
				APITestBase.groups = args[1];
			}
		}
		
		List<XmlSuite> suite; 
		try { 
			suite = (List <XmlSuite>)(new Parser(is).parse()); 
			testng.setXmlSuites(suite);
			testng.run(); 
		} catch (ParserConfigurationException e)
		{ 
			e.printStackTrace(); 
		} 
		catch (SAXException e) 
		{ 
			e.printStackTrace(); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}
	}

}
