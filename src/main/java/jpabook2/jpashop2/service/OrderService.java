package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.Delivery;
import jpabook2.jpashop2.domain.Item.Item;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.OrderItem;
import jpabook2.jpashop2.repository.ItemRepository;
import jpabook2.jpashop2.repository.MemberRepository;
import jpabook2.jpashop2.repository.OrderRepository;
import jpabook2.jpashop2.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long Order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);

        // 주문생성
        Order order = Order.createOrder(member,delivery,orderItem);

        //주문저장
        orderRepository.save(order);  // Order가 OrderItem이랑 Delivery를 CASCADE.ALL 옵션으로 갖고 있어서 Order가 persist될 때 얘네도 같이 persist해줌

        return order.getId();
    }


    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // 주문 검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
