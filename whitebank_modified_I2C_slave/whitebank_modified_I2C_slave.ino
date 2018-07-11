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

#define adress 1
 
String mySt = "";
char myChar;
boolean stringComplete = false;  // whether the string is complete
boolean motor_start = false;
const byte pin_a = 2;   //for encoder pulse A
const byte pin_b = 3;   //for encoder pulse B
const byte pin_fwd = 4; //for H-bridge: run motor forward
const byte pin_bwd = 6; //for H-bridge: run motor backward
const byte pin_pwm = 5; //for H-bridge: motor speed
const double ticksPerRev = 98;//encoder ticks per revolution
int encoder = 0;
int m_direction = 0;
int sv_speed = 100;     //this value is 0~255
double pv_speed = 0;
double set_speed = 0;
double e_speed = 0; //error of speed = set_speed - pv_speed
double e_speed_pre = 0;  //last error of speed
double e_speed_sum = 0;  //sum error of speed
double pwm_pulse = 0;     //this value is 0~255
double kp = 0;
double ki = 0;
double kd = 0;
int timer1_counter; //for timer
int i=0;
bool forward = true;

int request = 0;


void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  
  pinMode(pin_a,INPUT_PULLUP);
  pinMode(pin_b,INPUT_PULLUP);
  pinMode(pin_fwd,OUTPUT);
  pinMode(pin_bwd,OUTPUT);
  pinMode(pin_pwm,OUTPUT);
  attachInterrupt(digitalPinToInterrupt(pin_a), detect_a, RISING);

  //start i2c 
  Wire.begin(adress);                // join i2c bus with address #adress
  Wire.onReceive(receiveEvent); // register event
  //Wire.onRequest(requestEvent); // register event

  //Serial.begin(9600);
  
  //--------------------------timer setup
  noInterrupts();           // disable all interrupts
  TCCR1A = 0;
  TCCR1B = 0;
  timer1_counter = 59286;   // preload timer 65536-16MHz/256/2Hz (34286 for 0.5sec) (59286 for 0.1sec)

  
  TCNT1 = timer1_counter;   // preload timer
  TCCR1B |= (1 << CS12);    // 256 prescaler 
  TIMSK1 |= (1 << TOIE1);   // enable timer overflow interrupt
  interrupts();             // enable all interrupts
  //--------------------------timer setup
  

  analogWrite(pin_pwm,0);   //stop motor
  digitalWrite(pin_fwd,0);  //stop motor
  digitalWrite(pin_bwd,0);  //stop motor
}

void loop() {
  if (stringComplete) {  //receive command from Visual Studio
    if (mySt.substring(0,8) == "vs_start"){
      digitalWrite(pin_fwd,1);      //run motor run forward
      digitalWrite(pin_bwd,0);
      forward = true;
      motor_start = true;
      //Serial.println("starting");
    }
    if (mySt.substring(0,7) == "vs_stop"){
      digitalWrite(pin_fwd,0);
      digitalWrite(pin_bwd,0);      //stop motor
      set_speed = 0;
      motor_start = false;
    }
    if (mySt.substring(0,12) == "vs_set_speed"){
      set_speed = mySt.substring(12,mySt.length()).toFloat();  //get string after set_speed
      //Serial.print("setting speed ");
      //Serial.println(set_speed);
    }
    if (mySt.substring(0,5) == "vs_kp"){
      kp = mySt.substring(5,mySt.length()).toFloat(); //get string after vs_kp
    }
    if (mySt.substring(0,5) == "vs_ki"){
      ki = mySt.substring(5,mySt.length()).toFloat(); //get string after vs_ki
    }
    if (mySt.substring(0,5) == "vs_kd"){
      kd = mySt.substring(5,mySt.length()).toFloat(); //get string after vs_kd
    }
    if (mySt.substring(0,7) == "vs_data"){
      request = mySt.substring(7,mySt.length()).toInt();
      
      if(request == 0) Wire.print(pv_speed);
      if(request == 1) Wire.print(e_speed);
      if(request == 2) Wire.print(pwm_pulse);
      if(request == 3) Wire.print(set_speed);
      if(request == 4) Wire.print(kp);
      if(request == 5) Wire.print(ki);
      if(request == 6) Wire.print(kd);
    }   
    // clear the string when COM receiving is completed
    mySt = "";  //note: in code below, mySt will not become blank, mySt is blank until '\n' is received
    stringComplete = false;
  }

}

void detect_a() {
  encoder+=1; //increasing encoder at new pulse
  m_direction = digitalRead(pin_b); //read direction of motor
}
ISR(TIMER1_OVF_vect)        // interrupt service routine - tick every 0.1sec
{
  TCNT1 = timer1_counter;   // set timer
  pv_speed = 60.0*(encoder/ticksPerRev)/0.1;  //calculate motor speed, unit is rpm
  if(!forward)pv_speed = pv_speed*-1;
  encoder=0;

  //PID program
  if (motor_start){    
    e_speed = set_speed - pv_speed;
    pwm_pulse = e_speed*kp + e_speed_sum*ki + (e_speed - e_speed_pre)*kd;
    e_speed_pre = e_speed;  //save last (previous) error
    e_speed_sum += e_speed; //sum of error
    if (e_speed_sum >4000) e_speed_sum = 4000;
    if (e_speed_sum <-4000) e_speed_sum = -4000;
    if(set_speed == 0 && pv_speed == 0) pwm_pulse = 0;
  }
  else{
    e_speed = 0;
    e_speed_pre = 0;
    e_speed_sum = 0;
    pwm_pulse = set_speed;
  }

  //update new speed
  constrain(pwm_pulse,-255,255);
  if(pwm_pulse < 0) {digitalWrite(pin_fwd, LOW); digitalWrite(pin_bwd, HIGH); forward = false;}
  else {digitalWrite(pin_fwd, HIGH); digitalWrite(pin_bwd, LOW); forward = true;}
  analogWrite(pin_pwm,abs(pwm_pulse));  //set motor speed  
  
}


// function that executes whenever data is received from master
// this function is registered as an event, see setup()
void receiveEvent(int howLong) {
  while (Wire.available()) { // loop through all but the last
    char inChar = Wire.read(); // receive byte as a character
    mySt += inChar;         // print the character
  }
  stringComplete = true;
}

void requestEvent() {
//  if(request == 0) Wire.print(pv_speed);
//  if(request == 1) Wire.print(e_speed);
//  if(request == 2) Wire.print(pwm_pulse);
//  if(request == 3) Wire.print(set_speed);
//  if(request == 4) Wire.print(kp);
//  if(request == 5) Wire.print(ki);
//  if(request == 6) Wire.print(kd);
}

