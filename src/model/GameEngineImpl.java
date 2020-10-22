package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import model.interfaces.PlayingCard.Suit;
import model.interfaces.PlayingCard.Value;
import view.interfaces.GameEngineCallback;

public class GameEngineImpl implements GameEngine {
	private HashMap<String, Player> playerMap;
	private Collection<GameEngineCallback> callbackCollection;
	private Deque<PlayingCard> cardDeque;
	
	public GameEngineImpl() {
		playerMap = new HashMap<String, Player>();
		callbackCollection = new ArrayList<GameEngineCallback>();
		cardDeque = this.getShuffledHalfDeck();
	}
	
	@Override
	public void dealPlayer(Player player, int delay) throws IllegalArgumentException {
		if (delay < 0 || delay > 1000) {
			throw new IllegalArgumentException();
		}
		// reseting the player's result for the new round of dealing
		player.setResult(0);
		
		int previousResult = 0;
		boolean bust = false;
		
		// loops until player has bust
		while (bust !=true) {
			
			// checking if deck of cards is empty
			// if empty, replace with a new deck of shuffled cards
			if (cardDeque.isEmpty()) {
				cardDeque = this.getShuffledHalfDeck();
			} 
			
			PlayingCard card = cardDeque.pop();
			previousResult = player.getResult();
			player.setResult(player.getResult() + card.getScore());
			
			if (player.getResult() > BUST_LEVEL) {
				player.setResult(previousResult);
				
				for (GameEngineCallback cb : callbackCollection) {
					cb.bustCard(player, card, this);
					cb.result(player, player.getResult(), this);
				}
				bust = true;
			} else {
				for (GameEngineCallback cb : callbackCollection) {
					cb.nextCard(player, card, this);
				}
			}

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void dealHouse(int delay) throws IllegalArgumentException {
		
		if (delay < 0) {
			throw new IllegalArgumentException();
		}

		int previousResult = 0;
		int houseResult = 0;
		boolean bust = false;
		
		// loops until house has bust
		while (bust != true) {
			
			// checking if deck of cards is empty
			// if empty, replace with a new deck of shuffled cards
			if (cardDeque.isEmpty()) {
				cardDeque = this.getShuffledHalfDeck();
			}
			
			PlayingCard card = cardDeque.pop();
			
			previousResult = houseResult;
			houseResult += card.getScore();
			
			if (houseResult > BUST_LEVEL) {
				houseResult = previousResult;
				for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
					this.applyWinLoss(entry.getValue(), houseResult);
				}
				for (GameEngineCallback cb : callbackCollection) {
					cb.houseBustCard(card, this);
					cb.houseResult(houseResult, this);
				}
				bust = true;
			} else {
				for (GameEngineCallback cb : callbackCollection) {
					cb.nextHouseCard(card, this);
				}
			}
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
			entry.getValue().resetBet();
		}
	}

	@Override
	public void applyWinLoss(Player player, int houseResult) {
		if (player.getResult() > houseResult) {
			
			player.setPoints(player.getPoints() + player.getBet());
			
		} else if (player.getResult() < houseResult) {
			
			player.setPoints(player.getPoints() - player.getBet());
			
		} else if (player.getResult() == houseResult) {
			
			player.setPoints(player.getPoints());
		}
		
	}

	@Override
	public void addPlayer(Player player) {
		
		// checking for duplicates existing in the player hashmap
		for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
			if (entry.getKey() == player.getPlayerId()) {
				removePlayer(entry.getValue());
			}
		}
		playerMap.put(player.getPlayerId(), player);
	}

	@Override
	public Player getPlayer(String id) {
		for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
			if (entry.getValue().getPlayerId() == id) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public boolean removePlayer(Player player) {
		return playerMap.remove(player.getPlayerId(), player);
	}

	@Override
	public boolean placeBet(Player player, int bet) {
		if (player.setBet(bet)) {
			return true;
		}
		return false;
	}

	@Override
	public void addGameEngineCallback(GameEngineCallback gameEngineCallback) {
		callbackCollection.add(gameEngineCallback);
	}

	@Override
	public boolean removeGameEngineCallback(GameEngineCallback gameEngineCallback) {
		return callbackCollection.remove(gameEngineCallback);
	}

	@Override
	public Collection<Player> getAllPlayers() {
		List<Player> tempList = new ArrayList<Player>();
		
		// adding all players from hashmap to temporary list
		for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
			tempList.add(entry.getValue());
		}
		
		// sorting list via comparing String ids
		Collections.sort(tempList, new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				return o1.compareTo(o2);
			}

		});
		
		// creating an unmodifiable collection from the list
		Collection<Player> unmodifiablePlayerCollection = Collections.unmodifiableCollection(tempList);
		
		return unmodifiablePlayerCollection;
	}

	@Override
	public Deque<PlayingCard> getShuffledHalfDeck() {
		List<PlayingCard> cardList = new ArrayList<PlayingCard>();
		
		for (int i = 0; i < Suit.values().length; i++) {
			
			for (int j = 0; j < Value.values().length; j++) {
				
				PlayingCard card = new PlayingCardImpl(Suit.values()[i], Value.values()[j]);
				cardList.add(card);
			}
		}
		
		Collections.shuffle(cardList);
		
		cardDeque = new ArrayDeque<PlayingCard>(cardList);
		return cardDeque;
	}

}
