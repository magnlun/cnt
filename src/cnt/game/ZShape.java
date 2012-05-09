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
* Shape class representing a Z-shape
* 
* @author Calle Lejdbrandt <a href="mailto:callel@kth.se">callel@kth.se</a>
*/
public class ZShape extends Shape
{
	boolean flat;
	
	public ZShape()
	{
	    this.flat = true;
		this.shape = new Block[3][3];
		
		int[][] placement = new int[][] {{1,0},{0,0},{2,1},{1,1}};
		for (int[] place : placement)
		{
			this.shape[place[0]][place[1]] = new Block(this.player.getColor());
		}
	}
    
    private ZShape(final ZShape original)
    {
	original.cloneData(this);
	this.flat = original.flat;
    }
    
    
    
    /**
     * Momento class for {@link ZShape}
     */
    public static class Momento extends Shape.Momento
    {
        public Momento(final ZShape shape)
        {
            super(shape);
            this.flat = shape.flat;
        }
            
        private final boolean flat;
    
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof ZShape)
                throw new Error("Wrong shape type");
            super.restore(shape);
            ((ZShape)shape).flat = this.flat;
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
		Block[][] matrix = new Block[3][3];

		if (this.flat)
		{
			matrix[2][1] = this.shape[2][1];
			matrix[1][1] = this.shape[1][1];
			matrix[1][2] = this.shape[1][0];
			matrix[2][0] = this.shape[0][0];
			
			this.shape = matrix;
			this.flat = false;
		} else
		{
						
			matrix[2][1] = this.shape[2][1];
			matrix[1][1] = this.shape[1][1];
			matrix[1][0] = this.shape[1][2];
			matrix[0][0] = this.shape[2][0];
			
			this.shape = matrix;
			this.flat = true;

		}				
	}
    
    public ZShape clone()
    {
	return new ZShape(this);
    }
}
