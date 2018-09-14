#include <Wire.h>

#define MasterAdress 8

String S2ImySt = "";
String I2SmySt = "";
char myChar;
boolean S2IstringComplete = false;  // whether the string is complete
boolean I2SstringComplete = false;  // whether the string is complete

void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  
  Wire.begin(MasterAdress); // join i2c bus (address optional for master)
  Wire.onReceive(receiveEvent); // register event
  
  Serial.begin(19200);
  digitalWrite(LED_BUILTIN,LOW);
    while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  digitalWrite(LED_BUILTIN,HIGH);
  
}


void loop() {
  if (S2IstringComplete) {
    //digitalWrite(LED_BUILTIN,!digitalRead(LED_BUILTIN));
    int adress = S2ImySt.substring(0,2).toInt();
    S2ImySt = S2ImySt.substring(2,S2ImySt.length());
    Serial.print("To:");
    Serial.print(adress);
    Serial.print(" Sending:");
    Serial.println(S2ImySt);
    char little_s_string[S2ImySt.length()+1];
    S2ImySt.toCharArray(little_s_string, S2ImySt.length()+1);
    //Serial.println(little_s_string);
    Wire.beginTransmission(adress);
    Wire.write(little_s_string);
    Wire.endTransmission();
    S2ImySt = "";
    S2IstringComplete = false;
  }
  if (I2SstringComplete) {
    Serial.print("Recived:");
    Serial.println(I2SmySt.substring(0,I2SmySt.indexOf(",")+1));
    I2SmySt = "";
    I2SstringComplete = false;
  }
}


void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    if (inChar != '\n') {
      S2ImySt += inChar;
    }
    if (inChar == '\n') {
      S2IstringComplete = true;
    }
  }
}

void receiveEvent(int howLong) {
  while (Wire.available()) {
    // get the new byte:
    char inChar = Wire.read();
    // add it to the inputString:
    I2SmySt += inChar;
  }
  // if the incoming character is a newline, set a flag
  // so the main loop can do something about it:
  I2SstringComplete = true;
}

