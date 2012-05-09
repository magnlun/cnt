/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;

// Added this for clarity
import cnt.game.Board;
import cnt.game.Block;
import cnt.game.Shape;
import cnt.game.Player;

import java.io.*;


/**
* Shape class representing a J-shape
* 
* @author Calle Lejdbrandt <a href="mailto:callel@kth.se">callel@kth.se</a>
*/

public class JShape extends Shape
{	
	private Block[][][] states;
	private int currState = 0;
	public JShape()
	{
	    this.states = new Block[4][3][3];
		this.shape = new Block[3][3];
		
		int[][] placement = new int[][] {{1,0},{1,1},{1,2},{0,2}};
		for (int[] place : placement)
		{
			this.shape[place[0]][place[1]] = new Block(this.player.getColor());
		}
		
		this.states[0] = this.shape;
		
		int[][][] coords = new int[3][4][2];

		coords[0] = new int[][] {{0,1},{1,1},{2,1},{0,0}};
		coords[1] = new int[][] {{2,0},{1,0},{1,1},{1,2}};
		coords[2] = new int[][] {{2,2},{0,1},{1,1},{2,1}};
		
		int i = 1;
		for (int[][] coord : coords)
		{
			Block[][] matrix = new Block[3][3];
			for (int[] place : coord)
			{
				matrix[place[0]][place[1]] = new Block(this.player.getColor());
			}
			
			this.states[i++] = matrix;
		}
	}
    
    private JShape(final JShape original)
    {
	original.cloneData(this);
	this.states = original.states;
	this.currState = original.currState;
    }
    
    
    
    /**
     * Momento class for {@link JShape}
     */
    public static class Momento extends Shape.Momento
    {
        public Momento(final JShape shape)
        {
            super(shape);
            this.states = shape.states;
            this.currState = shape.currState;
        }
            
        private final Block[][][] states;
	    private final int currState;
    
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof JShape)
                throw new Error("Wrong shape type");
            super.restore(shape);
            ((JShape)shape).states = this.states;
            ((JShape)shape).currState = this.currState;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Momento store()
    {
        return new Momento(this);
    }
    
    

	public void rotate(final boolean clockwise)
	{
		if (clockwise)
		{
			this.currState = (this.currState + 1) % 3;
		} else
		{
			this.currState = (this.currState - 1) < 0 ? (this.currState + 2) : (this.currState - 1) ;
		}
		
		this.shape = this.states[this.currState];
	}
	
    public JShape clone()
    {
	return new JShape(this);
    }
}
