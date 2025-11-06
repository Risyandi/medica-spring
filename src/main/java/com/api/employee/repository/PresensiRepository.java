package com.api.employee.repository;

import com.api.employee.entity.Absensi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.employee.entity.Presensi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresensiRepository extends CrudRepository<Presensi, Long> {

    List<Absensi> findByUserIdAndTglAbsensiBetweenOrderByTglAbsensiAsc(
            String userId, Integer tglAwal, Integer tglAkhir
    );

    List<Absensi> findByTglAbsensiBetweenOrderByTglAbsensiAsc(
            Integer tglAwal, Integer tglAkhir
    );

    Optional<Absensi> findTopByUserIdAndTglAbsensiAndJamKeluarIsNullOrderByCreatedAtDesc(
            String userId, Integer tglAbsensi
    );

    Optional<Absensi> findTopByUserIdOrderByTglAbsensiDesc(String userId);

    List<Presensi> findByTglAbsensiBetween(Integer tglAwal, Integer tglAkhir);

    List<Presensi> findByIdUserAndTglAbsensiBetween(String idUser, Integer tglAwal, Integer tglAkhir);

    Optional<Presensi> findTodayCheckIn(String idUser, Integer todayDate);

    Optional<Presensi> findByIdUserAndTglAbsensi(String idUser, Integer tglAbsensi);
}