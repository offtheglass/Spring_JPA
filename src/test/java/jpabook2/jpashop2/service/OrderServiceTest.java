package jpabook2.jpashop2.service;

import jakarta.persistence.EntityManager;
import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Exception.NotEnoughStockException;
import jpabook2.jpashop2.domain.Item.Book;
import jpabook2.jpashop2.domain.Item.Item;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.OrderStatus;
import jpabook2.jpashop2.repository.OrderRepository;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

//    @PersistenceContext
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        // given
        Member member = cMember();


        Book book = cBook("PA", 10000, 10);

        int orderCount = 2;


        // when
        Long orderId = orderService.Order(member.getId(), book.getId(),orderCount);
        // then
        Order getOrder = orderRepository.findOne(orderId);



        Assertions.assertEquals(getOrder.getId(),orderId);
        Assertions.assertEquals(OrderStatus.ORDER,getOrder.getStatus());
        Assertions.assertEquals(1,getOrder.getOrderItems().size());
        Assertions.assertEquals(10000*orderCount, getOrder.getTotalPrice());
        Assertions.assertEquals(8,book.getStockQuantity());

    }

    private Book cBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member cMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","경기","123-123"));

        em.persist(member);
        return member;
    }

    @Test( expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = cMember();
        Item item = cBook("시골 JPA",10000,10);

        int orderCount = 11;
        // when

        orderService.Order(member.getId(), item.getId(), orderCount);
        // then
        fail("재고수량초과 에러가 발생해야한다.");
    }

    @Test
    public void 주문취소() throws Exception{
        // given
        Member member = cMember();
        Book item = cBook("서울 JPA",10000,10);

        int orderCount = 2;
        Long orderId = orderService.Order(member.getId(), item.getId(), orderCount);


        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL,getOrder.getStatus());
        Assertions.assertEquals(10,item.getStockQuantity());
    }

}
