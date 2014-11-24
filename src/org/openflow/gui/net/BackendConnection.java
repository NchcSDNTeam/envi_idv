package org.openflow.gui.net;

import org.openflow.gui.ConnectionHandler;
import org.openflow.gui.Topology;
import org.openflow.gui.net.protocol.NodeType;
import org.openflow.gui.net.protocol.NodesList;
import org.openflow.gui.net.protocol.OFGMessage;
import org.openflow.gui.net.protocol.OFGMessageType;
import org.openflow.gui.net.protocol.PollStart;
import org.openflow.nchc.wunyuan.FlowData;
import org.openflow.nchc.wunyuan.NodeData;
import org.openflow.nchc.wunyuan.ReadClientData;
import org.pzgui.layout.Edge;
import org.pzgui.layout.PZLayoutManager;
import org.pzgui.layout.Vertex;
import org.w3c.dom.NodeList;

import edu.uci.ics.jung.graph.Graph;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;


//add by wunyuan
import java.util.Stack;
//end
import java.util.concurrent.ConcurrentHashMap;


/**
 * Connects to a server instance to receive stats and send commands.
 * @author David Underhill
 */
public class BackendConnection<MSG_TYPE extends Message> extends Thread {
    /** whether to print messages we send and receive */
    public static final boolean PRINT_MESSAGES = false;
    
    /** how much time to remember a request before expiring it */
    private static final long REQUEST_LIFETIME_MSEC = 2000;
    
    /** maximum time to wait between tries to get connected */
    public static final int RETRY_WAIT_MSEC_MAX = 2 * 60 * 1000; // two minutes
    
    /**
     * Statistics about this connection.
     */
    public class NetStats {
        /** number of updates received from server */
        private long numUpdatesReceived = 0;

        /** time the last update was received */
        private long lastUpdateTime_ms;

        /** time we (dis)connected to server */
        private long timeConnected_ms;

        /** whether we are connected or disconnected */
        private boolean connected;

        public NetStats() {
            lastUpdateTime_ms = System.currentTimeMillis();
            disconnected();
        }

        /**
         * Returns the number of updates received from server.
         */
        public synchronized long getNumUpdatesdReceived() {
            return numUpdatesReceived;
        }

        /**
         * Notes that a new update has been received and updates the corresponding 
         * statistics.
         */
        public synchronized void updateReceived() {
            numUpdatesReceived += 1;
            lastUpdateTime_ms = System.currentTimeMillis();
        }

        /**
         * Gets the number of milliseconds which have passed since the last update 
         * was received.
         */
        public synchronized long getTimePassedSinceLastUpdate_ms() {
            return System.currentTimeMillis() - lastUpdateTime_ms;
        }

        /**
         * Returns how long the connection to the server server has been up.  A 
         * negative value indicates the connection has been down for the magnitude 
         * of the value.
         */
        public synchronized long getTimeConnected_ms() {
            long diff = System.currentTimeMillis() - timeConnected_ms;
            return connected ? diff : -diff;
        }

        /**
         * Returns how long the connection to the server server has been down.  A 
         * negative value indicates the connection has been up for the magnitude 
         * of the value.
         */
        public synchronized long getTimeDisconnected_ms() {
            return -getTimeConnected_ms();
        }

        public synchronized boolean isConnected() {
            return timeConnected_ms > 0;
        }

        /**
         * Sets the time the connection to the server server was established to the
         * current time.
         */
        public synchronized void connected() {
            setConnected(true);
        }

        /**
         * Sets the time the connection to the server server was lost to the
         * current time.
         */
        public synchronized void disconnected() {
            setConnected(false);
        }

        private void setConnected(boolean b) {
            timeConnected_ms = System.currentTimeMillis();
            connected = b;
        }
    }
    
    /** stats associated with this connection */
    private final NetStats stats = new NetStats();
    
    /** connection to the server */
    private SocketConnection conn = null;
    
    /** the IP the server server lives on */
    private final String serverIP;
    
    /** the port the server listens on */
    private final int serverPort;
    
    /** if true, then the connection should be re-initiated */
    private static boolean reconnect = false;
    
    /** the object responsible for processing messages */
    private final MessageProcessor<MSG_TYPE> msgProcessor;

    /** whether the connection should be turned off */
    private boolean done = false;

    /** whether the connection has been turned off */
    private boolean shutdown = false;
    
    //add by wunyuan
    public static Stack <String> connectionDone = new Stack<String>();
    public static Stack <String> controllerList = new Stack<String>();
    public static Stack <String> giveUpList = new Stack<String>();
    public static boolean giveUp = false; 
    //end
    
    /**
     * Connect to the server at the specified address and port.
     * 
     * @param mp    the processor for messages; if null, then "this" will be
     *              used as mp if it implements the MessageProcesser interface
     * @param ip    the IP where the server lives
     * @param port  the port the server listens on
     */
    public BackendConnection(MessageProcessor<MSG_TYPE> mp, String ip, int port) {
        if(mp == null && this instanceof MessageProcessor)
            msgProcessor = (MessageProcessor<MSG_TYPE>)this;
        else
            msgProcessor = mp;
        
        serverIP = ip;
        serverPort = port;  
    }

 
    
    
    
    /**
     * The BackendConnection thread will connect to the server and then 
     * continuously read from the socket and pass on received messages to the 
     * protocol handler.  It will automatically try to reconnect if disconnected
     * or if it fails to get connected.
     */
    
    
 // add by wunyuan    
    public void run() {
        // start a thread to scrub expired cached stateful requests
        new Thread() {
            public void run() {
                scrubExpiredStatefulRequests();
                try { Thread.sleep(REQUEST_LIFETIME_MSEC); }
                catch(InterruptedException e) {}
            }
        };
        boolean otherServer = false;
        int stop_ms = 250;
        String otherIP = null;
        
        connect(otherIP,otherServer);
        boolean matchAlConnection;
        boolean matchNoConnection;

        boolean jumpOut = false;
        
        String defaultIP = conn.s.getInetAddress().getHostAddress(); 
        boolean flag = true;
        while(!done) 
        {
        	if(conn != null )
        	{
        	try
        	{
            	System.out.println("ready to process data");
            	if(!jumpOut)
            	{
            		msgProcessor.process(recvMessage1());
            	}
                
            	if(NodesList.backupnodes != null)
            	{
            		if(NodesList.backupnodes[0].getNodeType()!= NodeType.HOST)
            		{
            			for(int i=0; i < (NodesList.backupnodes.length); i++)
            			{               		
            				String tempIp = NodesList.getController(i);
            				matchAlConnection = this.Ipcompare(tempIp, controllerList);
            				matchNoConnection = this.Ipcompare(tempIp, connectionDone);
            				if( matchAlConnection == false && matchNoConnection == false )
            					controllerList.push(tempIp);              	
            			}
            		}
            	}
            	flag = true;
            	if(ReadClientData.dataReadOver)
            	{
            		jumpOut= false;
            		while(!(controllerList.empty()) )
            		{
            			otherServer = true;
            			otherIP = controllerList.elementAt(0);

            			if(this.Ipcompare(otherIP, giveUpList))
            			{
            				giveUp=true;
            			}
            			else
            			{
            				disconnectWithTopology();
            				connect(otherIP, otherServer);
            				ReadClientData.dataReadOver = false;
            			}
            			
            			controllerList.remove(0);
            			flag = false;
            			if(giveUp)
            			{
            				disconnectWithTopology();
            				giveUp = false;
            			}
            			else
            				break;
            		}
            		if(controllerList.empty() && flag)
            		{
            			disconnectWithTopology();
            		}
                		
                }
            }
            catch(IOException e) 
            {
            	if(done)
            		break;
            	System.err.println("Network Error: " + e);
            	System.out.println("Network Error: " + e + "Set reconnect = true");
            	//reconnect = true;
            	ReadClientData.dataReadOver = true;
            	jumpOut = true;
            }
        	}
        	
            if(reconnect) 
            {
            	ReadClientData.dataReadOver = false;       
            	disconnect();
            	
            	try {
            		sleep(2000);
            		} 
            	catch (InterruptedException e) 
            	{
            		// TODO Auto-generated catch block
            		e.printStackTrace();
            	}
            	connect(defaultIP, otherServer);
            	reconnect = false;
             }
        } 
        shutdown = true;
    }
    
    
    
    public boolean Ipcompare(String Ip, Stack <String> Iplist)
    {
    	boolean match = false;
    	for(int i=0; i < Iplist.size(); i++)
    	{
    		String compip = Iplist.elementAt(i);
    		if( Ip.equals(compip))
    		{
    			match = true;
    			continue;
    		}
    	}
		return match;
    	
    }
    
    public static void setReconnect( boolean connect)
    {
    	reconnect = connect;
    }
    
//end
    
/*    original code
 * 
    public void run() {
        // start a thread to scrub expired cached stateful requests
        new Thread() {
            public void run() {
                scrubExpiredStatefulRequests();
                try { Thread.sleep(REQUEST_LIFETIME_MSEC); }
                catch(InterruptedException e) {}
            }
        };

        connect();

        while(!done) {
            try {
                msgProcessor.process(recvMessage1());
                
                
            } catch(IOException e) {
                if(done)
                    break;
                
                System.err.println("Network Error: " + e);
                reconnect = true;
            }
            if(reconnect) {
                reconnect = false;
                disconnect();
                connect(ha);
            }
        }
        shutdown = true;
    }
*/
    
    
    /** tells the connection to shut down as soon as possible */
    public void shutdown() {
        done = true;
        disconnect();
    }

    /** gets whether the connection has been shutdown yet */
    public boolean isShutdown() {
        return shutdown;
    }
    
    /** returns true if the connection to the server is currently alive */
    public boolean isConnected() {
        return conn!=null && conn.s!=null;
    }
    
    /** Continuously tries to connect to the server. */

    
    private void connect(String OtherIp ,boolean OtherServer ) {
    	
        stats.disconnected();
        int retry_ms = 250;
        giveUp=false;
        int tries = 0;
        do {
            if(done) return;
            
            if(tries++ > 0) {
                System.out.println("Retrying to establish connection to server  (IP="+OtherIp+") (try #" + tries + ")...");
                tryToClose(conn);
                if(tries > 2)
                {
                	giveUp=true;
                	System.out.println("Give up to connect to server  (IP="+OtherIp+")");
                	giveUpList.push(OtherIp);
                	return;
                }
            }
            else
            	if(OtherIp == null)
            	{
            		System.out.println("Trying to establish connection to server (IP="+serverIP+")...");
            	}
            	else
            		System.out.println("Trying to establish connection to server (IP="+OtherIp+")...");

            if(OtherServer)
            {
            	conn = new SocketConnection(OtherIp,serverPort );
            	if(!(this.Ipcompare(OtherIp, connectionDone)))
            		connectionDone.push(OtherIp);
            }
            else
            {
            	conn = new SocketConnection(serverIP, serverPort);
            	connectionDone.push(serverIP);
            }

            if(conn.s == null) {
            	
                System.out.println("Failed to establish connections to server!(IP="+OtherIp+")"+ "(will retry in " + retry_ms/1000.0f  + " seconds)");
      
                try {
                    Thread.sleep(retry_ms);
                    retry_ms = Math.min(retry_ms*2, RETRY_WAIT_MSEC_MAX);
                }
                catch (InterruptedException e) {
                    
                }
            }
        }
        while(conn==null || conn.s==null);
        
        System.out.println("Now connected to server");
        //stats.disconnected();
        stats.connected();
        msgProcessor.connectionStateChange(true);
    }
    
    
    /* original code
    private void connect() {
        stats.disconnected();
        int retry_ms = 250;
        
        int tries = 0;
        do {
            if(done) return;
            
            if(tries++ > 0) {
                System.out.println("Retrying to establish connection to server (try #" + tries + ")...");
                tryToClose(conn);
            }
            else
                System.out.println("Trying to establish connection to server ...");


            conn = new SocketConnection(serverIP, serverPort);

            if(conn.s == null) {
                System.out.println("Failed to establish connections to server! (will retry in " + retry_ms/1000.0f  + " seconds)");
                try {
                    Thread.sleep(retry_ms);
                    retry_ms = Math.min(retry_ms*2, RETRY_WAIT_MSEC_MAX);
                }
                catch (InterruptedException e) {
                    
                }
            }
        }
        while(conn==null || conn.s==null);
        
        System.out.println("Now connected to server");
        stats.connected();
        msgProcessor.connectionStateChange(true);
    }
    */
    
    
    
    /** tells the connection to disconnect and then connect again */
    public void reconnect() {
    	
        this.reconnect = true;
    }
    
    
    //add by wunyuan
    private MSG_TYPE recvMessage1() throws IOException {
        final CountingDataInputStream in = conn.in;
        MSG_TYPE msg;
        if(in == null)
            throw new IOException("connection is disconnected");
        
        System.out.println("Check whether there are data or not.");
        
               
        long bytesReadBefore = in.getBytesRead();


        int retry =0;
        
        if(in.checkAvailable() == 0 )
        {
        	while(retry != 5)
        	{
        	try {
        		System.out.println("retry ="+retry);
        		System.out.println("no data. Please wait fot data receving");
				sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("wait end.");
			if(in.checkAvailable() > 0)
			{
				System.out.println("break");
				break;
			}
			else
				retry++;
				
        	}

        }
        if(in.checkAvailable() > 0)
        {
        	System.out.println("Get data. process recevied data.");
        	
        	int len = in.readShort();
               
        	//add wunyuan
        	OFGMessage.datalength = in.readShort();
        

        	//end
        
        	// decode the message
        	msg = msgProcessor.decode(len, in);
        


        		// make sure we consume exactly the specified number of bytes or problems have
        		long bytesRead = in.getBytesRead() - bytesReadBefore;
        	if(bytesRead < len) {
        		int bytesLeftover = (int)(len - bytesRead);
            	if(in.skipBytes(bytesLeftover) != bytesLeftover)
            		throw new IOException("unable to skip leftover bytes (" + bytesLeftover + "B)");
            	else
            		System.err.println("Warning: " + bytesLeftover + "B leftover for message type " + msg.getType().toString());
        	}
        	else if(bytesRead > len) {
        		long bytesOverread = bytesRead - len;
            	throw new IOException("read " + bytesOverread + "B over the specified length for message type " + msg.getType().toString());
        	}
        	System.out.println("Finish the data process.");
        }
        else
        {
        	System.out.println("no data for processing.");
        	ReadClientData.dataReadOver = true;
        	msg = null;
        }
        return msg;
    }
    
    //end
    
    
    //original
    /** returns the next message received on the connection */
    private MSG_TYPE recvMessage() throws IOException {
        final CountingDataInputStream in = conn.in;
        if(in == null)
            throw new IOException("connection is disconnected");
        
    
        
        long bytesReadBefore = in.getBytesRead();

        // determine how long the message is
        int len = in.readShort();
        
        

        // decode the message
        MSG_TYPE msg = msgProcessor.decode(len, in);

        // make sure we consume exactly the specified number of bytes or problems have
        long bytesRead = in.getBytesRead() - bytesReadBefore;
        if(bytesRead < len) {
            int bytesLeftover = (int)(len - bytesRead);
            if(in.skipBytes(bytesLeftover) != bytesLeftover)
                throw new IOException("unable to skip leftover bytes (" + bytesLeftover + "B)");
            else
                System.err.println("Warning: " + bytesLeftover + "B leftover for message type " + msg.getType().toString());
        }
        else if(bytesRead > len) {
            long bytesOverread = bytesRead - len;
            throw new IOException("read " + bytesOverread + "B over the specified length for message type " + msg.getType().toString());
        }
        
        return msg;
    }
    
    /** next transaction ID to use */
    private int nextXID = 1;
    
    /** messages which are expecting a stateful response */
    protected ConcurrentHashMap<Integer, OFGMessage> outstandingStatefulRequests = new ConcurrentHashMap<Integer, OFGMessage>();
    
    /** stateful messages which are being polled by the backend for us */
    protected ConcurrentHashMap<Integer, OFGMessage> outstandingStatefulPollRequests = new ConcurrentHashMap<Integer, OFGMessage>();
    
    /** 
     * Tries to send a message and sets the transaction ID of the message
     * to the next available transaction ID.  If m is a POLL_REQUEST message, 
     * then the internal message's transaction ID is also set.
     */
    public void sendMessage(MSG_TYPE m) throws IOException {
        // get the current connection
        SocketConnection myConn = this.conn;
        java.io.DataOutput out = (myConn == null) ? null : myConn.out;
        
        // bail out if we are not connected - TODO: could try to mask this and 
        // buffer calls to this method and send them when we get reconnected
        if(out == null)
            throw new IOException("connection is down");
        
        if(m instanceof OFGMessage)
            sendOFGMessage((OFGMessage)m);
        
        m.write(out);
        
        if(PRINT_MESSAGES)
            System.out.println("sent: " + m.toString());
    }
    
    public void sendOFGMessage(OFGMessage m) throws IOException {
        if(m.xid == 0)
            m.xid = nextXID++;
        
        if(m.isStatefulRequest())
            outstandingStatefulRequests.put(m.xid, m);
        else if(m.type == OFGMessageType.POLL_START) {
            // store stateful poll requests in a different map since they do 
            // not expire when a reply comes in
            PollStart pollMsg = (PollStart)m;
            if(pollMsg.msg.isStatefulRequest()) {
                if(pollMsg.pollInterval != 0) {
                    pollMsg.msg.xid = nextXID++;
                    outstandingStatefulPollRequests.put(pollMsg.msg.xid, pollMsg.msg);
                }
                else
                    outstandingStatefulPollRequests.remove(pollMsg.msg.xid);
            }
        }
    }
    
    /** 
     * Returns the request sent with the specified transaction ID, if any.  The 
     * stateful request returned will no longer be remembered. 
     */
    public OFGMessage popAssociatedStatefulRequest(int xid) {
        // check the poll requests map first (more efficient if we assume must 
        // stateful replies come from poll requests)
        OFGMessage m = outstandingStatefulPollRequests.get(xid);
        return (m != null) ? m : outstandingStatefulRequests.remove(xid);
    }
    
    /** Remove cached stateful requests which have been cached for longer than getRequestLifetime() */
    protected void scrubExpiredStatefulRequests() {
        long now = System.currentTimeMillis();
        
        Iterator<OFGMessage> itr = outstandingStatefulRequests.values().iterator();
        while(itr.hasNext())
            if(now - itr.next().timeCreated() > REQUEST_LIFETIME_MSEC)
                itr.remove();
    }
    
    /** closes the connection to the server */
    
    /*private void disconnectToENVI() {
        System.out.println("Disconnecting from the server");
        tryToClose(conn);
        conn = null;
        stats.disconnected();
        connectiondone.clear();
        outstandingStatefulRequests.clear();
        outstandingStatefulPollRequests.clear();
        msgProcessor.connectionStateChange(false);
    }*/
    
    
    private void disconnectWithTopology() {
        System.out.println("Disconnecting from the server with topology");
        tryToClose(conn);
        conn = null;
        stats.disconnected();
        outstandingStatefulRequests.clear();
        outstandingStatefulPollRequests.clear();
    }
    
    private void disconnect() {
        System.out.println("reconnecting from the server");
        tryToClose(conn);
        conn = null;
        giveUpList.clear();
        stats.disconnected();
        connectionDone.clear();
        outstandingStatefulRequests.clear();
        outstandingStatefulPollRequests.clear();
        msgProcessor.connectionStateChange(false);
    }

    
    /** try to close the connection to the server */
    private static void tryToClose(SocketConnection sc) {
        if( sc != null ) {
            Socket s = sc.s;
            if(s != null && s.isConnected()) {
                // tell the backend we're disconnecting
                if(sc.out != null) {
                    try {
                        new OFGMessage(OFGMessageType.DISCONNECT, 0).write(sc.out);
                    }
                    catch(IOException e) { /* ignore */ }
                }

                try {
                    s.close();
                } catch(IOException e){}
            }
        }
    }
    
    /** returns the server address which this object connects to */
    public String getServerAddr() {
        return serverIP;
    }
    
    /** returns the TCP port this object connects to */
    public int getServerPort() {
        return serverPort;
    }
}
