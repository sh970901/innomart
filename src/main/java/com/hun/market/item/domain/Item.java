package com.hun.market.item.domain;

import com.hun.market.backoffice.dto.ItemModifyDto;
import com.hun.market.base.entity.BaseEntity;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.exception.ItemStockException;
import com.hun.market.order.order.domain.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="item_name", nullable = false, length = 1000)
    private String itemName;

    @Column(name="item_stock", nullable = false, length = 1000)
    private Long itemStock;

    @Column(name="item_price", nullable = false, length = 1000)
    private Long itemPrice;

    //상품 구매 링크
//    /*@Column(name="url", nullable = false, length = 1000)
//    private String url;*/

    //이미지경로(S3)
    @Column(name="image_path", nullable = false, length = 1000)
    private String imagePath;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name="item_description", length = 1000)
    private String description;

    @Version
    private Long version;

    @Builder
    public Item(String itemName, Long itemStock, Long itemPrice, String imagePath, String description) {
        this.itemName = itemName;
        this.itemStock = itemStock;
        this.itemPrice = itemPrice;
        this.imagePath = imagePath;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    public static Item from(ItemDto.ItemCreateRequestDto itemDto){
        return Item.builder()
                   .itemName(itemDto.getItemName())
                   .itemStock(itemDto.getItemStock())
                   .itemPrice(itemDto.getItemPrice())
                   .description(itemDto.getDescription())
                   .imagePath(itemDto.getImagePath())
                   .build();
    }

    public void updateItem(ItemModifyDto itemModifyDto) {
        this.itemName = itemModifyDto.getItemName();
        this.itemPrice = itemModifyDto.getItemPrice();
        this.itemStock = itemModifyDto.getItemStock();
        this.description = itemModifyDto.getDescription();
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public void decreaseStock(int quantity) {
        if ( quantity < 1){
            throw new ItemStockException("상품 처리 중 오류가 발생했습니다.");
        }
        if ( itemStock < 1){
            throw new ItemStockException("재고가 부족합니다. \n"+ itemName + "의 현재 재고는 "+itemStock+ "개 입니다.");
        }
        if (itemStock < quantity) {
            throw new ItemStockException("재고가 부족합니다. \n"+ itemName + "의 현재 재고는 "+itemStock+ "개 입니다.");
        }
        this.itemStock -= quantity;
    }

}
