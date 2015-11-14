#include <Usb.h>
#include <AndroidAccessory.h>

#include "Geppa.h"

//===================================================
#define PIN_R   3
#define PIN_G   5
#define PIN_B   6

#define RING_BUF_SIZE 10

//===================================================
AndroidAccessory gAcc("Cattaka Lab",
"Gcm Adk Alart",
"Gcm Adk Alart",
"1.0",
"https://play.google.com/store/apps/details?id=net.cattaka.android.gcmadkalart",
"0000000012345678");

//===================================================
void handleRecvPacket(unsigned char packetType, unsigned char opCode, int dataLen, unsigned char* data);
Geppa gGeppa(handleRecvPacket);

//===================================================
unsigned long gLastTime;

void setup() {
  Serial.begin(9600); 
  Serial.println("GcmAdkAlart");

  pinMode(PIN_R, OUTPUT);
  pinMode(PIN_G, OUTPUT);
  pinMode(PIN_B, OUTPUT);

  gLastTime = millis();

  gAcc.powerOn();
}

void loop() {
  Serial.println("A");
  if (gAcc.isConnected()) {
  Serial.println("B");
    unsigned long t = (1000 - (millis() - gLastTime));
    if (t<= 1000) {
      delay(t);
    }
  } else {
  Serial.println("C");
    while(!gAcc.isConnected()) {
      unsigned long t = (1000 - (millis() - gLastTime));
      if (t>= 1000) {
        break;
      }
    }
  }
  {  // Receive packet
    int len;
    unsigned char msg[0x10];
    // Serial.print("Resv:");
    while((len = gAcc.read(msg, sizeof(msg), 1))>0) {
      for (int i=0;i<len;i++) {
        gGeppa.feedData(msg[i]);
        // Serial.print(msg[i], DEC); 
        // Serial.print(' ');
      }
    }
    // Serial.println();
  }
}

void handleRecvPacket(unsigned char packetType, unsigned char opCode, int dataLen, unsigned char* data) {
//  Serial.print("packetType:");
//  Serial.println(packetType, DEC);
//  Serial.print("opCode:");
//  Serial.println(opCode, DEC);
  
  if (packetType == 0x01) {
    if (opCode == 0x01) {
      analogWrite(PIN_R, data[0]);
      analogWrite(PIN_G, data[1]);
      analogWrite(PIN_B, data[2]);
    }
  }
}
