import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-info-modal',
  templateUrl: './info-modal.component.html',
  styleUrls: ['./info-modal.component.scss']
})
export class InfoModalComponent implements OnInit {
  title: string = "";

  message: string = "";

  showDeny: boolean = false;

  acceptButtonText: string = ""

  denyButtonText: string = ""

  constructor(private dialogRef: MatDialogRef<InfoModalComponent>, @Inject(MAT_DIALOG_DATA) private data: DialogData) {
    this.title = data.title;
    this.message = data.message;
    this.showDeny = data.showDeny;
    this.acceptButtonText = data.acceptButtonText;
    this.denyButtonText = data.denyButtonText;
  }

  ngOnInit(): void {

  }

  onAccept(): void {
    this.dialogRef.close(true);
  }

  onDeny() : void {
    this.dialogRef.close(false);
  }
}

export interface DialogData {
  title: string;
  message: string;
  showDeny: boolean;
  acceptButtonText: string;
  denyButtonText: string;
}