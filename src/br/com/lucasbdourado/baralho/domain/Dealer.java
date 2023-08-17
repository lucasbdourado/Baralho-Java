package br.com.lucasbdourado.baralho.domain;

import java.util.ArrayList;
import java.util.List;

public class Dealer extends Player {
    private Deck deck;
    private List<Card> cards = new ArrayList<>();

    private Integer index = 0;

    public Dealer(String name, Deck deck){
        super(name);
        this.deck = deck;
    }

    public void giveCard(Player player){
        Card card = deck.getCard();

        player.addCard(card);
    }

    public void addCard(){
        Card card = deck.getCard();

        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }


    public void showCards(boolean showOnlyFirstCard) {
        int result = 0;


        if(showOnlyFirstCard){
            System.out.println(getName() + ": " + cards.get(0).getNumber() + cards.get(0).getSuit());
            return;
        }

        System.out.print("Dealer: ");
        for (Card card: cards) {
            System.out.print(card.getNumber() + card.getSuit() + " ");
            result = result + card.getValue();
        }

        System.out.println(result);
    }

    public Integer getCardsValue() {
        int result = 0;

        for (Card card: cards) {
            result = result + card.getValue();
        }

        return result;
    }

    public void checkPlay(){
        Game game = Game.getGame();
        List<Player> players = game.getPlayers();
        int bestHand = 0;

        this.showCards(false);

        for (Player player : players) {
            int playerHands = player.getCardsValue();

            if(playerHands > bestHand && playerHands <= 21){
                bestHand = playerHands;
            }
        }

        System.out.println("Valor da melhor Mão: " + bestHand);

        this.makePlay(bestHand, players);
    }

    public void makePlay(Integer bestHand, List<Player> players){
        int dealerHand = this.getCardsValue();

        this.showCards(false);

        boolean needBuy = false;
        for (Player player : players) {
            int playerHand = player.getCardsValue();

            if(playerHand <= 21 && dealerHand < playerHand && playerHand <= bestHand){
                needBuy = true;
            }
        }

        this.endPlay(needBuy, bestHand, players);
    }

    public void endPlay(boolean needBuy, Integer bestHand, List<Player> players){
        if(needBuy){
            this.addCard();
            this.makePlay(bestHand, players);
        }else{
            this.endGame(players);
        }
    }

    public void endGame(List<Player> players){
        Game game = Game.getGame();
        List<Player> winners = new ArrayList<>();
        Rules rules = new Rules();

        int dealerHands = this.getCardsValue();

        for (Player player : players) {
            int playerHand = player.getCardsValue();


            player.showCards();

            if(playerHand > dealerHands && playerHand <= 21 || dealerHands > 21 && playerHand <= 21){
                winners.add(player);
            }
        }

        this.showCards(false);

        if(winners.size() > 0){
            System.out.println("O(s) vencedor(es) são: ");
            for (Player player : winners) {
                System.out.println(player.getName());
            }
        }else{
            System.out.println("O dealer ganhou a partida!");
        }

        game.gameOver();
    }

    public void giveCardsForAllPlayers(List<Player> players) {
        for (int i=0; i<2; i++) {
            for (Player player : players) {
                this.giveCard(player);
            }
            this.addCard();
        }
    }
}
