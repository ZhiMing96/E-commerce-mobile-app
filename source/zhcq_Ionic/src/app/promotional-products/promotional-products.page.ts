import { Component, OnInit } from '@angular/core';
import { ProductEntity } from '../entities/product';
import { PromotionService } from '../services/promotion.service';
import { Promotion } from '../entities/promotion';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-promotional-products',
  templateUrl: './promotional-products.page.html',
  styleUrls: ['./promotional-products.page.scss'],
})
export class PromotionalProductsPage implements OnInit {

  errorMessage = '';
  public promotions: Promotion[];
  promotionId: number;

  sliderOpts = {
    zoom: false,
    slidesPerView: 2.5,
    spaceBetween: true
  };

  constructor(private promotionService: PromotionService, private alertController: AlertController) { }

  ngOnInit() {
   this.promotionService.retrieveAllPromotions().subscribe(
     response => {
       this.promotions = response.promotions;
     },
     error => {
       this.errorMessage = error;
       this.presentAlert(this.errorMessage.substring(37));
     }
   );
  }


  async presentAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'ERROR: ' + message,
      buttons: ['OK']
    });
    await alert.present();
  }
}
