# 설사

## 프로젝트 소개
### 설명
* 나만의 설문조사, 밸런스 게임을 간단하게 만들어서 즐길 수 있는 웹사이트

### 목적
팀 프로젝트와 개인 프로젝트를 경험하며, 웹 개발자로 성장하기 위한 핵심 경로를 고민했습니다. <br>
결론은 간단하게도 개발에 대한 끊임없는 공부와 열정, 그리고 그것들을 증명하고 공유하는 것이 가장 바르고 빠른 길이라고 생각 되었습니다.<br>
이번 프로젝트는 개발 연습을 위한 단순한 작업 뿐 아니라, 앞으로의 프로그래밍 공부와 개발 경험, 유지보수까지 함께 다룰 프로젝트로 설정하였습니다.
### 개발인원
* 총 1명(백엔드)
### 개발기능
* 로그인, 회원가입(이메일 인증), 공지사항(등록,수정,리스트,삭제), 설문(등록,삭제,리스트,결과)
### Language
* `Java`
### DB / Server / Storage / Deployment
* `AWS RDS MySQL` / `AWS EC2` / `AWS S3` / `Docker`
### Framework / SSR
* `Spring Boot` `MyBatis` / `Thymeleaf`
### Tools
* `IntelliJ` `Visual Studio Code` `DBeaver`
### Front-end
* `HTML` `CSS` `JavaScript` `jQuery` `Bootstrap`

## 메뉴 구조도 및 IA
* 사용자
![_  TYS(user) (1)](https://github.com/leejh-96/survey/assets/115613811/834e0497-9f38-4ef9-9b63-8cb918af8ea5)

* 관리자
![TYS(admin) (1)](https://github.com/leejh-96/survey/assets/115613811/f28f1b9c-c4a7-4db5-bf82-003843202cc6)

## ERDiagram
<img width="979" alt="설사최종db" src="https://github.com/leejh-96/survey/assets/115613811/e1b554ca-cc94-43a2-8248-9e92b8e00c75">

## 핵심 구현내용
  * - 중복되는 시간 업데이트 비즈니스 로직을 하나의 클래스로 모듈화하는 리팩토링 작업을 진행했습니다.
### **시간 업데이트란?**
  * - 작성 시간부터 현재 시간의 상대적인 시간을 아래의 화면과 같이( 1초 전 , 1분 전 , 1일 전 ) 제공합니다.
<img width="830" alt="상대시간업데이트" src="https://github.com/leejh-96/feedsoup/assets/115613811/a6e12d89-d677-46a5-9012-a70b17fe8504">

### **중복 코드 파악**
  * 설문조사 서비스 비즈니스 로직
<img width="233" alt="설문조사 서비스" src="https://github.com/leejh-96/feedsoup/assets/115613811/6d7dbddd-5ec3-45d5-bb73-36bb0f0af385">

  * 공지사항 서비스 비즈니스 로직
<img width="224" alt="공지사항 서비스" src="https://github.com/leejh-96/feedsoup/assets/115613811/35d28538-a842-42f0-a459-e204766ddb1d">

### **해결과정**
  * 1) TimeUpdatable 인터페이스 도입
설문조사와 공지사항에서 사용되는 객체(DTO)들이 시간 데이터를 다루는 경우에 읽어오고 가져옴에 있어서 일관된 표준 방법이 필요했습니다.
이러한 문제를 해결하고자, 인터페이스를 사용하여 객체(DTO)들이 시간 데이터를 다루는 표준화된 방법을 도입하고자 했습니다.
<img width="170" alt="인터페이스" src="https://github.com/leejh-96/feedsoup/assets/115613811/5ece599b-18e7-45b4-92b0-c22f69558a2a">

  * 2) TimeAgoUpdaterService 클래스 모듈화 제네릭을 사용하여 TimeAgoUpdaterService 클래스의 메서드들이 TimeUpdatable 인터페이스를 구현한 클래스의 객체들에 대해서만 동작하도록 제한함으로써,
잘못된 객체가 사용되는 것을 방지하고자 했습니다.
<img width="239" alt="제네릭" src="https://github.com/leejh-96/feedsoup/assets/115613811/28c0c923-88de-4740-af9d-c9f3317c3d84">

  * 또한, 설문조사 서비스와 공지사항 서비스의 중복된 메서드를 getTimeAgo 메서드로 모듈화 했습니다.
<img width="239" alt="서비스" src="https://github.com/leejh-96/feedsoup/assets/115613811/17b2eaf3-8ebf-4113-a8ac-df02ebb0965f">

  * 3) TimeAgoUpdaterService 전체 코드
<img width="240" alt="전체 코드" src="https://github.com/leejh-96/feedsoup/assets/115613811/0eebbf24-de62-4748-af94-7999539a681f">

  * 4) 리팩토링 후기
    * 유지 보수 단순화 : 중복 코드를 한 곳에서 관리하여 코드 변경이 필요할 때 한 곳만 수정하면 되므로 유지 보수가 단순화되고, 효율적으로 코드를 관리 할 수 있도록 되었습니다.
    * 재사용성 향상 및 일관성 유지 : TimeAgoUpdaterService를 이용하면 다양한 클래스에서 시간 업데이트 기능을 재사용 할 수 있게 되었고, 다양한 클래스에서 동일한 방식으로 시간을 처리하여 일관성을 유지할 수 있게 되었습니다.
    * 타입 제한을 통한 안정성 보장: TimeAgoUpdaterService는 TimeUpdatable 인터페이스를 구현한 클래스에서만 동작함으로써 안정성을 확보하고 예기치 않은 오류를 사전에 방지할 수 있게 되었습니다.
