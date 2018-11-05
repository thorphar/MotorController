#include <USIWire.h>


#define LED_1 7
#define LED_2 8
#define DEBUG 1
#define ADRESS 15
#define MasterAdress 8

String mySt = "";
boolean stringComplete = false;  // whether the string is complete

int pwm_1 = 0;
int pwm_2 = 0;

void setup() {
  Wire.begin(ADRESS);                // join i2c bus with address #8
  Wire.onReceive(receiveEvent); // register event
  pinMode(LED_1, OUTPUT);
  pinMode(LED_2, OUTPUT);
  pinMode(DEBUG, OUTPUT);
  digitalWrite(DEBUG,HIGH);
  
  analogWrite(LED_1,pwm_1);
  analogWrite(LED_2,pwm_2);
}


void loop() {

  if (stringComplete) {
    if (mySt.substring(0,5) == "LED_1"){
      pwm_1 = mySt.substring(5,mySt.length()).toInt();
    }
    else if (mySt.substring(0,5) == "LED_2"){
      pwm_2 = mySt.substring(5,mySt.length()).toInt();
    }
    analogWrite(LED_1,pwm_1);
    analogWrite(LED_2,pwm_2);
    
    mySt = "";
    stringComplete = false;
  }
  
}

void receiveEvent(uint8_t num_bytes)
{
  digitalWrite(DEBUG,LOW);
  
  while (Wire.available()) {
    // get the new byte:
    char inChar = Wire.read();
    // add it to the inputString:
    mySt += inChar;
  }
  // if the incoming character is a newline, set a flag
  // so the main loop can do something about it:
  stringComplete = true;

  digitalWrite(DEBUG,HIGH);
}
