package ru.darvell.blkp.lastfm.model;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 24.07.14
 * Time: 14:13
 * Contains scrobbling response info
 */
public class ScrobblingResp {

	private boolean accepted = false;

	private String track;
	private int track_correct;

	private String artist;
	private int artist_correct;

	private int timestamp;

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
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



	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ScrobblingResp{" +
				"accepted=" + accepted +
				", track='" + track + '\'' +
				", track_correct=" + track_correct +
				", artist='" + artist + '\'' +
				", artist_correct=" + artist_correct +
				", timestamp=" + timestamp +
				'}';
	}
}
