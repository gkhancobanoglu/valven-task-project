package com.cobanoglu.valventask.repository;

import com.cobanoglu.valventask.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    // Kullanıcı adını kullanarak geliştiriciyi getirir
    // Developer findByUsername(String username);

    // E-posta adresine göre geliştiriciyi getirir
    // Developer findByEmail(String email);
}
