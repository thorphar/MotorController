#define encoderA 3;
#define encoderB 2;
#define pwm 5;                      //Power of motor.
#define dir 4;                       //Direction of the motor.

#define total 980                        //x4 pulses per rotation.
#define motorSpeed 180                   //Change speed of the motor.



unsigned int key = -1;
int NUM_KEYS = 5;
long pulses = 0;                              //Output pulses.
int deg = 0;

double speedRPM = 0;
unsigned long previousMillis = 0;        // will store last time LED was updated

int m_direction = 0;
int encoder = 0;

double pwm_pulse = 0;

//const int analogIn = A0;
//int mVperAmp = 100; // use 100 for 20A Module and 66 for 30A Module
//unsigned int RawValue = 0; // can hold upto 64 10-bit A/D readings
//int ACSoffset = 2500;
//double Voltage = 0;
//double Amps = 0;
//int swing = 0;

void setup() {
  // put your setup code here, to run once:
  pinMode(encoderA, INPUT);
  pinMode(encoderB, INPUT);
  pinMode(pwm, OUTPUT);
  pinMode(dir, OUTPUT);
  attachInterrupt(digitalPinToInterrupt(encoderA), A_CHANGE, RISING);
  analogWrite(pwm, 0);                     //Make sure the motor in reset mode.
  digitalWrite(dir, HIGH);
  Serial.begin(9600);
  
  previousMillis = millis();
  
  analogWrite(pwm,pwm_pulse); // minimum 11
}


void loop(){
  Serial.println(pulses);

  analogWrite(pwm,pwm_pulse); // minimum 11
  
}

void change(){
  unsigned long currentMillis = micros();
  double diff = (double)(currentMillis-previousMillis)/1000;
  speedRPM = ((double)1/(double)98)/(diff/((double)1000*(double)60));
  previousMillis = currentMillis;

        
  e_speed = set_speed - pv_speed;
  pwm_pulse = e_speed*kp + e_speed_sum*ki + (e_speed - e_speed_pre)*kd;
  e_speed_pre = e_speed;  //save last (previous) error
  e_speed_sum += e_speed; //sum of error
  if (e_speed_sum >4000) e_speed_sum = 4000;
  if (e_speed_sum <-4000) e_speed_sum = -4000;
  
}



void A_CHANGE(){
  change();

  encoder+=1; //increasing encoder at new pulse

  m_direction = digitalRead(encoderB); //read direction of motor
  if(m_direction) pulses-=1;
  else pulses+=1;
}
