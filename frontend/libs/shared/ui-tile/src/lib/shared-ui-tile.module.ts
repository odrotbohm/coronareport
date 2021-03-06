import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { TileComponent } from './tile/tile.component';

@NgModule({
  imports: [CommonModule, RouterModule, SharedUiMaterialModule, SharedUtilTranslationModule],
  declarations: [TileComponent],
  exports: [TileComponent],
})
export class SharedUiTileModule {}
