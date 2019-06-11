package com.laguille.belote.test.integration;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.card.CardValue;
import com.laguille.belote.model.player.Player;

/**
 * This class will return the instance of an existing object of the game
 * that will match the string parameter provided as an input
 * @author guillaume
 *
 */
public class ParserUtil {

	
	
	public static Card parseCard(String s) {
		String valueString = s.split(" ")[0];
		String colorString = s.split(" ")[1];
		CardValue value = CardValue.valueOf(valueString);
		CardColor color = CardColor.valueOf(colorString);
		return IntegrationTest.model.getDeck().getCard(value, color);
	}

	public static Player parsePlayer(String playerName) {
		for (Player player : IntegrationTest.model.getPlayers())
		{
			if (player.getName().equals(playerName))
			{
				return player;
			}
		}
		return null;
	}

}
