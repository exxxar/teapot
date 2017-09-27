
  int led = 8;

  int pos = 0;
void setup() {
  pinMode(led, OUTPUT);
Serial.begin(9600);
}
void loop() {
/*
  if (pos==1) {
    digitalWrite(led, HIGH);

    delay(1000);

    digitalWrite(led, LOW);

delay(1000);
Serial.write(pos);
  }
  if (pos==0) 
    Serial.write(pos); */
}
void serialEvent() {
  while (Serial.available()) {
    int bytev= (byte)Serial.read();
     pos = map(bytev,0,127,0,180);
              if (pos==1){
                 digitalWrite(led, HIGH);

    delay(1000);

    digitalWrite(led, LOW);
        delay(1000);
         digitalWrite(led, HIGH);
            Serial.write(31);
                Serial.write(1);
              }

             if (pos==0){
            
                digitalWrite(led,LOW);
                    Serial.write(32);
                        Serial.write(0);
              }

              if (pos==2) {
                Serial.write(33);
                    Serial.write(40);

              }

               if (pos==3) {
                Serial.write(34);
                    Serial.write(50);

              }

                if (pos==4) {
                Serial.write(35);
                    Serial.write(17);

              }
    }
    
  }
