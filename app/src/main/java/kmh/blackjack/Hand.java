package kmh.blackjack;

import java.util.Vector;

public class Hand {

   private Vector<Card> hand;   // The cards in the hand.
   
   public Hand() {
      hand = new Vector<Card>();
   }
   
   public void clear() {
      hand.removeAllElements();
   }
   
   public void addCard(Card card) {
      if (card != null)
         hand.addElement(card);
   }
   
   public void removeCard(Card card) {
      hand.removeElement(card);
   }
   
   public void removeCard(int position) {
      if (position >= 0 && position < hand.size())
         hand.removeElementAt(position);
   }
   
   public int getCardCount() {
      return hand.size();
   }
   
   public Card getCard(int position) {
      if (position >= 0 && position < hand.size())
         return (Card)hand.elementAt(position);
      else
         return null;
   }
}