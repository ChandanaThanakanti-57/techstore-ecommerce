package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techstore.ecommercemaven.dto.CustomerGrowthDTO;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {


    User findByEmail(String email);
    long count();

    @Query("""
SELECT new com.techstore.ecommercemaven.dto.CustomerGrowthDTO(
    MONTH(u.createdAt),
    COUNT(u.id)
)
FROM User u
GROUP BY MONTH(u.createdAt)
ORDER BY MONTH(u.createdAt)
""")
    List<CustomerGrowthDTO> getCustomerGrowth();

    @Query("""
SELECT COUNT(u)
FROM User u
WHERE MONTH(u.createdAt)=MONTH(CURRENT_DATE)
AND YEAR(u.createdAt)=YEAR(CURRENT_DATE)
""")
    Long getNewUsersThisMonth();
}
