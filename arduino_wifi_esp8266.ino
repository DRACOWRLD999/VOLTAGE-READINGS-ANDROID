#include <Wire.h>
#include <FirebaseESP8266.h>
#include <ESP8266WiFi.h>


#define WIFI_SSID "shaheen"
#define WIFI_PASSWORD "ALI123456y"

/* 2. Define the API Key */
#define API_KEY "o4i10yfcBe7zL5tUxdzDSBQZihgeKiG8QjXfAJCL"

/* 3. Define the RTDB URL */
#define DATABASE_URL "https://voltageread-22aa9-default-rtdb.firebaseio.com/"  //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

// pin to read from
int analogDataRead = A0;

FirebaseData firebaseData;

// FirebaseJson json;

float num;
float out;

void setup() {

  // baud rate
  Serial.begin(115200);

  // connect to wifi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  // set the pin as an input
  pinMode(analogDataRead, INPUT);

  // connect to firebase
  Firebase.begin(DATABASE_URL, API_KEY);

  Firebase.reconnectWiFi(true);
}

void loop() {

  // re-assign num var to hold the data
  num = analogRead(analogDataRead);

  // convert input to voltage
  out = (0.53 / 1023.0) * num;

  // print output
  Serial.println(num);
  Serial.println(out);

  delay(500);
  // push num to node "/voltage"
  Firebase.pushString(firebaseData, "/voltage", out);

  // current psuh to firebase node "/current"
  Firebase.pushString(firebaseData, "/current", Current(out));

  delay(500);
}


float Current(float voltage) {
  return voltage / 23000;
}
