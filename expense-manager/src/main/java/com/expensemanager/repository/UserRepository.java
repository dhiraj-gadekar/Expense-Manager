package com.expensemanager.repository;

import com.expensemanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String email);

    @Query("SELECT u.userId FROM User u")
    Optional<List<Integer>> fetchAllUserIds();

    @Query("SELECT u.username FROM User u WHERE u.userId= :userId")
    String getUserName(@Param("userId") int userId);
}