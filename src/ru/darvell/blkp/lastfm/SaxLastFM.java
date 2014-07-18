package ru.darvell.blkp.lastfm;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 17.07.14
 * Time: 14:21
 * Sax parser
 */
public class SaxLastFM extends DefaultHandler {

	SaxLastFM(){
		super();
	}

	public void startDocument()
	{
		System.out.println("<?xml version=\"1.0\"?>");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

	}
}
