package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        if(orderSearch.getOrderStatus() != null && !StringUtils.hasText(orderSearch.getMemberName())) {
            return entityManager.createQuery(
                            "select o from Order o join o.member m " +
                                    "where o.status = :status", Order.class)
                    .setParameter("status", orderSearch.getOrderStatus())
                    .setMaxResults(1000)        // 최대 100건
                    .getResultList();
        } else if(orderSearch.getOrderStatus() == null && StringUtils.hasText(orderSearch.getMemberName())) {
            return entityManager.createQuery(
                            "select o from Order o join o.member m " +
                                    "where m.name like :name", Order.class)
                    .setParameter("name", orderSearch.getMemberName())
                    .setMaxResults(1000)        // 최대 100건
                    .getResultList();
        }
        return entityManager.createQuery(
                "select o from Order o join o.member m " +
                "where o.status = :status " +
                "and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)        // 최대 100건
                .getResultList();
    }


}
