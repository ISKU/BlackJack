package kmh.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private Intent intent;
    private static InputMethodManager keyboardControl;

    private static EditText edit_betting;
    private static TextView infoGame;
    private static Button button_bet;
    private static Button button_hit;
    private static Button button_stand;
    private static Button button_replay;
    private static Button button_end;

    private static int hasMoney;
    private static int betMoney;
    private static boolean winOrlose;

    private static Deck deck = new Deck();
    private static BlackJackHand dealerHand;
    private static BlackJackHand userHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        edit_betting = (EditText) findViewById(R.id.edit_betting);
        button_bet = (Button) findViewById(R.id.button_bet);
        button_hit = (Button) findViewById(R.id.button_hit);
        button_stand = (Button) findViewById(R.id.button_stand);
        button_replay = (Button) findViewById(R.id.button_replay);
        button_end = (Button) findViewById(R.id.button_end);

        edit_betting.setVisibility(View.VISIBLE);
        button_bet.setVisibility(View.VISIBLE);
        button_hit.setVisibility(View.INVISIBLE);
        button_stand.setVisibility(View.INVISIBLE);
        button_replay.setVisibility(View.INVISIBLE);
        button_end.setVisibility(View.INVISIBLE);

        keyboardControl = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        keyboardControl.showSoftInput(edit_betting, 0);

        intent = getIntent();
        hasMoney = intent.getIntExtra("hasMoney", hasMoney);
        infoGame = (TextView) findViewById(R.id.infoGame);
        infoGame.setText(Sentence.gameStart + "\n" + "You have " + hasMoney + " WONs\n");

        button_bet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int bet = Integer.parseInt(edit_betting.getText().toString());
                if(bet >= 1 && bet <= hasMoney) {
                    betMoney = Integer.parseInt(edit_betting.getText().toString());
                    edit_betting.setVisibility(View.INVISIBLE);
                    button_bet.setVisibility(View.INVISIBLE);
                    keyboardControl.hideSoftInputFromWindow(edit_betting.getWindowToken(), 0);
                    playBlackJack();
                }
                else
                    infoGame.setText(infoGame.getText() + Sentence.invalidBetting + hasMoney + "\n");
            }
        });

        button_hit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userSelectHit();
                if(checkBustedBlackJack()) {
                  endOfGame();
                }
                else
                    showingDealerAndUserCard(false);
            }
        });

        button_stand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userSelectStand();
                endOfGame();
            }
        });

        button_replay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        button_end.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    static void playBlackJack() {
        dealerHand = new BlackJackHand();
        userHand = new BlackJackHand();

        for (int i = 0; i < 2; i++) {
            dealerHand.addCard(deck.dealCard());
            userHand.addCard(deck.dealCard());
        }

        if (firstTurnBlackJack()) {
            endOfGame();
        }
        else {
            showingDealerAndUserCard(false);
            infoGame.setText(infoGame.getText() + Sentence.choiceHitOrStand + "\n");
            button_hit.setVisibility(View.VISIBLE);
            button_stand.setVisibility(View.VISIBLE);
        }
    }

    private static void endOfGame() {
        button_hit.setVisibility(View.INVISIBLE);
        button_stand.setVisibility(View.INVISIBLE);
        resultGame();
        infoGame.setText(infoGame.getText() + Sentence.gameEnd + Sentence.playAgain);
        if(hasMoney == 0)
            button_end.setVisibility(View.VISIBLE);
        else {
            button_replay.setVisibility(View.VISIBLE);
            button_end.setVisibility(View.VISIBLE);
        }
    }

    private static boolean checkBustedBlackJack() {
        if (dealerHand.getBlackJackValue() > 21)
            return true;
        if (userHand.getBlackJackValue() > 21)
            return true;
        return false;
    }

    private static boolean firstTurnBlackJack() {
        if (dealerHand.getBlackJackValue() == 21) {
            showingDealerAndUserCard(true);
            infoGame.setText(infoGame.getText() + Sentence.dealerBlackJack + "\n");
            winOrlose = false;
            return true;
        }
        if (userHand.getBlackJackValue() == 21) {
            showingDealerAndUserCard(true);
            System.out.println(Sentence.userBlackJack);
            winOrlose = true;
            return true;
        }
        return false;
    }

    private static void resultGame() {
        int dealerScore = dealerHand.getBlackJackValue();
        int userScore = userHand.getBlackJackValue();
        StringBuilder score = new StringBuilder(dealerScore  + " points to " + userScore);

        if (dealerScore <= 21 && userScore <= 21)
            if (dealerScore == userScore) {
                score.append(Sentence.draw);
                winOrlose = false;
            } else if (dealerScore > userScore) {
                score.append(Sentence.dealerWin);
                winOrlose = false;
            } else {
                score.append(Sentence.youWin);
                winOrlose = true;
            }
        infoGame.setText(infoGame.getText() + score.toString());

        if (winOrlose)
            hasMoney = hasMoney + betMoney;
        else
            hasMoney = hasMoney - betMoney;

        if (hasMoney == 0)
            infoGame.setText(infoGame.getText() + Sentence.outOfMoney + "\n");
    }

    private static void userSelectStand() {
        infoGame.setText(Sentence.userStand + "\n");
        while (dealerHand.getBlackJackValue() <= 16) {
            Card newCard = deck.dealCard();
            infoGame.setText(infoGame.getText() + Sentence.dealerHitAndGetCard + newCard + "\n");
            dealerHand.addCard(newCard);
            if (dealerHand.getBlackJackValue() > 21) {
                showingDealerAndUserCard(true);
                infoGame.setText(infoGame.getText() + "\n" + Sentence.dealerBusted + "\n");
                winOrlose = true;
                return;
            }
        }
        showingDealerAndUserCard(true);
        infoGame.setText(infoGame.getText() + Sentence.dealerTotal + dealerHand.getBlackJackValue() + "\n");
    }

    private static void userSelectHit() {
        Card newCard = deck.dealCard();
        userHand.addCard(newCard);

        infoGame.setText(Sentence.userHit + "\n");
        infoGame.setText(infoGame.getText() + Sentence.chooseCard + newCard + "\n");
        if (userHand.getBlackJackValue() > 21) {
            showingDealerAndUserCard(true);
            infoGame.setText(infoGame.getText() + Sentence.youBusted + "\n");
            winOrlose = false;
        }
    }

    private static void showingDealerAndUserCard(boolean dealerAllCardsOpen) {
        infoGame.setText(infoGame.getText() + Sentence.line + "\n");
        infoGame.setText(infoGame.getText() + String.format("<Dealer cards>: %4s ", dealerAllCardsOpen ? dealerHand.getCard(0) : "??"));
        for (int i = 1; i < dealerHand.getCardCount(); i++)
            infoGame.setText(infoGame.getText() + String.format("%4s ", dealerHand.getCard(i)));

        infoGame.setText(infoGame.getText() + "\n  <Your cards>: ");
        for (int i = 0; i < userHand.getCardCount(); i++)
            infoGame.setText(infoGame.getText() + String.format("%4s ", userHand.getCard(i)));
        infoGame.setText(infoGame.getText() + Sentence.yourTotal + userHand.getBlackJackValue() + "\n\n");
    }
}