# IoTCapstone
## IT 융합형 어린이 놀이시설
## 1. 프로젝트 목적
### - 프로젝트 주제
어린이들이 체육을 놀이처럼 인식하고 다양한 체력 요소를 증진할 수 있도록 게임에서 볼 수 있는 재미, 경쟁 등의 요소를 다른 분야에 적용하는 기법인 ‘Gamification’을 적용하여 어린이 놀이시설을 개발한다.  

###-  대상
초등학교 및 초등학생

## 2. 프로젝트 개발 내용 및 결과
### - 주요기능
![image01](https://user-images.githubusercontent.com/50151242/86029412-16359280-ba6e-11ea-9453-97855fe065b7.png)

### - 사용 부품
![image02](https://user-images.githubusercontent.com/50151242/86029411-16359280-ba6e-11ea-89ca-7e4957fcaeaa.png)

### - 체육 학교/반 대항전 APP 및 놀이시설

   ㅇ 제자리 달리기 놀이시설
   
![image03](https://user-images.githubusercontent.com/50151242/86029409-159cfc00-ba6e-11ea-8974-68d62c55c75e.png)
- 2인이 동시에 진행하는 게임으로 제한시간 30초 내목표 걸음 수 이상 도달해야 하는 게임이다. 목표 걸음 수는 학년별로 다르게 주어지며 옆 사람과 경쟁하는 느낌을 주도록 프로그레스바로 현재 목표대비 걸음 수를 보여준다. 목표 걸음 수에 도달 시 걸음당 30점의 점수가 데이터베이스에 insert 된다.

   ㅇ 우주선 놀이시설

![image04](https://user-images.githubusercontent.com/50151242/86029408-146bcf00-ba6e-11ea-9cc6-fc19ec1490a7.png)
- 사용자가 화면상의 우주선이 되어 외부에서 날아오는 공격을 미사일로 쏴 없애는 게임이다. 우주선 위치이동 버튼 5개와 1개의 미사일 발사 버튼으로 이루어져 있으며, 3번의 기회 안에 외부공격을 폭파한 수 *100점이 데이터베이스에 insert 된다.

   ㅇ 불빛 터치 놀이시설

![image05](https://user-images.githubusercontent.com/50151242/86029414-16ce2900-ba6e-11ea-8640-a059c32099c5.png)

- 9개의 발판 스위치가 정사각형으로 나열되어있으며, 9개의 발판 스위치중 4개의 발판에 노란색 불이 들어온다. 3초 내 노란불을 전부 누르면 APP 화면의 카운트가 진행되고, 카운트 동안 불빛을 계속 누르면 성공한다. 성공하면 5번의 카운트가 점점 빨라지고 실패 시 게임오버와 동시에 성공한 횟수*100점이 데이터베이스에 insert 된다.

   ㅇ 체육 학교/반 대항전 APP

![image06](https://user-images.githubusercontent.com/50151242/86029775-8fcd8080-ba6e-11ea-8636-90ea76437721.png)

- 각 놀이시설 별로 연동할 수 있는 게임이 있으며, 블루투스 연결 후 사용할 수 있다. 어린이들의 흥미를 높일 수 있도록 시설 별로 다른 게임을 제작했다. 
- 놀이시설을 게임형식으로 제작하고 이 놀이시설들을 사용하여 획득한 점수 등으로 기존에 스마트폰게임에서 볼 수 있었던 학교대항전을 오프라인에서 가능토록 한다. 학교 대항전의 경우 학교별로 놀이시설마다 점수로 순위 매기고, 반대항전은 같은학교 같은학년의 다른 반끼리의 순위를 매긴다.

   ㅇ 난이도조절

![image08](https://user-images.githubusercontent.com/50151242/86030425-6e20c900-ba6f-11ea-9163-866acc8b92aa.png)

- 초등학생은 1학년부터 6학년까지 나이가 다양하므로 1~2학년은 하, 3~4학년은 중 5~6학년은 상로 나눠 학년별로 난이도를 조절할 수 있도록 한다.
-  메인화면에서도 학년별로 난이도를 확인할 수 있다. 
- 제자리 달리기 놀이시설의 경우 목표 걸음 수를 다르게 설정하여 비슷한 걸음 수에도 프로그레스바가 확연히 차이난다. 
- 우주선 놀이시설은 외부공격이 날아오는 속도 설정을 다르게 하여 난이도를 조절한다. 

   ㅇ 데이터베이스
   
![image07](https://user-images.githubusercontent.com/50151242/86030125-08ccd800-ba6f-11ea-945a-d64a0f0d46de.png)

   ㅇ 프로토타입

![image09](https://user-images.githubusercontent.com/50151242/86030652-b5a75500-ba6f-11ea-90a6-b5c62cc1a292.png)

### - 회로도
   ㅇ 제자리 달리기 놀이시설

![image10](https://user-images.githubusercontent.com/50151242/86030650-b5a75500-ba6f-11ea-9f56-d0ebb90f96f1.png)

   ㅇ 우주선 놀이시설

![image11](https://user-images.githubusercontent.com/50151242/86030646-b50ebe80-ba6f-11ea-8427-1b13717a638d.png)

   ㅇ 불빛 터치 놀이시설

![image12](https://user-images.githubusercontent.com/50151242/86030641-b3dd9180-ba6f-11ea-89ca-2e7c69aed54e.png)


## 3. 프로젝트 전체 

https://youtu.be/FTsx6s_2rCw
