# 🚀 AWS 기반 Spring Boot 백엔드 서비스 (Lv.1 ~ Lv.4)

본 프로젝트는 Java와 Spring Boot를 기반으로 하며, AWS 인프라 구축부터 Docker 및 GitHub Actions를 활용한 CI/CD 파이프라인 자동화까지의 과정을 다룬다.

---

## 🛠️ 기술 스택 (Tech Stack)

| 분류 | 기술 |
| :--- | :--- |
| **Language** | Java 17 (Temurin JDK) |
| **Framework** | Spring Boot 3.3.0 |
| **Database** | MySQL 8.0 (AWS RDS), H2 (Local) |
| **ORM** | Spring Data JPA (Fetch Join을 통한 N+1 최적화) |
| **Infrastructure** | AWS EC2 (Amazon Linux), S3, Parameter Store |
| **DevOps** | Docker, GitHub Actions, Docker Hub |

---

## 🏗️ 시스템 아키텍처 (Architecture)

1.  **CI/CD**: `main` 브랜치에 코드가 `Push`되면 GitHub Actions가 자동으로 빌드 및 테스트를 수행.
2.  **Dockerization**: 빌드된 아티팩트(`.jar`)를 Docker 이미지로 패키징하여 Docker Hub에 업로드.
3.  **Deployment**: EC2 서버는 Docker Hub에서 최신 이미지를 `Pull` 받아 기존 컨테이너를 대체하고 실행.

---

## 📜 주요 구현 및 학습 내용

### **1. 기초 API 및 클라우드 연동 (Lv.1 & 2)**
* 팀원 정보(이름, 나이, MBTI)에 대한 CRUD API를 개발.
* AWS RDS(MySQL)를 연동하여 데이터베이스 환경을 클라우드로 이전.
* **보안**: DB 접속 정보 등 민감한 설정값은 `AWS Parameter Store`를 통해 관리하여 소스코드 노출을 방지했다.

### **2. S3 기반 이미지 관리 (Lv.3)**
* 프로필 사진 업로드 기능을 추가하고, 파일을 AWS S3 버킷에 저장하도록 구현했다.
* **보안**: S3 버킷의 퍼블릭 액세스를 차단하고, EC2에 부여된 `IAM Role`을 통해 내부 통신 권한을 제어했다.
* **접근**: 보안 강화를 위해 유효기간 **7일**의 `Presigned URL`을 생성하여 클라이언트에 제공했다.

### **3. Docker 및 배포 자동화 (Lv.4)**
* `Dockerfile` 작성을 통해 애플리케이션을 컨테이너화하여 일관된 실행 환경을 구축했다.
* GitHub Actions 워크플로우를 설계하여 수동 배포 과정을 완전 자동화했다.
* **플랫폼 최적화**: EC2의 ARM64 아키텍처에 대응하기 위해 `Docker Buildx`를 사용하여 멀티 플랫폼 빌드를 적용했다.

---

## 🚨 트러블슈팅 (Troubleshooting)

### **Q1. `exec format error` 발생**
* **원인**: 빌드 환경(인텔 칩)과 실행 환경(ARM64)의 아키텍처 불일치로 인해 발생했다.
* **해결**: `deploy.yml`에 `platforms: linux/arm64` 설정을 추가하여 대상 환경에 맞는 이미지를 빌드하도록 수정했다.

### **Q2. `address already in use` 발생**
* **원인**: 기존에 백그라운드에서 실행 중이던 Java 프로세스가 8080 포트를 점유하고 있었다.
* **해결**: `sudo lsof -i :8080`으로 PID를 확인한 후 `kill -9`로 종료하여 포트 충돌을 해결했다.

---

## 📸 과제 검증 결과

### **1. AWS Budget 설정**
> ![CleanShot 2026-05-18 at 10.44.06@2x.png](readmephoto/CleanShot%202026-05-18%20at%2010.44.06%402x.png)

### **2. EC2 Public IP**
> ![CleanShot 2026-05-21 at 15.17.09@2x.png](readmephoto/CleanShot%202026-05-21%20at%2015.17.09%402x.png)

### **3. Actuator Info 엔드포인트 URL**
> http://43.201.84.202:8080/actuator/info

> ![CleanShot 2026-05-21 at 15.24.46@2x.png](readmephoto/CleanShot%202026-05-21%20at%2015.24.46%402x.png)

### **4. RDS 보안 그룹 스크린샷**
> ![CleanShot 2026-05-20 at 15.07.37@2x.png](readmephoto/CleanShot%202026-05-20%20at%2015.07.37%402x.png)

### **5. 프로필 이미지 조회 결과**
* **발급 URL**: `https://my-name-camp-profile-bucket.s3.ap-northeast-2.amazonaws.com/profiles/1/0a2bbc40-f3bc-4643-8753-cb3ceb675acd.png?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEEoaDmFwLW5vcnRoZWFzdC0yIkYwRAIgEKp3zpg2%2FLgMJo%2BdWqcIRV98778arhp0RCRM9Z0yUR0CIHVPNFlUOCeOJfwsChs9GCOPVvtZjpVtgSk6PJc7WU6oKskFCBMQABoMNzM1MzkxMjE4MjEyIgw7%2B42dEQSU8RYMeacqpgVk4Qz5fXhpeAXCJbUe5GQDVzCec2vkfjcKp6D1sw5oQjcwGGNN5oncAkDSF%2FwLLfmOQ%2FhpgZRVO46mAT%2FExjuPQKrnMHl83cIuUihjJ9I3rIx5IHfeaOubrUL5nMhPszIBYNEPmNmJr9cRzvwWO7%2BWedBpGcbMLX78yf%2FbfNz1uZ3%2FKZDe49A%2BmX4wt4AmjzeAUoK7UHxuw%2FC%2FQFd3%2BPyThXWFTZXvgbPq8wIFEnLZkXO81K5DjaFA0iRX6vcgmWs4PHGnU9f8YOnWxoIbMIVNOv4vWmltww0Rer7HTJqfau8FGSshIdxDhLZcSZ%2BXAWOLsaeOCiPmRzXffbnAM6ZCRtL3Iz4l0ubUfFoAY7Gj%2Bv157LXHi9BseXybcoIXzUs6j7owTOVdaP4iZKc78V6hxE2VssRUp%2FzNqK3D3okKVINYI3NMqIB6Layh1rmWtap%2By%2FPRZ%2F0anyirbV8EdxA4mWZUbCB7m8kI%2BLqO5hnggSIKOllkcY6S%2FOGUqBQ15lLR7C1ys8QEXSttdvqMsbC8aSmxxmtUbVRWsx5xp6XvEGrSjOJs498Z3nRcmMvU5bkOZEjaJy54zk1zyA51L6KQk8joCSvUpVS0aNR%2BOIl5A3IRCcC56073oE2CqnF%2BgovlAXkNIvhn8ujXyHW5xpQOwqIqvLheR2sdnA9q8c2Ik9TpM5V%2FwU01Ov5w0PrAvMTpNkquahz2BQR4QRhNvrqGrRFO%2BfTFQCzxPgER0BQy2IRqDW49vh0Xi6jFVXrUiS4ye%2B931Sho018VzMpluf8K3moPwMg8XhmqLhQtAoSXCq0U5NI72q3VLhcm3c29e1SBWUzrykXLj4WTEAg3yfPzJRXSPAURBbv5EMGG2%2B8cLCikTUNqifODF5hYU3HwzXioQtJipBcw%2B%2FK%2B0AY6sgE50SCYWLllcRkQtYhhuKtYTrJpbDQTbcSmRybkM8e9ayVlAcnFWh8IiKFsZ%2Bvx03GMeSGqb%2FMMcNZrWzhSMpuLwGax9a1PwnVf5dtNnfZX0%2BcyhcoF3EyTQZ1NO7uRV1Rsrb%2FfG3ivwDhzEVBFudWHTqLxvS5ClJf1iUSJ9eBJJSrEvNCKF8uCxIOFtQmHAlYVYTB5VC4whyGijLdKHzNCCRoZAYYN5nmHzdab8W0fcPCP&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260522T021033Z&X-Amz-SignedHeaders=host&X-Amz-Credential=ASIA2WOF3PISDQFGSPKY%2F20260522%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=183b5c8cd02be9d9cd42b3635773e602dfae148c30e669601870f417c6e1a085`
* **만료 기한**: `2026.05.29 11:10:33`
> ![CleanShot 2026-05-22 at 11.12.02@2x.png](readmephoto/CleanShot%202026-05-22%20at%2011.12.02%402x.png)

### **6. GitHub Actions 배포 성공**
> ![CleanShot 2026-05-20 at 12.28.07@2x.png](readmephoto/CleanShot%202026-05-20%20at%2012.28.07%402x.png)

### **7. EC2 컨테이너 실행 확인 (`sudo docker ps`)**
> ![CleanShot 2026-05-20 at 12.27.49@2x.png](readmephoto/CleanShot%202026-05-20%20at%2012.27.49%402x.png)