package jpabook2.jpashop2.service;

import jakarta.persistence.EntityManager;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import jpabook2.jpashop2.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class) // JUNIT실행할 때 스프링이랑 같이 실행하겠다.
@SpringBootTest  // 스프링부트를 띄운 상태로 테스트를 하겠다. 얘네 두 개가 있어야 스프링이랑 integration해서 테스트할 수 있음
@Transactional  // @Transactional이 @Test에 있으면 롤백해서 commit이 안 나감
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(false) // @Test랑 @Transactional이 함께 작용하면 다 rollback시키니까 이를 안 시키고 DB에 쿼리가 날아가서 어떻게 적용되는지 보려고 @rollback(false)함
    public void testMember() throws Exception{
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush();

        Assertions.assertEquals(member,memberService.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        // when
        Long savedId = memberService.join(member1);
//        Long savedId2 = memberService.join(member2); // illegalStateException터져야함
        try {
            memberService.join(member2);
        }
        catch (IllegalStateException e){
            return;
        }

        // then
        em.flush();

        fail("에러가 발생해야합니다");
    }
}
