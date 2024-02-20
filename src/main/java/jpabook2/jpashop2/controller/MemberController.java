package jpabook2.jpashop2.controller;

import jakarta.validation.Valid;
import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Item.Book;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.service.ItemService;
import jpabook2.jpashop2.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        // BindingResult result는 요청에 오류가 있거나 하면 그 오류를 담아서 실행할 수 있도록 하는 파라미터
        if(result.hasErrors()){
            return "members/createMemberForm"; // result에 에러가 있으면 members/createMemberForm로 리다이렉팅, 이 때 form에 있는 데이터도 같이 보내준다!!!
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);   // member객체 자체를 넘기는 것보다는 form을 만들어서 넘기는 게 좋음
        return "members/memberList";
    }

}