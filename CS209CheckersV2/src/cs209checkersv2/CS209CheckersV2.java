/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs209checkersv2;

import java.util.*;

/**
 *
 * @author kendrick
 */
public class CS209CheckersV2 {
    static byte winner;
    static Tile[][] gameBoard;
    static Player[] players;
    static boolean playerTurn;
    static int curPlayer;
    
    static int[] sourceCoordinate = {0, 0};
    static int[] destinationCoordinate = {0, 0};
    
    static Scanner s = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("\u25CB\u25CF");
        
        initializeBoard();
        testPrintBoard();
        
        do {
            printBoard();
            playerTurn();
        } while(winner == 0);
        
        if(winner==1) {
            System.out.println("White wins.");
        }
        else if(winner==-1) {
            System.out.println("Black wins.");
        }
    }
    
    public static void initializeBoard() {
        gameBoard = new Tile[4][4];
        players = new Player[2];
        playerTurn = false;
        
        players[0] = new Player("White");
        players[1] = new Player("Black");
        
        
        //sets up the gameBoard to default null values
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j <gameBoard.length; j++) {
                gameBoard[i][j] = new Tile();
            }
        }
        //initializes the players' pieces to the default place
        //white pieces
        players[0].ownedPieces = new Piece[6];
        players[0].ownedPieces[0] = new Piece(0,0,"W", players[0]);
        players[0].ownedPieces[1] = new Piece(0,1,"W", players[0]);
        players[0].ownedPieces[2] = new Piece(0,2,"W", players[0]);
        players[0].ownedPieces[3] = new Piece(1,0,"W", players[0]);
        players[0].ownedPieces[4] = new Piece(1,1,"W", players[0]);
        players[0].ownedPieces[5] = new Piece(2,0,"W", players[0]);
        
        //black pieces
        players[1].ownedPieces = new Piece[6];
        players[1].ownedPieces[0] = new Piece(1,3,"B", players[1]);
        players[1].ownedPieces[1] = new Piece(2,2,"B", players[1]);
        players[1].ownedPieces[2] = new Piece(2,3,"B", players[1]);
        players[1].ownedPieces[3] = new Piece(3,1,"B", players[1]);
        players[1].ownedPieces[4] = new Piece(3,2,"B", players[1]);
        players[1].ownedPieces[5] = new Piece(3,3,"B", players[1]);
        
        //this places the white pieces on the gameboard
        for(int i = 0; i < players[0].ownedPieces.length; i++) {
            gameBoard[players[0].ownedPieces[i].xcoord][players[0].ownedPieces[i].ycoord] = new Tile(true, players[0].ownedPieces[i]);
            
        }       
        
        //this places black pieces on the gameboard
        for(int i = 0; i < players[1].ownedPieces.length; i++) {
            gameBoard[players[1].ownedPieces[i].xcoord][players[1].ownedPieces[i].ycoord] = new Tile(true, players[1].ownedPieces[i]);
        }       
    }
    
    
    public static void playerTurn() {
        //if player turn is false then player is white so current player is players[0], and vice versa
        if(playerTurn == false)
            curPlayer = 0;
        else
            curPlayer = 1;
        System.out.println("Player " + players[curPlayer].name + "'s turn.");
        
        boolean isValidSourceCell = false;
        do {
            isValidSourceCell = selectSource();
        } while(isValidSourceCell==false);
         
        
        int[][] possibleMoves = findPlaceableCells(sourceCoordinate);
        //prints out message
        System.out.print("Possible move/s are: ");
        for(int i = 0; i < possibleMoves.length; i++) {
            System.out.print(convertToCell(possibleMoves[i]) + ", ");
        }
        //System.out.println("and " + convertToCell(possibleMoves[possibleMoves.length-1]) + ".");
        System.out.println();
        
        
        boolean isValidDestinationCell = false;
        do {
            isValidDestinationCell = selectDestination(possibleMoves);
        } while(isValidDestinationCell == false);
        
        //swapPieces(gameBoard[sourceCoordinate[0]][sourceCoordinate[1]], gameBoard[destinationCoordinate[0]][destinationCoordinate[1]]);
        swapPieces();
        //swapOwnership()
        
        sourceCoordinate = new int[2];
        destinationCoordinate = new int[2];
        
        winner = checkWin();
        
        players[curPlayer].turnCount++;
        
        playerTurn = !playerTurn;
    }
    
    public static boolean selectSource() {
        String sourceCell;
        boolean isValidSourceCell;
        
        
        System.out.println("Please select a valid piece: (Format: A1, B3, D4)");
        sourceCell = s.nextLine();
        if(sourceCell.length()==2) {
            isValidSourceCell = checkCellValidity(sourceCell);
            //ends function call prematurely if not valid input
            if(isValidSourceCell==false)
                return false;


            //if input is valid then it is converted to a source coordinate
            sourceCoordinate = convertToCoordinate(sourceCell);

            //System.out.println(coordinate[0] + ", " + coordinate[1]);
            boolean isValidPiece = checkPieceValidity(sourceCoordinate);
            
            //if 
            if(isValidPiece==false)
                return false;
            
            //if the cell isn't owned the player or if the piece can't move then selected cell isn't valid
            if(isValidPiece==false)
                isValidSourceCell=false;
        }
        else {
            //if the input is not of length 2 then function call is ended prematurely to be called again
            return false;
        }
        
        System.out.println(sourceCell + " is selected.");
  
        return true;
    }
    
    public static boolean selectDestination(int[][] possibleMoves) {
        String destinationCell;
        boolean isValidDestinationCell;
        boolean isValidPlaceableCell;
        
        
        isValidDestinationCell = false;
        isValidPlaceableCell = false;
        System.out.println("Please select a valid destination: (Format: A2, C4, D1)");
        destinationCell = s.nextLine();
        
        if(destinationCell.length()==2) {
            isValidDestinationCell =  checkCellValidity(destinationCell);
            //ends function call prematurely if input is invalid
            if(isValidDestinationCell == false) {
                return false;
            }
            
            
            destinationCoordinate = convertToCoordinate(destinationCell);

            for(int i=0; i<possibleMoves.length; i++) {
                if(destinationCoordinate[0]==possibleMoves[i][0] && destinationCoordinate[1]==possibleMoves[i][1]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static byte checkWin() {
        //black wins
        if(gameBoard[0][0].hasPiece && gameBoard[0][1].hasPiece && gameBoard[0][2].hasPiece
                    && gameBoard[1][0].hasPiece && gameBoard[1][1].hasPiece
                    && gameBoard[2][0].hasPiece) {
            if(gameBoard[0][0].heldPiece.color.equals("B") && gameBoard[0][1].heldPiece.color.equals("B") && gameBoard[0][2].heldPiece.color.equals("B")
                    && gameBoard[1][0].heldPiece.color.equals("B") && gameBoard[1][1].heldPiece.color.equals("B")
                    && gameBoard[2][0].heldPiece.color.equals("B")) {
                return -1;
            }
        }
        //white wins
        if(gameBoard[1][3].hasPiece 
                && gameBoard[2][2].hasPiece && gameBoard[2][3].hasPiece
                && gameBoard[3][1].hasPiece && gameBoard[3][2].hasPiece && gameBoard[3][3].hasPiece) {
            if(gameBoard[1][3].heldPiece.color.equals("W") 
                    && gameBoard[2][2].heldPiece.color.equals("W") && gameBoard[2][3].heldPiece.color.equals("W")
                    && gameBoard[3][1].heldPiece.color.equals("W") && gameBoard[3][2].heldPiece.color.equals("W") && gameBoard[3][3].heldPiece.color.equals("W")) {
                return 1;
            }
        }
        
        //no winner yet
        return 0;
    }
    
    //public static void swapPieces(Tile sourceTile, Tile destinationTile) {
    public static void swapPieces() {
        Tile sourceTile = gameBoard[sourceCoordinate[0]][sourceCoordinate[1]]; 
        Tile destinationTile = gameBoard[destinationCoordinate[0]][destinationCoordinate[1]];
        
        /*
        tile1.hasPiece = tile2.hasPiece;
        tile1.heldPiece = tile2.heldPiece;
        
        tile2.hasPiece = tempHasPiece;
        tile2.heldPiece = tempPiece;
        */
        
        //if destination tile is empty, source tile will have no more piece, destination tile will have the piece.
        if(destinationTile.hasPiece == false) {
            destinationTile.heldPiece = sourceTile.heldPiece;
            destinationTile.hasPiece = true;
            sourceTile.hasPiece = false;
            sourceTile.heldPiece = null;
            
            //the piece will change its coordinates
            destinationTile.heldPiece.xcoord = destinationCoordinate[0];
            destinationTile.heldPiece.ycoord = destinationCoordinate[1];
            
            //the player will update its list of owned pieces
            players[curPlayer].updateOwnedPieces(sourceCoordinate, destinationCoordinate);
        }
        //if destination tile contains another player's piece(only happens in swaps),
        //source's piece will be held in a temp holder variable
        //source tile will then hold destination tile's piece
        //destination tile will then hold source tile's piece
        //then update both piece's coordinates and update both players' list of owned pieces
        else if(destinationTile.hasPiece == true) {
            Piece tempPiece = sourceTile.heldPiece;
            sourceTile.heldPiece = destinationTile.heldPiece;
            destinationTile.heldPiece = tempPiece;
            
            destinationTile.heldPiece.xcoord = sourceCoordinate[0];
            destinationTile.heldPiece.ycoord = sourceCoordinate[1];
            sourceTile.heldPiece.xcoord = destinationCoordinate[0];
            sourceTile.heldPiece.ycoord = destinationCoordinate[1];
            
            
            //keep track of other player to update their list of owned pieces
            int otherPlayer;
            
            //if current player is white, other player is black. else other player is white
            if(curPlayer == 0)
                otherPlayer = 1;
            else
                otherPlayer = 0;
            
            players[curPlayer].updateOwnedPieces(sourceCoordinate, destinationCoordinate);
            players[otherPlayer].updateOwnedPieces(destinationCoordinate, sourceCoordinate);
        }
    }
    
    public static boolean checkCellValidity(String cell) {
        char letter = 'A';
        int number = 1;
        String joined;
        
        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                joined = letter + new Integer(number).toString();
                //System.out.println(joined);
                if(joined.equals(cell))
                    return true;
                number++;
            }
            letter++;
            number = 1;
        }
        return false;
    }
    
    //this method just converts the A1, B3, C2, D4 stuff into coordinates that can be put into the gameBoard[row][col]
    public static int[] convertToCoordinate(String cell) {
        
        //this array is the coordinate wherein [x, y] or [row, col]
        int[] coordinate = new int[2];
        
        //checks if the row(the letter) is either A, B, C, or D and assigns numbers accordingly.
        //if not A, B, C, or D then set to 9999
        if(cell.charAt(0)=='A')
            coordinate[0] = 0;
        else if(cell.charAt(0)=='B')
            coordinate[0] = 1;
        else if(cell.charAt(0)=='C')
            coordinate[0] = 2;
        else if(cell.charAt(0)=='D') 
            coordinate[0] = 3;
        else
            coordinate[0] = 9999;
        
        
        //checks if the column(the number) is either 1, 2, 3, or 4 and assigns numbers accordingly
        if(cell.charAt(1)=='1')
            coordinate[1] = 0;
        else if(cell.charAt(1)=='2')
            coordinate[1] = 1;
        else if(cell.charAt(1)=='3')
            coordinate[1] = 2;
        else if(cell.charAt(1)=='4') 
            coordinate[1] = 3;
        else
            coordinate[1] = 9999;
        
        return coordinate;
    }
    
    //this method converts a coordinate (e.g. (1,3), (2,2), (0,1)) to a cell to display as output
    public static String convertToCell(int[] coordinate) {
        String cell = "";
        
        //checks if X coordinate is either 0, 1, 2, or 3 and assigns letters accordingly
        if(coordinate[0] == 0)
            cell = cell + "A";
        else if(coordinate[0] == 1)
            cell = cell + "B";
        else if(coordinate[0] == 2)
            cell = cell + "C";
        else if(coordinate[0] == 3)
            cell = cell + "D";
        
        //checks if Y coordinate is either 0, 1, 2, or 3 and assigns numbers accordingly
        if(coordinate[1] == 0)
            cell = cell + "1";
        else if(coordinate[1] == 1)
            cell = cell + "2";
        else if(coordinate[1] == 2)
            cell = cell + "3";
        else if(coordinate[1] == 3)
            cell = cell + "4";
        
        return cell;
    }
    
    
    //checks if the piece selected is valid
    //ADD CASES IF NO FORWARDABLE MOVES ARE POSSIBLE LIKE SWAPPABLE AND HOPPABLE
    public static boolean checkPieceValidity(int[] coordinate) {
        //these variables are just for easier access of the row and col coordinates
        int row = coordinate[0];
        int col = coordinate[1];
        //this variable is for checking if the player selects a piece that they own
        boolean isOwned = false;
        //this variable is for checking if the piece selected can move
        boolean isMovable = false;
        
        if(row != 9999 && col != 9999) { 
            //checks if it's white's turn
            if(playerTurn==false) {
                //if selected cell contains a white piece
                if(players[0].isOwner(row, col)) {
                    isOwned = true;
                    isMovable = checkMovability(players[0].obtainPiece(row, col));
                }
            }

            //check if  it's black's turn
            else if(playerTurn==true) {
                //if selected cell contains a black piece
                if(players[1].isOwner(row, col)) {
                    isOwned = true;
                    isMovable = checkMovability(players[1].obtainPiece(row, col));
                }
            }

            if(isOwned)
                System.out.print("Player owns the piece. ");
            else
                System.out.print("Player does not own the piece. ");
            if(isMovable)
                System.out.println("Piece is movable.");
            else
                System.out.println("Piece is not movable.");
        }
        else {
            System.out.println("Invalid cell");
            return false;
        }
        return (isOwned && isMovable);
    }
    
    //this method checks if the piece selected can be moved
    //EDIT THIS METHOD WHEN CONSIDERING HOPS AND SWAPS
    public static boolean checkMovability(Piece currpiece) {
        boolean isMovableUp = false;
        boolean isMovableDown = false;
        boolean isMovableRight = false;
        boolean isMovableLeft = false;
        
        //checks if the piece is in the top row (row A)
        if(currpiece.xcoord==0) {
            //if tile below piece is empty
            if(gameBoard[currpiece.xcoord+1][currpiece.ycoord].hasPiece == false)
                isMovableDown = true;
        }
        //checks if the piece is in the middle rows (row B or C)
        else if(currpiece.xcoord==1 || currpiece.xcoord==2) {
            //if tile above piece is empty
            if(gameBoard[currpiece.xcoord-1][currpiece.ycoord].hasPiece == false)
                isMovableUp = true;
            //if tile below piece is empty
            if(gameBoard[currpiece.xcoord+1][currpiece.ycoord].hasPiece == false)
                isMovableDown = true;
        }
        //checks if the piece is in the bottom row (row D)
        else {
            //if tile above piece is empty
            if(gameBoard[currpiece.xcoord-1][currpiece.ycoord].hasPiece == false)
                isMovableUp = true;
        }
        
        //checks if the piece is in the leftmost column (col 1)
        if(currpiece.ycoord==0) {
            //if tile to the right of piece is empty
            if(gameBoard[currpiece.xcoord][currpiece.ycoord+1].hasPiece == false)
                isMovableRight = true;
        }
        //checks if the piece is in the middle columns (col 2 or 3)
        else if(currpiece.ycoord==1 || currpiece.ycoord==2) {
            //if tile to the left of piece is empty
            if(gameBoard[currpiece.xcoord][currpiece.ycoord-1].hasPiece == false)
                isMovableLeft = true;
            //if tile to the right of piece is empty
            if(gameBoard[currpiece.xcoord][currpiece.ycoord+1].hasPiece == false)
                isMovableRight = true;
        }
        //checks if the piece is in the rightmost column (col 4)
        else {
            //if tile to the left of piece is empty
            if(gameBoard[currpiece.xcoord][currpiece.ycoord-1].hasPiece == false)
                isMovableLeft = true;
        }
        
        return (isMovableUp || isMovableDown || isMovableRight || isMovableLeft);
    }
    
    //will find cells that the piece can be moved to
    //EDIT THIS METHOD WHEN CONSIDERING HOPS AND SWAPS
    public static int[][] findPlaceableCells(int[] sourceCoordinate) {
        //just some holder variables
        int sourceX = sourceCoordinate[0];
        int sourceY = sourceCoordinate[1];
        
        //maximum of 4 possible moves per piece since there are 16 cells and 12 pieces, so 4 empty cells at all times
        //per cell there is an x y coordinate hence 4 by 2 array
        List<int[]> possibleMoves = new ArrayList<int[]>();
        
        //placeholder for the move
        int[] move = new int[2];
        
        //checks if the piece is in the leftmost column (col 1)
        if(sourceY==0) {
            //if cell to the right is empty
            if(gameBoard[sourceX][sourceY+1].hasPiece == false) {
                move[0] = sourceX;
                move[1] = sourceY + 1;
                possibleMoves.add(move);
                move = new int[2];
            }
        }
        //checks if the piece is in the middle columns (col 2 or 3)
        else if(sourceY==1 || sourceY==2) {
            //if cell to the left is empty
            if(gameBoard[sourceX][sourceY-1].hasPiece == false) {
                move[0] = sourceX;
                move[1] = sourceY - 1;
                possibleMoves.add(move);
                move = new int[2];
            }
            //if cell to the right is empty
            if(gameBoard[sourceX][sourceY+1].hasPiece == false) {
                move[0] = sourceX;
                move[1] = sourceY + 1;
                possibleMoves.add(move);
                move = new int[2];
            }
        }
        //checks if the piece is in the rightmost column (col 4)
        else {
            //if cell to the left is empty
            if(gameBoard[sourceX][sourceY-1].hasPiece == false) {
                move[0] = sourceX;
                move[1] = sourceY - 1;
                possibleMoves.add(move);
                move = new int[2];
            }
        }
        
        //checks if the piece is in the top row (row A)
        if(sourceX==0) {
            //if cell below is empty
            if(gameBoard[sourceX+1][sourceY].hasPiece == false) {
                move[0] = sourceX + 1;
                move[1] = sourceY;
                possibleMoves.add(move);
                move = new int[2];
            }
        }
        //checks if the piece is in the middle rows (row B or C)
        else if(sourceX==1 || sourceX==2) {
            //if cell above is empty
            if(gameBoard[sourceX-1][sourceY].hasPiece == false) {
                move[0] = sourceX - 1;
                move[1] = sourceY;
                possibleMoves.add(move);
                move = new int[2];
            }
            //if cell below is empty
            if(gameBoard[sourceX+1][sourceY].hasPiece == false) {
                move[0] = sourceX + 1;
                move[1] = sourceY;
                possibleMoves.add(move);
                move = new int[2];
            }
        }
        //checks if the piece is in the bottom row (row D)
        else {
            //if cell above is empty
            if(gameBoard[sourceX-1][sourceY].hasPiece == false) {
                move[0] = sourceX - 1;
                move[1] = sourceY;
                possibleMoves.add(move);
                move = new int[2];
            }
        }
        
        
        int[][] possibleMovesArray = new int[possibleMoves.size()][2];
        possibleMovesArray = possibleMoves.toArray(possibleMovesArray);
        
        return possibleMovesArray;
    }
    
    //swaps places of two pieces or a piece and an empty cell
    /*public static void swap(int[] piece1, int[] piece2) {
        int cell1 = gameBoard[piece1[0]][piece1[1]];
        int cell2 = gameBoard[piece2[0]][piece2[1]];
        int temp = cell2;
        
        gameBoard[piece1[0]][piece1[1]] = cell2;
        gameBoard[piece2[0]][piece2[1]] = cell1;      
    }
    */
    
    public static void testPrintBoard() {
        for(int i=0; i<gameBoard.length; i++) {
            for(int j=0; j<gameBoard.length; j++) {
                if(j!=gameBoard.length-1) {
                    if(gameBoard[i][j].hasPiece) {
                        System.out.print(gameBoard[i][j].heldPiece.color + "\t");
                    }
                    else
                        System.out.print("O" + "\t");
                }
                else {
                    if(gameBoard[i][j].hasPiece) {
                        System.out.println(gameBoard[i][j].heldPiece.color);
                    }
                    else
                        System.out.println("O");
                }
            }
        }
    }
    
    public static void printBoard() {
        System.out.println("Turn count:");
        System.out.println("White: " + players[0].turnCount + "\tBlack: " + players[1].turnCount);
        System.out.println("\t   1    2    3     4");
        
        
        //this line prints the top of the board
        System.out.println("\t\u2554\u2550\u2550\u2566\u2550\u2550\u2566\u2550\u2550\u2566\u2550\u2550\u2557");
        char letter = 'A';
        //this nested for loop prints the pieces in the board
        for(int i=0; i<gameBoard.length; i++) {
            System.out.print(letter++ + "\t");
            for(int j=0; j<gameBoard.length; j++) {
                if(j!=3) {
                    if(gameBoard[i][j].hasPiece) {
                        if(gameBoard[i][j].heldPiece.color.equals("W"))
                            System.out.print("\u2551 \u25CB ");
                        else if(gameBoard[i][j].heldPiece.color.equals("B"))
                            System.out.print("\u2551 \u25CF "); 
                    }
                    else
                        System.out.print("\u2551 \u23B5 ");
                }
                else {
                    if(gameBoard[i][j].hasPiece) {
                        if(gameBoard[i][j].heldPiece.color.equals("W"))
                            System.out.println("\u2551 \u25CB \u2551");
                        else if(gameBoard[i][j].heldPiece.color.equals("B"))
                            System.out.println("\u2551 \u25CF \u2551"); 
                    }
                    else
                        System.out.println("\u2551 \u23B5 \u2551");
                }
            }
            if(i!=3)
                System.out.println("\t\u2560\u2550\u2550\u256C\u2550\u2550\u256C\u2550\u2550\u256C\u2550\u2550\u2563");
        }
        System.out.println("\t\u255A\u2550\u2550\u2569\u2550\u2550\u2569\u2550\u2550\u2569\u2550\u2550\u255D");
    }
}
