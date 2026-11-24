import { queryOptions } from "@tanstack/react-query";
import { getActiveProjects, getFeaturedProjects, getCurrentProject } from "./projectService";

export const activeProjectsQueryOptions = (page = 0, size = 10) =>
  queryOptions({
    queryKey: ["activeProjects", page, size],
    queryFn: () => getActiveProjects(page, size),
    staleTime: 1000 * 60 * 10,
  });

export const featuredProjectsQueryOptions = () =>
  queryOptions({
    queryKey: ["featuredProjects"],
    queryFn: getFeaturedProjects,
    staleTime: 1000 * 60 * 10,
  });

export const currentProjectQueryOptions = () =>
  queryOptions({
    queryKey: ["currentProject"],
    queryFn: getCurrentProject,
    staleTime: 1000 * 60 * 10,
  });