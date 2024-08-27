package com.hun.market.item.repository;

import com.hun.market.item.domain.Item;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    Optional<Item> findById(Long id);

    void save(Item item);

//    @Lock(LockModeType.PESSIMISTIC_READ)
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT i FROM Item i WHERE i.id IN :ids")
    List<Item> findAllById(@Param("ids") List<Long> ids);

    Page<Item> findByItemNameContaining(String query, Pageable pageable);

    List<Item> findAllByOrderById();

    void delete(Item item);

    void deleteAll();
}
