/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.*;
import cnt.mock.GameNetworking;

import java.util.*;
import java.io.*;


/**
 * Blackboard networking layer
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class BlackboardNetworking implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     * 
     * @param  gameNetworking  The next layer the networking stack
     */
    public BlackboardNetworking(final GameNetworking gameNetworking)
    {
	this.gameNetworking = gameNetworking;
	
	Blackboard.registerObserver(this);
    }
    
    
    
    /**
     * The next layer the networking stack
     */
    private final GameNetworking gameNetworking;
    
    /**
     * Blackboard message to ignore (with how many times) to prevent an infinite resonance loop
     */
    private final HashMap<Blackboard.BlackboardMessage, Integer> ignore = new HashMap<Blackboard.BlackboardMessage, Integer>();
    
    
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	synchronized (this.ignore)
	{
	    if (ignore.containsKey(message))
	    {
		final int count = this.ignore.get(message).intValue();
		
		if (count == 1)  this.ignore.remove(message);
		else             this.ignore.put(message, new Integer(count - 1));
		
		return;
	    }
	}
	
	try
        {
	    if      (message instanceof Blackboard.MatrixPatch)    this.gameNetworking.forward(message);
	    else if (message instanceof Blackboard.ChatMessage)    this.gameNetworking.forward(message);
	    else if (message instanceof Blackboard.SystemMessage)  this.gameNetworking.forward(message);
	    else if (message instanceof Blackboard.UserMessage)
	    {
		Blackboard.UserMessage msg = (Blackboard.UserMessage)message;
		this.gameNetworking.chat(msg.message);
	    }
	    else
		assert false : "Update message types in BlackboardNetworking";
	}
	catch (final IOException err)
	{
	    //FIXME error!
	}
    }
    
    
    /**
     * Wait for, receive, and local broadcast a message
     * 
     * @throws  IOException             On networking exception
     * @throws  ClassNotFoundException  In the message type is not a part of the program
     */
    public void receiveAndBroadcast() throws IOException, ClassNotFoundException
    {
        final Serializable object = this.gameNetworking.receive();
	if (object instanceof Blackboard.BlackboardMessage)
	    broadcastMessage((Blackboard.BlackboardMessage)object);
    }
    
    
    /**
     * Broadcasts a message
     * 
     * @param  message  The message to broadcast
     * 
     * @throws  IOException             On networking exception
     * @throws  ClassNotFoundException  In the message type is not a part of the program
     */
    protected void broadcastMessage(final Blackboard.BlackboardMessage message) throws IOException, ClassNotFoundException
    {
	synchronized (this.ignore)
	{
	    Integer count = this.ignore.get(message);
	    
	    if (count == null)  count = new Integer(1);
	    else                count = new Integer(count.intValue() + 1);
	    
	    this.ignore.put(message, count);
	    
	    Blackboard.broadcastMessage(message);
	}
    }

}

