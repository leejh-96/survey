# 설사

## 프로젝트 소개
### 설명
* 나만의 설문조사, 밸런스 게임을 간단하게 만들어서 즐길 수 있는 웹사이트
* [설사 바로가기](http://13.124.47.242:8084/)
### 목적
팀 프로젝트와 개인 프로젝트를 경험하며, 웹 개발자로 성장하기 위한 핵심 경로를 고민했습니다. <br>
결론은 간단하게도 개발에 대한 끊임없는 공부와 열정, 그리고 그것들을 증명하고 공유하는 것이 가장 바르고 빠른 길이라고 생각 되었습니다.<br>
이번 프로젝트는 개발 연습을 위한 단순한 작업 뿐 아니라, 앞으로의 프로그래밍 공부와 개발 경험, 유지보수까지 함께 다룰 프로젝트로 설정하였습니다.
### 개발인원
* 총 1명(백엔드)
### 개발기능
* 로그인, 회원가입(이메일 인증), 공지사항(등록,수정,리스트,삭제), 설문(등록,삭제,리스트,결과)
### 개발 TOOLS
* `IntelliJ` `Visual Studio Code` `GitHub` `Sourcetree` `DBeaver`
### 인프라
* `AWS EC2` `AWS S3` `AWS RDS`
### 사용기술
* Frontend - 
`HTML` `CSS` `JavaScript` `jQuery` `Bootstrap 4`
* Backend - 
`SpringBoot` `Thymeleaf` `JAVA` `MyBatis` 
### 데이터베이스
* `Mysql`
### 빌드 툴
* `Gradle`
### 기타
* `Postman`

## 메뉴 구조도 및 IA
* 사용자
![_  TYS(user) (1)](https://github.com/leejh-96/survey/assets/115613811/834e0497-9f38-4ef9-9b63-8cb918af8ea5)

* 관리자
![TYS(admin) (1)](https://github.com/leejh-96/survey/assets/115613811/f28f1b9c-c4a7-4db5-bf82-003843202cc6)

## 화면기획서
* ※기획과 결과물에 차이가 있을 수 있습니다.
* 로그인
![TYS(take your survey)  로그인-로그아웃](https://github.com/leejh-96/survey/assets/115613811/951d5fe7-4e90-471b-abe3-387df5f56d37)
* 회원가입
![TYS(take your survey)  회원가입](https://github.com/leejh-96/survey/assets/115613811/c72a2f1e-f8ba-44be-a59f-9373a196b334)
* 설사 메인
![TYS(take your survey)  설사 메인](https://github.com/leejh-96/survey/assets/115613811/3bceb671-3755-4637-87e0-a882b91567c0)
* 설사 만들기
![TYS(take your survey)  설문만들기](https://github.com/leejh-96/survey/assets/115613811/4429e0bc-3ef4-4731-9149-27051ace0c94)
* 설사 참여하기
![TYS(take your survey)  설문참여하기](https://github.com/leejh-96/survey/assets/115613811/4e4f7b1a-a91d-4bb8-97c2-fe973908a61d)
* 설사 상세보기 및 결과보기
![TYS(take your survey)  설사 상세보기](https://github.com/leejh-96/survey/assets/115613811/70aa6188-87cd-4a62-9084-86f894f887dd)
![TYS(take your survey)  설사 결과보기](https://github.com/leejh-96/survey/assets/115613811/dc1ce2ba-31e9-4021-992b-c30afa6263d3)
* 공지사항 리스트
![TYS(take your survey)  공지사항](https://github.com/leejh-96/survey/assets/115613811/085f3611-3980-4772-be03-02ffa9fc6148)
* 공지사항 등록
![TYS(take your survey)  공지사항 글쓰기](https://github.com/leejh-96/survey/assets/115613811/1d3a489f-e77b-4531-9c5b-e10c5195419f)
* 공지사항 상세보기
![TYS(take your survey)  공지사항 상세보기](https://github.com/leejh-96/survey/assets/115613811/c993b4c1-68b1-46b1-8fd4-dcd16c121315)

## ERDiagram
<img width="979" alt="설사최종db" src="https://github.com/leejh-96/survey/assets/115613811/e1b554ca-cc94-43a2-8248-9e92b8e00c75">

## 프로젝트 내용
### 중복 코드 최소화

공지사항 서비스와 설문조사 서비스의 상대적 시간을 업데이트 시키는 코드에서 중복된 코드를 발견했습니다.

- 공지사항 서비스
    
    ```java
    private List<NoticeListDTO> updateTime(List<NoticeListDTO> list) {
		for (NoticeListDTO dto : list) {
		    dto.setTime(timeSettings(dto));
		}
		return list;
    }
    
    private String timeSettings(NoticeListDTO dto) {
        LocalDateTime boardWriteTime = dto.getBoardWriteTime();
        LocalDateTime now = LocalDateTime.now();
        Duration between = Duration.between(boardWriteTime, now);
        long seconds = between.getSeconds();
        long minutes = between.toMinutes();
        long hours = between.toHours();
        long days = between.toDays();
        String timeAgo = "";
        if (days > 0) {
            timeAgo = days + "일 전";
        } else if (hours > 0) {
            timeAgo = hours + "시간 전";
        } else if (minutes > 0) {
            timeAgo = minutes + "분 전";
        } else {
            timeAgo = seconds + "초 전";
        }
        return timeAgo;
    }
    ```
    
- 설문조사 서비스
    
    ```java
    private List<SurveyDTO> updateTimeAndStatus(List<SurveyDTO> list) {
        for (SurveyDTO dto : list) {
            timeAndStatus(dto);
        }
        return list;
    }
    
    private void timeAndStatus(SurveyDTO dto) {
        LocalDateTime surveyWriteTime = dto.getSurveyWriteTime();
        LocalDateTime now = LocalDateTime.now();
        String timeAgo = timeSettings(surveyWriteTime, now);
        dto.setTime(timeAgo);
    
    // 여기부터
        LocalDate surveyEndDate = dto.getSurveyEndDate();
        LocalDate endDate = LocalDate.now();
        boolean status = surveyEndDate.isBefore(endDate);
        dto.setStatus(status);
    // 여기까지를 제외한 나머지가 중복
    }
    
    private String timeSettings(LocalDateTime surveyWriteTime, LocalDateTime now) {
        Duration between = Duration.between(surveyWriteTime, now);
        long seconds = between.getSeconds();
        long minutes = between.toMinutes();
        long hours = between.toHours();
        long days = between.toDays();
        String timeAgo = "";
        if (days > 0) {
            timeAgo = days + "일 전";
        } else if (hours > 0) {
            timeAgo = hours + "시간 전";
        } else if (minutes > 0) {
            timeAgo = minutes + "분 전";
        } else {
            timeAgo = seconds + "초 전";
        }
        return timeAgo;
    }
    ```
    

설문조사 서비스의 설문조사 작성 날짜와 종료 날짜에 대한 유효 범위 상태 값을 구하는 코드를 제외하고는 중복된 코드입니다.

따라서 이 중복 코드를 제거하고 하나의 클래스로 관리하기 위한 방법을 고민했습니다.

- TimeUpdatable 인터페이스 (시간 관련 데이터를 다루기 위한 메서드 추상화)
```java
public interface TimeUpdatable {

    LocalDateTime getCreateTime();

    void setCreateTime(String timeAgo);
}
```
- TimeAgoUpdaterService 서비스 (중복되는 비즈니스 로직 공통화)
```java
@Service
public class TimeUpdateService {

    public <T extends TimeUpdatable> void updateListTime(List<T> list) {
        for (T dto : list) {
            updateItemTime(dto);
        }
    }

    public <T extends TimeUpdatable> void updateItemTime(T dto) {
        LocalDateTime writeTime = dto.getCreateTime();
        String timeAgo = getTimeAgo(writeTime);
        dto.setCreateTime(timeAgo);
    }

    private String getTimeAgo(LocalDateTime writeTime) {
        Duration between = Duration.between(writeTime, LocalDateTime.now());
        String timeAgo;
        if (between.toDays() > 0) {
            timeAgo = between.toDays() + "일 전";
        } else if (between.toHours() > 0) {
            timeAgo = between.toHours() + "시간 전";
        } else if (between.toMinutes() > 0) {
            timeAgo = between.toMinutes() + "분 전";
        } else {
            timeAgo = between.getSeconds() + "초 전";
        }
        return timeAgo;
    }

}
```

