package com.chumabanking.sentinel.repository;

import com.sentinel.common.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // This gives us save(), findById(), and delete() for free!
}