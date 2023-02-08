package com.suryansh.userservice.repository;

import com.suryansh.userservice.entity.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCartRepository extends JpaRepository<UserCart, Long> {
    Optional<UserCart> findByIdAndUserId(Long id, Long id1);

    @Modifying
    @Query("delete from UserCart c where c.id =:id")
    void deleteProductFromCart(Long id);

    UserCart findFirstByUserIdAndProductId(Long id, Long productId);
}