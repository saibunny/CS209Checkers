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
    
    
}