#include <Wire.h>

int delayN = 100;

void setup() {
  Wire.begin(1);                // join i2c bus with address #8
  Wire.onReceive(receiveEvent); // register event
}

void loop() {
  digitalWrite(13,HIGH);
  delay(delayN);
  digitalWrite(13,LOW);
  delay(delayN);
}

// function that executes whenever data is received from master
// this function is registered as an event, see setup()
String mySt = "";
char myChar;
void receiveEvent(int howMany) {
  while (Wire.available()) {
    // get the new byte:
    char inChar = Wire.read();
    // add it to the inputString:
    mySt += inChar; 
  }
  delayN = mySt.toInt();
  mySt = "";
}
