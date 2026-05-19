package com.example.aws.controller;

import com.example.aws.dto.MemberRequest;
import com.example.aws.entity.Member;
import com.example.aws.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Member createMember(@RequestBody MemberRequest request) {
        return memberService.saveMember(request);
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    // 프로필 사진 S3에 업로드 (POST)
    @PostMapping("/{id}/profile-image")
    public String uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile File) throws IOException {
        return memberService.uploadProfileImage(id, File);
    }

    // 7일짜리 임시 입장권 발급 (GET)
    @GetMapping("/{id}/profile-image")
    public String getProfileImage(@PathVariable Long id) {
        return memberService.getProfileImageUrl(id);
    }
}
