package com.example.app.ws.io.repositories;

        import com.example.app.ws.io.entity.SubjectEntity;
        import com.example.app.ws.io.entity.UserEntity;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.PagingAndSortingRepository;
        import org.springframework.data.repository.query.Param;
        import org.springframework.lang.Nullable;
        import org.springframework.stereotype.Repository;

        import java.util.List;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);

    @Query("SELECT u FROM users u " +
            "WHERE (:firstName is null or u.firstName = :firstName) "+
            "AND (:lastName is null or u.lastName = :lastName)"+
            "AND (:room is null or u.room = :room)"+
            "AND (:subject is null or :subject member u.subjectList)")
    Iterable<UserEntity> findFiltered(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("room") String room, @Param("subject") SubjectEntity subject);
}
