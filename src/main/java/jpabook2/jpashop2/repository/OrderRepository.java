package jpabook2.jpashop2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstContidion = true;

        // 주문 상태 검색
        if(orderSearch.getOrderStatus() !=null){
            if(isFirstContidion) {
                jpql += " where";
                isFirstContidion=false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원이름검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstContidion){
                jpql += " where";
                isFirstContidion = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql,Order.class).setMaxResults(1000); // 최대 1000건

        if(orderSearch.getOrderStatus() != null){
            query = query.setParameter("status",orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())){
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }
}
