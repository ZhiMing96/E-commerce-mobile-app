/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Member;
import entity.ProductEntity;
import entity.Promotion;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeletePromotionException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.PromotionExistException;
import util.exception.PromotionNotFoundException;
import util.exception.UpdatePromotionException;


@Stateless
@Local(PromotionControllerLocal.class)
public class PromotionController implements PromotionControllerLocal {

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;

    @PersistenceContext(unitName = "ZhcqRetailSystem-ejbPU")
    private EntityManager em;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @Override
    public List<Promotion> retrieveAllPromotions() {
        Query query = em.createQuery("SELECT p FROM Promotion p");
        return query.getResultList();
    }
    
    @Override
    public Promotion retrievePromotionByPromotionId(Long promotionId) throws PromotionNotFoundException
    {
        if(promotionId == null)
        {
            throw new PromotionNotFoundException("Promotion ID not provided");
        }
        
        Promotion promotion = em.find(Promotion.class, promotionId);
        
        if(promotion != null)
        {
            return promotion;
        }
        else
        {
            throw new PromotionNotFoundException("Promotion ID " + promotionId + " does not exist!");
        }               
    }
     
    public PromotionController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Promotion createNewPromotion(Promotion newPromotion, List<Long> productIds) throws ProductNotFoundException, PromotionExistException, InputDataValidationException{
        Set<ConstraintViolation<Promotion>> constraintViolations = validator.validate(newPromotion);
        
        if(constraintViolations.isEmpty()){
            em.persist(newPromotion);
            
            if(productIds != null && (!productIds.isEmpty()))
            {
                for(Long productId: productIds)
                {
                    ProductEntity product = productControllerLocal.retrieveProductById(productId);
                    newPromotion.addProduct(product);
                }
            } 
            em.flush();
          
            return newPromotion;
        } else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public void updatePromotion(Promotion promotion, List<Long> productIds) throws InputDataValidationException, PromotionNotFoundException,UpdatePromotionException,ProductNotFoundException,PromotionExistException 
    {
        Set<ConstraintViolation<Promotion>>constraintViolations = validator.validate(promotion);
        
        if(constraintViolations.isEmpty()){
            if(promotion.getPromotionId() != null)
            {
                Promotion promotionToUpdate = retrievePromotionByPromotionId(promotion.getPromotionId());
                
                if(promotionToUpdate.getPromotionName() == promotion.getPromotionName()){
                    if(productIds != null)
                    {
                        for(ProductEntity product: promotionToUpdate.getPromotionalProducts())
                        {
                            product.setPromotion(null);
                        }
                        promotionToUpdate.getPromotionalProducts().clear();
                        
                        for(Long id : productIds)
                        {
                            ProductEntity productEntity = productControllerLocal.retrieveProductById(id);
                            promotionToUpdate.addProduct(productEntity);
                        }
                    } 
                    
                } else {
                    throw new UpdatePromotionException("Promotion ID does not match the record in the Database!");
                }
            } else {
                throw new PromotionNotFoundException("No records of this promotion in the database!");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Promotion>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    public void deletePromotion(Long promotionId) throws PromotionNotFoundException
    {
        Promotion promotionToRemove = retrievePromotionByPromotionId(promotionId);
            
        for(ProductEntity product:promotionToRemove.getPromotionalProducts())
        {
            product.setPromotion(null);
        }

        em.remove(promotionToRemove);
                      
    }
    
    
    
    
    
    
}
