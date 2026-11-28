import TechPill from "./TechPill";
import ProjectRunLink from "./ProjectRunLink";
import type { Project as ProjectData } from "../api/projectService";
import ReactMarkdown from "react-markdown";
import remarkBreaks from "remark-breaks";

function Project({
  project,
  variant = "default",
}: {
  project: ProjectData;
  variant?: "default" | "current";
}) {
  const backgroundColor =
    variant === "current"
      ? "bg-linear-to-br from-amber-500 to-orange-500"
      : "bg-linear-to-br from-blue-300 to-blue-400";
  const descriptionText = project.descriptionOverride ?? project.description;

  return (
    <div className="w-full grid grid-cols-1 lg:grid-cols-2 border rounded-3xl mb-5 overflow-hidden">
      <div className="relative min-h-80 border-b md:border-r">
        <img
          src={project.photoUrl ?? undefined}
          alt="Project Image"
          className="absolute inset-0 w-full h-full object-cover"
        />
      </div>
      <div className={`p-4 ${backgroundColor}`}>
        <div className="flex items-start justify-between gap-3">
          <div className="flex min-w-0 flex-wrap items-center gap-2">
            <h3 className="m-0 text-2xl font-bold">
              {project.title === null ? project.name : project.title}
            </h3>
            {project.technologies &&
              project.technologies.map((technology) => (
                <TechPill key={technology} text={technology} />
              ))}
          </div>

          <div className="flex shrink-0 items-center gap-2 text-3xl">
            <ProjectRunLink runUrl={project.runUrl} isDownload={project.isDownload} />
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
        <div className="pt-2 text-lg">
          <ReactMarkdown
            remarkPlugins={[remarkBreaks]}
            components={{
              h3: ({ ...props }) => <h3 className="text-xl font-bold mt-1 mb-1" {...props} />,
              ul: ({ ...props }) => <ul className="list-disc pl-6 space-y-1.5" {...props} />,
            }}
          >
            {descriptionText}
          </ReactMarkdown>
        </div>
      </div>
    </div>
  );
}
export default Project;
