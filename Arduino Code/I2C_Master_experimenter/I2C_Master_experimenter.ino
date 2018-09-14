#include <Wire.h>

String mySt = "";
char myChar;
boolean stringComplete = false;  // whether the string is complete

void setup() {
  Wire.begin(); // join i2c bus (address optional for master)
  Serial.begin(9600);
  digitalWrite(13,LOW);
    while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  digitalWrite(13,HIGH);
  
}

byte x = 0;

void loop() {
  if (stringComplete) {
    Serial.println(mySt);
    char little_s_string[mySt.length()+1];
    mySt.toCharArray(little_s_string, mySt.length()+1);
    //Serial.println(little_s_string);
    Wire.beginTransmission(1);
    Wire.write(little_s_string);
    Wire.endTransmission();
    mySt = "";
    stringComplete = false;
  }
}


void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    if (inChar != '\n') {
      mySt += inChar;
    }
    if (inChar == '\n') {
      stringComplete = true;
    }
  }
}
