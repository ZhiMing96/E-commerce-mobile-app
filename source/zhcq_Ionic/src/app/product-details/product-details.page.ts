import { Component, OnInit } from '@angular/core';

import {AlertController, ModalController} from '@ionic/angular';
import { ProductService } from '../services/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductEntity } from '../entities/product';
import { Storage } from '@ionic/storage';
import { Member } from '../entities/member';
import { ShoppingCart } from '../entities/cart';
import { ShoppingCartService } from '../services/shoppingcart.service';
import { WishListService } from '../services/wishlist.service';
import { SizeguidePage } from '../sizeguide/sizeguide.page';
import { ignoreElements } from 'rxjs/operators';
import { Category } from '../entities/category';


@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.page.html',
  styleUrls: ['./product-details.page.scss'],
})
export class ProductDetailsPage implements OnInit {

  errorMessage = '';
  id: number;
  selectedProduct: ProductEntity;
  diffColours: ProductEntity[];
  diffSizes: ProductEntity[];
  suggestedProducts: ProductEntity[];
  onPromotion: boolean;
  promotionalPrice: any;
  isLogin: boolean;

  // Info Passed into modal
  selectedCategoryName: string;

  sizeXS: boolean;
  sizeS: boolean;
  sizeM: boolean;
  sizeL: boolean;
  sizeXL: boolean;
  currentSizeXS: boolean;
  currentSizeS: boolean;
  currentSizeM: boolean;
  currentSizeL: boolean;
  currentSizeXL: boolean;


  sizeXSId: number;
  sizeSId: number;
  sizeMId: number;
  sizeLId: number;
  sizeXLId: number;

  // For adding into cart
  member: Member;
  cart = new ShoppingCart();
  cartId: number ;
  quantity: number;

  sliderOpts = {
    zoom: false,
    slidesPerView: 1.5,
    spaceBetween: true
  };

  constructor(private productService: ProductService,
    private modalController: ModalController,
    private activatedRoute: ActivatedRoute,
    private alertController: AlertController,
    private storage: Storage,
    private shoppingCartService: ShoppingCartService,
    private wishListService: WishListService,
    private router: Router) {

      this.onPromotion = false;
      console.log("Promotion Status: " + this.onPromotion);
      this.selectedProduct = new ProductEntity();

      storage.get('currentCustomer').then((data) => {
        this.member = data;
        if (this.member != null && this.member !== undefined) {
          this.shoppingCartService.retrieveShoppingCart(this.member.memberId).subscribe(
            response => {
              this.cart = response.userShoppingCart;
              if (this.cart !== null) {
                this.cartId = this.cart.cartId;
                console.log('cartId = ' + this.cartId);
              }
          });
        }
      });
      storage.get('isLogin').then((data) => {
        this.isLogin = data;
        console.log('lOGIN Status: ' + this.isLogin );
        console.log('Member: ' + this.member);
        // if (this.isLogin) {
        //   this.cartId = this.member.shoppingCart.cartId;
        //   console.log("cartID = " + this.cartId);
        // }
      });
      this.quantity = 1;

     }


  ngOnInit() {
    this.id = parseInt(this.activatedRoute.snapshot.paramMap.get('id'));
    console.log('Selected productid = ' + this.id);

    if (!isNaN(this.id)) {
      console.log('Entered method 1');
      this.productService.retrieveProductById(this.id).subscribe(
        response => {
          console.log('Entered method 2');
          console.log('response = ' + response);
          this.selectedProduct = response.selectedProduct;
          this.selectedCategoryName = this.selectedProduct.productCategory.categoryName;
          this.diffColours = response.diffColours;
          this.diffSizes = response.diffSizes;
          this.suggestedProducts = response.suggestedProducts;

          if (this.selectedProduct.sizeEnum.toString() === 'XS') {
            this.currentSizeXS = true;
            this.sizeXSId = this.selectedProduct.productId;
          }
          if (this.selectedProduct.sizeEnum.toString() === 'S') {
            this.currentSizeS = true;
            this.sizeSId = this.selectedProduct.productId;
          }
          if (this.selectedProduct.sizeEnum.toString() === 'M') {
            this.currentSizeM = true;
            this.sizeMId = this.selectedProduct.productId;
          }
          if (this.selectedProduct.sizeEnum.toString() === 'L') {
            this.currentSizeL = true;
            this.sizeLId = this.selectedProduct.productId;
          }
          if (this.selectedProduct.sizeEnum.toString() === 'XL') {
            this.currentSizeXL = true;
            this.sizeXLId = this.selectedProduct.productId;
          }

          // Check for promotion
          if (this.selectedProduct.promotion != null) {
            this.onPromotion = true;
            this.promotionalPrice = ((1 - this.selectedProduct.promotion.discountRate) * this.selectedProduct.unitPrice);
            console.log('Product Price is: ' + this.selectedProduct.unitPrice);
            console.log('Promotional rate is: ' + this.selectedProduct.promotion.discountRate);
            console.log('Calculated Promotional Price is: ' + this.promotionalPrice);
          } else {
            this.onPromotion = false;
          }

          if (this.diffSizes.length !== 0) {
            for (let product of this.diffSizes) {
              if (product.sizeEnum.toString() === 'XS') {
                this.sizeXS = true;
                this.sizeXSId = product.productId;
              }
              if (product.sizeEnum.toString() === 'S') {
                this.sizeS = true;
                this.sizeSId = product.productId;
              }
              if (product.sizeEnum.toString() === 'M') {
                this.sizeM = true;
                this.sizeMId = product.productId;
              }
              if (product.sizeEnum.toString() === 'L') {
                this.sizeL = true;
                this.sizeLId = product.productId;
              }
              if (product.sizeEnum.toString() === 'XL') {
                this.sizeXL = true;
                this.sizeXLId = product.productId;
              }
            }
          }
        },
        error => {
          this.presentAlert(error.substring(37));
        }
      );
    } else {
      console.log('Entered method 3');
      this.errorMessage = 'Product Not Found';
      this.presentAlert(this.errorMessage);
      this.router.navigate(['categories']);
    }
  }

  async addToWishList() {

    const listSuccess = await this.alertController.create({
      header: 'added to wish list!'
    });
    if (this.isLogin) {
      console.log('Entered into add to wishlist method');

      this.wishListService.addToWishList(this.member.memberId, this.id).subscribe(
        response => {
          console.log('response = ' + response);
          listSuccess.present();
      },
      error => {
        this.presentAlert(error.substring(37));
        // this.ngOnInit();
      }
      );
    } else {
      this.presentAlert('Please Login!');
    }
  }

  async addToCart() {

    const cartAlert = await this.alertController.create({
      header: 'Added to Bag!',
      buttons: [
        {
          text: 'View Cart',
          cssClass: 'secondary',
          handler: () => {
            this.router.navigate(['shoppingcart']);
          }
        }, {
          text: 'Continue Shopping',
          cssClass: 'secondary',
          handler: () => {
          }
        }

      ]
    });

    if (this.isLogin) {
      if (this.quantity <= this.selectedProduct.quantityOnHand) {
        if (this.quantity === 0) {
          this.presentAlert('Please Enter A Quantity!');
        } else {
          this.shoppingCartService.addToCart(this.cartId, this.id, this.quantity).subscribe(response => {
            console.log('response = ' + response);
            cartAlert.present();
            },
            error => {
            this.presentAlert('ERROR FROM ADDING TO CART: ' + error.substring(37));
            // this.ngOnInit();
            }
          );
        }
      } else {
        this.presentAlert('OUT OF STOCK!');
      }
    } else {
        this.presentAlert('Please Login!');
    }
  }

  increment() {
    if (this.quantity === this.selectedProduct.quantityOnHand) {
      this.presentAlert('Oops! No Available Pieces Left, that\'s the last one!');
    } else {
      this.quantity++;
    }
  }

  decrement(product: ProductEntity) {

    if (this.quantity === 0) {
      this.presentAlert('Quantity cannot be < 0');
    } else {
      this.quantity--;
    }
  }

  async presentAlert(message: string) {
    const alert = await this.alertController.create({
      header: message,
      buttons: ['OK']
    });
    await alert.present();
  }


  async openSizeGuide() {
    const modal = await this.modalController.create({
      component: SizeguidePage,
      componentProps: { value: this.selectedCategoryName }

    });
    modal.present();
  }

}
