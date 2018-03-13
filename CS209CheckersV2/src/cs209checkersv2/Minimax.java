/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs209checkersv2;

import static cs209checkersv2.CS209CheckersV2.*;
import java.util.ArrayList;
/**
 *
 * @author kendrick
 */
public class Minimax {
    public static final int MAXDEPTH = 3;
    
    public static int evaluateBoard(Tile[][] gameBoard, String moveType) {
        int evaluationValue = 0;
        //weigh the different types of moves
        //DOUBLE HOP FORWARD > HOP FORWARD > MOVE FORWARD > SWAP FORWARD > PASS > MOVE BACK> HOP BACKWARD > DOUBLE HOP BACKWARD
        int otherPlayer = (curPlayer==0) ? 1 : 0;
        
        
        for(int i = 0; i < players[curPlayer].ownedPieces.length; i++) {
            if(players[curPlayer].name.equals("Black")) {
                if(players[curPlayer].ownedPieces[i].xcoord==0 && players[curPlayer].ownedPieces[i].ycoord==0) {
                    evaluationValue += 6;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==0 && players[curPlayer].ownedPieces[i].ycoord==1) {
                    evaluationValue += 4;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==1 && players[curPlayer].ownedPieces[i].ycoord==0) {
                    evaluationValue += 4;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==1 && players[curPlayer].ownedPieces[i].ycoord==1) {
                    evaluationValue += 2;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==0 && players[curPlayer].ownedPieces[i].ycoord==2) {
                    evaluationValue += 2;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==2 && players[curPlayer].ownedPieces[i].ycoord==0) {
                    evaluationValue += 2;
                }
                else {
                    evaluationValue -= 1;
                }
            }
            else if(players[curPlayer].name.equals("White")){
                if(players[curPlayer].ownedPieces[i].xcoord==3 && players[curPlayer].ownedPieces[i].ycoord==3) {
                    evaluationValue += 6;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==3 && players[curPlayer].ownedPieces[i].ycoord==2) {
                    evaluationValue += 4;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==2 && players[curPlayer].ownedPieces[i].ycoord==3) {
                    evaluationValue += 4;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==2 && players[curPlayer].ownedPieces[i].ycoord==2) {
                    evaluationValue += 2;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==3 && players[curPlayer].ownedPieces[i].ycoord==1) {
                    evaluationValue += 2;
                }
                else if(players[curPlayer].ownedPieces[i].xcoord==1 && players[curPlayer].ownedPieces[i].ycoord==3) {
                    evaluationValue += 2;
                }
                else {
                    evaluationValue -= 1;
                }
            }
        }
        
        
        
        return evaluationValue;
    }
    
    public static Move alphaBeta(int depth, int alpha, int beta, Move move, Player[] players, int curPlayer) {
        //Say that AI is listOfPlayers[1]
        Move newMove;
        Move bestMove = null;
        System.out.println("Player: " + players[curPlayer].name);
        Player currentPlayer = players[curPlayer];
        //ArrayList<PossibleMove> possibleMoveList = BoardAIController.getPossibleMoves(currentPlayer);
        ArrayList<Move> possibleMoveList = CS209CheckersV2.findPossibleMoves(currentPlayer);
        for(Move lol: possibleMoveList) {
            
        }
        int[][] possibleSources = CS209CheckersV2.findPossibleSources(currentPlayer.ownedPieces);
        int bestValue;

        if (depth == MAXDEPTH) {
            Piece newPiece = move.getPiece();
            newPiece.setLayoutX(move.getNewXPost() * 100);
            newPiece.setLayoutY(move.getNewYPost() * 100);
            int fitnessValue = BoardAIController.evaluatePiece(currentPlayer, move.getPiece(), move.getNewXPost(), move.getNewYPost());
            int fitnessVal = Minimax.evaluateBoard(gameBoard, moveType);
            System.out.println("Fitness value of move is: " + fitnessVal);
            newMove = new Move(currentPlayer, move, fitnessValue);
            return newMove;
        }

        for (int i = 0; i < possibleMoveList.size() - 1; i++) {
            //Maximizing player
            if (player == 1) {
                //Initialize current best move
                bestMove = new PlayerMove(currentPlayer, move, MIN);

                System.out.println("Who is the player: " + currentPlayer);
                newMove = alphaBeta(depth - 1, alpha, beta, possibleMoveList.get(i), listOfPlayers, Math.abs(player - 1));
                bestMove.setFitnessValue(Math.max(bestMove.getFitnessValue(), newMove.getFitnessValue()));
                alpha = Math.max(alpha, bestMove.getFitnessValue());

                //Pruning
                if (beta <= alpha) {
                    break;
                }
            } //Minimizing player
            else {
                //Initialize current best move
                bestMove = new PlayerMove(currentPlayer, move, MAX);
                System.out.println("Who is the player: " + currentPlayer);
                newMove = alphaBeta(depth - 1, alpha, beta, possibleMoveList.get(i), listOfPlayers, Math.abs(player + 1));
                bestMove.setFitnessValue(Math.max(bestMove.getFitnessValue(), newMove.getFitnessValue()));
                beta = Math.max(alpha, bestMove.getFitnessValue());

                //Pruning
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestMove;

    }
}
