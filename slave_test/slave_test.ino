#include <Wire.h>

/*
  Motor - PID speed control
  (1) Receive command from Visual Studio (via COM4): set_speed, kP, kI, kD
  (2) Control motor speed through PWM (PWM is base on PID calculation)
  (3) Send pv_speed to Visual Studio -> show in graph
  
 Created 31 Dec. 2016
 This example code is in the public domain.

 http://engineer2you.blogspot.com
 */
void setup() {


  //start i2c 
  Wire.begin(8);                // join i2c bus with address #adress
  Wire.onReceive(receiveEvent); // register event
  
  // start serial port at 9600 bps:
  Serial.begin(9600);

}

void loop() {

}

void receiveEvent(int howMany) {
  Serial.println("recived");
  while (Wire.available()) { // loop through all but the last
    char inChar = Wire.read(); // receive byte as a character
    Serial.println(inChar);       // print the character
  }
}


