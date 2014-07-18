package ru.darvell.blkp.lastfm.Parsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.darvell.blkp.lastfm.model.AuthResp;
import ru.darvell.blkp.lastfm.model.NowPlngResp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 17.07.14
 * Time: 14:40
 * A lot of parsers
 */
public class Parsers {

	public static AuthResp parseAuthResp(InputStream inputStream) {
		AuthResp resp = new AuthResp();
		try{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(false);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document doc = documentBuilder.parse(inputStream);

			Element rootEl = doc.getDocumentElement(); //<config>

			NodeList nodeList = rootEl.getElementsByTagName("subscriber");
			resp.setSubscriber(Integer.parseInt(nodeList.item(0).getTextContent()));

			nodeList = rootEl.getElementsByTagName("key");
			resp.setKey(nodeList.item(0).getTextContent());

			nodeList = rootEl.getElementsByTagName("name");
			resp.setName(nodeList.item(0).getTextContent());

		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}
		return resp;
	}

	public static NowPlngResp parseNowPlngResp(InputStream inputStream) {
		NowPlngResp resp = new NowPlngResp();
		try{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(false);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document doc = documentBuilder.parse(inputStream);

			Element rootEl = doc.getDocumentElement(); //<config>

			NodeList nodeList = rootEl.getElementsByTagName("track");
			resp.setTrack(nodeList.item(0).getTextContent());
			resp.setTrack_correct(Integer.parseInt(nodeList.item(0).getAttributes().getNamedItem("corrected").getTextContent()));

			nodeList = rootEl.getElementsByTagName("artist");
			resp.setArtist(nodeList.item(0).getTextContent());
			resp.setArtist_correct(Integer.parseInt(nodeList.item(0).getAttributes().getNamedItem("corrected").getTextContent()));

			nodeList = rootEl.getElementsByTagName("album");
			resp.setAlbum(nodeList.item(0).getTextContent());

			nodeList = rootEl.getElementsByTagName("albumArtist");
			resp.setAlbumArtist(nodeList.item(0).getTextContent());

			nodeList = rootEl.getElementsByTagName("ignoredMessage");
			resp.setIgnoredMessage(Integer.parseInt(nodeList.item(0).getTextContent()));

		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}
		return resp;
	}
}
