package ru.test.micro.streampropagation.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.test.micro.streampropagation.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

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
