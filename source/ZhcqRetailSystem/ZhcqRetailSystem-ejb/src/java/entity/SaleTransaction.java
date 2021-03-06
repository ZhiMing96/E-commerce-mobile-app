/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author chengyang
 */
@Entity
public class SaleTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionId;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;
    
    @OneToMany(mappedBy="saleTransaction")
    private List<SaleTransactionLineItem> saleTransactionLineItems;


    
    private BigDecimal totalPrice;
    

    public SaleTransaction() {
        saleTransactionLineItems = new ArrayList<SaleTransactionLineItem>();
        totalPrice = BigDecimal.ZERO;
    }
    
    public SaleTransaction(Member member){
        this();
        this.member = member;
    }

    public void addToTotalPrice(BigDecimal subtotal){
        totalPrice = totalPrice.add(subtotal);
    }
        
    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
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
     * @return the saleTransactionLineItems
     */
    public List<SaleTransactionLineItem> getSaleTransactionLineItems() {
        return saleTransactionLineItems;
    }

    /**
     * @param saleTransactionLineItems the saleTransactionLineItems to set
     */
    public void setSaleTransactionLineItems(List<SaleTransactionLineItem> saleTransactionLineItems) {
        this.saleTransactionLineItems = saleTransactionLineItems;
    }
    
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
