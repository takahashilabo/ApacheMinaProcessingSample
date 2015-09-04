import java.util.Date;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

class Players 
{
	private ArrayList<Player> list = new ArrayList<Player>();

	public void add(long id)
	{
		list.add(new Player(id, 0, 0, 0));
	}
	
	public void update(String s)
	{
        StringTokenizer t = new StringTokenizer(s, ",");
        if (t.countTokens() == 4) 
        {
        	int id 	= Integer.parseInt(t.nextToken());
        	int x 	= Integer.parseInt(t.nextToken());
        	int y 	= Integer.parseInt(t.nextToken());
        	int th 	= Integer.parseInt(t.nextToken());
    		
        	for(Player p : list)
        	{
    			if (p.id == id){
            		p.x = x;
            		p.y = y;
            		p.th = th;
            		return;
        		}
    		}
        }
	}
	
	public String toString()
	{
		String s = list.size() + ",";
		Player lastP = list.get(list.size() - 1);
		for (Player p : list)
		{
			s += p.toString();
			if (p != lastP) s += ",";
		}
		return s;
	}
}

class Player 
{
	long id;
	int x, y, th;

	public Player(long id, int x, int y, int th){
		this.id = id;
		this.x = x;
		this.y = y;
		this.th = th;
	}
	
	public String toString()
	{
		return id + "," + x + "," + y + "," + th;
	}
}

public class TimeServerHandler extends IoHandlerAdapter
{
	private Players p = new Players();
	
	@Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
        String str = message.toString();
        if( str.trim().equalsIgnoreCase("quit") ) 
        {
        	session.close();
            System.exit(1);
        }

        if( str.trim().equalsIgnoreCase("hello") ) 
        {
        	p.add(session.getId()); //new connection
        	session.write(Long.toString(session.getId()) + "," + p.toString() );
        	return;
        }

        p.update(str);
    	session.write(Long.toString(session.getId()) + "," + p.toString() );
    }

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
        System.out.println( "IDLE " + session.getIdleCount( status ));
    }
}