/*===============================================================*
 *  File: SWP.java                                               *
 *                                                               *
 *  This class implements the sliding window protocol            *
 *  Used by VMach class					         *
 *  Uses the following classes: SWE, Packet, PFrame, PEvent,     *
 *                                                               *
 *  Author: Professor SUN Chengzheng                             *
 *          School of Computer Engineering                       *
 *          Nanyang Technological University                     *
 *          Singapore 639798                                     *
 *===============================================================*/

import java.util.*;

public class SWP {

/*========================================================================*
 the following are provided, do not change them!!
 *========================================================================*/
   //the following are protocol constants.
   public static final int MAX_SEQ = 7; 
   public static final int NR_BUFS = (MAX_SEQ + 1)/2;

   // the following are protocol variables
   private int oldest_frame = 0;
   private PEvent event = new PEvent();  
   private Packet out_buf[] = new Packet[NR_BUFS];

   //the following are used for simulation purpose only
   private SWE swe = null;
   private String sid = null;  

   //Constructor
   public SWP(SWE sw, String s){
      swe = sw;
      sid = s;
   }

   //the following methods are all protocol related
   private void init(){
      for (int i = 0; i < NR_BUFS; i++){
	   out_buf[i] = new Packet();
      }
   }

   private void wait_for_event(PEvent e){
      swe.wait_for_event(e); //may be blocked
      oldest_frame = e.seq;  //set timeout frame seq
   }

   private void enable_network_layer(int nr_of_bufs) {
 	  //network layer is permitted to send if credit is available
		swe.grant_credit(nr_of_bufs);
   }

   private void from_network_layer(Packet p) {
      swe.from_network_layer(p);
   }

   private void to_network_layer(Packet packet) {
		swe.to_network_layer(packet);
   }

   private void to_physical_layer(PFrame fm)  {
      System.out.println("SWP: Sending frame: seq = " + fm.seq + 
			    " ack = " + fm.ack + " kind = " + 
			    PFrame.KIND[fm.kind] + " info = " + fm.info.data );
      System.out.flush();
      swe.to_physical_layer(fm);
   }

   private void from_physical_layer(PFrame fm) {
      PFrame fm1 = swe.from_physical_layer(); 
		fm.kind = fm1.kind;
		fm.seq = fm1.seq; 
		fm.ack = fm1.ack;
		fm.info = fm1.info;
   }


/*===========================================================================*
 	implement your Protocol Variables and Methods below: 
 *==========================================================================*/
	private boolean no_nak = true; // no noak has been sent yet

	private boolean between(int a, int b, int c) {
		return ((a <= b) && (b < c)) || ((c < a) && (a <= b)) || ((b < c) && (c < a));	
	}
	
	private int inc(int toincrease) {
		// circular increment
		toincrease++;
		return (toincrease % (MAX_SEQ + 1));
	}

	private void send_frame(int kind, int frame_no, int frame_expected, Packet buffer[]) {
		PFrame s = new PFrame();
		s.kind = kind;
		
		if (kind == PFrame.DATA) {
			s.info = buffer[frame_no % NR_BUFS];
		}
		
		s.seq = frame_no;
		s.ack = (frame_expected + MAX_SEQ) % (MAX_SEQ + 1);
		
		
		// set options
		if (kind == PFrame.NAK) {
			no_nak = false;
		}
		to_physical_layer(s);
	
		// timer stuff
		if (kind == PFrame.DATA) {
			stop_ack_timer();
			start_timer(frame_no % NR_BUFS);
		}

	}
	public static final boolean debug = true;

	// sender window. main job is to track the 
	// buffer and relate it to sequence numberes
	
	private int sender_ack_expected = 0; // lower
	private int sender_next_to_send = 0; // upper
	
	// reciever window is to track the ACKs that are 
	// supposed to be come.
	private int recv_frame_expected = 0; // lower
	private int recv_too_far = NR_BUFS; // upper
					
	private Packet in_buf[] = new Packet[NR_BUFS];
	private boolean arrived[] = new boolean[NR_BUFS];
	private int credits_to_offer = 0;
	
	public int timeoutseq = 0;
	
	private PFrame r = new PFrame();
	
   public void protocol6() {
		// initialize
		int buffers = 0;
	   	init();
	
		enable_network_layer(NR_BUFS);

		for (int i=0;i<NR_BUFS;i++) {
			arrived[i] = false;	
		}

		while(true) {
			

	      wait_for_event(event);
	      
		   switch(event.type) {
		      case (PEvent.NETWORK_LAYER_READY):
					
				// when there is credits, take a packet out of the network layer
				// and transmit over the wire. increment sender upper edge window to 
				// know which is the next frame to send.
				
				from_network_layer(out_buf[sender_next_to_send % NR_BUFS]);
				send_frame(PFrame.DATA, sender_next_to_send, recv_frame_expected, out_buf);				
				sender_next_to_send = inc(sender_next_to_send);					
	         break;
	         
		      case (PEvent.FRAME_ARRIVAL):
				
				from_physical_layer(r);
				
				// fancy smancy debug
				System.out.println("SWP: Receiving " + PFrame.KIND[r.kind] + " frame: seq = " + r.seq + " ack = " + r.ack + " info = [" + r.info.data + "]");
				System.out.flush();

				if (r.kind == PFrame.NAK) {
					// deal with NAK by retransmitting frame requested.
					if (between(sender_ack_expected, (r.ack+1)%(MAX_SEQ+1) , sender_next_to_send)) {						
						send_frame(PFrame.DATA, (r.ack+1)%(MAX_SEQ+1), recv_frame_expected, out_buf);
					}
					
				} else if (r.kind == PFrame.DATA) {
					// deal with data frames

					/*
					System.out.println("NoNAK: " + no_nak + " isBetween(" + recv_frame_expected + "," + r.seq + "," + recv_too_far + "): " + between(recv_frame_expected, r.seq, recv_too_far) + " buffer: "  + arrived[r.seq % NR_BUFS]);
					System.out.flush();
					*/

					// when the frame that arrive is not of the lower edge of the window
					// this means something was sent out of order hence we have to NAK the
					// lower edge of the window as it was expected. in this case since the 
					// network is assumed to have no latency (localhost). there is no need to
					// worry about flooding the sender with NAKs. This also ensures data that is
					// more reliable as well.
					// otherwise, we can start an ACK timer just in case there is no piggyback
					if ((r.seq != recv_frame_expected)) {
						send_frame(PFrame.NAK, 0, recv_frame_expected, out_buf);
					} else {
						start_ack_timer();
					}
					
					// accept the frames even if they are out of order, toggling and storing the
					// buffer as appropriate. just have to make sure the packet that arrives is
					// within the same window as the sender.
					
					if (between(recv_frame_expected, r.seq, recv_too_far) && (!arrived[r.seq % NR_BUFS])) {
						/*
						System.out.println("--- Storing " + r.seq);
						System.out.flush();
						*/
						
						arrived[r.seq % NR_BUFS] = true;
						in_buf[r.seq % NR_BUFS] = r.info;
						
						// check through all buffers starting with the lowest edge of window
						// if it has a valid buffer, pass it up to network layer and advance
						// the window. keep checking until the buffer currently pointed at is
						// empty.
						while (arrived[recv_frame_expected % NR_BUFS]) {
							
							/*
							System.out.println("--- Sending to network layer " + recv_frame_expected);
							System.out.flush();
							*/
							
							to_network_layer(in_buf[recv_frame_expected % NR_BUFS]);
							arrived[recv_frame_expected % NR_BUFS] = false;
							no_nak = true;
							
							// once pushed data to network layer, we can increase receiver window
							// to know what's the next bunch of frames to expect from the sender
							recv_frame_expected = inc(recv_frame_expected);
							recv_too_far = inc(recv_too_far);
							
							/*
							System.out.println("RECV WINDOW FROM " + recv_frame_expected + " to " + recv_too_far + " nonak: " + no_nak);
							System.out.flush();
							*/
							
							start_ack_timer();
						}
						
					}
					
				}
				
				credits_to_offer = 0;
				
				// deal with ACKs
				
				while (between(sender_ack_expected, r.ack, sender_next_to_send)) {
					// stop retransmission timers and increment credits to reoffer.
					stop_timer(sender_ack_expected % NR_BUFS);
					credits_to_offer++;
					
					// increase sender lower window. in a sense the oldest frame
					sender_ack_expected = inc(sender_ack_expected);					
				}				
				
				if (credits_to_offer > 0) {
					enable_network_layer(credits_to_offer);
				}
				
			   break;
			   	   
	         case (PEvent.CKSUM_ERR):
	
				// don't care if NAKs have already been sent. send anyway because
				// we are doing this over a super low latency connection (localhost)
				// ensures more realiable data as well.
				if (true) {
					send_frame(PFrame.NAK, 0, recv_frame_expected, out_buf);
				}
					
	      	   break;  
	         
	         case (PEvent.TIMEOUT):
					// oldest_frame strangely doesn't seem to work. the last unacknownledge frame
					// would be lower edge of the sender window anyways, so this is also correct.
					send_frame(PFrame.DATA, sender_ack_expected, recv_frame_expected, out_buf);	
		       break;
		       
		     case (PEvent.ACK_TIMEOUT): 
					send_frame(PFrame.ACK, 0, recv_frame_expected, out_buf);
	           break; 
	         
	         default: 
				   System.out.println("SWP: undefined event type = " 
	   	                                    + event.type); 
				   System.out.flush();
		   }		
				
				
/*			// stuff for debugging
			
			buffers = 0;
			for (int i=0;i<NR_BUFS;i++) {
				if (arrived[i]) {
					buffers++;
				}
			}
			System.out.println("Buffers: " + buffers);
			System.out.println("SEND WINDOW FROM " + sender_ack_expected + " to " + sender_next_to_send);
			System.out.flush();
*/			
	
		}      
   }

 /* Note: when start_timer() and stop_timer() are called, 
    the "seq" parameter must be the sequence number, rather 
    than the index of the timer array, 
    of the frame associated with this timer, 
   */

	private Timer timers[] = new Timer[NR_BUFS];
	private Timer acktimer = null;
 
   private void start_timer(int seq) {
		if (timers[seq] != null) {
			// if a timer exists, something is probably wrong so kill it.
			// should never come to this but just in case.
			timers[seq].cancel();
		}
		timers[seq] = new Timer();
		timers[seq].schedule(new TimeoutTask(this.swe, seq), 50);
		
   }

   private void stop_timer(int seq) {
	if (timers[seq] != null) {
		timers[seq].cancel();
	}
   }

   private void start_ack_timer() {
		if (acktimer != null) {
			acktimer.cancel();
		}
		acktimer = new Timer();
		acktimer.schedule(new AckTask(this.swe), 15);
   }

   private void stop_ack_timer() {
	if (acktimer != null) {
		acktimer.cancel();
	}
   }

}//End of class

/* Note: In class SWE, the following two public methods are available:
   . generate_acktimeout_event() and
   . generate_timeout_event(seqnr).

   To call these two methods (for implementing timers),
   the "swe" object should be referred as follows:
     swe.generate_acktimeout_event(), or
     swe.generate_timeout_event(seqnr).
*/


