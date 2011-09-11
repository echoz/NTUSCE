//-------------------------------------------------------------------------
// porttest.cc
//   -- Test cases for the Send() and Receive()
//-------------------------------------------------------------------------
#include "copyright.h"
#include "system.h"
#include "port.h"

Port port;

void Sender(int n) {

    printf("Sender #%d sending (%d).\n", n, n);
    port.Send(n);
    printf("Sender #%d finished.\n", n);

}

void Receiver(int n) {

    int result = 0;
    printf("Receiver #%d receiving.\n", n);
    port.Receive(&result);
    printf("Receiver #%d received (%d)\n", n, result);

}

//-------------------------------------------------------------------------
/*
  * Single/Multiple sender/receiver on the same port
1  - Sender1,   Receive1
2  - Receive1,  Sender1
3  - Sender1,   Sender2,   Receiver1, Receiver2
4  - Sender1,   Receiver1, Sender2,   Receiver2
5  - Sender1,   Receiver1, Receiver2, Sender2
6  - Receiver1, Receiver2, Sender1,   Sender2
7  - Receiver1, Sender1,   Receiver2, Sender2
8  - Receiver1, Sender1,   Sender2,   Receiver2
*/
//-------------------------------------------------------------------------


void createTest(int sendRecvArr[], int msgArr[], int elm) {
	Thread *t;
	for (int i=0;i<elm;i++) {
		t = new Thread("createTestThread");
		if (sendRecvArr[i] == 0) {
			t->Fork(Sender, msgArr[i], 0);
		} else {
			t->Fork(Receiver, msgArr[i], 0);
		}
	}
}

// Sender1, Receive1
void PortTest1()
{
  //your code here to test case 1
	int sendRecvArr[2] = {0,1};
	int msgArr[2] = {1,1};
	createTest(sendRecvArr, msgArr, 2);
}

// Receive1,  Sender1
void PortTest2()
{
  //your code here to test case 2
	int srarr[2] = {1,0};
	int marr[2] = {1,1};
	createTest(srarr, marr, 2);
}

// Sender1, Sender2, Receiver1, Receiver2
void PortTest3()
{
  //your code here to test case 3
	int sarr[4] = {0,0,1,1};
	int marr[4] = {1,2,1,2};
	createTest(sarr, marr, 4);
}

// Sender1, Receiver1, Sender2, Receiver2
void PortTest4()
{
  //your code here to test case 4
	int sarr[4] = {0,1,0,1};
	int marr[4] = {1,1,2,2};
	createTest(sarr, marr, 4);
}

// Sender1, Receiver1, Receiver2, Sender2
void PortTest5()
{
  //your code here to test case 5
	int sarr[4] = {0,1,1,0};
	int marr[4] = {1,1,2,2};
	createTest(sarr, marr, 4);
}

// Receiver1, Receiver2, Sender1,   Sender2
void PortTest6()
{
  //your code here to test case 6
	int sarr[4] = {1,1,0,0};
	int marr[4] = {1,2,1,2};
	createTest(sarr, marr, 4);
}

// Receiver1, Sender1, Receiver2, Sender2
void PortTest7()
{
  //your code here to test case 7
	int sarr[4] = {1,0,1,0};
	int marr[4] = {1,1,2,2};
	createTest(sarr, marr, 4);

}

// Receiver1, Sender1, Sender2, Receiver2
void PortTest8()
{
  //your code here to test case 8
	int sarr[4] = {1,0,0,1};
	int marr[4] = {1,1,2,2};
	createTest(sarr, marr, 4);

}

void PortTest()
{

//     PortTest1();
//     PortTest2();
//     PortTest3();
//     PortTest4();
//     PortTest5();
//     PortTest6();
//     PortTest7();
    PortTest8();

}
