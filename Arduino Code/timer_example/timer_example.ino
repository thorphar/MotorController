//useful links

//https://www.instructables.com/id/Arduino-Timer-Interrupts/
//https://arduino-info.wikispaces.com/Timers-Arduino
//storage variables
boolean toggle1 = 0;

void setup(){
  
  pinMode(13, OUTPUT);


cli();//stop interrupts

//set timer1 interrupt at 10Hz
  TCCR1A = 0;// set entire TCCR1A register to 0
  TCCR1B = 0;// same for TCCR1B
  TCNT1  = 0;//initialize counter value to 0
  
  // set compare match register for 1hz increments
  OCR1A = 19999;// = (16*10^6) / (1*1024) - 1 (must be <65536)
  // turn on CTC mode
  TCCR1B |= (1 << WGM12);
  // Set CS12 and CS10 bits for 8 prescaler
  TCCR1B |= (1 << CS21);  
  // enable timer compare interrupt
  TIMSK1 |= (1 << OCIE1A);

sei();//allow interrupts

}//end setup



ISR(TIMER1_COMPA_vect){//timer1 interrupt 1Hz toggles pin 13 (LED)
//generates pulse wave of frequency 1Hz/2 = 0.5kHz (takes two cycles for full wave- toggle high then toggle low)
  if (toggle1){
    digitalWrite(13,HIGH);
    toggle1 = 0;
  }
  else{
    digitalWrite(13,LOW);
    toggle1 = 1;
  }
}


void loop(){
  //do other things here
}
