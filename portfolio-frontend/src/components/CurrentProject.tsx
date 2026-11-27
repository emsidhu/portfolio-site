import { useQuery } from "@tanstack/react-query";
import SectionHeader from "./SectionHeader";
import TechPill from "./TechPill";
import { currentProjectQueryOptions } from "../api/queries";

function CurrentProject() {
  const { data: project, error, isPending, isError } = useQuery(currentProjectQueryOptions());

  return (
    <div className="text-gray-900 max-w-full sm:max-w-[90vw] md:max-w-[85vw] mx-auto mt-4 px-3">
      <SectionHeader text="Next Project" />

      {isPending ? (
        "Loading..."
      ) : isError ? (
        <div>Error: {error.message}</div>
      ) : (
        <div className="w-full grid grid-cols-1 md:grid-cols-2 md:max-h-[60vh] border rounded-3xl overflow-hidden">
          <div className="relative min-h-80 border-b md:border-r">
            <img
              src={project.photoUrl ?? undefined}
              alt="Project Image"
              className="absolute inset-0 w-full h-full object-cover"
            />
          </div>
          <div className="p-4 bg-linear-to-br from-blue-300 to-blue-400">
            <div className="flex flex-wrap items-center gap-3">
              <div className="flex min-w-0 flex-wrap items-center gap-2">
                <h3 className="inline m-0 text-2xl font-bold">
                  {project.title === null ? project.name : project.title}
                </h3>
                {project.technologies &&
                  project.technologies.map((technology) => (
                    <TechPill key={technology} text={technology} />
                  ))}
              </div>

              <div className="ml-auto flex shrink-0 items-center gap-2 text-3xl">
                <a href={`${project.runUrl}`}>
                  {project.isDownload ? (
                    <i className="bi bi-download" />
                  ) : (
                    <i className="bi bi-play-circle" />
                  )}
                </a>
                <a
                  href={`https://github.com/${project.owner}/${project.name}`}
                  target="_blank"
                  rel="noreferrer"
                >
                  <i className="bi bi-github" />
                </a>
              </div>
            </div>
            <p className="list-item list-disc ml-5 pt-1 text-md">
              Last Updated: {project.latestCommitDate}
            </p>
            <p className="pt-2 text-lg whitespace-pre-line">
              {project.descriptionOverride == null
                ? project.description
                : project.descriptionOverride}
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
export default CurrentProject;
