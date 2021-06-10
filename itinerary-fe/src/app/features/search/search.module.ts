import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchRoutingModule } from './search-routing.module';
import { SearchComponent } from './search.component';
import { UikitModule } from 'src/app/shared/uikit/uikit.module';
import { HttpClientModule } from '@angular/common/http';
import { HeroSearchComponent } from './hero-search/hero-search.component';
import { AddAttractionModalComponent } from './add-attraction-modal/add-attraction-modal.component';


@NgModule({
  declarations: [SearchComponent, HeroSearchComponent, AddAttractionModalComponent],
  imports: [
    CommonModule,
    SearchRoutingModule,
    UikitModule,
    HttpClientModule,
  ]
})
export class SearchModule { }
