
#define  tb A3

void setup() {
  // put your setup code here, to run once:
  Serial.begin(19200);

}

double val;
void loop() {
  // put your main code here, to run repeatedly:
  int tbRaw = analogRead(tb);     // read the input pin
  double tbV = ((double)5/(double)1024)*tbRaw;
  val =(tbV*1000-500)/10; // VO = (+10 mV/°C × T °C) + 500 mV
  Serial.println(val);             // debug value

}
