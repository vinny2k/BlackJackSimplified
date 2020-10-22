package model;

import model.interfaces.Player;

public class SimplePlayer implements Player {

	private String id;
	private String name;
	private int points;
	private int bet;
	private int result;
	
	public SimplePlayer(String id, String playerName, int initialPoints) {
		this.id = id;
		this.name = playerName;
		this.points = initialPoints;
		this.result = 0;
	}

	@Override
	public String getPlayerName() {
		return name;
	}

	@Override
	public void setPlayerName(String playerName) {
		this.name = playerName;

	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public void setPoints(int points) {
		this.points = points;

	}

	@Override
	public String getPlayerId() {
		return id;
	}

	@Override
	public boolean setBet(int bet) {
		if (bet > 0 && points >= bet) {
			this.bet = bet;
			return true;
		}
		return false;
	}

	@Override
	public int getBet() {
		return bet;
	}

	@Override
	public void resetBet() {
		this.bet = 0;

	}

	@Override
	public int getResult() {
		return result;
	}

	@Override
	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public boolean equals(Player player) {
		if (this.id == player.getPlayerId()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object player) {
		return player instanceof Player && ((Player) player).equals(this);
	}
	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 1;
		
		result = result * prime + Integer.parseInt(id) ^ (Integer.parseInt(id) >>> 32);
		result = result * prime + ((name == null) ? 0 : name.hashCode());
		
		return result;
	}
	
	@Override
	public int compareTo(Player player) {
		return this.getPlayerId().compareTo(player.getPlayerId());
	}

	@Override
	public String toString() {
		return String.format("Player: id=%s, name=%s, bet=%s, points=%s, RESULT .. %s",
				this.getPlayerId(), this.getPlayerName(), this.getBet(), this.getPoints(), this.getResult());
	}
}
