import { Component, OnInit } from '@angular/core';
import { Storage } from '@ionic/storage';
import { Member } from '../entities/member';
import { ProductEntity } from '../entities/product';
import { ShoppingCart } from '../entities/cart';
import { AlertController } from '@ionic/angular';
import { ShoppingCartService } from '../services/shoppingcart.service';
import { ProductService } from '../services/product.service';
import { decreaseElementDepthCount } from '@angular/core/src/render3/state';
import { queueComponentIndexForCheck } from '@angular/core/src/render3/instructions';
import { log } from 'util';
import { SaleTransaction } from '../entities/saletransaction';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shoppingcart',
  templateUrl: './shoppingcart.page.html',
  styleUrls: ['./shoppingcart.page.scss'],
})
export class ShoppingcartPage implements OnInit {
  errorMessage: string;
  member: Member;
  products: ProductEntity[];
  cart: ShoppingCart;
  quantity: number[];
  subtotal: number[];
  isLogin: boolean;
  transaction: SaleTransaction;

  constructor(private storage: Storage, private alertController: AlertController, private shoppingCartService: ShoppingCartService,
    private router: Router) {
  }

  ngOnInit() {
    this.subtotal = [];
  }

  ionViewWillEnter() {
    this.storage.get('currentCustomer').then((data) => {
      this.member = data;
    });

    this.storage.get('isLogin').then((data) => {
      this.isLogin = data;
      console.log('lOGIN Status: ' + this.isLogin);
      console.log('Member: ' + this.member);
      this.initialiseCart();
    });
  }

  initialiseCart() {
    if (this.isLogin) {
      this.shoppingCartService.retrieveShoppingCart(this.member.memberId).subscribe(
        response => {
          this.cart = response.userShoppingCart;
          this.products = this.cart.products;
          this.quantity = this.cart.quantity;
          for (var i = 0; i < this.products.length; i++) {
            this.subtotal.push(this.products[i].unitPrice * this.quantity[i]);
            console.log('initialized product ' + this.products[i].productName);
            console.log('initialized quantity ' + this.quantity[i]);
            console.log('initialized subtotal ' + this.subtotal[i]);
          }
        },
        error => {
          this.presentAlert(error);
          this.router.navigate(['/home']);
        }
      );
    } else {
      this.presentAlert('You are not logged in!');
      this.router.navigate(['/home']);
    }
  }

  increment(product: ProductEntity) {
    var idx = this.products.indexOf(product);
    this.quantity[idx]++;
    this.subtotal[idx] = this.quantity[idx]*this.products[idx].unitPrice;
  }

  decrement(product: ProductEntity) {
    var idx = this.products.indexOf(product);
    if (this.quantity[idx] === 0) {
      this.presentAlert('Quantity cannot be < 0');
    } else {
      this.quantity[idx]--;
      this.subtotal[idx] = this.quantity[idx]*this.products[idx].unitPrice;
    }
  }

  updateCart() {
    for (var i = 0; i < this.quantity.length; i++) {
      // if(this.quantity[i] <= 0) {
      //   this.quantity[i] = 0;
      //   this.presentAlert("Quantity of " + this.products[i].productName + " must be >= 0");
      // }
      this.shoppingCartService.updateCart(this.cart.cartId, this.products[i].productId, this.quantity[i]).subscribe(
        response => {
          console.log('Successfully updated cart!');
          this.presentAlert('Successfully updated cart!');
        },
        error => {
          this.errorMessage = error;
          this.presentAlert(this.errorMessage.substring(37));
        }
      );
    }
  }

  checkout() {
    this.shoppingCartService.checkout(this.cart.cartId).subscribe(
      response => {
        this.transaction = response.txn;
        console.log('transaction ID =' + this.transaction.saleTransactionId);

        this.presentAlert('Successfully checked out! Sale transaction Id: ' + this.transaction.saleTransactionId);
        this.router.navigate(['/home']);
      },
      error => {
        this.errorMessage = error;
        this.presentAlert(this.errorMessage.substring(37));
      }
    );

  }

  // removeProduct(product: ProductEntity) {
  //   console.log("start");
  //   this.presentAlertConfirm(product);
  // }

  async removeProduct(product: ProductEntity) {
    const alert = await this.alertController.create({
      header: 'Confirm!',
      message: 'Remove Item? <ion-icon ios="ios-sad" md="md-sad"></ion-icon>',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          cssClass: 'secondary',
          handler: () => {
            console.log('Cancelled remove product');
          }
        }, {
          text: 'Confirm',
          handler: () => {
            console.log('attempt to remove product');
            this.shoppingCartService.removeFromCart(this.cart.cartId, product.productId).subscribe(
              response => {
                const index: number = this.products.indexOf(product);
                if (index != -1) {
                  this.products.splice(index, 1);
                  this.quantity.splice(index, 1);
                  this.subtotal.splice(index, 1);
                  console.log('successfully removed product!');
                }
                this.presentAlert('Item removed from bag');
              }, error => {
                this.presentAlert(error);
              }
            );
          }
        }
      ]
    });

    await alert.present();
  }
  // const alert = await this.alertController.create({
  //   header: 'Confirm',
  //   message: 'Remove Item? <ion-icon ios="ios-sad" md="md-sad"></ion-icon>',
  //   buttons: [
  //     {
  //       text: 'Cancel',
  //       role: 'cancel',
  //       cssClass: 'secondary',
  //       handler: () => {
  //         console.log('Cancelled remove Product');
  //       }
  //     }, {
  //       text: 'Confirm',
  //       handler: () => {
  //         console.log('attempt to remove product');
  //         this.shoppingCartService.removeFromCart(this.cart.cartId, product.productId).subscribe(
  //           response => {
  //            const index:number = this.products.indexOf(product);
  //             if(index != -1) {
  //               this.products.splice(index,1);
  //               this.subtotal.splice(index,1);
  //               console.log('successfully removed product!');
  //             }
  //             this.presentAlert('Item removed from bag');
  //           },
  //           error => {
  //             this.presentAlert(error);
  //           }
  //         );
  //       }
  //     }
  //   ]
  // });
  //   }

  async presentAlert(message: string) {
    const alert = await this.alertController.create({
      header: message,
      buttons: ['OK']
    });
    await alert.present();
  }


}
