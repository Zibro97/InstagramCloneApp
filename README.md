# InstagramClone 앱

### 로그인 방식 </br>
1) 이메일 로그인 </br>
   파이어베이스에 이메일,패스워드 등록 -> 로그인 응답 수신 - > android 이벤트 처리
2) 소셜 로그인(Facebook, Google) </br>
   소셜로그인 -> 파이어베이스 -> 로그인 응답 -> android 이벤트 처리
### 이미지
디바이스의 엘범에서 이미지 url을 Firebase FireStore에 저장하여 사용,관리
### FCM 통신
Firebase Messaging 라이브러리를 이용하여 유저의 토큰을 Firebase에 저장 후 개별 push알림 설정(좋아요 이벤트, 댓글 이벤트, Follow 이벤트)


### 참고 자료
<a href="https://inf.run/bKW7">https://inf.run/bKW7</a>