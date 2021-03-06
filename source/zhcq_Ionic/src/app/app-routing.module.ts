import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadChildren: './home/home.module#HomePageModule'
  },
  { path: 'browse-products', loadChildren: './browse-products/browse-products.module#BrowseProductsPageModule' },
  { path: 'browse-products/:catId', loadChildren: './browse-products/browse-products.module#BrowseProductsPageModule' },
  { path: 'about-us', loadChildren: './about-us/about-us.module#AboutUsPageModule' },
  { path: 'product-details/:id', loadChildren: './product-details/product-details.module#ProductDetailsPageModule' },
  { path: 'register', loadChildren: './register/register.module#RegisterPageModule' },
  { path: 'login', loadChildren: './login/login.module#LoginPageModule' },
  { path: 'shoppingcart', loadChildren: './shoppingcart/shoppingcart.module#ShoppingcartPageModule' },
  { path: 'promotional-products', loadChildren: './promotional-products/promotional-products.module#PromotionalProductsPageModule' },
  { path: 'sizeguide', loadChildren: './sizeguide/sizeguide.module#SizeguidePageModule' },
  { path: 'coordinated-outfits', loadChildren: './coordinated-outfits/coordinated-outfits.module#CoordinatedOutfitsPageModule' },
  { path: 'coordinated-outfit-details/:outfitId',
  loadChildren: './coordinated-outfit-details/coordinated-outfit-details.module#CoordinatedOutfitDetailsPageModule' },
  { path: 'wishlist', loadChildren: './wishlist/wishlist.module#WishlistPageModule' },
  { path: 'account-details', loadChildren: './account-details/account-details.module#AccountDetailsPageModule' },
  { path: 'categories', loadChildren: './categories/categories.module#CategoriesPageModule' },
  { path: 'promotion-page/:id', loadChildren: './promotion-page/promotion-page.module#PromotionPagePageModule' }

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
