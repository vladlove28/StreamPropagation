package ru.test.micro.streampropagation.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.test.micro.streampropagation.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hibernate.jpa.HibernateHints.HINT_CACHEABLE;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.HibernateHints.HINT_READ_ONLY;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    String HINT_PASS_DISTINCT_THROUGH = "org.hibernate.readOnly.passDistinctThrough";

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.items " +
            "ORDER BY o.id")
    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "10000"),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = HINT_READ_ONLY, value = "true"),
            @QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false")
    })
    Stream<Order> findAllWithJoinFetchPropagation();


    @Query("SELECT o FROM Order o ")
    List<Order> findAllWithoutJoin();

    @Query("SELECT o FROM Order o JOIN FETCH o.items")
    List<Order> findAllWithJoinFetch();

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Order> findWithOptimisticLockById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findWithPessimisticLockById(@Param("id") Long id);
}
