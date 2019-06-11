/**
 * 
 */
package com.laguille.belote.model.card;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author guillaume
 * 
 */
public class CardTest
{

	
	@Test
	public void testEquals()
	{
		Card c1, c2, c3, c4;
		c1 = new Card(CardColor.DIAMOND, CardValue.NINE);
		c2 = new Card(CardColor.DIAMOND, CardValue.SEVEN);
		c3 = new Card(CardColor.DIAMOND, CardValue.NINE);		
		c4 = new Card(CardColor.HEART, CardValue.NINE);
		Assert.assertEquals(c1, c1);
		Assert.assertFalse(c1.equals(new Object()));
		Assert.assertEquals(c1, c3);
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c1.equals(c4));
	}
	
	
}
