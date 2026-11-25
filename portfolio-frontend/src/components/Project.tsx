import TechPill from "./TechPill";
import type { Project as ProjectData } from "../api/projectService";

function Project({ project }: { project: ProjectData }) {
  return (
    <div className="w-full border rounded-3xl mb-5 overflow-hidden">
      <div className="border-b">
        <img
          src={project.photoUrl ?? undefined}
          alt="Project Image"
          className="w-full max-h-[40lvw] md:max-h-[30lvw] xl:max-h-[20lvw] object-cover"
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

          <div className="flex justify-self-end text-3xl gap-2">
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
        <p className="pt-1 text-lg">{project.description}</p>
      </div>
    </div>
  );
}
export default Project;
