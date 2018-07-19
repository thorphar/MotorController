#include <Event.h>
#include <Timer.h>

#define pwm 5                      //Power of motor.
#define dir 4 

const int analogIn = A0;
int mVperAmp = 185; // use 100 for 20A Module and 66 for 30A Module

int RawValue = 0;
int ACSoffset = 2500; 
double Voltage = 0;
double Amps = 0;
unsigned int swing = 0;

Timer t;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  //t.every(100, incPWM);

  pinMode(pwm, OUTPUT);
  pinMode(dir, OUTPUT);

  analogWrite(pwm, 100);                     //Make sure the motor in reset mode.
  digitalWrite(dir, LOW);
}

void incPWM(){
  swing++;
  analogWrite(pwm, swing);
}

void loop() {
  t.update();
  // put your main code here, to run repeatedly:
  RawValue = analogRead(analogIn);
  //Serial.println(RawValue);
  
  Voltage = (RawValue / 1024.0) * 5000; // Gets you mV
  Serial.println(Voltage);
  
  Amps = ((Voltage - ACSoffset) / mVperAmp);
  //Serial.println(Amps);
}
