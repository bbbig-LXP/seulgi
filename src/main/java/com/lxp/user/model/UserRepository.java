package com.lxp.user.model;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);
    
}
