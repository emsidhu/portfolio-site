import { useQuery } from "@tanstack/react-query";
import SectionHeader from "./SectionHeader";
import { currentProjectQueryOptions } from "../api/queries";
import Project from "./Project";

function CurrentProject() {
  const { data: project, error, isPending, isError } = useQuery(currentProjectQueryOptions());

  return (
    <div className="text-gray-900 max-w-full sm:max-w-[90vw] md:max-w-[85vw] mx-auto mt-4 px-3">
      <SectionHeader text="Current Project" />

      {isPending ? (
        "Loading..."
      ) : isError ? (
        <div>Error: {error.message}</div>
      ) : (
        <Project project={project} variant="current" />
      )}
    </div>
  );
}
export default CurrentProject;
