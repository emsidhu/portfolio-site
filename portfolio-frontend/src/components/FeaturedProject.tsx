import type { Project as ProjectData } from "../api/projectService.ts";

function FeaturedProject({ project }: { project: ProjectData }) {
  return (
    <div className="col-span-12 lg:col-span-4 m-4 rounded-3xl overflow-hidden text-black">
      <div className="relative border-b">
        <img
          src={project.photoUrl ?? undefined}
          alt="Project Image"
          className="text-gray-50 w-full max-h-[40lvw] md:max-h-[30lvw] lg:max-h-full object-cover"
        />
      </div>
      <div className="grid grid-rows-2 p-2 lg:px-1 lg:pb-0 text-gray-900 bg-linear-to-br from-purple-300 to-purple-400">
        <div className="grid grid-cols-[1fr_auto_1fr] items-center text-3xl font-extrabold px-2">
          <a href={`${project.runUrl}`}>
            {project.isDownload ? (
              <i className="bi bi-download" />
            ) : (
              <i className="bi bi-play-circle" />
            )}
          </a>

          <h3 className="text-center">{project.title === null ? project.name : project.title}</h3>

          <a
            className="flex justify-self-end"
            href={`https://github.com/${project.owner}/${project.name}`}
            target="_blank"
            rel="noreferrer"
          >
            <i className="bi bi-github" />
          </a>
        </div>

        <p className="text-center mt-2 pb-1 lg:pb-2 text-xl font-bold whitespace-pre-line">
          {project.featuredDescription}
        </p>
      </div>
    </div>
  );
}
export default FeaturedProject;
