package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
    @Query(value = "SELECT * FROM tb_contact c " + "WHERE c.email = :email", nativeQuery = true)
    List<Contact> findAllByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM tb_contact c " + "WHERE c.date = :date", nativeQuery = true)
    List<Contact> findAllByDate(@Param("date") Date date);
}
