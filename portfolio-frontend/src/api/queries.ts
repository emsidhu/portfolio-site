import { queryOptions } from "@tanstack/react-query";
import { getProjects } from "./projectService";

export const projectsQueryOptions = (page = 0, size = 10) =>
  queryOptions({
    queryKey: ["projects", page, size],
    queryFn: async () => {
      const { data } = await getProjects(page, size);
      return data;
    },
    staleTime: 1000 * 60 * 5,
  });