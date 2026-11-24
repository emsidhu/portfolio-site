import { useQuery } from "@tanstack/react-query";
import SectionHeader from "./SectionHeader";
import TechPill from "./TechPill";
import { currentProjectQueryOptions } from "../api/queries";

function CurrentProject() {
  const { data: project, isPending, isError } = useQuery(currentProjectQueryOptions());

  if (isPending || isError) {
    return <></>;
  }

  return (
    <div className="max-w-full sm:max-w-[90vw] md:max-w-[85vw] mx-auto mt-4">
      <SectionHeader text="Next Project" />

      <div className="w-full grid grid-cols-1 md:grid-cols-2 border rounded-3xl overflow-hidden">
        <div className="border-b md:border-r">
          <img
            src={project.photoUrl ?? undefined}
            alt="Project Image"
            className="w-full h-full object-cover"
          />
        </div>
        <div className="p-4 bg-linear-to-br from-blue-300 to-blue-400">
          <div className="grid grid-cols-2">
            <div className="flex gap-2 items-center justify-self-start">
              <h3 className="inline m-0 text-2xl font-bold">{project.name}</h3>
              {project.technologies &&
                project.technologies.map((technology) => (
                  <TechPill key={technology} text={technology} />
                ))}
            </div>

            <div className="flex justify-self-end text-2xl gap-2">
              <a href="">
                <i className="bi bi-play-circle" />
              </a>
              <a href="https://github.com/emsidhu" target="_blank" rel="noreferrer">
                <i className="bi bi-github" />
              </a>
            </div>
          </div>
          <p className="list-item list-disc ml-5 pt-1 text-md">
            Last Updated: {project.latestCommitDate}
          </p>
          <p className="pt-2 text-lg">{project.description}</p>
        </div>
      </div>
    </div>
  );
}
export default CurrentProject;
