import SectionHeader from "./SectionHeader";
import TechPill from "./TechPill";

function CurrentProject() {
  return (
    <div className="container mx-auto">
      <SectionHeader text="Current Project" />

      <div className="w-full grid grid-cols-1 md:grid-cols-2 border rounded-3xl overflow-hidden">
        <div className="border-b md:border-r">
          <img
            src="https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
            alt="Project Image"
            className="w-full h-full object-cover"
          />
        </div>
        <div className="p-4 bg-linear-to-br from-orange-400 to-orange-600">
          <div className="flex items-center flex-nowrap gap-2">
            <h3 className="inline m-0 p-0 text-xl font-semibold">Title</h3>
            <TechPill name="techName" />
            <a className="ml-auto mr-2" href="">
              <i className="bi bi-play-circle text-2xl" />
            </a>
            <a className="mr-1" href="https://github.com/emsidhu" target="_blank" rel="noreferrer">
              <i className="bi bi-github text-2xl" />
            </a>
          </div>
          <p className="list-item list-disc ml-5">Last Updated: [date]</p>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
            incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
            exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure
            dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
            Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
            mollit anim id est laborum.
          </p>
        </div>
      </div>
    </div>
  );
}
export default CurrentProject;
