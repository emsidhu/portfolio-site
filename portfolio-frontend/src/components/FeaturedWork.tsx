import { useQuery } from "@tanstack/react-query";
import { featuredProjectsQueryOptions } from "../api/queries";
import FeaturedProject from "./FeaturedProject";
import SectionHeader from "./SectionHeader";

function FeaturedWork() {
  const { data: projects, error, isPending, isError } = useQuery(featuredProjectsQueryOptions());

  return (
    <div className="p-4 md:p-20 bg-gray-900 text-gray-50">
      <SectionHeader text="Featured Work" />
      <div className="grid grid-cols-12">
        {isPending ? (
          <div>Loading Projects...</div>
        ) : isError ? (
          <div>Error: {error.message}</div>
        ) : (
          projects.map((project) => <FeaturedProject key={project.id} project={project} />)
        )}
      </div>
    </div>
  );
}
export default FeaturedWork;
