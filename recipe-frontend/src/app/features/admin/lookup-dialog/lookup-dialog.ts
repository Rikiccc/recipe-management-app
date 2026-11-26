import { Component, Inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface LookupDialogData {
  title: string;
  label: string;
  initialName?: string;
}

@Component({
  selector: 'app-lookup-dialog',
  standalone: false,
  templateUrl: './lookup-dialog.html',
  styleUrl: './lookup-dialog.scss'
})
export class LookupDialog {
  nameControl: FormControl<string>;

  constructor(
    public dialogRef: MatDialogRef<LookupDialog>,
    @Inject(MAT_DIALOG_DATA) public data: LookupDialogData
  ) {
    this.nameControl = new FormControl(data.initialName || '', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(2), Validators.maxLength(100)]
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.nameControl.valid) {
      this.dialogRef.close(this.nameControl.value.trim());
    }
  }
}