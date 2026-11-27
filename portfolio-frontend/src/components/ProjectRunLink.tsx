import type { Project } from "../api/projectService";

type ProjectRunLinkProps = Pick<Project, "runUrl" | "isDownload">;

function ProjectRunLink({ runUrl, isDownload }: ProjectRunLinkProps) {
  if (runUrl === null) {
    return null;
  }

  return (
    <a
      href={runUrl}
      target="_blank"
      rel="noreferrer"
      aria-label={isDownload ? "Download project" : "Open project"}
    >
      <i
        className={`bi ${isDownload ? "bi-download" : "bi-play-circle"}`}
        aria-hidden="true"
      />
    </a>
  );
}

export default ProjectRunLink;
