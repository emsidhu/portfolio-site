import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import "animate.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { PAGE_SIZE } from "./constants.ts";
import {
  activeProjectsQueryOptions,
  featuredProjectsQueryOptions,
  currentProjectQueryOptions,
} from "./api/queries.ts";

const queryClient = new QueryClient();

// Prefetch projects
void queryClient.query(activeProjectsQueryOptions(0, PAGE_SIZE)).catch(() => {});
void queryClient.query(featuredProjectsQueryOptions()).catch(() => {});
void queryClient.query(currentProjectQueryOptions()).catch(() => {});

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </QueryClientProvider>
  </StrictMode>,
);
