package com.blueground.appserver.repository;

import java.util.List;

import com.blueground.appserver.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

    List<Unit> findByPublished(boolean published);

    List<Unit> findByTitleContaining(String title);
}