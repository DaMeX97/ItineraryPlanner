import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-button-spinner',
  templateUrl: './button-spinner.component.html',
  styleUrls: ['./button-spinner.component.scss']
})
export class ButtonSpinnerComponent implements OnInit {

  @Input()
  callback! : Function;

  @Input()
  spin: boolean = false;

  @Input()
  text: string = "";

  @Input()
  fullWidth: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  buttonClick() {
    this.callback();
  }

}
