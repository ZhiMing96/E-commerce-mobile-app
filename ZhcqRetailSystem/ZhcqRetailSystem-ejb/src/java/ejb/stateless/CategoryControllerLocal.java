/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Category;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author zhimingkoh
 */
@Local
public interface CategoryControllerLocal {

    public Category retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    
}
