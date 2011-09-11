//----------------------------------------------------------------------
// port.cc -- Send and Receive operations for port.
//
// Including two member functions:
//    Send (int message);
//    Receive (int *message);
//
//----------------------------------------------------------------------

#include "system.h"
#include "port.h"

//----------------------------------------------------------------------
// Port::Port
//    Initialization
//----------------------------------------------------------------------

Port::Port() {

    nSender = 0;        // no sender
    nReceiver = 0;      // no receiver
    cSender = new Condition("Port-cSender");
    cReceiver = new Condition("Port-cReceiver");
    cBuffer = new Condition("Port-cBuffer");
    plock = new Lock("Port-plock");             // lock for using port

}

//----------------------------------------------------------------------
// Port::~Port
//    Clean-up
//----------------------------------------------------------------------

Port::~Port() {

  delete cSender;
  delete cReceiver;
  delete cBuffer;
  delete plock;

}

//----------------------------------------------------------------------
// Port::Send
//    Send a message to a port.
//
//----------------------------------------------------------------------

void Port::Send(int msg) {
  //your code here to send a message to a port

//	printf("-- acquire send lock\n");
	plock->Acquire();

//	printf("-- increase  sender count to %d\n", nSender+1);
	// sending something so add it to the senders list
	nSender++;

//	printf("-- waiting for reciever to not be 0\n");
	//wait for receiver to wake sender up and make sure there is receivers
	while (nReceiver == 0) {
		cSender->Wait(plock);
	}

//	printf("-- waiting for buffer to be clear\n");
	// checks if buffer is in a "clear" state = 0
	while (buffer != 0) {
		cBuffer->Wait(plock);
	}

//	printf("-- waking up a reciever and setting buffer = %d\n", msg);
	// if everything is clear wait a receiver up
	buffer = msg;
	cReceiver->Signal(plock);
	cSender->Wait(plock);

//	printf("-- releasing lock and decreasing count to %d\n", nSender-1);
	// reset buffer to clear state and remove current sender
	nSender--;
	cBuffer->Signal(plock);

	plock->Release();
}

//----------------------------------------------------------------------
// Port::Receive
//    Receive a message from a port.
//
//----------------------------------------------------------------------

void Port::Receive(int *msg) {
  //your code here to receive a message from a port

//	printf("== acquring lock and increase receiver to %d\n", nReceiver+1);
	// waiting to receiver
	plock->Acquire();
	nReceiver++;

//	printf("== wait for sender\n");
	// wait for senders
	while (nSender == 0) {
		cReceiver->Wait(plock);
	}

	while (buffer == 0) {
//		printf("== empty buffer, waking up sender\n");
		cSender->Signal(plock);
		cReceiver->Wait(plock);
	}

//	printf("== copying buffer\n");
	*msg = buffer;

//	printf("== losing lock, resetting buffer and decrease receiver count to %d\n", nReceiver-1);
	nReceiver--;
	buffer = 0;
	cSender->Signal(plock);
	plock->Release();
}
