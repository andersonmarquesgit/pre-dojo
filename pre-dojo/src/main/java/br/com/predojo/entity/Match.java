package br.com.predojo.entity;

import java.util.Date;

public class Match {

	private String matchNumber;
	private Date startMatch;
	private Date finishMatch;

	public Match(Date startMatch) {
		this.startMatch = startMatch;
	}

	public Date getStartMatch() {
		return startMatch;
	}

	public void setStartMatch(Date startMatch) {
		this.startMatch = startMatch;
	}

	public Date getFinishMatch() {
		return finishMatch;
	}

	public void setFinishMatch(Date finishMatch) {
		this.finishMatch = finishMatch;
	}

	public String getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}
}
