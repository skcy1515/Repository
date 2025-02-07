# MongoDB
https://cloud.mongodb.com/v2#/org/67a2eb864bd38a79b8d2bf48/projects

1. Create 을 눌러 클러스터 생성하기

![image](https://github.com/user-attachments/assets/40540559-3763-4367-8b24-1d4fad23a5bc)

2. 무료 버전인 M0 를 선택, 이름과 프로바이더, 리전을 선택하여 Create Deployment 클릭

![image](https://github.com/user-attachments/assets/56d97ff9-8c3f-4bcf-8dc2-a2437f3265c3)

3. Username과 Password를 복사해서 저장. 이후 MongoDB Atlas에 연결할 때 사용됨

![image](https://github.com/user-attachments/assets/a641dd18-c942-40d6-9123-95ff38bc60e3)

![image](https://github.com/user-attachments/assets/bd264ceb-d1a8-4133-9374-00bb237d5ed4)

![image](https://github.com/user-attachments/assets/9b068bff-9742-4892-a5ec-213115f57ac5)

4. Network Access를 눌러줌. MongoDB Atlas를 어디서든 연결할 수 있게 함. ADD IP ADDRESS를 누르고, ALLOW ACCESS FROM ANYWHERE를 선택

# pymongo 사용
![image](https://github.com/user-attachments/assets/9c26f138-d735-4188-a0e3-6f74943a26cd)

- connect -> drivers 이동

![image](https://github.com/user-attachments/assets/e260ef12-cb48-4897-9995-428797dc4615)

- 이미지의 빨간색으로 표시해 둔 부분을 확인하시면 URI를 확인
- 맨 뒤에 &tlsAllowInvalidCertificates=true 를 추가
- 예시 uri
```
# 아이디, unique는 uri를 복사하실 때 같이 복사됨. 비밀번호만 바꿔서 저장
uri = "mongodb+srv://<아이디>:<비밀번호>@cluster0.<unique>.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tlsAllowInvalidCertificates=true"
```

```
pip install pymongo
```
- pymongo 패키지 설치

```
# pymongo를 임포트 하기
from pymongo import MongoClient
# 아래 uri를 복사해둔 uri로 수정하기
uri = "mongodb+srv://<아이디>:<비밀번호>@cluster0.<unique>.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tlsAllowInvalidCertificates=true"
client = MongoClient(uri, 27017)  # MongoDB는 27017 포트로 돌아감
db = client.dbjungle    # 'dbjungle'라는 이름의 db를 만듦
# 코딩 시작
```
- 기본 템플릿

# MongoDB Compass 사용
https://downloads.mongodb.com/compass/mongodb-compass-1.45.0-win32-x64.exe

![image](https://github.com/user-attachments/assets/50e0b48b-0a6c-40c1-99a1-1545d5c92e10)

- 좌측 + 버튼 혹은 + Add new connection 버튼을 누름

![image](https://github.com/user-attachments/assets/417d6e50-355a-49b6-a031-2de54f01bb7c)

- URL 칸에 위에서 저장해둔 uri 입력, name 칸에 db_jungle 입력, Color 칸은 자유롭게 설정하고 Save & Connect 클릭
- 이 때 uri에 “” 가 같이 입력되지 않게 주의
