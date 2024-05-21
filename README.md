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

  * 공지사항 서비스 비즈니스 로직
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

### **해결과정**
* 1) TimeUpdatable 인터페이스 도입
설문조사와 공지사항에서 사용되는 객체(DTO)들이 시간 데이터를 다루는 경우에 읽어오고 가져옴에 있어서 일관된 표준 방법이 필요했습니다.
이러한 문제를 해결하고자, 인터페이스를 사용하여 객체(DTO)들이 시간 데이터를 다루는 표준화된 방법을 도입하고자 했습니다.
```java
public interface TimeUpdatable {

    LocalDateTime getCreateTime();// 객체의 작성 시간을 가져오는 메서드

    void setCreateTime(String timeAgo);// 작성 시간을 시간 업데이트 문자열로 저장하는 메서드
}
```

* 2) TimeAgoUpdaterService 클래스 모듈화 제네릭을 사용하여 TimeAgoUpdaterService 클래스의 메서드들이 TimeUpdatable 인터페이스를 구현한 클래스의 객체들에 대해서만 동작하도록 제한함으로써,
잘못된 객체가 사용되는 것을 방지하고자 했습니다.
```java
@Service
public class TimeAgoUpdaterService {

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

}
```

* 또한, 설문조사 서비스와 공지사항 서비스의 중복된 메서드를 getTimeAgo 메서드로 모듈화 했습니다.
```java
@Service
public class TimeAgoUpdaterService {

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

* 3) TimeAgoUpdaterService 전체 코드
```java
@Service
public class TimeAgoUpdaterService {

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

* 4) 리팩토링 후기
 * 유지 보수 단순화 : 중복 코드를 한 곳에서 관리하여 코드 변경이 필요할 때 한 곳만 수정하면 되므로 유지 보수가 단순화되고, 효율적으로 코드를 관리 할 수 있도록 되었습니다.
 * 재사용성 향상 및 일관성 유지 : TimeAgoUpdaterService를 이용하면 다양한 클래스에서 시간 업데이트 기능을 재사용 할 수 있게 되었고, 다양한 클래스에서 동일한 방식으로 시간을 처리하여 일관성을 유지할 수 있게 되었습니다.
 * 타입 제한을 통한 안정성 보장: TimeAgoUpdaterService는 TimeUpdatable 인터페이스를 구현한 클래스에서만 동작함으로써 안정성을 확보하고 예기치 않은 오류를 사전에 방지할 수 있게 되었습니다.

## 프로젝트 후기
## 1. **세분화된 테이블 설계**

초기에는 세분화된 테이블이 데이터의 유지보수에 도움이 될 것으로 생각했지만, 실제 구현 중에는 성능 문제와 복잡성이 나타났습니다. 특히 설문조사 기능을 구현하는 단계에서, 테이블을 지나치게 세분화한 것이 여러 테이블을 조인해야 하는 상황이 자주 발생하게 되었고, 쿼리 실행 속도가 지연되면서 성능 문제가 더욱 두드러지게 나타났습니다.

전체적인 구조를 재설계 하는 것은 프로젝트 일정에 부담이 될 수 있었지만, 기존의 작성해두었던 메뉴 구조도, 화면 기획서 및 ER다이어그램을 참고해 보다 신속하고 빠르게 재설계 할 수 있었습니다.

이 과정을 통해 전체적인 프로젝트의 복잡성을 줄일 수 있었고, 여러 테이블의 조인 문제로 인한 성능 문제도 많은 부분이 해소 될 수 있었습니다. 그리고 무엇보다 설계 초기 단계에서 여러가지 문제점들을 고려해 설계와 성능을 균형 있게 기획해야 함을 깨달았습니다.

## 2. **클라우드 서비스 기술**

프로젝트를 시작하기 전, `AWS EC2` 인스턴스를 생성하여 간단한 프로젝트를 배포하는 과정을 경험했습니다. 처음에는 클라우드 서비스와 명령어에 익숙하지 않아서 많은 어려움을 겪었습니다. `EC2` 인스턴스에 어떻게 접속해야 하는지도 몰라서, 처음에는 단순히 인스턴스를 삭제하고 다시 생성하는 실수를 반복했습니다.

접속에 실패한 이유를 파악하지 않고 실수를 반복하다가, `공식 AWS 문서`를 참고해보니 제가 생성한 `SSH` 키의 저장 경로에 문제가 있음을 깨달았습니다. 이를 인지하고 경로 재설정과 권한을 설정한 후, 접속 문제를 해결할 수 있었습니다. 

프로젝트 진행 중에도, 파일을 관리하기 위해 `AWS S3`를 설정 및 사용하는데 권한과 접근 문제에 직면하면서 미로처럼 어둡고 혼란스러운 길을 헤매는 느낌이었습니다. 그러나 이런 문제를 극복하기 위해 하루 중 대부분의 시간을 투자하며 집중하는 제 모습을 발견하며 **‘나는 개발에 몰입할 수 있다. 어떤 문제라도 해결할 수 있다’** 하는 자신감이 생기게 되었습니다. 

이번 프로젝트를 통해서 낯선 기술과 문제에 직면했을 때도 꾸준한 노력과 인내로 문제를 해결할 수 있다는 자신감과 확신을 얻을 수 있었습니다. 그리고 아직 배워야 할 것이 많고, 공부를 할수록 새로운 공부 과정이 계속해서 등장해 끝이 없다는 것을 체감했습니다. 그러나 이러한 도전이 힘들기만 한 것이 아니라 오히려 재미있고 흥미로운 과정임을 이번 프로젝트를 통해 얻어갈 수 있었습니다.

## 3. 보완해야할 점

- **테스트 코드 부재**: 테스트 코드 작성하는 것을 간과한 결과, 변경 사항이 프로젝트 전반에 미치는 영향을 파악하기 어려워졌고, 버그를 찾아내기 어려웠습니다. 이로 인해 기능 및 성능 테스트를 실시하지 못한 점을 반성하게 되었습니다. 앞으로는 테스트 코드를 작성해 코드의 안정성과 품질을 향상 시킬 계획입니다.
- **예외 처리 부족**: 프로젝트를 진행하며 예외 상황을 충분히 고려하지 않았고, 예외 처리를 제대로 구현하지 못했습니다. 향후 개선 사항으로 `예외 처리 클래스`를 도입하여 프로젝트의 안정성을 높이는 데 노력할 계획입니다.
- **배포 자동화 계획**: 초기 계획은 `AWS EC2` 및 `Github Action`을 활용하여 배포 자동화를 달성하는 것이었습니다. 현재는 `EC2` 인스턴스만을 이용한 배포가 이루어지고 있습니다. 향후에는 지속적인 학습을 통해 빌드 및 배포 자동화를 함께 구현하는 것이 목표입니다. 이를 통해 프로젝트의 개발 및 배포 프로세스를 효율적으로 관리할 계획입니다.

## 4. 느낀점

이번 프로젝트에서 `AWS`의 클라우드 서비스를 활용하여 프로젝트를 배포하고 데이터를 저장하면서 클라우드 환경에서의 서비스 운영을 이해하는 데 큰 도움이 되었습니다.  특히, `AWS EC2` 인스턴스를 생성, 관리, 및 배포하는 과정에서 겪은 어려움과 해결하는 과정은 제 자신에게 자신감을 부여했습니다.

비록 배포 자동화 목표를 완전히 달성하지는 못했지만, 명령어와 웹 어플리케이션 아키텍처에 대한 더 깊은 이해를 얻을 수 있었고, 클라우드 기술 사용에 대한 경험에 의의를 두고 계속해서 발전시킬 계획입니다.

프로젝트 진행 과정에서, 실제 배포까지 할 프로젝트라고 생각하니 성능 부분에서도 보다 좋은 성능을 제공하고자 노력했습니다. 특히 설문 결과를 보여주는 쿼리에서 여러 테이블의 데이터를 한 번에 읽어오는 것이 효율적이라고 판단하여, 하나의 쿼리문을 몇 일 동안 여러 차례 고민하고 수정함으로써 투자한 노력이 개발자로서 향후 개발 방향을 정립할 수 있는 중요한 학습 경험 중 하나로 남게 되었습니다.

새로운 도전은 계속해서 학습의 기회를 제공한다는 것을 깨닫게 되었습니다. 아직 부족한 것이 너무 많고, 공부를 하면 할수록 새로운 학습 대상이 몇 배로 다가오지만 그 모든 과정이 어렵고 고통스럽기보다는 흥미로워지고 즐겁게 느껴지는 변화를 처음 경험했습니다. 이번 설문 프로젝트를 통해 얻은 경험과 자신감을 바탕으로, 개선점은 더 개선하여 더욱 더 발전할 수 있도록 하겠습니다.
