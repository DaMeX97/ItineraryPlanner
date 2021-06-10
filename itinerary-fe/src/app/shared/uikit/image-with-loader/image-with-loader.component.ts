import { Component, Input, OnInit } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-image-with-loader',
  templateUrl: './image-with-loader.component.html',
  styleUrls: ['./image-with-loader.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ImageWithLoaderComponent implements OnInit {

  @Input() 
  height:number=200;
  @Input() 
  width:number=200;
  @Input() 
  image:string = "";
  @Input() 
  alt: string = "";

  isLoading:boolean;
  
  constructor() { 
    this.isLoading=true;
  }

  ngOnInit() {

  }

  hideLoader(){
    this.isLoading = false;
  }
}
