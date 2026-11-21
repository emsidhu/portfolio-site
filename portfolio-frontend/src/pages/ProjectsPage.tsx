import CurrentProject from "../components/CurrentProject";
import FeaturedProject from "../components/FeaturedWork";

function ProjectsPage() {
  return (
    <>
      <CurrentProject />
      <div className="my-5"></div>
      <FeaturedProject />
    </>
  );
}
export default ProjectsPage;
