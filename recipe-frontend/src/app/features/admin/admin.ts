import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  AdminLookupService,
  LookupCategoryKey,
  LookupItem
} from './services/admin-lookup.service';
import { LookupDialog } from './lookup-dialog/lookup-dialog';

interface AdminTabConfig {
  key: LookupCategoryKey;
  label: string;
  singularLabel: string;
}

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class Admin implements OnInit, AfterViewInit {
  tabs: AdminTabConfig[] = [
    { key: 'meal-types', label: 'Tipovi obroka', singularLabel: 'tip obroka' },
    { key: 'dish-types', label: 'Tipovi jela', singularLabel: 'tip jela' },
    { key: 'diet-types', label: 'Tipovi ishrane', singularLabel: 'tip ishrane' },
    { key: 'ingredient-categories', label: 'Kategorije namirnica', singularLabel: 'kategoriju namirnica' },
    { key: 'allergens', label: 'Alergeni', singularLabel: 'alergen' }
  ];

  selectedTabIndex = 0;
  displayedColumns: string[] = ['id', 'name', 'actions'];
  dataSource = new MatTableDataSource<LookupItem>([]);
  filterValue = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private adminLookupService: AdminLookupService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.dataSource.filterPredicate = (data: LookupItem, filter: string) =>
      data.name.toLowerCase().includes(filter) || data.id.toString().includes(filter);
    this.loadCurrentTabData();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  get currentTab(): AdminTabConfig {
    return this.tabs[this.selectedTabIndex];
  }

  onTabChange(index: number): void {
    this.selectedTabIndex = index;
    this.filterValue = '';
    this.dataSource.filter = '';
    this.loadCurrentTabData();
  }

  loadCurrentTabData(): void {
    this.adminLookupService.getAll(this.currentTab.key).subscribe({
      next: (items) => {
        this.dataSource.data = items;
        if (this.paginator) {
          this.paginator.firstPage();
        }
      },
      error: () => {
        this.showMessage('Greška pri učitavanju podataka sa servera.');
      }
    });
  }

  applyFilter(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.filterValue = value;
    this.dataSource.filter = value.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(LookupDialog, {
      width: '400px',
      data: {
        title: `Dodaj ${this.currentTab.singularLabel}`,
        label: 'Naziv'
      }
    });

    dialogRef.afterClosed().subscribe((result: string | undefined) => {
      if (result) {
        this.adminLookupService.create(this.currentTab.key, result).subscribe({
          next: () => {
            this.showMessage('Stavka je uspešno dodata.');
            this.loadCurrentTabData();
          },
          error: (err) => {
            this.showMessage(err?.error?.message || 'Greška pri dodavanju stavke.');
          }
        });
      }
    });
  }

  openEditDialog(item: LookupItem): void {
    const dialogRef = this.dialog.open(LookupDialog, {
      width: '400px',
      data: {
        title: `Izmeni ${this.currentTab.singularLabel}`,
        label: 'Naziv',
        initialName: item.name
      }
    });

    dialogRef.afterClosed().subscribe((result: string | undefined) => {
      if (result && result !== item.name) {
        this.adminLookupService.update(this.currentTab.key, item.id, result).subscribe({
          next: () => {
            this.showMessage('Stavka je uspešno izmenjena.');
            this.loadCurrentTabData();
          },
          error: (err) => {
            this.showMessage(err?.error?.message || 'Greška pri izmeni stavke.');
          }
        });
      }
    });
  }

  deleteItem(item: LookupItem): void {
    const confirmed = window.confirm(
      `Da li ste sigurni da želite da obrišete "${item.name}"?`
    );
    if (!confirmed) {
      return;
    }

    this.adminLookupService.delete(this.currentTab.key, item.id).subscribe({
      next: () => {
        this.showMessage('Stavka je uspešno obrisana.');
        this.loadCurrentTabData();
      },
      error: (err) => {
        this.showMessage(err?.error?.message || 'Greška pri brisanju stavke.');
      }
    });
  }

  private showMessage(message: string): void {
    this.snackBar.open(message, 'Zatvori', { duration: 3000 });
  }
}