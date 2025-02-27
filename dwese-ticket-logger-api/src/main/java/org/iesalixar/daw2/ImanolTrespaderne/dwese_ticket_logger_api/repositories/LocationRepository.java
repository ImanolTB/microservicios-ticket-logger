package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories;


import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT COUNT(l) > 0 FROM Location l WHERE l.city = :city AND l.id != :id")
    boolean existsLocationByCityAndNotId(@Param("city") String city, @Param("id") Long id);
    boolean existsLocationByAddressAndCity(String address, String city) ;
    @Query("SELECT COUNT(l) > 0 FROM Location l WHERE l.address = :address AND l.id != :id")
    boolean existsLocationByAddressAndNotId(@Param("address") String address, @Param("id") Long id);

}
