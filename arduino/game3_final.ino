#include <SimpleTimer.h> //random함수를 사용하기 위한 라이브러리
#include <Adafruit_NeoPixel.h>

#define PIN_1 22                  //data out이 연결된 핀 번호
#define NUM_LEDS_1 24      // 네오픽셀 LED 갯수(네오픽셀 링의 경우 LED 개수 : 7개/ 네오픽셀 스트립의 경우 60개 사용

#define PIN_2 23 
#define NUM_LEDS_2 24

#define PIN_3 24
#define NUM_LEDS_3 24

#define PIN_4 25 
#define NUM_LEDS_4 24

#define PIN_5 26
#define NUM_LEDS_5 24

#define PIN_6 27
#define NUM_LEDS_6 24

#define PIN_7 28
#define NUM_LEDS_7 24

#define PIN_8 29
#define NUM_LEDS_8 24

#define PIN_9 30 
#define NUM_LEDS_9 24

//Adafruit_NeoPixel pixels = Adafruit_NeoPixel(네오픽셀 LED 개수, 아두이노 디지털 핀, NEO_GRB + NEO_KHZ800);

Adafruit_NeoPixel strip1 = Adafruit_NeoPixel(NUM_LEDS_1, PIN_1, NEO_GRBW + NEO_KHZ800);  
Adafruit_NeoPixel strip2 = Adafruit_NeoPixel(NUM_LEDS_2, PIN_2, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip3 = Adafruit_NeoPixel(NUM_LEDS_3, PIN_3, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip4 = Adafruit_NeoPixel(NUM_LEDS_4, PIN_4, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip5 = Adafruit_NeoPixel(NUM_LEDS_5, PIN_5, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip6 = Adafruit_NeoPixel(NUM_LEDS_6, PIN_6, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip7 = Adafruit_NeoPixel(NUM_LEDS_7, PIN_7, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip8 = Adafruit_NeoPixel(NUM_LEDS_8, PIN_8, NEO_GRBW + NEO_KHZ800);
Adafruit_NeoPixel strip9 = Adafruit_NeoPixel(NUM_LEDS_9, PIN_9, NEO_GRBW + NEO_KHZ800);

int STATUS =0;
int RUN =1;
int STOP = 2;

int num = 0;
int time = 1000;

unsigned long pressTime;
unsigned long pressTime1;


int button_1 = 2; //A 22
int button_2 = 3; //L2 23
int button_3 = 21; //L1 24
int button_4=15;//CNT 25
int button_5=14;//R1 26
int button_6=4;//R2 27
int button_7=20;//R2 28
int button_8=16;//R2 29
int button_9=17;//R2 30

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial1.begin(9600);// 아두이노 메가를 사용할 때의 블루투스 선언
//tx1_18->rx| rx1_19->tx 교차 연결
  
  pinMode(button_1,INPUT_PULLUP);
  pinMode(button_2,INPUT_PULLUP);
  pinMode(button_3,INPUT_PULLUP);//INPUT_PULLUP
  pinMode(button_4,INPUT_PULLUP);
  pinMode(button_5,INPUT_PULLUP);
  pinMode(button_6,INPUT_PULLUP);//INPUT_PULLUP
  pinMode(button_7,INPUT_PULLUP);
  pinMode(button_8,INPUT_PULLUP);
  pinMode(button_9,INPUT_PULLUP);//INPUT_PULLUP

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
  
  strip7.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip7.begin();//네오픽셀을 사용하기 위한 함수
  strip7.show(); // Initialize all pixels to 'off'

  strip8.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip8.begin();//네오픽셀을 사용하기 위한 함수
  strip8.show(); // Initialize all pixels to 'off'

  strip9.setBrightness(30);//네오픽셀 밝기 조절(0~255)
  strip9.begin();//네오픽셀을 사용하기 위한 함수
  strip9.show(); // Initialize all pixels to 'off'
  
  Serial.print("setup!\n");
}

void loop() {
    //불빛터치 게임의 4번과 6번 발판이 동시에 눌리면 게임이 시작되게 구현(3초 동안)
while(digitalRead(button_4)==true&&digitalRead(button_6)==true)
  {
    delay(time);
    pressTime1++;
    Serial.print(pressTime1); Serial.println("초 경과");

    if(pressTime1==3){break;}//3초가 경과하면 while문 break;
  }
  if(pressTime1==3){
    Serial.print("pressTime1 : "); Serial.println(pressTime1);
    delay(time);
    Serial.println("시작");
    Serial1.write('O');//버튼을 누르고 3초가 지나면 안드로이드에 'O'을 보냄

    delay(1000);
  }    
      pressTime1 = 0;//게임이 새로 시작할때마다 새로운 초를 count해야하기 때문에 pressTime1 초기화
      
      
   if (Serial1.available()) { //안드로이드로 부터 값을 받을경우
    char bt;
    bt=Serial1.read();
    Serial.write(bt);

   if(bt =='1')//받은 값이 1
    {
      time-=100; //1을 받을 경우 게임이 진행 속도 빠르게 
    }

    else if(bt =='K') //받은 값이 k
    {
       STATUS =RUN; //RandomFunction을 조절하는 STATUS 상태를 변환하여 k를 받으면 게임 시작되게
    }
    else if(bt =='X')//받은 값이 x
    {
       STATUS = STOP; //RandomFunction을 조절하는 STATUS 상태를 변환하여 k를 받으면 게임 종료
    }
    else{

    }
  } 
 if(STATUS==RUN){ //STATUS가 RUN일 경우에만 게임 실행 ( 안드로이드로 부터 받은 값 K)
   RandomFunction();  
    }
}

// random seed를 활용하여 1~9 숫자 중 4개의 숫자를 중복 없이 선택한 후 배열에 저장하는 방식으로 구현
void RandomFunction() 
{
        Serial1.write('L'); 

        int a[9]; //int형 배열 선언

        Serial.print("중복없이 뽑힌 4개의 숫자는 : ");
        
        for(int i=0;i<=3;i++)    //숫자 4개를 뽑기위한 for문
        {
            a[i] = random(9)+1; //1~9숫자중 랜덤으로 하나를 뽑아 a[0]~a[5]에 저장
            for(int j=0;j<i;j++) //중복제거를 위한 for문 
            {
                if(a[i]==a[j])  
                {
                    i--;
                }
           }
            
        }
        
        int t=1;
        boolean test =true;
        for(int i=4;i<=8;i++)    //랜덤으로 뽑히지 않은 숫자 5개를 뽑기위한 for문
        {
          while(test)
          {
            if((a[0]!=t)&&(a[1]!=t)&&(a[2]!=t)&&(a[3]!=t))
            {
              a[i] = t;
              //Serial.println(a[i]);
              test=false;
            }
            t++;
          }
          test =true;
        }

         
        for(int k=0;k<=3;k++) //채워진 배열을 출력하기 위한 for문
        {
            Serial.print(a[k]);Serial.print(" ");
        }
        Serial.println("입니다.");


        for(int s=4;s<=8;s++)
        {
          Serial.print(a[s]); Serial.print(" ");
        }
        
        delay(500);
        check_btnpress(a,9); 
        
}

//버튼을 누를때 판단 함수
void check_btnpress(int arr[], int count){
  int btn[count];
  
  Serial.print("push");
  for(int i=0; i<count; i++){
    int bttn=0;
    if(i<4){
      Serial.print(arr[i]);
    }
//switch case문과 배열을 사용하여 랜덤으로 선택된 버튼이 눌렀는지 판별
    switch(arr[i]){
      case 1:
        bttn =button_1;
        break;
        case 2:
        bttn =button_2;
        break;
        case 3:
        bttn =button_3;
        break;
        case 4:
        bttn =button_4;
        break;
        case 5:
        bttn =button_5;
        break;
        case 6:
        bttn =button_6;
        break;
        case 7:
        bttn =button_7;
        break;
        case 8:
        bttn =button_8;
        break;
        case 9:
        bttn =button_9;
        break;                
      }
      btn[i]=bttn; 
  }

  Serial.println("!");
  
for(int i=0; i<count; i++){ //랜덤에 해당하는 발판의 네오픽셀 switch문을 사용하여 노락색으로 변환

    if(i<4){
    switch(arr[i]){
      case 1:
        YELLOW1(); 
        break;
        case 2:
        YELLOW2();
        break;
        case 3:
        YELLOW3();
        break;
        case 4:
        YELLOW4();
        break;
        case 5:
        YELLOW5();
        break;
        case 6:
        YELLOW6();
        break;
        case 7:
        YELLOW7();
        break;
        case 8:
        YELLOW8();
        break;
        case 9:
        YELLOW9();
        break;                
      }
    }
  }

  delay(5000);
  
    //btn[0~3]에 해당하는 값은 랜덤에 해당하는 값
    //랜덤에 해당하는 값(4개)의 버튼을 모두 누르면 while문 실행
  while(digitalRead(btn[0])==true&&digitalRead(btn[1])==true&&digitalRead(btn[2])==true&&digitalRead(btn[3])==true&&digitalRead(btn[4])==false&&digitalRead(btn[5])==false&&digitalRead(btn[6])==false&&digitalRead(btn[7])==false&&digitalRead(btn[8])==false)
  {
    delay(time);
  for(int i=0; i<count; i++){
    if(i<4){ //버튼이 모두 눌리면 흰색으로 변경 (흰색 0.5초, off 0.5초->깜빡이는 느낌으로 구현)
    switch(arr[i]){
      case 1:
        WHITE1(); 
        break;
        case 2:
        WHITE2();
        break;
        case 3:
        WHITE3();
        break;
        case 4:
        WHITE4();
        break;
        case 5:
        WHITE5();
        break;
        case 6:
        WHITE6();
        break;
        case 7:
        WHITE7();
        break;
        case 8:
        WHITE8();
        break;
        case 9:
        WHITE9();
        break;                
      }
    }
  }
    pressTime++;
    String strTime = String(pressTime);
    char charTime = strTime.charAt(0);//string값을 char로 변환
    Serial1.write(charTime);
    Serial.print(pressTime); Serial.println("초 경과");
    
    delay(500);
    OFFLED();
    delay(500);
     
    if(pressTime==5){break;} //5초 경과하면 while문 break;
  }
  
  if(pressTime==5){
    Serial.print("pressTime : "); Serial.println(pressTime);
    delay(time);
    Serial.println("통과"); 
    pressTime = 0;//게임이 새로 시작할때마다 새로운 초를 count해야하기 때문에 pressTime 초기화
    GREEN(); //전체 네오픽셀 초록색으로 변경
    Serial1.write('T'); //블루투스로 T를 보냄
    delay(2000);
    OFFLED(); //전체 네오픽셀 off
   }else{
    Serial.println("실패");
    pressTime = 0;//게임이 새로 시작할때마다 새로운 초를 count해야하기 때문에 pressTime 초기화
    RED(); //전체 네어픽셀 빨간색으로 변경
    Serial1.write('F'); //블루트스로 F를보냄   
    delay(2000);
    OFFLED(); // 전체 네오픽셀 off
   }
   delay(3000);
  
}

///////////////////////////////////////////////////////
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

void colorWipe_7(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_7; i++) {
    strip7.setPixelColor(i, c);   //LED 순서, LED 색상
    strip7.show();                 
    delay(wait);
  }
}

void colorWipe_8(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_8; i++) {
    strip8.setPixelColor(i, c);   //LED 순서, LED 색상
    strip8.show();                 
    delay(wait);
  }
}

void colorWipe_9(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<NUM_LEDS_9; i++) {
    strip9.setPixelColor(i, c);   //LED 순서, LED 색상
    strip9.show();                 
    delay(wait);
  }
}
//동시에 불이 켜지게 하기 위해 wait 값 : 0으로 설정
//led마다 하나씩 순서대로 들어오게 하기 위해서는 wait값만 수정하면 됨.
//////////////////////////////////////////////////////////////////
void WHITE1()
{
  colorWipe_1(strip1.Color(0, 0, 0,255), 0); // white
}
void WHITE2()
{
  colorWipe_2(strip2.Color(0, 0, 0,255), 0); // white
}
void WHITE3()
{
  colorWipe_3(strip3.Color(0, 0, 0,255), 0); // white
}
void WHITE4()
{
  colorWipe_4(strip4.Color(0, 0, 0,255), 0); // white
}
void WHITE5()
{
  colorWipe_5(strip5.Color(0, 0, 0,255), 0); // white
}
void WHITE6()
{
  colorWipe_6(strip6.Color(0, 0, 0,255), 0); // white
}
void WHITE7()
{
  colorWipe_7(strip7.Color(0, 0, 0,255), 0); // white
}
void WHITE8()
{
  colorWipe_8(strip8.Color(0, 0, 0,255), 0); // white
}
void WHITE9()
{
  colorWipe_9(strip9.Color(0, 0, 0,255), 0); // white
}
//////////////////////////////////////////////////////
void W_BLINK1(){
    WHITE1();
    delay(500);
    OFFLED1();
    delay(500); 
}
void W_BLINK2(){
    WHITE2();
    delay(500);
    OFFLED2();
    delay(500); 
}
void W_BLINK3(){
    WHITE3();
    delay(500);
    OFFLED3();
    delay(500); 
}
void W_BLINK4(){
    WHITE4();
    delay(500);
    OFFLED4();
    delay(500); 
}
void W_BLINK5(){
    WHITE5();
    delay(500);
    OFFLED5();
    delay(500); 
}
void W_BLINK6(){
    WHITE6();
    delay(500);
    OFFLED6();
    delay(500); 
}
void W_BLINK7(){
    WHITE7();
    delay(500);
    OFFLED7();
    delay(500); 
}
void W_BLINK8(){
    WHITE8();
    delay(500);
    OFFLED8();
    delay(500); 
}
void W_BLINK9(){
    WHITE9();
    delay(500);
    OFFLED9();
    delay(500); 
}
////////////////////////////////////////////////
void OFFLED()
{
  colorWipe_1(strip1.Color(0, 0, 0), 0);    // Black/off
  colorWipe_2(strip2.Color(0, 0, 0), 0);
  colorWipe_3(strip3.Color(0, 0, 0), 0);
  colorWipe_4(strip4.Color(0, 0, 0), 0);
  colorWipe_5(strip5.Color(0, 0, 0), 0);
  colorWipe_6(strip6.Color(0, 0, 0), 0);
  colorWipe_7(strip7.Color(0, 0, 0), 0);
  colorWipe_8(strip8.Color(0, 0, 0), 0);
  colorWipe_9(strip9.Color(0, 0, 0), 0);
}
//////////////////////////////////////////////////////////
void OFFLED1()
{
  colorWipe_1(strip1.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED2()
{
  colorWipe_2(strip2.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED3()
{
  colorWipe_3(strip3.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED4()
{
  colorWipe_4(strip4.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED5()
{
  colorWipe_5(strip5.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED6()
{
  colorWipe_6(strip6.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED7()
{
  colorWipe_7(strip7.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED8()
{
  colorWipe_8(strip8.Color(0, 0, 0), 0);    // Black/off
}
void OFFLED9()
{
  colorWipe_9(strip9.Color(0, 0, 0), 0);    // Black/off
}
///////////////////////////////////////////////////////////
void RED(){
  colorWipe_1(strip1.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_2(strip2.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_3(strip3.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_4(strip4.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_5(strip5.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_6(strip6.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_7(strip7.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_8(strip9.Color(255, 0, 0), 0); // Red (Color, wait) 
  colorWipe_9(strip9.Color(255, 0, 0), 0); // Red (Color, wait) 

}
void GREEN()
{
  colorWipe_1(strip1.Color(0, 255, 0), 0); // Green
  colorWipe_2(strip2.Color(0, 255, 0), 0); // Green
  colorWipe_3(strip3.Color(0, 255, 0), 0); // Green
  colorWipe_4(strip4.Color(0, 255, 0), 0); // Green
  colorWipe_5(strip5.Color(0, 255, 0), 0); // Green
  colorWipe_6(strip6.Color(0, 255, 0), 0); // Green
  colorWipe_7(strip7.Color(0, 255, 0), 0); // Green
  colorWipe_8(strip8.Color(0, 255, 0), 0); // Green
  colorWipe_9(strip9.Color(0, 255, 0), 0); // Green
}
////////////////////////////////////////////////////////////////
void YELLOW1()
{
  colorWipe_1(strip1.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW2()
{
  colorWipe_2(strip2.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW3()
{
  colorWipe_3(strip3.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW4()
{
  colorWipe_4(strip4.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW5()
{
  colorWipe_5(strip5.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW6()
{
  colorWipe_6(strip6.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW7()
{
  colorWipe_7(strip7.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW8()
{
  colorWipe_8(strip8.Color(255, 255, 0), 0);  // Yellow
}
void YELLOW9()
{
  colorWipe_9(strip9.Color(255, 255, 0), 0);  // Yellow
}
