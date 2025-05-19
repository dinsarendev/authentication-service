package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.models.ResponseCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseCodeRepository extends JpaRepository<ResponseCode, Integer> {

    Optional<ResponseCode> findByCodeAndStatusNot(String code, String status);

    Optional<ResponseCode> findByHttpStatus(String httpStatus);

    Optional<ResponseCode> findByKey(String key);

    List<ResponseCode> findByType(String type);

    List<ResponseCode> findByStatus(String status);
}
