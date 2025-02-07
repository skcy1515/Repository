# AWS 설치
https://ap-northeast-2.console.aws.amazon.com/ec2/v2/home?region=ap-northeast-2

EC2 검색해서 인스턴스 시작 누르기

![image](https://github.com/user-attachments/assets/db4aaa61-b806-4cc3-a7dc-cb46999015c4)

- 애플리케이션 및 OS 이미지는 Quick Start의 Ubuntu로 설정
- Amazon Machine Image(AMI)는 Ubuntu Server 24.04 LTS로 설정

![image](https://github.com/user-attachments/assets/d541ce60-2f4f-46c4-9ac0-0e4ecc7a3469)

- 인스턴스 유형은 t2.micro
- 3개 트래픽 허용

![image](https://github.com/user-attachments/assets/3163614e-eaf5-473d-a6ca-bc43f3169a1c)

- 유형은 RSA, 형식은 .pem 으로 설정
- 새 키 페어 생성하고 저장

### EC2 서버 종료하는 방법 (1년 후 자동 결제 방지)
- 무료 기간(1년) 후 결제가 되기 전에, 종료해야됨
- 대상 인스턴스에 마우스 우클릭 → 인스턴스 종료(삭제)

# AWS EC2 접속
![image](https://github.com/user-attachments/assets/81eb13a1-65ed-4719-ab7a-3cc7273003c2)
- SSH: 다른 컴퓨터에 접속할 때 쓰는 프로그램이며, 서로 22번 포트가 열려있어야 접속 가능
- git bash를 통해 명령어 실행
```
ssh -i 받은키페어를끌어다놓기 ubuntu@AWS에적힌내아이피
```

