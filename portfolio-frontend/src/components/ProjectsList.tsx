import Project from "./Project";
import SectionHeader from "./SectionHeader";

function ProjectsList() {
  return (
    <div className="container mx-auto mt-8">
      <div className="grid grid-cols-[1fr_auto_1fr] items-center">
        <div />
        <div className="flexjustify-center">
          <SectionHeader text="All Projects" />
        </div>
        <div className="flex items-center justify-end">
          <label htmlFor="technologies">Filter: </label>
          <select
            className="ml-1.5 p-0.5 text-sm text-center bg-neutral-secondary-medium border border-default-medium text-heading rounded-full focus:ring-brand focus:border-brand shadow-xs placeholder:text-body"
            name="technologies"
            id="technologies"
            defaultValue=""
          >
            <option value="">--Technology--</option>
            <option value="React">React</option>
            <option value="Java">Java</option>
          </select>
        </div>
      </div>
      <div className="container mb-10">
        <Project />
      </div>
    </div>
  );
}
export default ProjectsList;
