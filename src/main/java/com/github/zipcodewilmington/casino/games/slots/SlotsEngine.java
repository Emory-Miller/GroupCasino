package com.github.zipcodewilmington.casino.games.slots;

import com.github.zipcodewilmington.Casino;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.casinoaccount.CasinoAccount;
import com.github.zipcodewilmington.casino.casinoaccount.CasinoAccountManager;
import com.github.zipcodewilmington.casino.games.CoinToss.CoinTossPlayer;

import java.io.IOException;

import static com.github.zipcodewilmington.casino.casinoaccount.CasinoAccountManager.database;

public class SlotsEngine {

    public void run(CasinoAccount activeAccount) throws IOException {

        SlotsGame slots = new SlotsGame();
        SlotsPlayer slotsPlayer = new SlotsPlayer();
        Casino casino = new Casino();

        slots.announceGame();

        while(slots.continuePlaying){
            slots.resetRound();
            String input = slots.getUserInput();
            if(input.equals("2")){ // EXIT
                break;
            }
            while(!slots.endRound) {

                double balance = activeAccount.getBalance();
                System.out.println("Balance is: " + balance);
                double bet = slotsPlayer.placeBet();

                slots.runSlotsGame(input);
                boolean didIWin = slots.evaluateTurn();
                slots.checkIfWinner();

                if (didIWin) {
                    balance = slots.winBet(bet, balance);
                    System.out.println("Balance is: " + balance);}
                else {
                    balance = slots.loseBet(bet, balance); }
                activeAccount.setBalance(balance);

                if (slots.endRound){
                    break;
                }
            }
        }
        casino.writeAllAccsToFile(database);
//        casino.writeToFileTest(activeAccount); //TODO WORKS WITH SINGLE ACC
        //casino.calvinAccWriteNewBalance(activeAccount); //TODO WORKS

        //casino.writeNewBalance(activeAccount);
        //casino.writeAllAccsToFile(CasinoAccountManager.database);

        casino.checkSelectionEnterGame(activeAccount);
    }

}
