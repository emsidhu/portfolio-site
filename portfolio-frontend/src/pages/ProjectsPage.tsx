import CurrentProject from "../components/CurrentProject";
import FeaturedProject from "../components/FeaturedWork";
import ProjectsList from "../components/ProjectsList";

function ProjectsPage() {
  return (
    <>
      <CurrentProject />
      <div className="my-5"></div>
      <FeaturedProject />
      <ProjectsList />
    </>
  );
}
export default ProjectsPage;
