/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ProductTag;
import javax.ejb.Local;
import util.exception.ProductTagNotFoundException;

/**
 *
 * @author zhimingkoh
 */
@Local
public interface ProductTagControllerLocal {

    public ProductTag retrieveTagByTagId(Long tagId) throws ProductTagNotFoundException;
    
}
