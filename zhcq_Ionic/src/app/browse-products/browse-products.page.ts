import { Component, OnInit } from '@angular/core';
import { NavController, NavParams } from '@ionic/angular';
import { ProductEntity } from '../entities/product';
import { ProductService } from '../services/product.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-browse-products',
  templateUrl: './browse-products.page.html',
  styleUrls: ['./browse-products.page.scss'],
})
export class BrowseProductsPage implements OnInit {
  errorMessage: string;
  products: ProductEntity[];
  catId: number;
 
  // images: Array<string>;  
  // grid: Array<Array<string>>;

  constructor(private productService: ProductService, 
              private activatedRoute: ActivatedRoute) {
    // this.images = this.navParams.get('images'); //get product image URIs
    // this.grid = Array(Math.ceil(this.images.length/2)); 
  }
  ngOnInit() {
    this.catId = parseInt(this.activatedRoute.snapshot.paramMap.get('catId'));
    console.log("CATEGORY ID IS: "+this.catId);

    if(!isNaN(this.catId))
		{
      this.productService.retrieveProductByCat(this.catId).subscribe(
        response => {
          console.log(response);
          this.products = response.products 
          // imagePath = "../ZhcqRetailSystem/ZhcqRetailSystem-war/build/web/systemAdministration/images/canyon_top_black.jpg";
        },
        error => {
          this.errorMessage = error 
        }
      );
    } 
    else 
    {
      this.productService.retrieveAllProducts().subscribe(
        response => {
          console.log(response);
          this.products = response.products 
        },
        error => {
          this.errorMessage = error 
        }
      );
    }
  }

  // ionViewLoaded() {

  //   let rowNum = 0; //counter to iterate over the rows in the grid
  
  //   for (let i = 0; i < this.images.length; i+=2) { //iterate images
  
  //     this.grid[rowNum] = Array(2); //declare two elements per row
  
  //     if (this.images[i]) { //check file URI exists
  //       this.grid[rowNum][0] = this.images[i] //insert image
  //     }
  
  //     if (this.images[i+1]) { //repeat for the second image
  //       this.grid[rowNum][1] = this.images[i+1]
  //     }
  
  //     rowNum++; //go on to the next row
  //   }
  
  // }

  // openProductPage(product){
  //   this.navCtrl.push('ProductDetails', {"product": product} );
  // }

}
