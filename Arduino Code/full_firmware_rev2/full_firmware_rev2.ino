/*
 * Driver Board Control Firmware Rev 1.0
 * 
 */
#include <Wire.h>

#define masterAdress 8 // adress of master on I2C 
#define adress 9      //adress of driver on I2C

#define  pin_a 2      //for encoder pulse A
#define  pin_b 3      //for encoder pulse B
#define  en_check 7   //high if encoder is connected
#define  pin_dir 4    //for H-bridge: run motor forward
#define  pin_pwm 5    //for H-bridge: run motor backward
#define  ls_1 8       //limit switch 1 forward
#define  ls_2 9       //limit switch 2 reverse
#define  cs A0        //current sensor value
#define  tm A1        //motor housing temprature
#define  tg A2        //gear box housing temprature
#define  tb A3        //board temprature

#define tbCutoff 60   //board cutoff temprature


String mySt = "";
char myChar;
boolean stringComplete = false;  // whether the string is complete

boolean motor_start = false;
const double ticksPerRev = 392;//encoder ticks per revolution; 14:1 gearing, 7 ticks per rotation, 4 percision 14*7*4=392
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

bool tuneMode = false; //if true then will send speed for tuning

void setup() {
  // put your setup code here, to run once:

  //---------------------------setup pins and interrupts
  pinMode(pin_a,INPUT_PULLUP);
  pinMode(pin_b,INPUT_PULLUP);
  pinMode(ls_1,INPUT);
  pinMode(ls_2,INPUT);
  pinMode(pin_dir,OUTPUT);
  pinMode(pin_pwm,OUTPUT);
  attachInterrupt(digitalPinToInterrupt(pin_a), detect_a_rise, RISING);
  attachInterrupt(digitalPinToInterrupt(pin_a), detect_a_fall, FALLING);
  attachInterrupt(digitalPinToInterrupt(pin_b), detect_b_rise, RISING);
  attachInterrupt(digitalPinToInterrupt(pin_b), detect_b_fall, FALLING);

  pinMode(en_check,INPUT);

  //--------------------------start i2c 
  Wire.begin(adress);             // join i2c bus with address #adress
  Wire.onReceive(receiveEvent);   // register event

  // start serial port at 9600 bps:
  //Serial.begin(19200);

  //--------------------------timer setup
  noInterrupts();           // disable all interrupts
  TCCR1A = 0;
  TCCR1B = 0;
  timer1_counter = 59286;   // preload timer 65536-16MHz/256/2Hz (34286 for 0.5sec) (59286 for 0.1sec)

  TCNT1 = timer1_counter;   // preload timer
  TCCR1B |= (1 << CS12);    // 256 prescaler 
  TIMSK1 |= (1 << TOIE1);   // enable timer overflow interrupt
  interrupts();             // enable all interrupts

  //--------------------------ensure mtotr is stopped
  analogWrite(pin_pwm,0);    //stop motor
  digitalWrite(pin_dir,0);    //stop motor
}

//detect recived command
void loop() {
  // put your main code here, to run repeatedly:
  //if tunign enabled send speed
  if(tuneMode){
    Wire.beginTransmission(masterAdress);
    Wire.write("Data:pvspeed");
    Wire.print(pv_speed);
    Wire.write(",");
    Wire.endTransmission();
  }
  //check if encoder is attached
  if(!digitalRead(en_check)){// if not attached then stop
    Stop();
    Wire.beginTransmission(masterAdress);
    Wire.write("Error:EncoderDetach,");
    Wire.endTransmission();
  }
  if(GetBoardTemp()>=tbCutoff){
    Stop();
    Wire.beginTransmission(masterAdress);
    Wire.write("Error:BoardTemp,");
    Wire.endTransmission();
  }
    if(digitalRead(ls_1) && set_speed>0){
    set_speed=0;
    analogWrite(pin_pwm,0);
    Wire.beginTransmission(masterAdress);
    Wire.write("Error:Limit_1,");
    Wire.endTransmission();
  }
  if(digitalRead(ls_2)&& set_speed<0){
    set_speed=0;
    analogWrite(pin_pwm,0);
    Wire.beginTransmission(masterAdress);
    Wire.write("Error:Limit_2,");
    Wire.endTransmission();
  }

  if (stringComplete) {
    
    //receive command from master
    if (mySt.substring(0,8) == "vs_start"){
      digitalWrite(pin_dir, HIGH);      //run motor run forward
      forward = true;
      motor_start = true;
    }
    if (mySt.substring(0,7) == "vs_stop"){
      Stop();
    }
    if (mySt.substring(0,12) == "vs_set_speed"){
      set_speed = mySt.substring(12,mySt.length()).toFloat();  //get string after set_speed
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
    if (mySt.substring(0,7) == "vs_tune"){
      tuneMode = !tuneMode;
    }
    
    // clear the string when COM receiving is completed
    mySt = "";  //note: in code below, mySt will not become blank, mySt is blank until '\n' is received
    stringComplete = false;
  }

}

//interupts to detect encoder movment
void detect_a_rise() {
  encoder+=1; //increasing encoder at new pulse
  m_direction = digitalRead(pin_b); //read direction of motor
}
void detect_a_fall() {
  encoder+=1; //increasing encoder at new pulse
  m_direction = !digitalRead(pin_b); //read direction of motor
}
void detect_b_rise() {
  encoder+=1; //increasing encoder at new pulse
  m_direction = !digitalRead(pin_b); //read direction of motor
}
void detect_b_fall() {
  encoder+=1; //increasing encoder at new pulse
  m_direction = digitalRead(pin_b); //read direction of motor
}


//calculate PID and set output pins accordingly
ISR(TIMER1_OVF_vect)        // interrupt service routine - tick every 0.1sec
{
  digitalWrite(13,!digitalRead(13));
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

  //update new speed //set motor speed 
  constrain(pwm_pulse,-255,255);
  if(pwm_pulse < 0 && digitalRead(ls_2) == false){
    digitalWrite(pin_dir, LOW);
    forward = false;
    analogWrite(pin_pwm,abs(pwm_pulse));
  }
  else if(pwm_pulse > 0 && digitalRead(ls_1) == false){
    digitalWrite(pin_dir, HIGH);
    forward = true;
    analogWrite(pin_pwm,abs(pwm_pulse));
  }
  else{
    digitalWrite(pin_dir, LOW);
    forward = false;
    analogWrite(pin_pwm,0);
  }
     
}

//get board temprature
double GetBoardTemp(){
  int tbRaw = analogRead(tb);       // read the input pin
  double tbV = ((double)5/(double)1024)*tbRaw;  //convert to volts
  double val =(tbV*1000-500)/10;    //convert to temprature where VO = (+10 mV/°C × T °C) + 500 mV
  return val;
}

void Stop(){
      digitalWrite(pin_dir, LOW);
      forward = false;
      analogWrite(pin_pwm,0);      //stop motor
      set_speed = 0;
      motor_start = false;
}

// function that executes whenever data is received from master
// this function is registered as an event, see setup()
void receiveEvent(int howLong) {
  while (Wire.available()) {
    // get the new byte:
    char inChar = Wire.read();
    // add it to the inputString:
    mySt += inChar;
  }
  // if the incoming character is a newline, set a flag
  // so the main loop can do something about it:
  stringComplete = true;
}
