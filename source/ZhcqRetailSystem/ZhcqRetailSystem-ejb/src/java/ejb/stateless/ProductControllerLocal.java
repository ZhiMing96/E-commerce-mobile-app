/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ProductEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.ProductTagNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateProductException;


@Local
public interface ProductControllerLocal {

    public ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> productTags) throws InputDataValidationException, CreateNewProductException;

    public ProductEntity retrieveProductById(Long id) throws ProductNotFoundException;

    public List<ProductEntity> retrieveAllUniqueProducts();

    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException;

    public List<ProductEntity> searchProductsByName(String searchString);

    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition);

    public void deleteProduct(Long productId) throws ProductNotFoundException;

    public long updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, ProductNotFoundException, CategoryNotFoundException, ProductTagNotFoundException, UpdateProductException;

    public List<ProductEntity> retrieveAllProducts();

    public List<ProductEntity> productsAvailableForOutfit();

    public List<ProductEntity> retrieveDiffColours(Long productId) throws ProductNotFoundException;
    
    public List<ProductEntity> retrieveDiffSizes(Long productId) throws ProductNotFoundException;

    public List<ProductEntity> retrieveProductSuggestions(Long productId) throws ProductNotFoundException;

    public List<ProductEntity> retrieveDistinctNames(List<ProductEntity> allProducts);
    
}
