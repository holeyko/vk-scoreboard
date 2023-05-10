package com.holeyko.vkscoreboard.repository;


import com.holeyko.vkscoreboard.model.User;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPasswordSha(String login, String passwordSha);

    @Query("SELECT u FROM User u JOIN u.statistic.categoryStatistics uStat WHERE uStat.category.name = ?1")
    List<User> findAllByCategoryName(String categoryName);
}
