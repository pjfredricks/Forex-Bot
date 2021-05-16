package com.forexbot.api.repository;

import com.forexbot.api.dao.backoffice.userdata.BackOfficeUserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackOfficeRepository extends JpaRepository<BackOfficeUserData, Integer> {
    boolean existsByUserId(String userId);

    BackOfficeUserData getBackOfficeUserDataByEmailIdAndActive(String emailId, boolean isActive);

    BackOfficeUserData getBackOfficeUserDataByUserId(String userId);

    BackOfficeUserData getBackOfficeUserDataByUserNameAndEmailIdAndMobileNum(String userName,
                                                                           String emailId,
                                                                           String mobileNum);
}
