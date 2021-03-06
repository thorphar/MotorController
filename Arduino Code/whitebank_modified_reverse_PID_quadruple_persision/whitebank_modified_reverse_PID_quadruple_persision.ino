/*
  Motor - PID speed control
  (1) Receive command from Visual Studio (via COM4): set_speed, kP, kI, kD
  (2) Control motor speed through PWM (PWM is base on PID calculation)
  (3) Send pv_speed to Visual Studio -> show in graph
  
 Created 31 Dec. 2016
 This example code is in the public domain.

 http://engineer2you.blogspot.com
 */
String mySt = "";
char myChar;
boolean stringComplete = false;  // whether the string is complete
boolean motor_start = false;
const byte pin_a = 2;   //for encoder pulse A
const byte pin_b = 3;   //for encoder pulse B
const byte pin_fwd = 4; //for H-bridge: run motor forward
const byte pin_bwd = 6; //for H-bridge: run motor backward
const byte pin_pwm = 5; //for H-bridge: motor speed
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


void setup() {
  pinMode(pin_a,INPUT_PULLUP);
  pinMode(pin_b,INPUT_PULLUP);
  pinMode(pin_fwd,OUTPUT);
  pinMode(pin_bwd,OUTPUT);
  pinMode(pin_pwm,OUTPUT);
  attachInterrupt(digitalPinToInterrupt(pin_a), detect_a_rise, RISING);
  attachInterrupt(digitalPinToInterrupt(pin_a), detect_a_fall, FALLING);
  attachInterrupt(digitalPinToInterrupt(pin_b), detect_b_rise, RISING);
  attachInterrupt(digitalPinToInterrupt(pin_b), detect_b_fall, FALLING);
  // start serial port at 9600 bps:
  Serial.begin(19200);
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
  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  analogWrite(pin_pwm,0);   //stop motor
  digitalWrite(pin_fwd,0);  //stop motor
  digitalWrite(pin_bwd,0);  //stop motor
}

void loop() {
  if (stringComplete) {
    // clear the string when COM receiving is completed
    mySt = "";  //note: in code below, mySt will not become blank, mySt is blank until '\n' is received
    stringComplete = false;
  }

  //receive command from Visual Studio
  if (mySt.substring(0,8) == "vs_start"){
    digitalWrite(pin_fwd,1);      //run motor run forward
    digitalWrite(pin_bwd,0);
    forward = true;
    motor_start = true;
  }
  if (mySt.substring(0,7) == "vs_stop"){
    digitalWrite(pin_fwd,0);
    digitalWrite(pin_bwd,0);      //stop motor
    set_speed = 0;
    motor_start = false;
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
}

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

ISR(TIMER1_OVF_vect)        // interrupt service routine - tick every 0.1sec
{
  TCNT1 = timer1_counter;   // set timer
  pv_speed = 60.0*(encoder/ticksPerRev)/0.1;  //calculate motor speed, unit is rpm
  if(!forward)pv_speed = pv_speed*-1;
  encoder=0;
  //print out speed
  if (Serial.available() <= 0) {
    Serial.print("pvspeed");
    Serial.print(pv_speed);         //Print speed (rpm) value to Visual Studio
    Serial.print(", setspeed");
    Serial.print(set_speed);
    Serial.print(", pwmspeed");
    Serial.println(pwm_pulse);
    }


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

