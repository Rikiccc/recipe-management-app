/** Error format that backend sends (GlobalExceptionHandler / ErrorResponse). */
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  fieldErrors?: Record<string, string>;
}

/** Endpoint reply /api/health. */
export interface HealthStatus {
  status: string;
  message: string;
}
