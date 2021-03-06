/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Category;
import entity.ProductEntity;
import entity.ProductTag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.ColourEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.ProductTagNotFoundException;
import util.exception.UpdateProductException;


@Stateless
public class ProductController implements ProductControllerLocal {

    @PersistenceContext(unitName = "ZhcqRetailSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private ProductTagControllerLocal productTagControllerLocal;

    @EJB
    private CategoryControllerLocal categoryControllerLocal;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ProductController()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public List<ProductEntity> retrieveAllProducts(){
        Query q = em.createQuery("SELECT p FROM ProductEntity p");
        
        List<ProductEntity> allProducts = q.getResultList();
  
        return allProducts;
        
    }
    
    @Override
    public List<ProductEntity> retrieveAllUniqueProducts(){
//        
//        Query p = em.createQuery("SELECT p FROM ProductEntity p WHERE p.productId = 1");
//        ProductEntity product = (ProductEntity) p.getResultList().get(0);
//
//        Query q = em.createQuery("SELECT DISTINCT p FROM ProductEntity p WHERE p.sizeEnum = :size ");
//        q.setParameter("size", product.getSizeEnum());
//
//        List<ProductEntity> allUniqueProducts = q.getResultList();
//  
//        return allUniqueProducts;
            List<ProductEntity> products = retrieveAllProducts();
            List<ProductEntity> unique = new ArrayList<ProductEntity>();
            for(ProductEntity p : products) {
                String productName = p.getProductName();
                ColourEnum colour = p.getColourEnum();
                boolean exists = false;
                for(ProductEntity pe : unique) {
                    if(pe.getColourEnum() == colour && pe.getProductName().equals(productName)) {
                        exists = true;
                        break;
                    }
                }
                if(!exists) {
                    unique.add(p);
                }
            }
            
            return unique;
        
    }
    
    @Override
    public ProductEntity retrieveProductById(Long id) throws ProductNotFoundException{
        ProductEntity product = em.find(ProductEntity.class, id);
        if(product != null) {
            
            product.getProductCategory();
            product.getProductTags().size();
            
            return product;
        } else {
            throw new ProductNotFoundException("Product with id " + id + " not found!");
        }
    }
    
    @Override
    public ProductEntity createNewProduct(ProductEntity newProductEntity,Long categoryId,List<Long> productTagIds) throws InputDataValidationException, CreateNewProductException
    {
        
     Set<ConstraintViolation<ProductEntity>>constraintViolations = validator.validate(newProductEntity);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                if(categoryId == null)
                {
                    throw new CreateNewProductException("The new product must be associated with a category");
                }
                
                Category categoryEntity = categoryControllerLocal.retrieveCategoryByCategoryId(categoryId);
                categoryEntity.addProduct(newProductEntity);
                
                em.persist(newProductEntity);
                newProductEntity.setProductCategory(categoryEntity);
                
                if(productTagIds != null && (!productTagIds.isEmpty()))
                {
                    for(Long tagId:productTagIds)
                    {
                        ProductTag tagEntity = productTagControllerLocal.retrieveProductTagByTagId(tagId);
                        newProductEntity.addTag(tagEntity);
                    }
                }
                
                em.flush();
                
                System.out.println("pic path: " + newProductEntity.getPicturePath());

                return newProductEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewProductException("Product with same SKU code already exist: " + ex.getMessage() );
                }
                else
                {
                    throw new CreateNewProductException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewProductException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
          
    }
    
    //Order by Newest to Oldest
    @Override
    public List<ProductEntity> searchProductsByName(String searchString)
    {
        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.name LIKE :inSearchString ORDER BY p.dateAdded DESC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<ProductEntity> productEntities = query.getResultList();
        
        for(ProductEntity productEntity:productEntities)
        {
            productEntity.getProductCategory();
            productEntity.getProductTags().size();
        }
        
        return productEntities;
    }
    
    @Override
    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException
    {
         
        Category categoryEntity = categoryControllerLocal.retrieveCategoryByCategoryId(categoryId);
        List<ProductEntity> productEntities = categoryEntity.getProductEntities();            
        
         Collections.sort(productEntities, new Comparator<ProductEntity>()
            {
                public int compare(ProductEntity pe1, ProductEntity pe2) {
                    return pe1.getProductId().compareTo(pe2.getProductId()); // NOT SURE BOUT THIS ONE 
                }
            });

        return productEntities;
    }
    
    @Override
    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition){
        List<ProductEntity> productEntities = new ArrayList<>();
        if(tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR")))
        {
            return productEntities;
        } else {
            if(condition.equals("OR"))
            {
                Query query = em.createQuery("SELECT DISTINCT pe FROM ProductEntity pe, IN (pe.productTags) pt WHERE pt.productTagId IN :inTagIds ORDER BY pe.dateAdded DESC");
                query.setParameter("inTagIds", tagIds);
                productEntities = query.getResultList();
                
                for(ProductEntity productEntity:productEntities)
                {
                    productEntity.getProductCategory();
                    productEntity.getProductTags().size();
                }
            } 
            else // AND
            {
                //Halp, i dunno how to edit weekek's code for this one
                
                
                for(ProductEntity productEntity:productEntities)
                {
                    productEntity.getProductCategory();
                    productEntity.getProductTags().size();
                }
            }
            
            Collections.sort(productEntities, new Comparator<ProductEntity>()
            {
                public int compare(ProductEntity pe1, ProductEntity pe2) 
                {
                    return pe1.getProductId().compareTo(pe2.getProductId()); //PLEASE CHECK
                }
            });
            return productEntities;
        }
    }
    
    @Override
     public long updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, ProductNotFoundException, CategoryNotFoundException, ProductTagNotFoundException, UpdateProductException
    {
        Set<ConstraintViolation<ProductEntity>>constraintViolations = validator.validate(productEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(productEntity.getProductId()!= null)
            {
                ProductEntity productEntityToUpdate = retrieveProductById(productEntity.getProductId());
                if(productEntityToUpdate.getProductName().equals(productEntity.getProductName()))
                {
                    if(categoryId != null && (!productEntityToUpdate.getProductCategory().getCategoryId().equals(categoryId)))
                    {
                        Category categoryEntityToUpdate = categoryControllerLocal.retrieveCategoryByCategoryId(categoryId);
                        productEntityToUpdate.setProductCategory(categoryEntityToUpdate);
                    }
                    
                    if(tagIds != null)
                    {
                        for(ProductTag tagEntity:productEntityToUpdate.getProductTags())
                        {
                            tagEntity.getProductEntities().remove(productEntityToUpdate);
                        }
                        
                        productEntityToUpdate.getProductTags().clear();
                        
                        for(Long productTagId:tagIds)
                        {
                            ProductTag tagEntity = productTagControllerLocal.retrieveProductTagByTagId(productTagId);
                            productEntityToUpdate.addTag(tagEntity);
                        }
                    }
                    
                    productEntityToUpdate.setProductName(productEntity.getProductName());
                    productEntityToUpdate.setDescription(productEntity.getDescription());
                    productEntityToUpdate.setUnitPrice(productEntity.getUnitPrice());
                    productEntityToUpdate.setQuantityOnHand(productEntity.getQuantityOnHand());
                    productEntityToUpdate.setSizeEnum(productEntity.getSizeEnum());
                    productEntityToUpdate.setColourEnum(productEntity.getColourEnum());
                    em.flush();
                    
                    return productEntityToUpdate.getProductId();
                
                } else {
                    throw new UpdateProductException("Product ID does not match record in database!");
                }    
            } else {
                throw new ProductNotFoundException("Product ID not provided!");
            }      
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException
    {
        ProductEntity productEntityToRemove = retrieveProductById(productId);
        
        //List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = saleTransactionEntityControllerLocal.retrieveSaleTransactionLineItemsByProductId(productId);
        
        /*if(saleTransactionLineItemEntities.isEmpty())
        {*/
            productEntityToRemove.getProductCategory().getProductEntities().remove(productEntityToRemove);
            
            for(ProductTag tagEntity:productEntityToRemove.getProductTags())
            {
                tagEntity.getProductEntities().remove(productEntityToRemove);
            }
            
            productEntityToRemove.getProductTags().clear();
            
            em.remove(productEntityToRemove);
        /*}
        else
        {
            throw new DeleteProductException("Product ID " + productId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
        }*/
    }
    
    @Override
    public List<ProductEntity> productsAvailableForOutfit(){
        List<ProductEntity> products = retrieveAllUniqueProducts();
        
//        Query query = em.createQuery("SELECT pe FROM ProductEntity pe WHERE pe.coordinatedOutfit IS NULL");
    
//        List<ProductEntity> productEntities = query.getResultList();

        List<ProductEntity> productEntities = new ArrayList<>();
        
        for(ProductEntity pdt : products){
            if(pdt.getCoordinatedOutfit() == null ){
                productEntities.add(pdt);
            }
        }
        
        List<ProductEntity> filteredProducts = retrieveDistinctNames(productEntities);
                
        for(ProductEntity productEntity:filteredProducts)
        {
            productEntity.getProductCategory();
            productEntity.getProductTags().size();
        }
        
        return filteredProducts;
    }
    
    @Override
    public List<ProductEntity> retrieveProductSuggestions(Long productId) throws ProductNotFoundException 
    {
        ProductEntity selectedProduct = retrieveProductById(productId);
        
        if(selectedProduct != null)
        {
            List<ProductTag> tags = selectedProduct.getProductTags();

            

            if(tags != null && !tags.isEmpty())
            {
                Random rand = new Random();
                Query query = em.createQuery("SELECT DISTINCT pe FROM ProductEntity pe WHERE pe.productTags = :tag ");
                query.setParameter("tag", tags.get(rand.nextInt(tags.size())));
        //            query.setParameter("colour", selectedProduct.getColourEnum().values()[rand.nextInt(selectedProduct.getColourEnum().values().length)]);
        //            query.setParameter("size", selectedProduct.getColourEnum().values()[rand.nextInt(selectedProduct.getColourEnum().values().length)]);
                System.out.println("Randomly selected colour is: " + selectedProduct.getColourEnum().values()[rand.nextInt(selectedProduct.getColourEnum().values().length)]);

                List<ProductEntity> allSuggestedProducts = query.getResultList();


                List<ProductEntity> suggestedProducts = retrieveDistinctNames(allSuggestedProducts);

                return suggestedProducts; 
            }
            else 
            {
                return null;
            }
        } else {
            throw new ProductNotFoundException("Error Occured! Product does not exist in the database");
        }
    }
    
    @Override
    public List<ProductEntity> retrieveDiffColours(Long productId) throws ProductNotFoundException
    {
        
        ProductEntity selectedProduct = retrieveProductById(productId);
        if(selectedProduct!= null){
            Query query = em.createQuery("SELECT DISTINCT pe FROM ProductEntity pe WHERE pe.productName = :name AND pe.colourEnum <> :colour AND pe.sizeEnum = :size ");
            query.setParameter("name", selectedProduct.getProductName());
            query.setParameter("colour", selectedProduct.getColourEnum());
            query.setParameter("size", selectedProduct.getSizeEnum());
            List<ProductEntity> diffColours = query.getResultList();
            if (diffColours==null || diffColours.isEmpty()) {
                return null;
            }
            return diffColours;
        } else {
            throw new ProductNotFoundException("Product of ID "+ productId + " Not Found!");
        }
    }
    
    @Override
    public List<ProductEntity> retrieveDiffSizes(Long productId) throws ProductNotFoundException
    {
        
        ProductEntity selectedProduct = retrieveProductById(productId);
        if(selectedProduct!= null){
            System.out.println("Colour : " + selectedProduct.getColourEnum());
            Query query = em.createQuery("SELECT DISTINCT pe FROM ProductEntity pe WHERE pe.productName = :name AND pe.colourEnum = :colour AND pe.sizeEnum <> :size");
            query.setParameter("name", selectedProduct.getProductName());
            query.setParameter("colour", selectedProduct.getColourEnum());
            query.setParameter("size", selectedProduct.getSizeEnum());
            List<ProductEntity> diffSizes = query.getResultList();
            if (diffSizes==null || diffSizes.isEmpty()) {
                return null;
            }
            return diffSizes;
        } else {
            throw new ProductNotFoundException("Product of ID "+ productId + " Not Found!");
        }
    }
    
    @Override
    public List<ProductEntity> retrieveDistinctNames(List<ProductEntity> allProducts) 
    {
        List<ProductEntity> filteredProducts = new ArrayList<>();
        
        for(ProductEntity pdt : allProducts) 
        {
            String name = pdt.getProductName();
                 
            if(filteredProducts == null || filteredProducts.isEmpty()){
                filteredProducts.add(pdt);
            } else {
                List<String> names = new ArrayList<>();
                for(ProductEntity product : filteredProducts) 
                {
                    names.add(product.getProductName());
                }
                
                if(!names.contains(name)){
                    filteredProducts.add(pdt);
                }                
                     
            }
        }
        return filteredProducts;
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ProductEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
}
