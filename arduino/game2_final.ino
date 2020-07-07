#include <Adafruit_NeoPixel.h> //네오픽셀을 사용하기 위한 라이브러리
#include<SoftwareSerial.h> //블루투스 연동을 위한 소프트웨어 시리얼 구현
SoftwareSerial BTSerial(6,5);// 시리얼 정의 시 아두이노에 연결된 핀 번호와 크로스 되게
//tx->6 rx->5
//인터럽트 핀 위치 /////////////////////////////////////////
//int.0 2|int.1 3|int.2 21|int.3 20|int.4 19| int.5 18|
//////////////////////////////////////////////////////

#define PIN_1 22           //data out이 연결된 핀 번호
#define NUM_LEDS_1 24      // 네오픽셀 LED 갯수(네오픽셀 링 LED 개수 : 24개)

#define PIN_2 23 
#define NUM_LEDS_2 24

#define PIN_3 24
#define NUM_LEDS_3 24

#define PIN_4 28 
#define NUM_LEDS_4 24

#define PIN_5 30 
#define NUM_LEDS_5 24

#define PIN_6 29 
#define NUM_LEDS_6 24

int time = 1000;
unsigned long pressTime;

//Adafruit_NeoPixel pixels = Adafruit_NeoPixel(네오픽셀 LED 개수, 아두이노 디지털 핀, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip1 = Adafruit_NeoPixel(NUM_LEDS_1, PIN_1, NEO_GRBW + NEO_KHZ800);  
//led1_r에 해당하는 네오픽셀
Adafruit_NeoPixel strip2 = Adafruit_NeoPixel(NUM_LEDS_2, PIN_2, NEO_GRBW + NEO_KHZ800); 
//led1_l에 해당하는 네오픽셀

Adafruit_NeoPixel strip3 = Adafruit_NeoPixel(NUM_LEDS_3, PIN_3, NEO_GRBW + NEO_KHZ800);  
//led2_r에 해당하는 네오픽셀
Adafruit_NeoPixel strip4 = Adafruit_NeoPixel(NUM_LEDS_4, PIN_4, NEO_GRBW + NEO_KHZ800); 
//led2_l에 해당하는 네오픽셀

Adafruit_NeoPixel strip5 = Adafruit_NeoPixel(NUM_LEDS_5, PIN_5, NEO_GRBW + NEO_KHZ800);  
//led2_r에 해당하는 네오픽셀
Adafruit_NeoPixel strip6 = Adafruit_NeoPixel(NUM_LEDS_6, PIN_6, NEO_GRBW + NEO_KHZ800); 
//led2_l에 해당하는 네오픽셀

//리미트 스위치 &  리미트 스위치 작동시 led 
//인터럽트 핀 위치 /////////////////////////////////////////
//int.0 2|int.1 3|int.2 21|int.3 20|int.4 19| int.5 18|
//////////////////////////////////////////////////////

int button_L2 = 2; //22    button_1
int button_L1 = 3; //23    button_2 
int button_ATK = 21; //24  button_3
int button_R1=20; //28     button_7
int button_R2 = 19; // 30  button_9
int button_CNT=18;  //29   button_8


int Flag_L2=0;
int count_L2=0;
int excount_L2=1;

int Flag_L1=0;
int count_L1=0;
int excount_L1=1;

int Flag_ATK=0;
int count_ATK=0;
int excount_ATK=1;

int Flag_R1=0;
int count_R1=0;
int excount_R1=1;

int Flag_R2=0;
int count_R2=0;
int excount_R2=1;

int Flag_CNT=0;
int count_CNT=0;
int excount_CNT=1;

void setup() {
   // put your setup code here, to run once:
  //블루투스의 시리얼 통신을 위한 정의 함
  Serial.begin(9600);//블루투스의 시리얼 통신의 속도는 9600
  //시리얼 모니터
  BTSerial.begin(9600);//블루투스 통신 시작


   //리미트 스위치 & led
  pinMode(button_L2, INPUT);
  digitalWrite(button_L2,INPUT_PULLUP);

  pinMode(button_L1, INPUT);
  digitalWrite(button_L1,INPUT_PULLUP);

  pinMode(button_ATK, INPUT);//공격
  digitalWrite(button_ATK,INPUT_PULLUP);

  pinMode(button_R1, INPUT);
  digitalWrite(button_R1,INPUT_PULLUP);

  pinMode(button_R2,INPUT);
  digitalWrite(button_R2,INPUT_PULLUP);

  pinMode(button_CNT,INPUT);
  digitalWrite(button_CNT,INPUT_PULLUP);

//버튼 눌림 여부를 판변하기 위해 버튼 코드는 인터럽트로 구현
  attachInterrupt(digitalPinToInterrupt(button_L2),BUTTON_L2,CHANGE);
  attachInterrupt(digitalPinToInterrupt(button_L1),BUTTON_L1,CHANGE);
  attachInterrupt(digitalPinToInterrupt(button_ATK),BUTTON_ATK,CHANGE);
  attachInterrupt(digitalPinToInterrupt(button_R1),BUTTON_R1,CHANGE);
  attachInterrupt(digitalPinToInterrupt(button_R2),BUTTON_R2,CHANGE);
  attachInterrupt(digitalPinToInterrupt(button_CNT),BUTTON_CNT,CHANGE);
  Serial.println("setup");

  strip1.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip1.begin();//네오픽셀을 사용하기 위한 함수
  //begin 함수의 경우 내부에 단순히 아두이노 디지털 핀을 out으로 선언하고 low가 출력되도록 하는 역할
  strip1.show(); // Initialize all pixels to 'off'

  strip2.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip2.begin();//네오픽셀을 사용하기 위한 함수
  strip2.show(); // Initialize all pixels to 'off'
  
  strip3.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip3.begin();//네오픽셀을 사용하기 위한 함수
  strip3.show(); // Initialize all pixels to 'off'

  strip4.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip4.begin();//네오픽셀을 사용하기 위한 함수
  strip4.show(); // Initialize all pixels to 'off'

  strip5.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip5.begin();//네오픽셀을 사용하기 위한 함수
  strip5.show(); // Initialize all pixels to 'off'

  strip6.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip6.begin();//네오픽셀을 사용하기 위한 함수
  strip6.show(); // Initialize all pixels to 'off'

}

void loop() {   
  //우주선 게임의 중앙 방향키와 공격 버튼이 동시에 눌리면 게임이 시작되게 구현(3초 동안)
while(digitalRead(button_CNT)==true&&digitalRead(button_ATK)==true)
  {
    delay(time);
    pressTime++;
    Serial.print(pressTime); Serial.println("초 경과");

    if(pressTime==3){break;}//3초가 경과하면 while문 break;
  }
  if(pressTime==3){
    Serial.print("pressTime : "); Serial.println(pressTime);
    delay(time);
    Serial.println("시작");
    BTSerial.write('O');//버튼을 누르고 3초가 지나면 안드로이드에 'O'을 보냄
    delay(1000);
  }    
      pressTime = 0;//게임이 새로 시작할때마다 새로운 초를 count해야하기 때문에 pressTime 초기화

}

void BUTTON_L2()
{
  delayMicroseconds(1000);
  if(digitalRead(button_L2)==HIGH)//버튼이 눌렸을 때 
  {
    if(Flag_L2 ==0){//플래그가 0이면 실행
      Flag_L2 =1;//플래그 1로 변경
      //Serial.println("PUSHED=====");
      count_L2++;
    GREEN1(); //초록색 네오픽셀 on     
    strip1.clear();//네오픽셀 색상 초기화하는 함수
  
    }
    else //반응 x
    {

    }
  }
  else //스위치가 눌러져 있지 않으면 실행됨
  {
    Flag_L2=0;     //플래그 0으로 변경
    //Serial.println("=====no!PUSHED");
    COUNT_L2();
      OFFLED1();//네오픽셀 off
  }
}


void BUTTON_L1()
{
  delayMicroseconds(1000);
  if(digitalRead(button_L1)==HIGH)//버튼이 눌렸을 때 실행
  {
    if(Flag_L1 ==0){//플래그가 0이면 실행
      Flag_L1 =1;//플래그 1로 변경
      //Serial.println("PUSHED=====");
      count_L1++;
      GREEN2();   //초록색 네오픽셀 on         
      strip2.clear();//네오픽셀 색상 초기화하는 함수
      
    }
    else//반응 x
    {

    }
  }
  else//스위치가 눌러져 있지 않으면 실행됨
  {
    Flag_L1=0;//플래그 0으로 변경
    //Serial.println("=====no!PUSHED");
    COUNT_L1();
    OFFLED2();//네오픽셀 off
  }
}

void BUTTON_ATK()
{
  delayMicroseconds(1000);
  if(digitalRead(button_ATK)==HIGH)//버튼이 눌렸을 때 
  {
    if(Flag_ATK ==0){//플래그가 0이면 실행
      Flag_ATK =1;//플래그 1로 변경
      //Serial.println("PUSHED=====");
      count_ATK++;
      RED3();//빨간색 네오픽셀 on
      strip3.clear();//네오픽셀 색상 초기화하는 함수
      
    }
    else //반응 x
    {

    }
  }
  else //스위치가 눌러져 있지 않으면 실행됨
  {
    Flag_ATK=0;//플래그 0으로 변경
    //Serial.println("=====no!PUSHED");
    COUNT_ATK();
    OFFLED3();//네오픽셀 off
    
  }
}

void BUTTON_R1()
{
  delayMicroseconds(1000);
  if(digitalRead(button_R1)==HIGH)//버튼이 눌렸을 때 
  {
    if(Flag_R1 ==0){//플래그가 0이면 실행
      Flag_R1 =1;//플래그 1로 변경
      //Serial.println("PUSHED=====");
      count_R1++;
      GREEN4();//초록색 네오픽셀 on
      strip4.clear(); //네오픽셀 색상 초기화하는 함수
    }
    else//반응 x
    {

    }
  }
  else//스위치가 눌러져 있지 않으면 실행됨
  {
    Flag_R1=0; //플래그 0으로 변경
    //Serial.println("=====no!PUSHED");
    COUNT_R1();
    OFFLED4(); //네오픽셀 off   
    
  }
}

void BUTTON_R2()
{
  delayMicroseconds(1000);
  if(digitalRead(button_R2)==HIGH)//버튼이 눌렸을 때 
  {
    if(Flag_R2 ==0){//플래그가 0이면 실행
      Flag_R2 =1;  //플래그 1로 변경
      //Serial.println("PUSHED=====");
      count_R2++;
    GREEN5();    //초록색 네오픽셀 on  
    strip5.clear(); //네오픽셀 색상 초기화하는 함수
      
    }
    else//반응 x
    {

    }
  }
  else
  {
    Flag_R2=0;
    //Serial.println("=====no!PUSHED");
    COUNT_R2();
      OFFLED5();    
    
  }
}

void BUTTON_CNT()
{
  delayMicroseconds(1000);
  if(digitalRead(button_CNT)==HIGH)//버튼이 눌렸을 때 
  {
    if(Flag_CNT ==0){//플래그가 0이면 실행
      Flag_CNT =1;//플래그 1로 변경
      //Serial.println("PUSHED=====");
      count_CNT++;
    GREEN6();     //초록색 네오픽셀 on 
    strip6.clear();//네오픽셀 색상 초기화하는 함수
      
    }
    else //반응 x
    {

    }
  }
  else//스위치가 눌러져 있지 않으면 실행됨
  {
    Flag_CNT=0;//플래그 0으로 변경
    //Serial.println("=====no!PUSHED");
    COUNT_CNT();
    OFFLED6(); //네오픽셀 off   
    
  }
}

void COUNT_L2()//버튼이 눌렸을 경우 블루트수를 통해 안드로이드에 '0' 전송
{
 if(count_L2==excount_L2){//이번에 누른 카운트가 지금까지 누적된 카운트와 같아지면, 그 횟수를 출력
      Serial.println('0');
      BTSerial.write('0');
      excount_L2++;
    }
    else{
    }
}
void COUNT_L1()//버튼이 눌렸을 경우 블루트수를 통해 안드로이드에 '1' 전송
{
  if(count_L1==excount_L1){//이번에 누른 카운트가 지금까지 누적된 카운트와 같아지면, 그 횟수를 출력
      Serial.println('1');
      BTSerial.write('1');
      excount_L1++;
    }
    else{
    }
}
void COUNT_ATK()//버튼이 눌렸을 경우 블루트수를 통해 안드로이드에 'A' 전송
{
   if(count_ATK==excount_ATK){//이번에 누른 카운트가 지금까지 누적된 카운트와 같아지면, 그 횟수를 출력
      Serial.println('A');
      BTSerial.write('A');
      excount_ATK++;
    }
    else{
    }
  

}
void COUNT_R1()//버튼이 눌렸을 경우 블루트수를 통해 안드로이드에 '3' 전송
{
  if(count_R1==excount_R1){//이번에 누른 카운트가 지금까지 누적된 카운트와 같아지면, 그 횟수를 출력
      Serial.println('3');
      BTSerial.write('3');
      excount_R1++;
    }
    else{
    }
  
}

void COUNT_R2() //버튼이 눌렸을 경우 블루트수를 통해 안드로이드에 '4' 전송
{
  if(count_R2==excount_R2){//이번에 누른 카운트가 지금까지 누적된 카운트와 같아지면, 그 횟수를 출력
      Serial.println('4');
      BTSerial.write('4');
      excount_R2++;
    }
    else{
    }
}

void COUNT_CNT()//버튼이 눌렸을 경우 블루트수를 통해 안드로이드에 '2' 전송
{
  if(count_CNT==excount_CNT){//이번에 누른 카운트가 지금까지 누적된 카운트와 같아지면, 그 횟수를 출력
      Serial.println('2');
      BTSerial.write('2');
      excount_CNT++;
    }
    else{
    }
}
/////////////////////////////////////////////////////////////////
//NeoPixel에 달린 LED를 각각 주어진 인자값 색으로 채워나가는 함수
void colorWipe_1(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_1; i++) {
    strip1.setPixelColor(i, c);   //LED 순서, LED 색상
    strip1.show();                 
    delay(wait);
  }
}

void colorWipe_2(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_2; i++) {
    strip2.setPixelColor(i, c);   //LED 순서, LED 색상
    strip2.show();                 
    delay(wait);
  }
}


void colorWipe_3(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_3; i++) {
    strip3.setPixelColor(i, c);   //LED 순서, LED 색상
    strip3.show();                 
    delay(wait);
  }
}

void colorWipe_4(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_4; i++) {
    strip4.setPixelColor(i, c);   //LED 순서, LED 색상
    strip4.show();                 
    delay(wait);
  }
}

void colorWipe_5(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_5; i++) {
    strip5.setPixelColor(i, c);   //LED 순서, LED 색상
    strip5.show();                 
    delay(wait);
  }
}

void colorWipe_6(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_6; i++) {
    strip6.setPixelColor(i, c);   //LED 순서, LED 색상
    strip6.show();                 
    delay(wait);
  }
}
//동시에 불이 켜지게 하기 위해 wait 값 : 0으로 설정
//led마다 하나씩 순서대로 들어오게 하기 위해서는 wait값만 수정하면 됨.
//////////////////////////////////////////////////////////////
void GREEN1()
{
  colorWipe_1(strip1.Color(0, 255, 0,0), 0); // Green
}

void GREEN2()
{
  colorWipe_2(strip2.Color(0, 255, 0,0), 0); // Green
}
/*void WHITE3()//atk->red로 변경
{
  colorWipe_3(strip3.Color(0, 0, 0,255), 0); // white
}*/
void GREEN4()
{
  colorWipe_4(strip4.Color(0, 255, 0,0), 0); // Green
}
void GREEN5()
{
  colorWipe_5(strip5.Color(0, 255, 0,0), 0); // Green
}
void GREEN6()
{
  colorWipe_6(strip6.Color(0, 255, 0,0), 0); // Green
}
///////////////////////////////////////////////////////////////////
void OFFLED1()
{
  colorWipe_1(strip1.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED2()
{
  colorWipe_2(strip2.Color(0, 0, 0), 0);// Black/off
}
void OFFLED3()
{
  colorWipe_3(strip3.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED4()
{
  colorWipe_4(strip4.Color(0, 0, 0), 0);// Black/off
}
void OFFLED5()
{
  colorWipe_5(strip5.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED6()
{
  colorWipe_6(strip6.Color(0, 0, 0), 0);// Black/off
}
void RED3()//atk
{
   colorWipe_3(strip3.Color(255, 0, 0), 0);    // red
}
