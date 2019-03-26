/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
public class CoordinatedOutfit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coordinatedOutfitId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date dateCreated;
    
    @OneToMany(mappedBy = "coordinatedOutfit")
    private List<ProductEntity> productEntities;

    public CoordinatedOutfit() {
        productEntities = new ArrayList<ProductEntity>();
    }

    public CoordinatedOutfit(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    

    public Long getCoordinatedOutfitId() {
        return coordinatedOutfitId;
    }

    public void setCoordinatedOutfitId(Long coordinatedOutfitId) {
        this.coordinatedOutfitId = coordinatedOutfitId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


}
