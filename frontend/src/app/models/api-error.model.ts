export interface ApiError {
  status: number;
  mensagem: string;
  campos: Record<string, string>;
  timestamp: string;
}
