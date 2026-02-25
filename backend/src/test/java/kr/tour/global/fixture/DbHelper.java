package kr.tour.global.fixture;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public abstract class DbHelper {

  @Autowired
  @PersistenceContext
  protected EntityManager em;
}