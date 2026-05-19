package com.example.aws.controller;

import com.example.aws.dto.MemberRequest;
import com.example.aws.entity.Member;
import com.example.aws.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
