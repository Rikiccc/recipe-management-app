import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'recipes/:id',
    renderMode: RenderMode.Client,
  },
  { path: 'auth', renderMode: RenderMode.Client },
  { path: 'auth/**', renderMode: RenderMode.Client },
  { path: 'admin', renderMode: RenderMode.Client },
  { path: 'pantry', renderMode: RenderMode.Client },
  { path: 'shopping', renderMode: RenderMode.Client },
  { path: 'reports', renderMode: RenderMode.Client },
  { path: 'notifications', renderMode: RenderMode.Client },
  {
    path: '**',
    renderMode: RenderMode.Prerender,
  },
];