#include <Wire.h>


void setup() {
  // put your setup code here, to run once:

  pinMode(LED_BUILTIN, OUTPUT);

  Wire.begin(); // join i2c bus (address optional for master)
  
  // start serial port at 9600 bps:
  Serial.begin(9600);

  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
}

void loop() {
  
  digitalWrite(LED_BUILTIN, HIGH);
  
  Wire.beginTransmission(8); // transmit to device #adress
  Wire.write("hello"); 
  Wire.endTransmission();    // stop transmitting
  delay(10);
  digitalWrite(LED_BUILTIN, LOW);
  
  delay(500);

 
}


