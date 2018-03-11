/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs209checkersv2;

/**
 *
 * @author kendrick
 */
public class Piece {
    public int xcoord;
    public int ycoord;
    public String color;
    public boolean isMovable;
    public boolean isForwardable;
    public boolean isSwappable;
    public boolean isHoppable;
    public Player owner;

    public Piece(int xcoord, int ycoord, String color, Player owner) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.color = color;
        this.isForwardable = false;
        this.isHoppable = false;
        this.isSwappable = false;
        this.owner = owner;
    }
    
    public boolean checkMovability(Tile[][] gameBoard, Player current) {
        boolean isMovableUp = false;
        boolean isMovableDown = false;
        boolean isMovableRight = false;
        boolean isMovableLeft = false;
        
        //checks if the piece is in the top row (row A)
        if(this.xcoord==0) {
            //if tile below piece is empty
            if(gameBoard[this.xcoord+1][this.ycoord].hasPiece == false)
                isMovableDown = true;
        }
        //checks if the piece is in the middle rows (row B or C)
        else if(this.xcoord==1 || this.xcoord==2) {
            //if tile above piece is empty
            if(gameBoard[this.xcoord-1][this.ycoord].hasPiece == false)
                isMovableUp = true;
            //if tile below piece is empty
            if(gameBoard[this.xcoord+1][this.ycoord].hasPiece == false)
                isMovableDown = true;
        }
        //checks if the piece is in the bottom row (row D)
        else {
            //if tile above piece is empty
            if(gameBoard[this.xcoord-1][this.ycoord].hasPiece == false)
                isMovableUp = true;
        }
        
        //checks if the piece is in the leftmost column (col 1)
        if(this.ycoord==0) {
            //if tile to the right of piece is empty
            if(gameBoard[this.xcoord][this.ycoord+1].hasPiece == false)
                isMovableRight = true;
        }
        //checks if the piece is in the middle columns (col 2 or 3)
        else if(this.ycoord==1 || this.ycoord==2) {
            //if tile to the left of piece is empty
            if(gameBoard[this.xcoord][this.ycoord-1].hasPiece == false)
                isMovableLeft = true;
            //if tile to the right of piece is empty
            if(gameBoard[this.xcoord][this.ycoord+1].hasPiece == false)
                isMovableRight = true;
        }
        //checks if the piece is in the rightmost column (col 4)
        else {
            //if tile to the left of piece is empty
            if(gameBoard[this.xcoord][this.ycoord-1].hasPiece == false)
                isMovableLeft = true;
        }
        
        if(current.name.equals("W")) {
            this.isForwardable = (isMovableRight || isMovableDown);
        }
        else if(current.name.equals("B")) {
            this.isForwardable = (isMovableLeft || isMovableUp);
        }
        
        return (isMovableUp || isMovableDown || isMovableRight || isMovableLeft || this.isHoppable || this.isSwappable);
    }
    
    public boolean isForwardable(Tile[][] gameBoard, Player current) {
        boolean isMovableUp = false;
        boolean isMovableDown = false;
        boolean isMovableRight = false;
        boolean isMovableLeft = false;
        
        //checks if the piece is in the top row (row A)
        if(this.xcoord==0) {
            //if tile below piece is empty
            if(gameBoard[this.xcoord+1][this.ycoord].hasPiece == false)
                isMovableDown = true;
        }
        //checks if the piece is in the middle rows (row B or C)
        else if(this.xcoord==1 || this.xcoord==2) {
            //if tile above piece is empty
            if(gameBoard[this.xcoord-1][this.ycoord].hasPiece == false)
                isMovableUp = true;
            //if tile below piece is empty
            if(gameBoard[this.xcoord+1][this.ycoord].hasPiece == false)
                isMovableDown = true;
        }
        //checks if the piece is in the bottom row (row D)
        else {
            //if tile above piece is empty
            if(gameBoard[this.xcoord-1][this.ycoord].hasPiece == false)
                isMovableUp = true;
        }
        
        //checks if the piece is in the leftmost column (col 1)
        if(this.ycoord==0) {
            //if tile to the right of piece is empty
            if(gameBoard[this.xcoord][this.ycoord+1].hasPiece == false)
                isMovableRight = true;
        }
        //checks if the piece is in the middle columns (col 2 or 3)
        else if(this.ycoord==1 || this.ycoord==2) {
            //if tile to the left of piece is empty
            if(gameBoard[this.xcoord][this.ycoord-1].hasPiece == false)
                isMovableLeft = true;
            //if tile to the right of piece is empty
            if(gameBoard[this.xcoord][this.ycoord+1].hasPiece == false)
                isMovableRight = true;
        }
        //checks if the piece is in the rightmost column (col 4)
        else {
            //if tile to the left of piece is empty
            if(gameBoard[this.xcoord][this.ycoord-1].hasPiece == false)
                isMovableLeft = true;
        }
        
        if(current.name.equals("W")) {
            this.isForwardable = (isMovableRight || isMovableDown);
        }
        else if(current.name.equals("B")) {
            this.isForwardable = (isMovableLeft || isMovableUp);
        }
        
        return this.isForwardable;
    }
}