import CurrentProject from "../components/CurrentProject";
import FeaturedProject from "../components/FeaturedWork";
import ProjectsList from "../components/ProjectsList";
import { useState } from "react";
import { useQuery, keepPreviousData } from "@tanstack/react-query";
import { PAGE_SIZE } from "../constants";
import { projectsQueryOptions } from "../api/queries";

function ProjectsPage() {
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: projects,
    error,
    isPending,
    isError,
    isPlaceholderData,
  } = useQuery({
    ...projectsQueryOptions(currentPage, PAGE_SIZE),
    placeholderData: keepPreviousData,
  });

  if (isPending) {
    return <div>Loading Projects...</div>;
  }

  if (isError) {
    return <div>Error: {error.message}</div>;
  }

  return (
    <>
      {isPending ? <div>Loading Projects...</div> : <pre>{JSON.stringify(projects, null, 2)}</pre>}
      <CurrentProject />
      <div className="my-5"></div>
      <FeaturedProject />
      <ProjectsList />
    </>
  );
}
export default ProjectsPage;
