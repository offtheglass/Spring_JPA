package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 기본적으로 JPA에서 데이터에 손을 대는 것들은 @Transactional이 있어야한다.
// class레벨에 @Transactional를 쓰면 Public 메서드에는 기본적으로 @Transactional아 걸려들어간다.
// readOnly = true는 데이터 조회만 하겠다는 뜻으로 조금 더 조회에 맞게 최적화 시켜준다.
@RequiredArgsConstructor
// 얘는 final이 붙은 field들을 parameter로 하는 생성자를 한 개를 만들어준다.
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional // 여기는 readOnly만 할 게 아니니깐 @Transactional을 붙여줬다.
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByName(member.getName());
        if(!members.isEmpty()){
            throw new IllegalStateException("이미 존재하는  회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
