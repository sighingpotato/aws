package com.example.aws.service;

import com.example.aws.dto.MemberRequest;
import com.example.aws.entity.Member;
import com.example.aws.repository.MemberRepository;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final S3Template s3Template;    // S3 업로드용
    private final S3Presigner s3Presigner;    // S3 프리사인 URL 생성용

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public Member saveMember(MemberRequest request) {
        Member member = new Member(request.getName(), request.getAge(), request.getMbti());
        return memberRepository.save(member);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다. ID: " + id));
    }

    // 프로필 이미지 업로드 로직 (POST)
    @Transactional
    public String uploadProfileImage(Long memberId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String s3Key = "profiles/" + memberId + "/" + UUID.randomUUID() + extension;

        // S3에 업로드
        s3Template.upload(bucketName, s3Key, file.getInputStream());

        // DB에 S3 키 저장
        member.updateProfileImage(s3Key);

        return "업로드 성공";
    }

    // 7일짜리 Presigned URL 생성 로직 (GET)
    public String getProfileImageUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));

        String s3Key = member.getProfileImageKey();
        if (s3Key == null) {
            return "프로필 이미지가 없습니다.";
        }

        // S3에서 파일을 가져오겠다는 요청서 작성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        // 7일짜리 임시 입장권 요청서 작성
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(getObjectRequest)
                .build();

        // 최종 URL 생성
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }
}
