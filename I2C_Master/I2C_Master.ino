#include <Wire.h>

String mySt = "";
char myChar;
boolean stringComplete = false;  // whether the string is complete

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
  
//  digitalWrite(LED_BUILTIN, HIGH);
//  
//  Wire.beginTransmission(1); // transmit to device #adress
//  Wire.write("vs_set_speed");
//  double set_speed = 100; 
//  Wire.print(set_speed); 
//  Wire.endTransmission();    // stop transmitting
//  delay(10);
//  digitalWrite(LED_BUILTIN, LOW);
//  
//  delay(500);
  
  // put your main code here, to run repeatedly:
 if (stringComplete) {
  int adress = mySt.substring(0,1).toInt();
  Serial.print("Slave ");
  Serial.print(adress);
  mySt.remove(0,1);
  //receive command from Visual Studio
  if (mySt.substring(0,8) == "vs_start"){
    Serial.print(" vs_start ");
    
    Wire.beginTransmission(adress); // transmit to device #adress
    Wire.write("vs_start"); 
    Wire.endTransmission();    // stop transmitting
    }
    else if (mySt.substring(0,7) == "vs_stop"){
      Serial.print(" vs_stop ");
  
      Wire.beginTransmission(adress); // transmit to device #adress
      Wire.write("vs_stop"); 
      Wire.endTransmission();    // stop transmitting
      
    }
    else if (mySt.substring(0,6) == "vs_set"){
      Serial.print(" vs_set ");
      double set_speed = mySt.substring(6,mySt.length()).toFloat();
      Serial.print(set_speed);
  
      Wire.beginTransmission(adress); // transmit to device #adress
      Wire.write("vs_set_speed"); 
      Wire.print(set_speed);
      Wire.endTransmission();    // stop transmitting
      
    }
    else if (mySt.substring(0,5) == "vs_kp"){
      Serial.print(" vs_kp ");
      double kp = mySt.substring(5,mySt.length()).toFloat();
      Serial.print(kp);
  
      Wire.beginTransmission(adress); // transmit to device #adress
      Wire.write("vs_kp"); 
      Wire.print(kp);
      Wire.endTransmission();    // stop transmitting
      
    }
    else if (mySt.substring(0,5) == "vs_ki"){
      Serial.print(" vs_ki ");
      double ki = mySt.substring(5,mySt.length()).toFloat();
      Serial.print(ki);
  
      Wire.beginTransmission(adress); // transmit to device #adress
      Wire.write("vs_ki"); 
      Wire.print(ki);
      Wire.endTransmission();    // stop transmitting
      
    }
    else if (mySt.substring(0,5) == "vs_kd"){
      Serial.print(" vs_kd ");
      double kd = mySt.substring(5,mySt.length()).toFloat();
      Serial.print(kd);
  
      Wire.beginTransmission(adress); // transmit to device #adress
      Wire.write("vs_kd"); 
      Wire.print(kd);
      Wire.endTransmission();    // stop transmitting
      
    }  
    else if (mySt.substring(0,7) == "vs_data"){
      Serial.print(" vs_data ");
      int data = mySt.substring(7,mySt.length()).toInt();
      Serial.print(data);
  
      Wire.beginTransmission(adress); // transmit to device #adress
      Wire.write("vs_data");
      Wire.print(data); 
      Wire.endTransmission();    // stop transmitting
  
      Wire.requestFrom(adress, 4);    // request 4 bytes from slave device #adress
      String temp = "";
      while (Wire.available()) { // slave may send less than requested
        char inChar = Wire.read(); // receive a byte as character
        temp += inChar;
      }
      Serial.print(temp);
      double datarec = temp.toFloat();
      Serial.print(", recived ");         // print the character
      Serial.print(datarec);
    }
    else Serial.print(" invalid command");
    Serial.println();
    // clear the string when COM receiving is completed
    mySt = "";  //note: in code below, mySt will not become blank, mySt is blank until '\n' is received
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
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    }
  }
}
