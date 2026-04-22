package com.lxp.user.repository;

import com.lxp.user.model.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

}
