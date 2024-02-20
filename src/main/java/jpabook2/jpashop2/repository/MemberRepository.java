package jpabook2.jpashop2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook2.jpashop2.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // final이 붙은 필드들을 parameter로 하는 생성자를 만들어줌
public class MemberRepository {
    // @PersistenceContext  //  Spring이 생성한 entitymanager를 주입해줌
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select  m from Member m where m.name=:name",Member.class).setParameter("name",name).getResultList();
    }

}
