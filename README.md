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
> ![CleanShot 2026-05-18 at 10.44.06@2x.png](attachment:ea1daecb-34a1-45d5-b972-ace17f83f962:CleanShot_2026-05-18_at_10.44.062x.png)

### **2. EC2 Public IP**
> ![CleanShot 2026-05-21 at 15.17.09@2x.png](attachment:d37e71d8-0f42-46be-8722-8e854c6f4037:CleanShot_2026-05-21_at_15.17.092x.png)

### **3. Actuator Info 엔드포인트 URL**
> http://43.201.84.202:8080/actuator/info
> ![CleanShot 2026-05-21 at 15.24.46@2x.png](attachment:18e3db85-ee50-4c54-b261-fc6ba4838519:CleanShot_2026-05-21_at_15.24.462x.png)

### **4. RDS 보안 그룹 스크린샷**
> ![CleanShot 2026-05-20 at 15.07.37@2x.png](attachment:237b105f-fdaa-46eb-af3f-35ca8d859fd1:4f139c2d-8b12-4dfe-bfa0-4353807ac5cd.png)

### **5. 프로필 이미지 조회 결과**
* **발급 URL**: `https://my-name-camp-profile-bucket.s3.ap-northeast-2.amazonaws.com/profiles/1/3afab590-29c6-492f-a84f-88cfbca3c32f.png?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEDUaDmFwLW5vcnRoZWFzdC0yIkYwRAIgEICn6mDLv%2B6T8UbRIogf7g6g0OXVkLAp1KBLW4978eMCIAaJlLEPmqdEEaSKXaCdjUxZmpuML%2FRW9G6nateYgM4JKtIFCP7%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEQABoMNzM1MzkxMjE4MjEyIgyTmxzf%2FMDWV%2F1zRKUqpgU4YgMm2g0FmswRKm%2BDOCLN4uRp8K%2F%2BnwolrLgCRn%2Bta6M9pF%2FpDJZbrwm%2FCxGNrq9IPv%2BUYNCodtprMB%2B1qaU2hOJh7NYfOH%2B7vn23csLFPu3aiyq3RESFKfL2L4SP62U%2FdSC0ojGQ5erzWdOFLlipZUqc2oVlWRLc36o7uQXZftE7L2OfQlrj3%2Bn0Ix72phTonMFUyetMQg48tY9uy4p39634nCIsPowYb2K1%2F526FmKdRBicvZVfOrhBEjj8ZPXzO85IkBtqAYSAeSdkrM1WJplAjPseC98iskamzNEN%2BxqeZq8ES7tn5FDIU%2FCkAfcsH5J21%2Bxya5FXksLl8TVLJI6iM1zQPXElq85DCAB5iQs13gCTyunvU6fJgCuT7ivgt40d0fD8707IqiqAwa%2FT%2FVMPfrkhpD3CDupFTlGmLhyHMBaVjzN6Wqqj0kWQStia%2BdcxdOg8ssIhY3sO5zmlgMUc9RjVwVtS6DuySDoBzuyHKp%2BGugCogNc0Xsu%2BY%2FSqjpJwybu1hP4NrIfNVeXEFcK7lW3t2brsAwxqoHMujrXQeiBEkMABlhh7FLGBkO%2F27DHNZ%2F7vWm2s2a7ry6czroQ3b8B3w6DcMDKL9fVIOju%2BsmT%2BRqtXc1EsPLxvkSbEknrvtjQ5ZtgT9mJ5%2F%2FyrnRdKBX%2BgY4KQfbqlkbM4eDQYASdKOOGFx5Z6wK4sYkWGekTQayFHiRlXM8rYIBGVzAhv4Zpjz9IP3VCgqpwoWTnVgLQc0iLFY%2BSToTEVlUj5%2B85eRrlvZ%2B53q1kqZIZ2uIdEjQ1yeNey4qIAapQsL2Nj5SFFn8sbBzyKNNPk%2B4uD0qFkzbqkRbopCZVoUWgXLhrjBYCbikTU0irVeydVn5LEg8kYhboh5j6chrZeBfhZJ5Yhtw0wmKa60AY6sgGFIp2zJB8LuzkfFE1zfHMxD1cpaz6hsjhXpqBbxgXfEMSQCxC4c7YBB7WLpVCakLiO%2FAdJ%2FSJsMgQyvOX0LVgx9yBeu4n3IK4DkCdm%2BJyjnCGS762oGBhMTmX5yeq9ch9RESYeNELcn%2BD3%2FIlmcgSKk37w6iA6TMxch7NECqQofeQHiZHmYq0XI1%2BA1aOoQr208AXrbqf4Uohs3j%2B4qBof96Kw%2BepsjuSZA1axcG0vxtcJ&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260521T060938Z&X-Amz-SignedHeaders=host&X-Amz-Credential=ASIA2WOF3PISJOCFHMU7%2F20260521%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=6c0101f53ca9122d13f72fa693f5810e6874a478e2fb826dd337f662a805c2bb`
* **만료 기한**: `2026.05.28 15:10:02`
> ![CleanShot 2026-05-21 at 15.10.02@2x.png](../../../Library/Application%20Support/CleanShot/media/media_t1aw1ljd3s/CleanShot%202026-05-21%20at%2015.10.02%402x.png)

### **6. GitHub Actions 배포 성공**
> ![CleanShot 2026-05-20 at 12.28.07@2x.png](attachment:35039f1e-49c7-4274-ae2e-10aff668e473:3b409154-5639-44c7-8ab1-b522c9d9545d.png)
 
### **7. EC2 컨테이너 실행 확인 (`sudo docker ps`)**
> ![CleanShot 2026-05-20 at 12.27.49@2x.png](attachment:9e2b0c88-4def-49b3-ac89-8ee6ba8f82de:4d92679d-47f2-4c3f-8211-c6b01e45b149.png)