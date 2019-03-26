
package ejb.stateless;

import entity.Member;
import entity.ProductEntity;
import entity.SaleTransaction;
import entity.SaleTransactionLineItem;
import entity.ShoppingCart;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.SaleTransactionNotFoundException;
import util.exception.ShoppingCartNotFoundException;


@Stateless
@Local(CheckoutControllerLocal.class)
public class CheckoutController implements CheckoutControllerLocal {

    @EJB
    private ProductControllerLocal productController;

    @PersistenceContext(unitName = "ZhcqRetailSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    
    public CheckoutController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public ShoppingCart retrieveShoppingCartById(Long cartId) throws ShoppingCartNotFoundException {
        ShoppingCart shoppingCart = em.find(ShoppingCart.class, cartId);

        if (shoppingCart != null) {
           
            return shoppingCart;
        } else {
            throw new ShoppingCartNotFoundException("Shopping Cart " + cartId + " does not exist!");
        }
    }
    
    @Override
    public SaleTransaction retrieveSaleTransactionById(Long transactionId) throws SaleTransactionNotFoundException {
        SaleTransaction saleTransaction = em.find(SaleTransaction.class, transactionId);

        if (saleTransaction != null) {
           
            return saleTransaction;
        } else {
            throw new SaleTransactionNotFoundException("Sale Transaction " + transactionId + " does not exist!");
        }
    }
    
    @Override
    public void updateCart (Long cartId, Long productId, boolean addition) { //true if adding, false if deleting
        try {
            ShoppingCart shoppingCart = retrieveShoppingCartById(cartId);
            ProductEntity prod = productController.retrieveProductById(productId);
            
            if (prod.getQuantityOnHand()<=0) {
                System.out.println("Oops! Product out of stock!");
            }
            
            if (addition) {
                shoppingCart.getProducts().add(prod);
                prod.setQuantityOnHand(prod.getQuantityOnHand()-1);
            } else {
                shoppingCart.getProducts().remove(prod);
                prod.setQuantityOnHand(prod.getQuantityOnHand()+1);
            }
        } catch (ShoppingCartNotFoundException | ProductNotFoundException ex) {
            
        }
      
    }
    
    @Override
    public SaleTransaction checkOut (Long cartId) throws ShoppingCartNotFoundException  {
        
        
        ShoppingCart shoppingCart = retrieveShoppingCartById(cartId);
        SaleTransaction transaction = new SaleTransaction(shoppingCart.getMember());

        List <SaleTransactionLineItem> list = new ArrayList <> ();
        for (ProductEntity product : shoppingCart.getProducts()) {
            SaleTransactionLineItem lineItem = new SaleTransactionLineItem (transaction, product);
            list.add(lineItem);
            em.persist(lineItem);
        }
        transaction.setSaleTransactionLineItems(list);
        em.persist(transaction);
        return transaction;
          
        
    }
}
