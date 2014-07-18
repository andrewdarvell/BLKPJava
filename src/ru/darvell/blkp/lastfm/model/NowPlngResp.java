package ru.darvell.blkp.lastfm.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 17.07.14
 * Time: 16:39
 * Contains now plaing resp
 */
public class NowPlngResp {
	private String track;
	private int track_correct;

	private String artist;
	private int artist_correct;

	private String album;
	private String albumArtist;
	private int ignoredMessage;

	public NowPlngResp() {
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public int getTrack_correct() {
		return track_correct;
	}

	public void setTrack_correct(int track_correct) {
		this.track_correct = track_correct;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getArtist_correct() {
		return artist_correct;
	}

	public void setArtist_correct(int artist_correct) {
		this.artist_correct = artist_correct;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	public int getIgnoredMessage() {
		return ignoredMessage;
	}

	public void setIgnoredMessage(int ignoredMessage) {
		this.ignoredMessage = ignoredMessage;
	}

	@Override
	public String toString() {
		return "NowPlngResp{" +
				"track='" + track + '\'' +
				", track_correct=" + track_correct +
				", artist='" + artist + '\'' +
				", artist_correct=" + artist_correct +
				", album='" + album + '\'' +
				", albumArtist='" + albumArtist + '\'' +
				", ignoredMessage=" + ignoredMessage +
				'}';

	}
}
