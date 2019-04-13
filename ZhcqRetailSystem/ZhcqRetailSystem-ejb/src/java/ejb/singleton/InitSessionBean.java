
package ejb.singleton;

import ejb.stateless.CategoryControllerLocal;
import ejb.stateless.MemberControllerLocal;
import ejb.stateless.ProductControllerLocal;
import ejb.stateless.ProductTagControllerLocal;
import ejb.stateless.PromotionControllerLocal;
import ejb.stateless.StaffControllerLocal;
import entity.Category;
import entity.Member;
import entity.ProductEntity;
import entity.ProductTag;
import entity.Promotion;
import entity.Staff;
import entity.WishList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.ColourEnum;
import util.enumeration.SizeEnum;
import util.exception.StaffNotFoundException;


@Singleton
@LocalBean
@Startup
public class InitSessionBean {

    @EJB
    private MemberControllerLocal memberControllerLocal;

    @EJB(name = "PromotionControllerLocal")
    private PromotionControllerLocal promotionControllerLocal;

    @EJB(name = "ProductTagControllerLocal")
    private ProductTagControllerLocal productTagControllerLocal;

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;

    @EJB(name = "StaffControllerLocal")
    private StaffControllerLocal staffControllerLocal;
    
    
    
    

    public InitSessionBean() {
    }

   @PostConstruct
   public void postConstruct()
   {
       try{
           staffControllerLocal.retrieveStaffByUsername("staff");
       } catch(StaffNotFoundException ex) {
           initializeData();
       }
   }
   
   private void initializeData()
   {
        try {
            staffControllerLocal.createNewStaff(new Staff("Admin", "Staff", "staff", "password"));
            memberControllerLocal.createNewMember(new Member("Member", "Default", "member", "password", 0));
            
            Category dresses = categoryControllerLocal.createNewCategoryEntity(new Category("DRESSES","DRESSES"));
            Category tops = categoryControllerLocal.createNewCategoryEntity(new Category("TOPS","TOPS"));
            Category skirts = categoryControllerLocal.createNewCategoryEntity(new Category("SKIRTS","SKIRTS"));
            Category bottoms = categoryControllerLocal.createNewCategoryEntity(new Category("BOTTOMS","BOTTOMS"));
            
            ProductTag tagPopular = productTagControllerLocal.createNewProductTag(new ProductTag("POPULAR"));
            ProductTag tagPolkaDots = productTagControllerLocal.createNewProductTag(new ProductTag("POLKADOTS"));
            ProductTag tagFloral = productTagControllerLocal.createNewProductTag(new ProductTag("FLORAL"));
            ProductTag tagDenim = productTagControllerLocal.createNewProductTag(new ProductTag("DENIM"));
            ProductTag tagStripes = productTagControllerLocal.createNewProductTag(new ProductTag("STRIPES"));
            ProductTag tagPrints = productTagControllerLocal.createNewProductTag(new ProductTag("PRINTS"));
            ProductTag tagCheckered = productTagControllerLocal.createNewProductTag(new ProductTag("CHECKERED"));


            
            List<Long> tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            tagIds.add(tagFloral.getProductTagId());
            ProductEntity product1 = productControllerLocal.createNewProduct(new ProductEntity("Bardot Top", "100% Cotton", BigDecimal.valueOf(28.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/bardot_top_black.jpg"), tops.getCategoryId() , tagIds);
            ProductEntity product2 = productControllerLocal.createNewProduct(new ProductEntity("Bardot Top", "100% Cotton", BigDecimal.valueOf(28.00), 20, SizeEnum.S, ColourEnum.CREAM,"images/bardot_top_cream.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Bardot Top", "100% Cotton", BigDecimal.valueOf(28.00), 20, SizeEnum.S, ColourEnum.PINK,"images/bardot_top_pink.jpg"), tops.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagPolkaDots.getProductTagId());
            tagIds.add(tagPopular.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Calista Dress", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.S, ColourEnum.BLUE,"images/calista_dress_blue.jpg"), dresses.getCategoryId(),tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Calista Dress", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.S, ColourEnum.WHITE,"images/calista_dress_white.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Calista Dress", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.M, ColourEnum.BLUE,"images/calista_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Calista Dress", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.M, ColourEnum.WHITE,"images/calista_dress_white.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Calista Dress", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.L, ColourEnum.BLUE,"images/calista_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Calista Dress", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/calista_dress_white.jpg"), dresses.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagStripes.getProductTagId());
            tagIds.add(tagPrints.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/canyon_top_black.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.S, ColourEnum.GREEN,"images/canyon_top_green.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.S, ColourEnum.PINK,"images/canyon_top_pink.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.S, ColourEnum.WHITE,"images/canyon_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.M, ColourEnum.BLACK,"images/canyon_top_black.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.M, ColourEnum.GREEN,"images/canyon_top_green.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.M, ColourEnum.PINK,"images/canyon_top_pink.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.M, ColourEnum.WHITE,"images/canyon_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.L, ColourEnum.BLACK,"images/canyon_top_black.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.L, ColourEnum.GREEN,"images/canyon_top_green.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.L, ColourEnum.PINK,"images/canyon_top_pink.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Canyon Top", "100% Cotton", BigDecimal.valueOf(30.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/canyon_top_white.jpg"), tops.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            tagIds.add(tagPolkaDots.getProductTagId());
            
            productControllerLocal.createNewProduct(new ProductEntity("Capulet Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.S, ColourEnum.WHITE,"images/capulet_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cherry Skirt", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.S, ColourEnum.BLUE,"images/cherry_skirt_blue.jpg"),  skirts.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cherry Skirt", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.S, ColourEnum.WHITE,"images/cherry_skirt_white.jpg"), skirts.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Capulet Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.M, ColourEnum.WHITE,"images/capulet_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cherry Skirt", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.M, ColourEnum.BLUE,"images/cherry_skirt_blue.jpg"),  skirts.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cherry Skirt", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.M, ColourEnum.WHITE,"images/cherry_skirt_white.jpg"), skirts.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Capulet Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/capulet_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cherry Skirt", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.L, ColourEnum.BLUE,"images/cherry_skirt_blue.jpg"),  skirts.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cherry Skirt", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/cherry_skirt_white.jpg"), skirts.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagDenim.getProductTagId());
            tagIds.add(tagPopular.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/christina_dress_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.S, ColourEnum.BLUE,"images/christina_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.S, ColourEnum.YELLOW,"images/christina_dress_yellow.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.M, ColourEnum.BLACK,"images/christina_dress_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.M, ColourEnum.BLUE,"images/christina_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.M, ColourEnum.YELLOW,"images/christina_dress_yellow.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.L, ColourEnum.BLACK,"images/christina_dress_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.L, ColourEnum.BLUE,"images/christina_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christina Dress", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.L, ColourEnum.YELLOW,"images/christina_dress_yellow.jpg"), dresses.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagFloral.getProductTagId());
            tagIds.add(tagPrints.getProductTagId());
            
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/christine_dress_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.S, ColourEnum.PINK,"images/christine_dress_pink.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.S, ColourEnum.RED,"images/christine_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.M, ColourEnum.BLACK,"images/christine_dress_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.M, ColourEnum.PINK,"images/christine_dress_pink.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.M, ColourEnum.RED,"images/christine_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.L, ColourEnum.BLACK,"images/christine_dress_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.L, ColourEnum.PINK,"images/christine_dress_pink.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Christine Dress", "100% Cotton", BigDecimal.valueOf(65.00), 20, SizeEnum.L, ColourEnum.RED,"images/christine_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.S, ColourEnum.BLUE,"images/crimini_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.S, ColourEnum.RED,"images/crimini_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.S, ColourEnum.WHITE,"images/crimini_dress_white.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.M, ColourEnum.BLUE,"images/crimini_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.M, ColourEnum.RED,"images/crimini_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.M, ColourEnum.WHITE,"images/crimini_dress_white.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.L, ColourEnum.BLUE,"images/crimini_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.L, ColourEnum.RED,"images/crimini_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Crimini Dress", "100% Cotton", BigDecimal.valueOf(89.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/crimini_dress_white.jpg"), dresses.getCategoryId() ,tagIds);
           
            tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            tagIds.add(tagStripes.getProductTagId());
            tagIds.add(tagPolkaDots.getProductTagId());
            
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.S, ColourEnum.BLUE,"images/cybill_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.S, ColourEnum.PINK,"images/cybill_dress_pink.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.S, ColourEnum.RED,"images/cybill_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.M, ColourEnum.BLUE,"images/cybill_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.M, ColourEnum.PINK,"images/cybill_dress_pink.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.M, ColourEnum.RED,"images/cybill_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.L, ColourEnum.BLUE,"images/cybill_dress_blue.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.L, ColourEnum.PINK,"images/cybill_dress_pink.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cybill Dress", "100% Cotton", BigDecimal.valueOf(87.00), 20, SizeEnum.L, ColourEnum.RED,"images/cybill_dress_red.jpg"), dresses.getCategoryId() ,tagIds);
            
            tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/cynthia_jeans_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.S, ColourEnum.BLUE,"images/cynthia_jeans_blue.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.S, ColourEnum.CREAM,"images/cynthia_jeans_cream.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.M, ColourEnum.BLACK,"images/cynthia_jeans_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.M, ColourEnum.BLUE,"images/cynthia_jeans_blue.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.M, ColourEnum.CREAM,"images/cynthia_jeans_cream.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.L, ColourEnum.BLACK,"images/cynthia_jeans_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.L, ColourEnum.BLUE,"images/cynthia_jeans_blue.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Cynthia Jeans", "100% Cotton", BigDecimal.valueOf(100.00), 20, SizeEnum.L, ColourEnum.CREAM,"images/cynthia_jeans_cream.jpg"), bottoms.getCategoryId() ,tagIds);
            
            
            tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Della Jumpsuit", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/della_jumpsuit_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Della Jumpsuit", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.S, ColourEnum.RED,"images/della_jumpsuit_red.jpg"), dresses.getCategoryId() , tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Della Jumpsuit", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.M, ColourEnum.BLACK,"images/della_jumpsuit_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Della Jumpsuit", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.M, ColourEnum.RED,"images/della_jumpsuit_red.jpg"), dresses.getCategoryId() , tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Della Jumpsuit", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.L, ColourEnum.BLACK,"images/della_jumpsuit_black.jpg"), dresses.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Della Jumpsuit", "100% Cotton", BigDecimal.valueOf(80.00), 20, SizeEnum.L, ColourEnum.RED,"images/della_jumpsuit_red.jpg"), dresses.getCategoryId() , tagIds);
            
            
            tagIds = new ArrayList<>();
            tagIds.add(tagPolkaDots.getProductTagId());
            tagIds.add(tagPopular.getProductTagId());
            
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.XS, ColourEnum.BLACK,"images/dixie_shorts_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.XS, ColourEnum.WHITE,"images/dixie_shorts_white.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.XS, ColourEnum.BLACK,"images/dixie_shorts_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/dixie_shorts_white.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.L, ColourEnum.BLACK,"images/dixie_shorts_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.L, ColourEnum.WHITE,"images/dixie_shorts_white.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.XL, ColourEnum.BLACK,"images/dixie_shorts_black.jpg"), bottoms.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Dixie Shorts", "100% Cotton", BigDecimal.valueOf(60.00), 20, SizeEnum.XL, ColourEnum.WHITE,"images/dixie_shorts_white.jpg"), bottoms.getCategoryId() ,tagIds);
           
            
            tagIds = new ArrayList<>();
            tagIds.add(tagCheckered.getProductTagId());
            tagIds.add(tagStripes.getProductTagId());
            productControllerLocal.createNewProduct(new ProductEntity("Eliana Top", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.S, ColourEnum.WHITE,"images/eliana_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Emily Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.S, ColourEnum.BLACK,"images/emily_top_black.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Emily Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.S, ColourEnum.CREAM,"images/emily_top_cream.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Eliana Top", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.XXS, ColourEnum.WHITE,"images/eliana_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Emily Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.XXS, ColourEnum.BLACK,"images/emily_top_black.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Emily Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.XXS, ColourEnum.CREAM,"images/emily_top_cream.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Eliana Top", "100% Cotton", BigDecimal.valueOf(55.00), 20, SizeEnum.XXL, ColourEnum.WHITE,"images/eliana_top_white.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Emily Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.XXL, ColourEnum.BLACK,"images/emily_top_black.jpg"), tops.getCategoryId() ,tagIds);
            productControllerLocal.createNewProduct(new ProductEntity("Emily Top", "100% Cotton", BigDecimal.valueOf(50.00), 20, SizeEnum.XXL, ColourEnum.CREAM,"images/emily_top_cream.jpg"), tops.getCategoryId() ,tagIds);
            
            Promotion promotion = new Promotion("Promotion1", new BigDecimal(0.25), new Date(), new Date(119,11, 5));
            List<Long> productIds = new ArrayList<Long>();
            productIds.add(product1.getProductId());
            productIds.add(product2.getProductId());
            promotionControllerLocal.createNewPromotion(promotion, productIds);
            
            
            Member member = new Member("member", "1", "member1", "password", 0);
            memberControllerLocal.createNewMember(member);
            Member member2 = new Member("member", "2", "member2", "password", 0);
            memberControllerLocal.createNewMember(member2);
            Member member3 = new Member("member", "3", "member3", "password", 0);
            memberControllerLocal.createNewMember(member3);
            
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
   
        }
   }
   
   
   
}
