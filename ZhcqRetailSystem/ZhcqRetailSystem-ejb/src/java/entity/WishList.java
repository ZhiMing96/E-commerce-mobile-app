/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author chengyang
 */
@Entity
public class WishList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishListId;
    
    @OneToOne(mappedBy="wishList")
    private Member member;
    
    @OneToMany(mappedBy = "wishList")
    private List<ProductEntity> productEntities;
    
    public WishList() {
        productEntities = new ArrayList<ProductEntity>();
    }

    public Long getWishListId() {
        return wishListId;
    }

    public void setWishListId(Long wishListId) {
        this.wishListId = wishListId;
    } 

    /**
     * @return the member
     */
    public Member getMember() {
        return member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * @return the productEntities
     */
    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    /**
     * @param productEntities the productEntities to set
     */
    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
    
}